package com.github.alexthe666.wikizoomer.client;

import com.github.alexthe666.wikizoomer.tileentity.TileEntityEntityZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityZoomerBase;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.widget.Slider;

@OnlyIn(Dist.CLIENT)
public class GuiEntityZoomer extends Screen {
    private final Slider.ISlider sliderResponder;
    private TileEntityEntityZoomer zoomerBase;
    private boolean greenscreen = false;
    private float sliderValue = 100;
    private float prevSliderValue = sliderValue;

    public GuiEntityZoomer(TileEntityEntityZoomer zoomerBase) {
        super(new TranslationTextComponent("entity_zoomer"));
        this.zoomerBase = zoomerBase;
        sliderResponder = slider -> GuiEntityZoomer.this.setSliderValue(0, (float) slider.sliderValue);
        init();
    }

    private void setSliderValue(int i, float sliderValue) {
        this.sliderValue = Math.round(MathHelper.clamp(sliderValue * 300F, 1, 300F));
        prevSliderValue = this.sliderValue;
    }

    public void init() {
        super.init();
        this.buttons.clear();
        int i = (this.width) / 2;
        int j = (this.height - 166) / 2;
        String exit = I18n.format("gui.wikizoomer.close");
        String greenscreen = I18n.format("gui.wikizoomer.greenscreen");
        int maxLength = 120;
        this.addButton(new Slider(i - 120 / 2 - 140, j + 180, 120, 20, I18n.format("gui.wikizoomer.zoom") + ": ", "%", 1, 300, 100, false, true, (p_214132_1_) -> {

        }, sliderResponder){

        });
        this.addButton(new Button(i - maxLength / 2, j + 180, maxLength, 20, greenscreen, (p_214132_1_) -> {
            GuiEntityZoomer.this.greenscreen = !GuiEntityZoomer.this.greenscreen;
        }));
        this.addButton(new Button(i - maxLength / 2 + 140, j + 180, maxLength, 20, exit, (p_214132_1_) -> {
            Minecraft.getInstance().displayGuiScreen(null);
        }));

        this.buttons.get(0).active = true;
        this.buttons.get(1).active = true;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (getMinecraft() != null) {
            try {
                if (greenscreen) {
                    int integer = 0X4CFF00;
                    float brightness = 1.0F;
                    int alpha = 255;
                    float f = (float) (integer >> 16 & 255) / 255.0F;
                    float f1 = (float) (integer >> 8 & 255) / 255.0F;
                    float f2 = (float) (integer & 255) / 255.0F;
                    fill(0, 0, this.width, this.height, MathHelper.rgb(f * brightness, f1 * brightness, f2 * brightness) | alpha << 24);
                } else {
                    this.renderBackground();
                }
            } catch (Exception e) {

            }
        }
        super.render(mouseX, mouseY, partialTicks);
        int i = (this.width - 248) / 2 + 10;
        int j = (this.height - 166) / 2 + 8;
        GlStateManager.pushMatrix();
        GlStateManager.translatef(0, 0, 10F);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Entity renderEntity = zoomerBase.getCachedEntity();
        float scale1 = (sliderValue / 100F);
        GlStateManager.translatef(i, j, 10F);
        float scale = scale1 * 100F;
        if (renderEntity != null) {
            float f = 0.75F;
            float f1 = Math.max(renderEntity.getWidth(), renderEntity.getHeight());
            if ((double)f1 > 1.0D) {
                f /= f1;
            }
            if(renderEntity instanceof LivingEntity){
                drawEntityOnScreen(115, 130, f * scale, 0, 0, (LivingEntity)renderEntity);
            }
        }
        GlStateManager.popMatrix();
    }

    public static void drawEntityOnScreen(int x, int y, float scale, float yaw, float pitch, LivingEntity entity) {
        float f = (float)Math.atan((double)(yaw / 40.0F));
        float f1 = (float)Math.atan((double)(pitch / 40.0F));
        float rotate = 180F - 45F;
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)x, (float)y, 1050.0F + scale);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale((float)scale, (float)scale, (float)scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(-30.0F);
        Quaternion quaternion2 = Vector3f.YP.rotationDegrees(rotate);
        quaternion.multiply(quaternion1);
        matrixstack.rotate(quaternion);
        matrixstack.rotate(quaternion2);
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        quaternion1.conjugate();
        entityrenderermanager.setCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        entityrenderermanager.renderEntityStatic(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
        irendertypebuffer$impl.finish();
        entityrenderermanager.setRenderShadow(true);
        RenderSystem.popMatrix();
    }


    public boolean isPauseScreen() {
        return false;
    }
}
