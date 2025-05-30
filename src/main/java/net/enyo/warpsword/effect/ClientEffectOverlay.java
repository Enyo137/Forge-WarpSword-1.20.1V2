package net.enyo.warpsword.effect;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.systems.RenderSystem;
import net.enyo.warpsword.warpsword;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static org.apache.commons.lang3.Range.is;

@Mod.EventBusSubscriber(modid = warpsword.MOD_ID, value = Dist.CLIENT)
public class ClientEffectOverlay {

    private static final ResourceLocation OVERLAY_TEXTURE =
            new ResourceLocation(warpsword.MOD_ID, "textures/gui/overlay.png");

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        // Only draw overlay if player has the effect
        if (!mc.player.hasEffect(ModEffects.BLACK_WHITE.get())) return;

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader); // Allows color tinting with alpha
        RenderSystem.setShaderTexture(0, OVERLAY_TEXTURE);
        RenderSystem.setShaderColor(1.0f, 0.0f, 0.0f, 0.05f); // RGBA (A = opacity, 0.0 = transparent, 1.0 = opaque)

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(0, height, 0).uv(0, 1).endVertex();
        buffer.vertex(width, height, 0).uv(1, 1).endVertex();
        buffer.vertex(width, 0, 0).uv(1, 0).endVertex();
        buffer.vertex(0, 0, 0).uv(0, 0).endVertex();
        tesselator.end();

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f); // Reset alpha
    }
}