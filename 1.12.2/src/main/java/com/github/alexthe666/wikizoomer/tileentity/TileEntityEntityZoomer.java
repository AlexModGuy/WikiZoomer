package com.github.alexthe666.wikizoomer.tileentity;

import com.github.alexthe666.wikizoomer.WikiZoomerMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Function;

public class TileEntityEntityZoomer extends TileEntityZoomerBase {
    private Entity chachedEntity = null;

    public TileEntityEntityZoomer() {
        super();
    }

    @Override
    public void update() {
        super.update();
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
                    if(this.getStackInSlot(0).getTagCompound() != null){
                        NBTTagCompound nbt = (NBTTagCompound)this.getStackInSlot(0).getTagCompound().getCompoundTag("EntityTag");
                        chachedEntity = EntityList.createEntityFromNBT(nbt, this.getWorld());
                        if(chachedEntity instanceof EntityLivingBase){
                            ((EntityLivingBase) chachedEntity).hurtTime = 0;
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
