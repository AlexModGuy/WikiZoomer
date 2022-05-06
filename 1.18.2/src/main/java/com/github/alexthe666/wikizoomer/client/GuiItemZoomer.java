package com.github.alexthe666.wikizoomer.client;

import com.github.alexthe666.wikizoomer.ClientProxy;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityZoomerBase;
import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.Slider;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class GuiItemZoomer extends Screen {

    public static final ResourceLocation GREENSCREEN = new ResourceLocation("wikizoomer:textures/gui/greenscreen.png");
    private final Slider.ISlider sliderResponder;
    private final TileEntityZoomerBase zoomerBase;
    private boolean greenscreen = false;
    private float sliderValue = 100;
    private float prevSliderValue = sliderValue;
    private boolean screenshot = false;

    public GuiItemZoomer(TileEntityZoomerBase zoomerBase) {
        super(new TranslatableComponent("item_zoomer"));
        this.zoomerBase = zoomerBase;
        this.init();
        sliderResponder = slider -> GuiItemZoomer.this.setSliderValue(0, (float) slider.sliderValue);
    }

    private void setSliderValue(int i, float sliderValue) {
        this.sliderValue = Math.round(Mth.clamp(sliderValue * 300F, 1, 300F));
        prevSliderValue = this.sliderValue;
    }

    public void init() {
        super.init();
        this.clearWidgets();
        int i = (this.width) / 2;
        int j = (this.height - 166) / 2;
        TranslatableComponent exit = new TranslatableComponent("gui.wikizoomer.close");
        TranslatableComponent greenscreen = new TranslatableComponent("gui.wikizoomer.greenscreen");
        TranslatableComponent export = new TranslatableComponent("gui.wikizoomer.export_png");
        int maxLength = 120;
        this.addRenderableWidget(new Slider(i - 120 / 2 - 140, j + 180, 120, 20, new TranslatableComponent("gui.wikizoomer.zoom"), new TextComponent("%"), 1, 300, 100, false, true, (p_214132_1_) -> {

        }, sliderResponder) {

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
