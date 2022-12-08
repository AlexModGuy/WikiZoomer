package com.github.alexthe666.wikizoomer.client;

import com.github.alexthe666.wikizoomer.BlockZoomer;
import com.github.alexthe666.wikizoomer.ClientProxy;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityEntityZoomer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;

public class RenderEntityZoomer implements BlockEntityRenderer<TileEntityEntityZoomer> {

    public RenderEntityZoomer(BlockEntityRendererProvider.Context manager) {
    }

    @Override
    public void render(TileEntityEntityZoomer tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Entity renderEntity = null;
        int ticksExisted = 0;
        if (tileEntityIn != null && tileEntityIn.getLevel() != null && tileEntityIn.getLevel().getBlockState(tileEntityIn.getBlockPos()).getBlock() instanceof BlockZoomer) {
            renderEntity = tileEntityIn.getCachedEntity();
            ticksExisted = tileEntityIn.ticksExisted;
        }
        float rrr = (float) ticksExisted - 1 + partialTicks;
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5D, 1.0D, 0.5D);
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(new Quaternionf().rotationY((float) Math.toRadians(rrr * 2F)));
        matrixStackIn.translate(0D, 0.1F + Math.sin(rrr * 0.05F) * 0.1F, 0D);
        matrixStackIn.scale(0.5F, 0.5F, 0.5F);
        if(renderEntity != null){
            if(ClientProxy.dataMimic != null){
                if(renderEntity.getType() == ClientProxy.dataMimic.getType()){
                    renderEntity = ClientProxy.dataMimic;
                }
            }
            float f = 0.75F;
            float f1 = Math.max(renderEntity.getBbWidth(), renderEntity.getBbHeight());
            if ((double)f1 > 1.0D) {
                f /= f1;
            }
            matrixStackIn.translate(0.0D, 0.4000000059604645D, 0.0D);
            matrixStackIn.translate(0.0D, -0.20000000298023224D, 0.0D);
            matrixStackIn.scale(f, f, f);
            renderEntity.setYRot(0.0F);
            renderEntity.setXRot(0.0F);
            if (renderEntity instanceof LivingEntity) {
                ((LivingEntity) renderEntity).yBodyRot = 0.0F;
                ((LivingEntity) renderEntity).yHeadRotO = 0.0F;
                ((LivingEntity) renderEntity).yHeadRot = 0.0F;
            }
            Minecraft.getInstance().getEntityRenderDispatcher().render(renderEntity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, matrixStackIn, bufferIn, combinedLightIn);

        }
        matrixStackIn.popPose();
        matrixStackIn.popPose();

    }
}
