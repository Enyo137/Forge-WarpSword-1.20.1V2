package net.enyo.warpsword.effect;

import net.enyo.warpsword.warpsword;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = warpsword.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEffectOverlay {

    private static final ResourceLocation BLACK_WHITE_SHADER =
            new ResourceLocation(warpsword.MOD_ID, "shaders/post/impact_frame.json");

    private static boolean shaderActive = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.phase != TickEvent.Phase.END || mc.player == null || mc.level == null) return;

        boolean hasEffect = mc.player.hasEffect(ModEffects.BLACK_WHITE.get());

        if (hasEffect && !shaderActive) {
            mc.gameRenderer.loadEffect(BLACK_WHITE_SHADER);
            shaderActive = true;
        } else if (!hasEffect && shaderActive) {
            mc.gameRenderer.shutdownEffect();
            shaderActive = false;


        }

    }

}
