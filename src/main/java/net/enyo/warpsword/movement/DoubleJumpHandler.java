package net.enyo.warpsword.movement;

import net.enyo.warpsword.item.ModItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "warpsword", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class DoubleJumpHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        Level level = player.level();

        if (!(player instanceof LocalPlayer localPlayer)) return;
        if (!level.isClientSide()) return;

        ItemStack held = player.getMainHandItem();
        if (!held.is(ModItems.WARPSWORD.get())) return;

        CompoundTag data = player.getPersistentData();
        boolean isOnGround = player.onGround();
        boolean isJumping = localPlayer.input.jumping;

        boolean hasDoubleJumped = data.getBoolean("hasDoubleJumped");
        boolean hasReleasedJump = data.getBoolean("hasReleasedJump");

        // Reset on landing
        if (isOnGround) {
            data.putBoolean("hasDoubleJumped", false);
            data.putBoolean("hasReleasedJump", false);
            return;
        }


        // While in the air:
        if (!hasDoubleJumped) {
            // Track if jump was released
            if (!isJumping) {
                data.putBoolean("hasReleasedJump", true);
            }

            // Only trigger double jump if the player released and pressed jump again
            if (hasReleasedJump && isJumping) {
                Vec3 look = player.getLookAngle();
                player.setDeltaMovement(look.x * 1.5, 1.0, look.z * 1.5);
                player.hasImpulse = true;

                // Particles
                for (int i = 0; i < 10; i++) {
                    double offsetX = (player.getRandom().nextDouble() - 0.5) * 0.5;
                    double offsetY = player.getRandom().nextDouble() * 0.5;
                    double offsetZ = (player.getRandom().nextDouble() - 0.5) * 0.5;

                    level.addParticle(ParticleTypes.CLOUD,
                            player.getX(),
                            player.getY() + 0.5,
                            player.getZ(),
                            offsetX, offsetY, offsetZ);

                    level.addParticle(ParticleTypes.CRIT,
                            player.getX(),
                            player.getY() + 0.5,
                            player.getZ(),
                            offsetX * 0.5, offsetY * 0.5, offsetZ * 0.5);
                }

                data.putBoolean("hasDoubleJumped", true);
                data.putBoolean("hasReleasedJump", false);
            }
        }
    }
}

