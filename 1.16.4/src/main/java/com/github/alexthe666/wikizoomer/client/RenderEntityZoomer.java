package com.github.alexthe666.wikizoomer.client;

import com.github.alexthe666.wikizoomer.BlockZoomer;
import com.github.alexthe666.wikizoomer.ClientProxy;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityEntityZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityItemZoomer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class RenderEntityZoomer extends TileEntityRenderer<TileEntityEntityZoomer> {
    public RenderEntityZoomer(TileEntityRendererDispatcher manager) {
        super(manager);
    }

    @Override
    public void render(TileEntityEntityZoomer tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Entity renderEntity = null;
        int ticksExisted = 0;
        if (tileEntityIn != null && tileEntityIn.getWorld() != null && tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).getBlock() instanceof BlockZoomer) {
            renderEntity = tileEntityIn.getCachedEntity();
            ticksExisted = tileEntityIn.ticksExisted;
        }
        float rrr = (float) ticksExisted - 1 + Minecraft.getInstance().getRenderPartialTicks();
        matrixStackIn.push();
        matrixStackIn.translate(0.5D, 1.0D, 0.5D);
        matrixStackIn.push();
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, rrr * 2F, true));
        matrixStackIn.translate(0D, 0.1F + Math.sin(rrr * 0.05F) * 0.1F, 0D);
        matrixStackIn.scale(0.5F, 0.5F, 0.5F);
        if(renderEntity != null){
            if(ClientProxy.dataMimic != null){
                if(renderEntity.getType() == ClientProxy.dataMimic.getType()){
                    renderEntity = ClientProxy.dataMimic;
                }
            }
            float f = 0.75F;
            float f1 = Math.max(renderEntity.getWidth(), renderEntity.getHeight());
            if ((double)f1 > 1.0D) {
                f /= f1;
            }
            matrixStackIn.translate(0.0D, 0.4000000059604645D, 0.0D);
            matrixStackIn.translate(0.0D, -0.20000000298023224D, 0.0D);
            matrixStackIn.scale(f, f, f);
            Minecraft.getInstance().getRenderManager().renderEntityStatic(renderEntity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, matrixStackIn, bufferIn, combinedLightIn);

        }
        matrixStackIn.pop();
        matrixStackIn.pop();

    }
}
