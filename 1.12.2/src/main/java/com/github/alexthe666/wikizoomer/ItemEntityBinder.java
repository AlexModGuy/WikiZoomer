package com.github.alexthe666.wikizoomer;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ItemEntityBinder extends Item {

    public ItemEntityBinder() {
        super();
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.MISC);
        this.setRegistryName("wikizoomer", "entity_binder");
        this.setUnlocalizedName("wikizoomer.entity_binder");
        this.addPropertyOverride(new ResourceLocation("bound"), (p_call_1_, p_call_2_, p_call_3_) -> ItemEntityBinder.isEntityBound(p_call_1_) ? 1 : 0);
    }

    public boolean hasEffect(ItemStack stack) {
        return isEntityBound(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(!isEntityBound(stack)){
            tooltip.add(I18n.format("item.wikizoomer.entity_binder.desc"));
        }
        if (stack.getTagCompound() != null) {
            boolean isPlayer = stack.getTagCompound().getBoolean("IsPlayerEntity");
            NBTTagCompound entity = stack.getTagCompound().getCompoundTag("EntityTag");
            Class clazz = EntityList.getClassFromName(entity.getString("id"));
            EntityEntry entry = EntityRegistry.getEntry(clazz);
            if(entry != null) {
                String name = isPlayer ? I18n.format("entity.player.name") : I18n.format("entity." + entry.getName() + ".name");
                tooltip.add(name);
            }
        }
    }

    public static boolean isEntityBound(ItemStack stack) {
        if(stack.getTagCompound() != null){
            boolean isPlayer = stack.getTagCompound().getBoolean("IsPlayerEntity");
            NBTTagCompound entity = stack.getTagCompound().getCompoundTag("EntityTag");
            if(isPlayer){
                return true;
            }else{
                Class clazz = EntityList.getClassFromName(entity.getString("id"));
                return clazz != null;
            }
        }
        return false;
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        stack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        NBTTagCompound nbt = stack.getTagCompound() == null ? new NBTTagCompound() : stack.getTagCompound();
        nbt.setBoolean("IsPlayerEntity", target instanceof EntityPlayer);
        NBTTagCompound entityTag = new NBTTagCompound();
        entityTag.setString("id", EntityList.getKey(target).toString());
        target.writeEntityToNBT(entityTag);
        nbt.setTag("EntityTag", entityTag);
        ItemStack stackReplacement = new ItemStack(this);
        if(!playerIn.isCreative()){
            stack.shrink(1);
        }
        stackReplacement.setTagCompound(nbt);
        playerIn.swingArm(hand);
        if(!playerIn.addItemStackToInventory(stackReplacement)){
            EntityItem itementity = playerIn.dropItem(stackReplacement, false);
            if (itementity != null) {
                itementity.setNoPickupDelay();
            }
        }
        return true;
    }
}
