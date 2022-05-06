package com.github.alexthe666.wikizoomer.tileentity;

import com.github.alexthe666.wikizoomer.WikiZoomerMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class TileEntityItemZoomer extends TileEntityZoomerBase {

    public TileEntityItemZoomer(BlockPos pos, BlockState state) {
        super(WikiZoomerMod.ITEM_ZOOMER_TE, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileEntityItemZoomer entity) {
        entity.baseTick();
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("block.wikizoomer.item_zoomer");
    }
}
