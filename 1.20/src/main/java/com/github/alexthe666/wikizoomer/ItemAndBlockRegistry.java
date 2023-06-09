package com.github.alexthe666.wikizoomer;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = "wikizoomer", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemAndBlockRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "wikizoomer");
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "wikizoomer");

    public static final RegistryObject<Item> ENTITY_BINDER_ITEM = ITEMS.register("entity_binder", () -> new ItemEntityBinder());
    public static final RegistryObject<Item> DATA_COPIER = ITEMS.register("data_copier", () -> new ItemDataCopier());
    public static final RegistryObject<Block> ITEM_ZOOMER_BLOCK = BLOCKS.register("item_zoomer", () -> new BlockZoomer(true));
    public static final RegistryObject<Block> ENTITY_ZOOMER_BLOCK = BLOCKS.register("entity_zoomer", () -> new BlockZoomer(false));
    public static final RegistryObject<Item> ITEM_ZOOMER_BLOCK_ITEM = ITEMS.register("item_zoomer", () -> new BlockItem(ItemAndBlockRegistry.ITEM_ZOOMER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> ENTITY_ZOOMER_BLOCK_ITEM = ITEMS.register("entity_zoomer", () -> new BlockItem(ItemAndBlockRegistry.ENTITY_ZOOMER_BLOCK.get(), new Item.Properties()));

}
