package com.github.alexthe666.wikizoomer;

import com.github.alexthe666.wikizoomer.tileentity.TileEntityRegistry;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("wikizoomer")
@Mod.EventBusSubscriber(modid = "wikizoomer")
public class WikiZoomerMod {
    public static final Logger LOGGER = LogManager.getLogger();
    public static CommonProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public WikiZoomerMod() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::registerTabItems);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(PROXY);
        ItemAndBlockRegistry.ITEMS.register(modEventBus);
        ItemAndBlockRegistry.BLOCKS.register(modEventBus);
        TileEntityRegistry.TILE_ENTITIES.register(modEventBus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PROXY.setup();
    }

    private void registerTabItems(final BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS){
            event.accept(ItemAndBlockRegistry.ITEM_ZOOMER_BLOCK_ITEM.get());
            event.accept(ItemAndBlockRegistry.ENTITY_ZOOMER_BLOCK_ITEM.get());
        }
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ItemAndBlockRegistry.ENTITY_BINDER_ITEM.get());
            event.accept(ItemAndBlockRegistry.DATA_COPIER.get());
        }
    }
}