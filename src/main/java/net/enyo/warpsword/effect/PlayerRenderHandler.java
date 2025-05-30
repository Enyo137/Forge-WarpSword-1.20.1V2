package net.enyo.warpsword.effect;

import net.enyo.warpsword.warpsword;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = warpsword.MOD_ID, value = Dist.CLIENT)
public class PlayerRenderHandler {

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        if (player.hasEffect(ModEffects.BLACK_WHITE.get())) {
            // This will hide the whole player model, armor and items included
            event.setCanceled(true);
        }
    }
}