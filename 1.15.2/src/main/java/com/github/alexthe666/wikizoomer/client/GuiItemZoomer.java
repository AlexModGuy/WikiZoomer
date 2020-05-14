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
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
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
import net.minecraft.util.text.ITextComponent;
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
            GuiItemZoomer.this.greenscreen = !GuiItemZoomer.this.greenscreen;
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
        ItemStack stack = zoomerBase.getStackInSlot(0);
        float scale1 = (sliderValue / 100F);
        GlStateManager.translatef(i, j, 10F);
        float scale = scale1 * 12F;
        if (!stack.isEmpty()) {
            GlStateManager.translatef(113.5F - scale1 * 100, 76 - scale1 * 100, -10F - sliderValue * 10);
            GlStateManager.scalef(scale, scale, scale);
            RenderSystem.enableRescaleNormal();
            Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(stack, 0, 0);
            RenderSystem.disableRescaleNormal();
        }
        GlStateManager.popMatrix();
    }


    public boolean isPauseScreen() {
        return false;
    }
}
