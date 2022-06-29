package com.github.alexthe666.wikizoomer.tileentity;

import com.github.alexthe666.wikizoomer.ItemAndBlockRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TileEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, "wikizoomer");
    public static final RegistryObject<BlockEntityType<TileEntityItemZoomer>> ITEM_ZOOMER_TE = TILE_ENTITIES.register("item_zoomer", () -> BlockEntityType.Builder.of(TileEntityItemZoomer::new, ItemAndBlockRegistry.ITEM_ZOOMER_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<TileEntityEntityZoomer>> ENTITY_ZOOMER_TE = TILE_ENTITIES.register("entity_zoomer", () -> BlockEntityType.Builder.of(TileEntityEntityZoomer::new, ItemAndBlockRegistry.ENTITY_ZOOMER_BLOCK.get()).build(null));

}
