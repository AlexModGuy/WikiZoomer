package com.github.alexthe666.wikizoomer;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDataCopier extends Item {

    public ItemDataCopier() {
        super(new Properties().stacksTo(1));
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.wikizoomer.data_copier.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.wikizoomer.data_copier.desc2").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if(player.level.isClientSide){
            WikiZoomerMod.PROXY.onDataCopierUse(entity);
        }
        return InteractionResult.SUCCESS;
    }
}
