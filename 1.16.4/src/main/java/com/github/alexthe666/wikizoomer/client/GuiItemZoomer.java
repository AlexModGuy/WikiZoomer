package com.github.alexthe666.wikizoomer.client;

import com.github.alexthe666.wikizoomer.tileentity.TileEntityZoomerBase;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.widget.Slider;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiItemZoomer extends Screen {

    private final Slider.ISlider sliderResponder;
    private TileEntityZoomerBase zoomerBase;
    private boolean greenscreen = false;
    private float sliderValue = 100;
    private float prevSliderValue = sliderValue;

    public GuiItemZoomer(TileEntityZoomerBase zoomerBase) {
        super(new TranslationTextComponent("item_zoomer"));
        this.zoomerBase = zoomerBase;
        sliderResponder = slider -> GuiItemZoomer.this.setSliderValue(0, (float) slider.sliderValue);
        func_231160_c_();
    }

    private void setSliderValue(int i, float sliderValue) {
        this.sliderValue = Math.round(MathHelper.clamp(sliderValue * 300F, 1, 300F));
        prevSliderValue = this.sliderValue;
    }

    public void func_231160_c_() {
        super.func_231160_c_();
        this.field_230710_m_.clear();
        int i = (this.field_230708_k_) / 2;
        int j = (this.field_230709_l_ - 166) / 2;
        ITextComponent exit = new TranslationTextComponent("gui.wikizoomer.close");
        ITextComponent greenscreen = new TranslationTextComponent("gui.wikizoomer.greenscreen");
        int maxLength = 120;
        this.func_230480_a_(new Slider(i - 120 / 2 - 140, j + 180, 120, 20, new TranslationTextComponent("gui.wikizoomer.zoom"), new StringTextComponent("%"), 1, 300, 100, false, true, (p_214132_1_) -> {

        }, sliderResponder){

        });
        this.func_230480_a_(new Button(i - maxLength / 2, j + 180, maxLength, 20, greenscreen, (p_214132_1_) -> {
            GuiItemZoomer.this.greenscreen = !GuiItemZoomer.this.greenscreen;
        }));
        this.func_230480_a_(new Button(i - maxLength / 2 + 140, j + 180, maxLength, 20, exit, (p_214132_1_) -> {
            Minecraft.getInstance().displayGuiScreen(null);
        }));

        this.field_230710_m_.get(0).field_230693_o_ = true;
        this.field_230710_m_.get(1).field_230693_o_ = true;
    }

    private void fill(Matrix4f p_238460_0_, int p_238460_1_, int p_238460_2_, int p_238460_3_, int p_238460_4_, int p_238460_5_) {
        if (p_238460_1_ < p_238460_3_) {
            int i = p_238460_1_;
            p_238460_1_ = p_238460_3_;
            p_238460_3_ = i;
        }

        if (p_238460_2_ < p_238460_4_) {
            int j = p_238460_2_;
            p_238460_2_ = p_238460_4_;
            p_238460_4_ = j;
        }

        float f3 = (float)(p_238460_5_ >> 24 & 255) / 255.0F;
        float f = (float)(p_238460_5_ >> 16 & 255) / 255.0F;
        float f1 = (float)(p_238460_5_ >> 8 & 255) / 255.0F;
        float f2 = (float)(p_238460_5_ & 255) / 255.0F;
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(p_238460_0_, (float)p_238460_1_, (float)p_238460_4_, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(p_238460_0_, (float)p_238460_3_, (float)p_238460_4_, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(p_238460_0_, (float)p_238460_3_, (float)p_238460_2_, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(p_238460_0_, (float)p_238460_1_, (float)p_238460_2_, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.finishDrawing();
        WorldVertexBufferUploader.draw(bufferbuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }


    @Override
    public void func_230430_a_(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (getMinecraft() != null) {
            try {
                if (greenscreen) {
                    int integer = 0X4CFF00;
                    float brightness = 1.0F;
                    int alpha = 255;
                    float f = (float) (integer >> 16 & 255) / 255.0F;
                    float f1 = (float) (integer >> 8 & 255) / 255.0F;
                    float f2 = (float) (integer & 255) / 255.0F;
                    fill(stack.getLast().getMatrix(), 0, 0, this.field_230708_k_, this.field_230709_l_, MathHelper.rgb(f * brightness, f1 * brightness, f2 * brightness) | alpha << 24);
                } else {
                    this.func_230446_a_(stack);
                }
            } catch (Exception e) {

            }
        }
        super.func_230430_a_(stack, mouseX, mouseY, partialTicks);
        int i = (this.field_230708_k_ - 248) / 2 + 10;
        int j = (this.field_230709_l_ - 166) / 2 + 8;
        GlStateManager.pushMatrix();
        GlStateManager.translatef(0, 0, 10F);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        ItemStack itemStack = zoomerBase.getStackInSlot(0);
        float scale1 = (sliderValue / 100F);
        GlStateManager.translatef(i, j, 10F);
        float scale = scale1 * 12F;
        if (!itemStack.isEmpty()) {
            GlStateManager.translatef(113.5F - scale1 * 100, 76 - scale1 * 100, -10F - sliderValue * 10);
            GlStateManager.scalef(scale, scale, scale);
            RenderSystem.enableRescaleNormal();
            Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(itemStack, 0, 0);
            RenderSystem.disableRescaleNormal();
        }
        GlStateManager.popMatrix();
    }


    public boolean func_231177_au__() {
        return false;
    }
}
