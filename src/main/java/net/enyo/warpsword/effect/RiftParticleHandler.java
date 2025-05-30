package net.enyo.warpsword.effect;


import net.enyo.warpsword.warpsword;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.sounds.SoundSource;
import net.enyo.warpsword.sound.ModSounds;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = warpsword.MOD_ID, value = Dist.CLIENT)
public class RiftParticleHandler {
    private static final Set<UUID> triggeredPlayers = new HashSet<>();
    private static final Set<UUID> hadEffectLastTick = new HashSet<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (!player.level().isClientSide) return;

        UUID playerId = player.getUUID();
        MobEffectInstance effect = player.getEffect(ModEffects.BLACK_WHITE.get());
        boolean hasEffectNow = (effect != null);
        boolean hadEffectBefore = hadEffectLastTick.contains(playerId);

        // Effect just ended this tick — trigger exit particles
        if (hadEffectBefore && !hasEffectNow) {
            // Play the "pop out" sound when effect disappears
            player.level().playSound(
                    player,
                    player.getX(), player.getY(), player.getZ(),
                    ModSounds.RIFT_POP_OUT.get(),  // <-- Your custom "pop out" sound
                    SoundSource.PLAYERS,
                    1.0F,  // volume
                    1.0F   // pitch
            );
            spawnParticleExplosion(player);
            triggeredPlayers.remove(playerId);  // reset for next effect use
        }

        // Effect active, but just started — trigger entry particles once
        if (hasEffectNow && !triggeredPlayers.contains(playerId)) {
            spawnParticleExplosion(player);
            triggeredPlayers.add(playerId);
        }

        // Ongoing small particles while effect active
        if (hasEffectNow) {
            double px = player.getX() + (player.getRandom().nextDouble() - 0.5) * 0.5;
            double py = player.getY() + player.getRandom().nextDouble() * 1.0;
            double pz = player.getZ() + (player.getRandom().nextDouble() - 0.5) * 0.5;

            player.level().addParticle(ParticleTypes.PORTAL, px, py, pz, 0, 0, 0);
            player.level().addParticle(ParticleTypes.SMOKE, px, py, pz, 0, 0.01, 0);
        }

        // Update tracking set for next tick
        if (hasEffectNow) {
            hadEffectLastTick.add(playerId);
        } else {
            hadEffectLastTick.remove(playerId);
        }
    }

    public static void spawnParticleExplosion(Player player) {
        net.minecraft.util.RandomSource random = player.getRandom();
        double px = player.getX();
        double py = player.getY() + player.getEyeHeight() / 2;
        double pz = player.getZ();

        int particleCount = 50;  // Reduced count to avoid lag/crash

        for (int i = 0; i < particleCount; i++) {
            double vx = (random.nextDouble() - 0.5) * 0.5;
            double vy = (random.nextDouble() - 0.5) * 0.5;
            double vz = (random.nextDouble() - 0.5) * 0.5;

            player.level().addParticle(ParticleTypes.END_ROD, px, py, pz, vx, vy, vz);
            player.level().addParticle(ParticleTypes.PORTAL, px, py, pz, vx, vy, vz);
        }
    }
}
