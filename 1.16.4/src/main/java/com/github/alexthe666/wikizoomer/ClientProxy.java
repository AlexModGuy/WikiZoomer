package com.github.alexthe666.wikizoomer;

import com.github.alexthe666.wikizoomer.client.GuiEntityZoomer;
import com.github.alexthe666.wikizoomer.client.GuiItemZoomer;
import com.github.alexthe666.wikizoomer.client.RenderEntityZoomer;
import com.github.alexthe666.wikizoomer.client.RenderItemZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityEntityZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityZoomerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy{
    public static Entity dataMimic = null;

    @Override
    public void setup() {
        ClientRegistry.bindTileEntityRenderer(WikiZoomerMod.ITEM_ZOOMER_TE, manager -> new RenderItemZoomer(manager));
        ClientRegistry.bindTileEntityRenderer(WikiZoomerMod.ENTITY_ZOOMER_TE, manager -> new RenderEntityZoomer(manager));
        RenderTypeLookup.setRenderLayer(WikiZoomerMod.ITEM_ZOOMER_BLOCK, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WikiZoomerMod.ENTITY_ZOOMER_BLOCK, RenderType.getCutout());
        ItemModelsProperties.func_239418_a_(WikiZoomerMod.ENTITY_BINDER_ITEM, new ResourceLocation("bound"), (p_239428_0_, p_239428_1_, p_239428_2_) -> {
            return ItemEntityBinder.isEntityBound(p_239428_0_) ? 1 : 0;
        });
    }

    @Override
    public void openItemZoomerGui(TileEntityZoomerBase tileEntity) {
        Minecraft.getInstance().displayGuiScreen(new GuiItemZoomer(tileEntity));
    }

    @Override
    public void openEntityZoomerGui(TileEntityEntityZoomer tileEntity) {
        Minecraft.getInstance().displayGuiScreen(new GuiEntityZoomer(tileEntity));
    }

    @Override
    public void onDataCopierUse(LivingEntity target) {
        this.dataMimic = target;
    }
}
