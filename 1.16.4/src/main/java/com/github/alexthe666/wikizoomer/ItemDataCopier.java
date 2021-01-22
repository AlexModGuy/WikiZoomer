package com.github.alexthe666.wikizoomer;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ItemDataCopier extends Item {

    public ItemDataCopier() {
        super(new Properties().group(ItemGroup.MISC).maxStackSize(1));
        this.setRegistryName("wikizoomer:data_copier");
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.wikizoomer.data_copier.desc").func_240699_a_(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.wikizoomer.data_copier.desc2").func_240699_a_(TextFormatting.GRAY));
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, PlayerEntity player) {
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if(playerIn.world.isRemote){
            WikiZoomerMod.PROXY.onDataCopierUse(target);
        }
        return ActionResultType.SUCCESS;
    }
}
