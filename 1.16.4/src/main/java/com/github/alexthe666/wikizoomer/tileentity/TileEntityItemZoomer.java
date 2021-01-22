package com.github.alexthe666.wikizoomer.tileentity;

import com.github.alexthe666.wikizoomer.WikiZoomerMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class TileEntityItemZoomer extends TileEntityZoomerBase {

    public TileEntityItemZoomer() {
        super(WikiZoomerMod.ITEM_ZOOMER_TE);
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("block.wikizoomer.item_zoomer");
    }

}
