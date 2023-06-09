package com.github.alexthe666.wikizoomer.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityItemZoomer extends TileEntityZoomerBase {

    public TileEntityItemZoomer(BlockPos pos, BlockState state) {
        super(TileEntityRegistry.ITEM_ZOOMER_TE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileEntityItemZoomer entity) {
        entity.baseTick();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.wikizoomer.item_zoomer");
    }
}
