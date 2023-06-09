package com.github.alexthe666.wikizoomer.client;

import com.github.alexthe666.wikizoomer.ClientProxy;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityEntityZoomer;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Quaternionf;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class GuiEntityZoomer extends Screen {
    private TileEntityEntityZoomer zoomerBase;
    private boolean greenscreen = false;
    private float sliderValue = 100;
    private float prevSliderValue = sliderValue;
    private boolean screenshot = false;
    private int yOffset = 0;
    private Button screenshotButton;

    public GuiEntityZoomer(TileEntityEntityZoomer zoomerBase) {
        super(Component.translatable("entity_zoomer"));
        this.zoomerBase = zoomerBase;
    }

    private void setSliderValue(int i, float sliderValue) {
        this.sliderValue = Math.round(Mth.clamp(sliderValue, 1, 300F));
        prevSliderValue = this.sliderValue;
    }

    protected void init() {
        super.init();
        this.clearWidgets();
        int i = (this.width) / 2;
        int j = (this.height - 166) / 2;
        MutableComponent exit = Component.translatable("gui.wikizoomer.close");
        MutableComponent greenscreen = Component.translatable("gui.wikizoomer.greenscreen");
        MutableComponent export = Component.translatable("gui.wikizoomer.export_png");
        int maxLength = 120;
        this.addRenderableWidget(new ForgeSlider(i - 120 / 2 - 140, j + 180, 120, 20, Component.translatable("gui.wikizoomer.zoom"), Component.literal("%"), 1, 300, 100, 1, 1, true){
            @Override
            protected void applyValue() {
                GuiEntityZoomer.this.setSliderValue(2, (float)getValue());
            }
        });
        this.addRenderableWidget(Button.builder(greenscreen, (button) -> {
            GuiEntityZoomer.this.greenscreen = !GuiEntityZoomer.this.greenscreen;
        }).size(maxLength, 20).pos(i - maxLength / 2, j + 180).build());
        this.addRenderableWidget(Button.builder(exit, (button) -> {
            Minecraft.getInstance().setScreen(null);
        }).size(maxLength, 20).pos(i - maxLength / 2 + 140, j + 180).build());
        this.addRenderableWidget(screenshotButton = Button.builder(export, (button) -> {
            screenshot = true;
        }).size(maxLength, 20).pos(i - maxLength / 2 + 140, j + 160).build());
        this.addRenderableWidget(Button.builder(Component.translatable("/\\"), (button) -> {
            yOffset -= 5;
        }).size(20, 20).pos(i - 120 / 2 - 140, j + 160).build());
        this.addRenderableWidget(Button.builder(Component.translatable("\\/"), (button) -> {
            yOffset += 5;
        }).size(20, 20).pos(i - 120 / 2 - 120, j + 160).build());
    }

    public void renderGreenscreen(int z) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, GuiItemZoomer.GREENSCREEN);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.vertex(0.0D, (double)this.height, 0.0D).uv(0.0F, (float)this.height / 32.0F + (float)z).color(255, 255, 255, 255).endVertex();
        bufferbuilder.vertex((double)this.width, (double)this.height, 0.0D).uv((float)this.width / 32.0F, (float)this.height / 32.0F + (float)z).color(255, 255, 255, 255).endVertex();
        bufferbuilder.vertex((double)this.width, 0.0D, 0.0D).uv((float)this.width / 32.0F, (float)z).color(255, 255, 255, 255).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, 0.0D).uv(0.0F, (float)z).color(255, 255, 255, 255).endVertex();
        tesselator.end();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if(screenshot){
            screenshot = false;
            Entity renderEntity = zoomerBase.getCachedEntity();
            String name = renderEntity == null ? "none" : ForgeRegistries.ENTITY_TYPES.getKey(renderEntity.getType()).getPath();
            ScreenshotHelper.exportScreenshot(name, () -> {
                renderFocus(guiGraphics);
            });
            if(screenshotButton != null){
                screenshotButton.setFocused(false);
            }
        }
        if (getMinecraft() != null) {
            try {
                if (greenscreen) {
                    this.renderGreenscreen(-10);
                } else {
                    this.renderBackground(guiGraphics);
                }
            } catch (Exception e) {

            }
        }
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderFocus(guiGraphics);
    }

    private void renderFocus(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 1000F);
        Entity renderEntity = zoomerBase.getCachedEntity();
        float scale = prevSliderValue + (sliderValue - prevSliderValue) * Minecraft.getInstance().getFrameTime();
        if (renderEntity != null) {
            float f = 0.75F;
            float f1 = Math.max(renderEntity.getBbWidth(), renderEntity.getBbHeight());
            if ((double)f1 > 1.0D) {
                f /= f1;
            }
            float scale2 = scale;
            int i = (this.width) / 2;
            int j = (this.height + (int)((scale / 100F) * (renderEntity.getBbHeight() * 100F + yOffset))) / 2;

            if(ClientProxy.dataMimic != null){
                if(renderEntity.getType() == ClientProxy.dataMimic.getType()){
                    renderEntity = ClientProxy.dataMimic;
                }
            }
            if(renderEntity instanceof LivingEntity){
                guiGraphics.pose().translate(i, j, 10F);
                drawEntityOnScreen(guiGraphics, 100, 0, scale2,  false, -30, 135, 180, 0, 0, (LivingEntity)renderEntity);
            }
        }
        guiGraphics.pose().popPose();
        prevSliderValue = sliderValue;
    }

    public static void drawEntityOnScreen(GuiGraphics guiGraphics, int posX, int posY, float scale, boolean follow, double xRot, double yRot, double zRot, float mouseX, float mouseY, Entity entity) {
        guiGraphics.pose().pushPose();
        float f = (float) Math.atan(-mouseX / 40.0F);
        float f1 = (float) Math.atan(mouseY / 40.0F);
        float partialTicksForRender = Minecraft.getInstance().getFrameTime();
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(180.0F));
        guiGraphics.pose().scale(scale, scale, scale);
        entity.setOnGround(false);
        float partialTicks = Minecraft.getInstance().getFrameTime();
        RenderSystem.applyModelViewMatrix();

        Quaternionf quaternion1 = Axis.XP.rotationDegrees(30);
        Quaternionf quaternion2 = Axis.YP.rotationDegrees(45);
        Quaternionf quaternion = Axis.ZP.rotationDegrees(0.0F);
        quaternion.mul(quaternion1);
        guiGraphics.pose().mulPose(quaternion);
        guiGraphics.pose().mulPose(quaternion2);
        Vector3f INVENTORY_DIFFUSE_LIGHT_0 = Util.make(new Vector3f(-0.2F, 0.0F, 1.0F), Vector3f::normalize);
        Vector3f INVENTORY_DIFFUSE_LIGHT_1 = Util.make(new Vector3f(-0.2F, -1.0F, 0.0F), Vector3f::normalize);

        RenderSystem.setShaderLights(INVENTORY_DIFFUSE_LIGHT_0, INVENTORY_DIFFUSE_LIGHT_1);

        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conjugate();
        entityrenderdispatcher.overrideCameraOrientation(quaternion1);
        entityrenderdispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        entity.setYRot(0.0F);
        entity.setXRot(0.0F);
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).yBodyRot = 0.0F;
            ((LivingEntity) entity).yHeadRotO = 0.0F;
            ((LivingEntity) entity).yHeadRot = 0.0F;
        }
        entity.setOldPosAndRot();
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render( entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicksForRender, guiGraphics.pose(), multibuffersource$buffersource, 15728880);
        });
        multibuffersource$buffersource.endBatch();
        guiGraphics.flush();
        entityrenderdispatcher.setRenderShadow(true);
        guiGraphics.pose().popPose();
        Lighting.setupFor3DItems();
    }

    public boolean isPauseScreen() {
        return false;
    }
}
