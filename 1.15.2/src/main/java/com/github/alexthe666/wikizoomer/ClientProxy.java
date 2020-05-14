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
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy{

    @Override
    public void setup() {
        ClientRegistry.bindTileEntityRenderer(WikiZoomerMod.ITEM_ZOOMER_TE, manager -> new RenderItemZoomer(manager));
        ClientRegistry.bindTileEntityRenderer(WikiZoomerMod.ENTITY_ZOOMER_TE, manager -> new RenderEntityZoomer(manager));
        RenderTypeLookup.setRenderLayer(WikiZoomerMod.ITEM_ZOOMER_BLOCK, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WikiZoomerMod.ENTITY_ZOOMER_BLOCK, RenderType.getCutout());

    }

    @Override
    public void openItemZoomerGui(TileEntityZoomerBase tileEntity) {
        Minecraft.getInstance().displayGuiScreen(new GuiItemZoomer(tileEntity));
    }

    @Override
    public void openEntityZoomerGui(TileEntityEntityZoomer tileEntity) {
        Minecraft.getInstance().displayGuiScreen(new GuiEntityZoomer(tileEntity));
    }
}
