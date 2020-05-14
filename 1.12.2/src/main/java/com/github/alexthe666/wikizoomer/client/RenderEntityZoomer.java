package com.github.alexthe666.wikizoomer.client;

import com.github.alexthe666.wikizoomer.BlockZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityEntityZoomer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntityZoomer extends TileEntitySpecialRenderer<TileEntityEntityZoomer> {
    public RenderEntityZoomer() {
        super();
    }

    @Override
    public void render(TileEntityEntityZoomer tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {        Entity renderEntity = null;
        int ticksExisted = 0;
        if (tileEntityIn != null && tileEntityIn.getWorld() != null && tileEntityIn.getWorld().getBlockState(tileEntityIn.getPos()).getBlock() instanceof BlockZoomer) {
            renderEntity = tileEntityIn.getCachedEntity();
            ticksExisted = tileEntityIn.ticksExisted;
        }
        float rrr = (float) ticksExisted - 1 + Minecraft.getMinecraft().getRenderPartialTicks();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0.5D, 1.0D, 0.5D);
        GlStateManager.pushMatrix();
        GlStateManager.rotate(rrr * 2F, 0, 1, 0);
        GlStateManager.translate(0D, 0.1F + Math.sin(rrr * 0.05F) * 0.1F, 0D);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        if(renderEntity != null){
            float f = 0.75F;
            float f1 = Math.max(renderEntity.width, renderEntity.height);
            if ((double)f1 > 1.0D) {
                f /= f1;
            }
            GlStateManager.translate(0.0D, 0.4000000059604645D, 0.0D);
            GlStateManager.translate(0.0D, -0.20000000298023224D, 0.0D);
            GlStateManager.scale(f, f, f);
            if(renderEntity instanceof EntityLivingBase)
                ((EntityLivingBase)renderEntity).rotationYawHead = 0;
            Minecraft.getMinecraft().getRenderManager().renderEntity(renderEntity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);

        }
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();

    }
}
