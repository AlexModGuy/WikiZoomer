package com.github.alexthe666.wikizoomer.client;

import com.github.alexthe666.wikizoomer.tileentity.TileEntityZoomerBase;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.ForgeSlider;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class GuiItemZoomer extends Screen {

    public static final ResourceLocation GREENSCREEN = new ResourceLocation("wikizoomer:textures/gui/greenscreen.png");
    private final TileEntityZoomerBase zoomerBase;
    private boolean greenscreen = false;
    private float sliderValue = 100;
    private float prevSliderValue = sliderValue;
    private boolean screenshot = false;

    public GuiItemZoomer(TileEntityZoomerBase zoomerBase) {
        super(Component.translatable("item_zoomer"));
        this.zoomerBase = zoomerBase;
        this.init();
    }

    private void setSliderValue(int i, float sliderValue) {
        this.sliderValue = Math.round(Mth.clamp(sliderValue, 1, 300F));
        prevSliderValue = this.sliderValue;
    }

    public void init() {
        super.init();
        this.clearWidgets();
        int i = (this.width) / 2;
        int j = (this.height - 166) / 2;
        MutableComponent exit = Component.translatable("gui.wikizoomer.close");
        MutableComponent greenscreen = Component.translatable("gui.wikizoomer.greenscreen");
        MutableComponent export = Component.translatable("gui.wikizoomer.export_png");
        int maxLength = 120;
        this.addRenderableWidget(new ForgeSlider(i - 120 / 2 - 140, j + 180, 120, 20, Component.translatable("gui.wikizoomer.zoom"), Component.literal("%"), 1, 300, 100, 1, 1, true) {
            @Override
            protected void applyValue() {
                GuiItemZoomer.this.setSliderValue(2, (float)getValue());
            }
        });
        this.addRenderableWidget(new Button(i - maxLength / 2, j + 180, maxLength, 20, greenscreen, (p_214132_1_) -> {
            GuiItemZoomer.this.greenscreen = !GuiItemZoomer.this.greenscreen;
        }));
        this.addRenderableWidget(new Button(i - maxLength / 2 + 140, j + 180, maxLength, 20, exit, (p_214132_1_) -> {
            Minecraft.getInstance().setScreen(null);
        }));
        this.addRenderableWidget(new Button(i - maxLength / 2 + 140, j + 160, maxLength, 20, export, (p_214132_1_) -> {
            this.screenshot = true;
        }));
    }

    public void renderGreenscreen(int z) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, GREENSCREEN);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.vertex(0.0D, this.height, 0.0D).uv(0.0F, (float) this.height / 32.0F + (float) z).color(255, 255, 255, 255).endVertex();
        bufferbuilder.vertex(this.width, this.height, 0.0D).uv((float) this.width / 32.0F, (float) this.height / 32.0F + (float) z).color(255, 255, 255, 255).endVertex();
        bufferbuilder.vertex(this.width, 0.0D, 0.0D).uv((float) this.width / 32.0F, (float) z).color(255, 255, 255, 255).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, 0.0D).uv(0.0F, (float) z).color(255, 255, 255, 255).endVertex();
        tesselator.end();
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        if(screenshot){
            screenshot = false;
            ItemStack itemStack = zoomerBase.getItem(0);
            String name = itemStack.isEmpty() || itemStack == null ? "none" : itemStack.getItem().toString();
            ScreenshotHelper.exportScreenshot(name, () -> {
                renderFocus(RenderSystem.getModelViewStack());
            });
        }
        if (getMinecraft() != null) {
            try {
                if (greenscreen) {
                    renderGreenscreen(10);
                } else {
                    this.renderBackground(stack);
                }
            } catch (Exception e) {

            }
            super.render(stack, mouseX, mouseY, partialTicks);
            renderFocus(RenderSystem.getModelViewStack());
        }

    }

    private void renderFocus(PoseStack modelStack) {
        int i = (this.width - 248) / 2 + 10;
        int j = (this.height - 166) / 2 + 8;
        ItemStack itemStack = zoomerBase.getItem(0);
        float scale1 = (sliderValue / 100F);
        float scale = scale1 * 12F;
        if (!itemStack.isEmpty()) {
            modelStack.pushPose();
            modelStack.translate(i, j, 10F);
            modelStack.translate(113.5F - scale1 * 100, 76 - scale1 * 100, -10F - sliderValue * 10);
            modelStack.scale(scale, scale, scale);
            Minecraft.getInstance().getItemRenderer().renderGuiItem(itemStack, 0, 0);
            modelStack.popPose();
        }
    }

    public boolean isPauseScreen() {
        return false;
    }
}
