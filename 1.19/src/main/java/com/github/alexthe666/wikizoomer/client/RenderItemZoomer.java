package com.github.alexthe666.wikizoomer.client;

import com.github.alexthe666.wikizoomer.BlockZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityItemZoomer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

public class RenderItemZoomer implements BlockEntityRenderer<TileEntityItemZoomer> {

    public RenderItemZoomer(BlockEntityRendererProvider.Context manager) {
    }

    @Override
    public void render(TileEntityItemZoomer tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack stack = ItemStack.EMPTY;
        int ticksExisted = 0;
        if (tileEntityIn != null && tileEntityIn.getLevel() != null && tileEntityIn.getLevel().getBlockState(tileEntityIn.getBlockPos()).getBlock() instanceof BlockZoomer) {
            stack = tileEntityIn.getItem(0);
            ticksExisted = tileEntityIn.ticksExisted;
        }
        float rrr = (float) ticksExisted - 1 + partialTicks;
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5D, 1.25D, 0.5D);
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(new Quaternionf().rotationY((float) Math.toRadians(rrr * 2F)));
        matrixStackIn.translate(0D, 0.1F + Math.sin(rrr * 0.05F) * 0.1F, 0D);
        matrixStackIn.scale(0.5F, 0.5F, 0.5F);
        BakedModel ibakedmodel = Minecraft.getInstance().getItemRenderer().getModel(stack, tileEntityIn.getLevel(), (LivingEntity)null, 0);
        Minecraft.getInstance().getItemRenderer().render(stack, ItemTransforms.TransformType.FIXED, false, matrixStackIn, bufferIn, combinedLightIn, OverlayTexture.NO_OVERLAY, ibakedmodel);
        matrixStackIn.popPose();
        matrixStackIn.popPose();

    }
}
