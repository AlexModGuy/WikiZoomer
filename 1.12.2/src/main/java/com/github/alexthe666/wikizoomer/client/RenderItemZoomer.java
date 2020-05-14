package com.github.alexthe666.wikizoomer.client;

import com.github.alexthe666.wikizoomer.BlockZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityItemZoomer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderItemZoomer extends TileEntitySpecialRenderer<TileEntityItemZoomer> {
    public RenderItemZoomer() {
        super();
    }

    @Override
    public void render(TileEntityItemZoomer tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {        Entity renderEntity = null;
        ItemStack stack = ItemStack.EMPTY;
        int ticksExisted = 0;
        if (tileEntityIn != null && tileEntityIn.getWorld() != null && tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).getBlock() instanceof BlockZoomer) {
            stack = tileEntityIn.getStackInSlot(0);
            ticksExisted = tileEntityIn.ticksExisted;
        }
        float f = (float) ticksExisted - 1 + Minecraft.getMinecraft().getRenderPartialTicks();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0.5D, 1.25D, 0.5D);
        GlStateManager.pushMatrix();
        GlStateManager.rotate(f * 2F, 0, 1, 0);
        GlStateManager.translate(0D, 0.1F + Math.sin(f * 0.05F) * 0.1F, 0D);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();

    }
}
