package net.enyo.warpsword.handler;

import net.enyo.warpsword.effect.ModEffects;
import net.enyo.warpsword.warpsword;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = warpsword.MOD_ID)
public class AggroBlocker {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        ServerLevel world = getServerLevel(event);
        if (world == null) return;

        // Only consider players who currently have the Rift effect active
        for (Player player : world.players()) {
            if (player.hasEffect(ModEffects.BLACK_WHITE.get())) {
                // Search monsters around this player only
                List<Monster> monsters = world.getEntitiesOfClass(Monster.class,
                        player.getBoundingBox().inflate(32)); // smaller radius for performance

                for (Monster monster : monsters) {
                    LivingEntity target = monster.getTarget();
                    if (target instanceof Player targetPlayer && targetPlayer.equals(player)) {
                        monster.setTarget(null); // Clear the target if targeting this rift-affected player
                    }
                }
            }
        }
    }

    private static ServerLevel getServerLevel(ServerTickEvent event) {
        // The ServerTickEvent doesn't directly give you the world,
        // but you can grab it from Minecraft server instance if needed
        // Here’s a simple way assuming you only run on a dedicated server:

        return event.getServer().overworld(); // or whichever dimension you want
    }
}
