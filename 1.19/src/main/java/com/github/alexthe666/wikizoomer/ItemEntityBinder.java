package com.github.alexthe666.wikizoomer;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ItemEntityBinder extends Item {

    public ItemEntityBinder() {
        super(new Properties().stacksTo(1));
    }

    public boolean hasEffect(ItemStack stack) {
        return isEntityBound(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if(!isEntityBound(stack)){
            tooltip.add(Component.translatable("item.wikizoomer.entity_binder.desc").withStyle(ChatFormatting.GRAY));
        }
        if (stack.getTag() != null) {
            boolean isPlayer = stack.getTag().getBoolean("IsPlayerEntity");
            Tag entity = stack.getTag().get("EntityTag");
            if (entity instanceof CompoundTag) {
                Optional<EntityType<?>> optional = EntityType.by((CompoundTag) entity);
                if(optional.isPresent()){
                    Component untranslated = isPlayer ? Component.translatable("entity.player.name").withStyle(ChatFormatting.GRAY) : optional.get().getDescription();
                    tooltip.add(untranslated);
                }
            }
        }
    }

    public static boolean isEntityBound(ItemStack stack) {
        if(stack.getTag() != null){
            boolean isPlayer = stack.getTag().getBoolean("IsPlayerEntity");
            Tag entity = stack.getTag().get("EntityTag");
            if(isPlayer){
                return true;
            }else{
                if(entity instanceof CompoundTag){
                    Optional<EntityType<?>> optional = EntityType.by((CompoundTag) entity);
                    if (optional.isPresent()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        CompoundTag nbt = stack.getTag() == null ? new CompoundTag() : stack.getTag();
        nbt.putBoolean("IsPlayerEntity", target instanceof Player);
        CompoundTag entityTag = target.serializeNBT();
        entityTag.putString("id", ForgeRegistries.ENTITY_TYPES.getKey(target.getType()).toString());
        nbt.put("EntityTag", entityTag);
        ItemStack stackReplacement = new ItemStack(this);
        if(!playerIn.isCreative()){
            stack.shrink(1);
        }
        stackReplacement.setTag(nbt);
        playerIn.swing(hand);
        if(!playerIn.addItem(stackReplacement)){
            ItemEntity itementity = playerIn.drop(stackReplacement, false);
            if (itementity != null) {
                itementity.setNoPickUpDelay();
                itementity.setOwner(playerIn.getUUID());
            }
        }
        return InteractionResult.SUCCESS;
    }
}
