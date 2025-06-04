package net.enyo.warpsword.event;

import net.enyo.warpsword.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "warpsword")
public class ModEvents {

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        // Check if holding the Warpsword in either hand
        boolean hasWarpsword =
                player.getMainHandItem().is(ModItems.WARPSWORD.get()) ||
                        player.getOffhandItem().is(ModItems.WARPSWORD.get());

        if (hasWarpsword) {
            event.setCanceled(true); // Cancel fall damage

            // Spawn a burst of particles on the client side
            if (player.level().isClientSide) {
                for (int i = 0; i < 50; i++) {
                    double angle = Math.random() * 2 * Math.PI;
                    double speed = 0.5 + Math.random() * 0.5;
                    double dx = Math.cos(angle) * speed;
                    double dz = Math.sin(angle) * speed;
                    double x = player.getX();
                    double y = player.getY();
                    double z = player.getZ();

                    player.level().addParticle(
                            ParticleTypes.PORTAL,
                            x, y + 0.1, z,
                            dx, 0.1, dz
                    );
        }   }   }
    }
}
