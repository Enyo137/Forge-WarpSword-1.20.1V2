package net.enyo.warpsword.effect;

import net.enyo.warpsword.warpsword;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = warpsword.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ShaderFlashHandler {

    private static final ResourceLocation BLACK_WHITE_SHADER =
            new ResourceLocation(warpsword.MOD_ID, "shaders/post/black_white.json");

    private static int timer = 0;

    public static void triggerBlackWhiteEffect(int durationTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            mc.gameRenderer.loadEffect(BLACK_WHITE_SHADER);

            timer = durationTicks;
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && timer > 0) {
            timer--;
            if (timer == 0) {
                Minecraft.getInstance().gameRenderer.shutdownEffect();
            }
        }
    }
}
