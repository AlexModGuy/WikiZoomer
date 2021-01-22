package com.github.alexthe666.wikizoomer.tileentity;

import com.github.alexthe666.wikizoomer.WikiZoomerMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.function.Function;

public class TileEntityEntityZoomer extends TileEntityZoomerBase {
    private Entity chachedEntity = null;

    public TileEntityEntityZoomer() {
        super(WikiZoomerMod.ENTITY_ZOOMER_TE);
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("block.wikizoomer.entity_zoomer");
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getStackInSlot(0).getItem() != WikiZoomerMod.ENTITY_BINDER_ITEM) {
            chachedEntity = null;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        super.setInventorySlotContents(index, stack);
        chachedEntity = null;
    }

    @Nullable
    public Entity getCachedEntity(){
        if(this.getStackInSlot(0).getItem() == WikiZoomerMod.ENTITY_BINDER_ITEM){
            if(chachedEntity != null){
                return chachedEntity;
            }else{
                try{
                    if(this.getStackInSlot(0).getTag() != null){
                        CompoundNBT nbt = (CompoundNBT)this.getStackInSlot(0).getTag().get("EntityTag");
                        chachedEntity = EntityType.func_220335_a(nbt, this.getWorld(), Function.identity());
                        if(chachedEntity instanceof LivingEntity){
                            ((LivingEntity) chachedEntity).hurtTime = 0;
                        }
                        return chachedEntity;
                    }
                }catch (Exception e){
                }
            }
        }
        return null;
    }

}
