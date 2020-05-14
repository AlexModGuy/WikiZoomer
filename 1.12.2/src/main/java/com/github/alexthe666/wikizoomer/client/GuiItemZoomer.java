package com.github.alexthe666.wikizoomer.client;

import com.github.alexthe666.wikizoomer.tileentity.TileEntityZoomerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiItemZoomer extends GuiScreen {

    private final GuiPageButtonList.GuiResponder sliderResponder;
    private TileEntityZoomerBase zoomerBase;
    private boolean greenscreen = false;
    private float sliderValue = 100;
    private float prevSliderValue = sliderValue;

    public GuiItemZoomer(TileEntityZoomerBase zoomerBase) {
        super();
        this.zoomerBase = zoomerBase;
        sliderResponder = new GuiPageButtonList.GuiResponder() {
            @Override
            public void setEntryValue(int id, boolean value) {

            }

            @Override
            public void setEntryValue(int id, float value) {
                GuiItemZoomer.this.setSliderValue(id, value);
            }

            @Override
            public void setEntryValue(int id, String value) {

            }
        };
        initGui();
    }

    private void setSliderValue(int i, float sliderValue) {
        this.sliderValue = sliderValue;
        prevSliderValue = this.sliderValue;
    }

    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        int i = (this.width) / 2;
        int j = (this.height - 166) / 2;
        String exit = I18n.format("gui.wikizoomer.close");
        String greenscreen = I18n.format("gui.wikizoomer.greenscreen");
        int maxLength = 120;
        net.minecraft.client.gui.GuiSlider.FormatHelper formatHelper = new net.minecraft.client.gui.GuiSlider.FormatHelper() {
            @Override
            public String getText(int id, String name, float value) {
                return name + ": " + (int)Math.round(value) + "%";
            }
        };
        net.minecraft.client.gui.GuiSlider slider = new net.minecraft.client.gui.GuiSlider(sliderResponder, 0, i - 120 / 2 - 140, j + 180, I18n.format("gui.wikizoomer.zoom"), 1, 300, sliderValue, formatHelper);
        slider.width = 120;
        slider.height = 20;
        this.addButton(slider);
        this.addButton(new GuiButton(1, i - maxLength / 2, j + 180, maxLength, 20, greenscreen));
        this.addButton(new GuiButton(2, i - maxLength / 2 + 140, j + 180, maxLength, 20, exit));

        this.buttonList.get(0).enabled = true;
        this.buttonList.get(1).enabled = true;
        this.buttonList.get(2).enabled = true;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled && button.id == 1) {
            greenscreen = !greenscreen;
        }
        if (button.enabled && button.id == 2) {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
        initGui();
    }



    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (Minecraft.getMinecraft() != null) {
            try {
                if (greenscreen) {
                    int integer = 0X4CFF00;
                    float brightness = 1.0F;
                    int alpha = 255;
                    float f = (float) (integer >> 16 & 255) / 255.0F;
                    float f1 = (float) (integer >> 8 & 255) / 255.0F;
                    float f2 = (float) (integer & 255) / 255.0F;
                    drawRect(0, 0, this.width, this.height, MathHelper.rgb(f * brightness, f1 * brightness, f2 * brightness) | alpha << 24);
                } else {
                    this.drawDefaultBackground();
                }
            } catch (Exception e) {

            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        int i = (this.width - 248) / 2 + 10;
        int j = (this.height - 166) / 2 + 8;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 10F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ItemStack stack = zoomerBase.getStackInSlot(0);
        GlStateManager.translate(i, j, 10F);
        float scale = sliderValue/100F * 12F;
        if (!stack.isEmpty()) {
            GlStateManager.translate(113.5F - sliderValue, 76 - sliderValue,  Math.min(1000 -sliderValue * 20, 50));
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.enableLighting();
            RenderHelper.enableGUIStandardItemLighting();
            Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
        }
        GlStateManager.popMatrix();
    }


    public boolean doesGuiPauseGame() {
        return false;
    }
}
