package net.enyo.warpsword.effect;

import net.enyo.warpsword.warpsword;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;


@Mod.EventBusSubscriber(modid = warpsword.MOD_ID)
public class RiftEffectHandler {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity().hasEffect(ModEffects.BLACK_WHITE.get())) {
            // Prevent damage to player when effect active
            if (event.getEntity() instanceof Player) {
                event.setCanceled(true);
            }
        }
    }


    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        if (event.getEntity().hasEffect(ModEffects.BLACK_WHITE.get())) {
            // Prevent healing while effect active
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (player.hasEffect(ModEffects.BLACK_WHITE.get())) {
            // Prevent player dealing damage
            event.setCanceled(true);
        }
    }
}
