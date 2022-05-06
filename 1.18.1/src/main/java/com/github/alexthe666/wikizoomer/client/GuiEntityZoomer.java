package com.github.alexthe666.wikizoomer.client;

import com.github.alexthe666.wikizoomer.ClientProxy;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityEntityZoomer;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.Slider;

@OnlyIn(Dist.CLIENT)
public class GuiEntityZoomer extends Screen {
    private final Slider.ISlider sliderResponder;
    private TileEntityEntityZoomer zoomerBase;
    private boolean greenscreen = false;
    private float sliderValue = 100;
    private float prevSliderValue = sliderValue;
    private boolean screenshot = false;
    private int yOffset = 0;

    public GuiEntityZoomer(TileEntityEntityZoomer zoomerBase) {
        super(new TranslatableComponent("entity_zoomer"));
        this.zoomerBase = zoomerBase;
        sliderResponder = slider -> GuiEntityZoomer.this.setSliderValue(0, (float) slider.sliderValue);
    }

    private void setSliderValue(int i, float sliderValue) {
        this.sliderValue = Math.round(Mth.clamp(sliderValue * 300F, 1, 300F));
        prevSliderValue = this.sliderValue;
    }

    protected void init() {
        super.init();
        this.clearWidgets();
        int i = (this.width) / 2;
        int j = (this.height - 166) / 2;
        TranslatableComponent exit = new TranslatableComponent("gui.wikizoomer.close");
        TranslatableComponent greenscreen = new TranslatableComponent("gui.wikizoomer.greenscreen");
        TranslatableComponent export = new TranslatableComponent("gui.wikizoomer.export_png");
        int maxLength = 120;
        this.addRenderableWidget(new Slider(i - 120 / 2 - 140, j + 180, 120, 20, new TranslatableComponent("gui.wikizoomer.zoom"), new TextComponent("%"), 1, 300, 100, false, true, (p_214132_1_) -> {

        }, sliderResponder){

        });
        this.addRenderableWidget(new Button(i - maxLength / 2, j + 180, maxLength, 20, greenscreen, (p_214132_1_) -> {
            GuiEntityZoomer.this.greenscreen = !GuiEntityZoomer.this.greenscreen;
        }));
        this.addRenderableWidget(new Button(i - maxLength / 2 + 140, j + 180, maxLength, 20, exit, (p_214132_1_) -> {
            Minecraft.getInstance().setScreen(null);
        }));
        this.addRenderableWidget(new Button(i - maxLength / 2 + 140, j + 160, maxLength, 20, export, (p_214132_1_) -> {
            screenshot = true;
        }));
        this.addRenderableWidget(new Button(i - 120 / 2 - 140, j + 160, 20, 20, new TextComponent("/\\"), (p_214132_1_) -> {
            yOffset -= 5;
        }));
        this.addRenderableWidget(new Button(i - 120 / 2 - 120, j + 160, 20, 20, new TextComponent("\\/"), (p_214132_1_) -> {
            yOffset += 5;
        }));
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
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        if(screenshot){
            screenshot = false;
            Entity renderEntity = zoomerBase.getCachedEntity();
            String name = renderEntity == null ? "none" : renderEntity.getType().getRegistryName().getPath();
            ScreenshotHelper.exportScreenshot(name, () -> {
                renderFocus(new PoseStack());
            });
        }
        if (getMinecraft() != null) {
            try {
                if (greenscreen) {
                    this.renderGreenscreen(-10);
                } else {
                    this.renderBackground(stack);
                }
            } catch (Exception e) {

            }
        }
        super.render(stack, mouseX, mouseY, partialTicks);
        renderFocus(stack);
    }

    private void renderFocus(PoseStack stack) {
        stack.pushPose();
        stack.translate(0, 0, 10F);
        Entity renderEntity = zoomerBase.getCachedEntity();
        float scale1 = (sliderValue / 100F);
        float scale = scale1 * 100F;
        if (renderEntity != null) {
            float f = 0.75F;
            float f1 = Math.max(renderEntity.getBbWidth(), renderEntity.getBbHeight());
            if ((double)f1 > 1.0D) {
                f /= f1;
            }
            float scale2 = scale;
            int i = (this.width) / 2;
            int j = (this.height + (int)(scale1 * (renderEntity.getBbHeight() * 100F + yOffset))) / 2;

            if(ClientProxy.dataMimic != null){
                if(renderEntity.getType() == ClientProxy.dataMimic.getType()){
                    renderEntity = ClientProxy.dataMimic;
                }
            }
            if(renderEntity instanceof LivingEntity){
                stack.translate(i, j, 10F);
                drawEntityOnScreen(stack, 100, 0, scale2,  false, -30, 135, 180, 0, 0, (LivingEntity)renderEntity);
            }
        }
        stack.popPose();
    }

    public static void drawEntityOnScreen(PoseStack matrixstack, int posX, int posY, float scale, boolean follow, double xRot, double yRot, double zRot, float mouseX, float mouseY, Entity entity) {
        float f = (float) Math.atan(-mouseX / 40.0F);
        float f1 = (float) Math.atan(mouseY / 40.0F);
        float partialTicksForRender = Minecraft.getInstance().getFrameTime();
        matrixstack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        matrixstack.scale(scale, scale, scale);
        entity.setOnGround(false);
        float partialTicks = Minecraft.getInstance().getFrameTime();
        RenderSystem.applyModelViewMatrix();

        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(30);
        Quaternion quaternion2 = Vector3f.YP.rotationDegrees(45);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(0.0F);
        quaternion.mul(quaternion1);
        matrixstack.mulPose(quaternion);
        matrixstack.mulPose(quaternion2);
        Vector3f INVENTORY_DIFFUSE_LIGHT_0 = Util.make(new Vector3f(-0.2F, 0.0F, 1.0F), Vector3f::normalize);
        Vector3f INVENTORY_DIFFUSE_LIGHT_1 = Util.make(new Vector3f(-0.2F, -1.0F, 0.0F), Vector3f::normalize);

        RenderSystem.setShaderLights(INVENTORY_DIFFUSE_LIGHT_0, INVENTORY_DIFFUSE_LIGHT_1);

        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
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
            entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicksForRender, matrixstack, multibuffersource$buffersource, 15728880);
        });
        multibuffersource$buffersource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

    public boolean isPauseScreen() {
        return false;
    }
}
