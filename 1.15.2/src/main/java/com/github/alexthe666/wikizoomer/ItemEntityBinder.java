package com.github.alexthe666.wikizoomer;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ItemEntityBinder extends Item {

    public ItemEntityBinder() {
        super(new Properties().group(ItemGroup.MISC).maxStackSize(1));
        this.setRegistryName("wikizoomer:entity_binder");
        this.addPropertyOverride(new ResourceLocation("bound"), (p_call_1_, p_call_2_, p_call_3_) -> ItemEntityBinder.isEntityBound(p_call_1_) ? 1 : 0);
    }

    public boolean hasEffect(ItemStack stack) {
        return isEntityBound(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(!isEntityBound(stack)){
            tooltip.add(new TranslationTextComponent("item.wikizoomer.entity_binder.desc").applyTextStyle(TextFormatting.GRAY));
        }
        if (stack.getTag() != null) {
            boolean isPlayer = stack.getTag().getBoolean("IsPlayerEntity");
            INBT entity = stack.getTag().get("EntityTag");
            if (entity instanceof CompoundNBT) {
                Optional<EntityType<?>> optional = EntityType.readEntityType((CompoundNBT) entity);
                if(optional.isPresent()){
                    ITextComponent untranslated = isPlayer ? new TranslationTextComponent("entity.player.name") : optional.get().getName();
                    tooltip.add(untranslated.applyTextStyle(TextFormatting.GRAY));
                }
            }
        }
    }

    public static boolean isEntityBound(ItemStack stack) {
        if(stack.getTag() != null){
            boolean isPlayer = stack.getTag().getBoolean("IsPlayerEntity");
            INBT entity = stack.getTag().get("EntityTag");
            if(isPlayer){
                return true;
            }else{
                if(entity instanceof CompoundNBT){
                    Optional<EntityType<?>> optional = EntityType.readEntityType((CompoundNBT) entity);
                    if (optional.isPresent()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, PlayerEntity player) {
        itemStack.setTag(new CompoundNBT());
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        CompoundNBT nbt = stack.getTag() == null ? new CompoundNBT() : stack.getTag();
        nbt.putBoolean("IsPlayerEntity", target instanceof PlayerEntity);
        CompoundNBT entityTag = new CompoundNBT();
        entityTag.putString("id", Registry.ENTITY_TYPE.getKey(target.getType()).toString());
        target.writeAdditional(entityTag);
        nbt.put("EntityTag", entityTag);
        ItemStack stackReplacement = new ItemStack(this);
        if(!playerIn.isCreative()){
            stack.shrink(1);
        }
        stackReplacement.setTag(nbt);
        playerIn.swingArm(hand);
        if(!playerIn.addItemStackToInventory(stackReplacement)){
            ItemEntity itementity = playerIn.dropItem(stackReplacement, false);
            if (itementity != null) {
                itementity.setNoPickupDelay();
                itementity.setOwnerId(playerIn.getUniqueID());
            }
        }
        return true;
    }
}
