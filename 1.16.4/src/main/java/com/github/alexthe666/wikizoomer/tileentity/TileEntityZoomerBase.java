package com.github.alexthe666.wikizoomer.tileentity;

import com.github.alexthe666.wikizoomer.WikiZoomerMod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public abstract class TileEntityZoomerBase extends LockableTileEntity implements ITickableTileEntity {

    private NonNullList<ItemStack> stacks = NonNullList.withSize(1, ItemStack.EMPTY);
    public int ticksExisted = 0;

    protected TileEntityZoomerBase(TileEntityType<?> typeIn) {
        super(typeIn);
    }


    public int getSizeInventory() {
        return this.stacks.size();
    }

    public boolean isEmpty() {
        for (ItemStack itemstack : this.stacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public ItemStack getStackInSlot(int index) {
        return this.stacks.get(index);
    }

    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.stacks, index, count);
    }

    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.stacks, index);
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.stacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.stacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            this.markDirty();
        }
    }

    public void func_230337_a_(BlockState p_230337_1_, CompoundNBT compound) {
        super.func_230337_a_(p_230337_1_, compound);
        this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, stacks);
    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        ItemStackHelper.saveAllItems(compound, this.stacks);
        return compound;
    }


    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    public void tick() {
        ticksExisted++;
    }

    @Override
    public void clear() {
        stacks.clear();
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        this.write(tag);
        return new SUpdateTileEntityPacket(pos, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        func_230337_a_(this.getBlockState(), packet.getNbtCompound());
    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

}
