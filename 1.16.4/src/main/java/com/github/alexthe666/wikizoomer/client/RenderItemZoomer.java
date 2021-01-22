package com.github.alexthe666.wikizoomer.client;

import com.github.alexthe666.wikizoomer.BlockZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityItemZoomer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class RenderItemZoomer extends TileEntityRenderer<TileEntityItemZoomer> {
    public RenderItemZoomer(TileEntityRendererDispatcher manager) {
        super(manager);
    }

    @Override
    public void render(TileEntityItemZoomer tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack stack = ItemStack.EMPTY;
        int ticksExisted = 0;
        if (tileEntityIn != null && tileEntityIn.getWorld() != null && tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).getBlock() instanceof BlockZoomer) {
            stack = tileEntityIn.getStackInSlot(0);
            ticksExisted = tileEntityIn.ticksExisted;
        }
        float f = (float) ticksExisted - 1 + Minecraft.getInstance().getRenderPartialTicks();
        matrixStackIn.push();
        matrixStackIn.translate(0.5D, 1.25D, 0.5D);
        matrixStackIn.push();
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, f * 2F, true));
        matrixStackIn.translate(0D, 0.1F + Math.sin(f * 0.05F) * 0.1F, 0D);
        matrixStackIn.scale(0.5F, 0.5F, 0.5F);
        Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
        matrixStackIn.pop();
        matrixStackIn.pop();

    }
}
