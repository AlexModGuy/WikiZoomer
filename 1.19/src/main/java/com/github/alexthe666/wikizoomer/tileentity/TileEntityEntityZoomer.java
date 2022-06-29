package com.github.alexthe666.wikizoomer.tileentity;

import com.github.alexthe666.wikizoomer.ItemAndBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Function;

public class TileEntityEntityZoomer extends TileEntityZoomerBase {
    private Entity chachedEntity = null;

    public TileEntityEntityZoomer(BlockPos pos, BlockState state) {
        super(TileEntityRegistry.ENTITY_ZOOMER_TE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileEntityEntityZoomer entity) {
        entity.baseTick();
        if (entity.getItem(0).getItem() != ItemAndBlockRegistry.ENTITY_BINDER_ITEM.get()) {
            entity.chachedEntity = null;
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        super.setItem(index, stack);
        chachedEntity = null;
    }

    @Nullable
    public Entity getCachedEntity(){
        if(this.getItem(0).getItem() == ItemAndBlockRegistry.ENTITY_BINDER_ITEM.get()){
            if(chachedEntity != null){
                return chachedEntity;
            }else{
                try{
                    if(this.getItem(0).getTag() != null){
                        CompoundTag nbt = (CompoundTag)this.getItem(0).getTag().get("EntityTag");
                        chachedEntity = EntityType.loadEntityRecursive(nbt, this.getLevel(), Function.identity());
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

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.wikizoomer.entity_zoomer");
    }
}
