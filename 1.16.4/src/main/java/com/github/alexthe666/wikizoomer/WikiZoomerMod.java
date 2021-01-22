package com.github.alexthe666.wikizoomer;

import com.github.alexthe666.wikizoomer.tileentity.TileEntityEntityZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityItemZoomer;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("wikizoomer")
@Mod.EventBusSubscriber(modid = "wikizoomer", bus = Mod.EventBusSubscriber.Bus.MOD)
public class WikiZoomerMod {
    private static final Logger LOGGER = LogManager.getLogger();
    public static CommonProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static final Item ENTITY_BINDER_ITEM = new ItemEntityBinder();
    public static final Item DATA_COPIER = new ItemDataCopier();
    public static final Block ITEM_ZOOMER_BLOCK = new BlockZoomer(true);
    public static final Block ENTITY_ZOOMER_BLOCK = new BlockZoomer(false);
    public static final TileEntityType ITEM_ZOOMER_TE = TileEntityType.Builder.create(TileEntityItemZoomer::new, ITEM_ZOOMER_BLOCK).build(null).setRegistryName("item_zoomer_te");
    public static final TileEntityType ENTITY_ZOOMER_TE = TileEntityType.Builder.create(TileEntityEntityZoomer::new, ENTITY_ZOOMER_BLOCK).build(null).setRegistryName("entity_zoomer_te");

    public WikiZoomerMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PROXY.setup();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ITEM_ZOOMER_BLOCK);
        event.getRegistry().registerAll(ENTITY_ZOOMER_BLOCK);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(ENTITY_BINDER_ITEM);
        event.getRegistry().register(DATA_COPIER);
        event.getRegistry().register(new BlockItem(ITEM_ZOOMER_BLOCK, new Item.Properties().group(ItemGroup.DECORATIONS)).setRegistryName(ITEM_ZOOMER_BLOCK.getRegistryName()));
        event.getRegistry().register(new BlockItem(ENTITY_ZOOMER_BLOCK, new Item.Properties().group(ItemGroup.DECORATIONS)).setRegistryName(ENTITY_ZOOMER_BLOCK.getRegistryName()));
    }

    @SubscribeEvent
    public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().registerAll(ITEM_ZOOMER_TE);
        event.getRegistry().registerAll(ENTITY_ZOOMER_TE);
    }
}