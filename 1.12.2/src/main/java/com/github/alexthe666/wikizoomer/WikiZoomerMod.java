package com.github.alexthe666.wikizoomer;

import com.github.alexthe666.wikizoomer.tileentity.TileEntityEntityZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityItemZoomer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "wikizoomer", name = "Wiki Zoomer",  version = "1.0.0")
@Mod.EventBusSubscriber
public class WikiZoomerMod {
    private static final Logger LOGGER = LogManager.getLogger();
    @SidedProxy(clientSide = "com.github.alexthe666.wikizoomer.ClientProxy", serverSide = "com.github.alexthe666.wikizoomer.CommonProxy")
    public static CommonProxy PROXY;

    public static final Item ENTITY_BINDER_ITEM = new ItemEntityBinder();
    public static final Block ITEM_ZOOMER_BLOCK = new BlockZoomer(true);
    public static final Block ENTITY_ZOOMER_BLOCK = new BlockZoomer(false);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PROXY.setup();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(PROXY);
        PROXY.postSetup();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ITEM_ZOOMER_BLOCK);
        event.getRegistry().registerAll(ENTITY_ZOOMER_BLOCK);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(ENTITY_BINDER_ITEM);
        event.getRegistry().register(new ItemBlock(ITEM_ZOOMER_BLOCK).setRegistryName(ITEM_ZOOMER_BLOCK.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ENTITY_ZOOMER_BLOCK).setRegistryName(ENTITY_ZOOMER_BLOCK.getRegistryName()));
        PROXY.onBlockRegister();
    }

}