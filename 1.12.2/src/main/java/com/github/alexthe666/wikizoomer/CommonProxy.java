package com.github.alexthe666.wikizoomer;

import com.github.alexthe666.wikizoomer.tileentity.TileEntityEntityZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityItemZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityZoomerBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {

    public void setup() {
        GameRegistry.registerTileEntity(TileEntityItemZoomer.class, "wikizoomer:item_zoomer");
        GameRegistry.registerTileEntity(TileEntityEntityZoomer.class, "wikizoomer:entity_zoomer");
    }

    public void openItemZoomerGui(TileEntityZoomerBase tileEntity) {
    }

    public void openEntityZoomerGui(TileEntityEntityZoomer tileEntity) {
    }

    public void postSetup() {
    }

    public void onBlockRegister() {
    }
}
