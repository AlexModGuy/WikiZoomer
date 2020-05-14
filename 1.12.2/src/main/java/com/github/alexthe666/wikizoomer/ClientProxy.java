package com.github.alexthe666.wikizoomer;

import com.github.alexthe666.wikizoomer.client.GuiEntityZoomer;
import com.github.alexthe666.wikizoomer.client.GuiItemZoomer;
import com.github.alexthe666.wikizoomer.client.RenderEntityZoomer;
import com.github.alexthe666.wikizoomer.client.RenderItemZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityEntityZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityItemZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityZoomerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {

    @Override
    public void setup() {
        super.setup();
          ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemZoomer.class, new RenderItemZoomer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEntityZoomer.class, new RenderEntityZoomer());
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        ModelBakery.registerItemVariants(WikiZoomerMod.ENTITY_BINDER_ITEM, new ResourceLocation("wikizoomer:entity_binder"), new ResourceLocation("wikizoomer:entity_binder_bound"));
        ModelLoader.setCustomModelResourceLocation(WikiZoomerMod.ENTITY_BINDER_ITEM, 0, new ModelResourceLocation("wikizoomer:entity_binder", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(WikiZoomerMod.ITEM_ZOOMER_BLOCK), 0, new ModelResourceLocation("wikizoomer:item_zoomer_item", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(WikiZoomerMod.ENTITY_ZOOMER_BLOCK), 0, new ModelResourceLocation("wikizoomer:entity_zoomer_item", "inventory"));
    }

        @Override
    public void onBlockRegister() {
        super.onBlockRegister();
 }

    @Override
    public void openItemZoomerGui(TileEntityZoomerBase tileEntity) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiItemZoomer(tileEntity));
    }

    @Override
    public void openEntityZoomerGui(TileEntityEntityZoomer tileEntity) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiEntityZoomer(tileEntity));
    }
}
