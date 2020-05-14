package com.github.alexthe666.wikizoomer;

import com.github.alexthe666.wikizoomer.tileentity.TileEntityEntityZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityItemZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityZoomerBase;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockZoomer extends BlockContainer {
    private boolean itemOrEntity = false;

    public BlockZoomer(boolean itemOrEntity) {
        super(Material.ROCK);
        this.setHarvestLevel("pickaxe", 1);
        this.setSoundType(SoundType.METAL);
        this.setHardness(5F);
        this.setResistance(20F);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setTickRandomly(true);
        this.setRegistryName("wikizoomer", (itemOrEntity ? "item_zoomer" : "entity_zoomer"));
        this.setUnlocalizedName("wikizoomer." + (itemOrEntity ? "item_zoomer" : "entity_zoomer"));
        this.itemOrEntity = itemOrEntity;
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityZoomerBase) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityZoomerBase) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
    }


    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand handIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!playerIn.isSneaking()) {
            if (worldIn.getTileEntity(pos) instanceof TileEntityZoomerBase) {
                TileEntityZoomerBase zoomer = (TileEntityZoomerBase) worldIn.getTileEntity(pos);
                if (!zoomer.getStackInSlot(0).isEmpty()) {
                    EntityItem dropped = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, zoomer.getStackInSlot(0).copy());
                    zoomer.clear();
                    if(!worldIn.isRemote){
                        worldIn.spawnEntity(dropped);
                    }
                }
                ItemStack heldItem = playerIn.getHeldItem(handIn);
                ItemStack single = heldItem.copy();
                single.setCount(1);
                if (itemOrEntity || single.getItem() == WikiZoomerMod.ENTITY_BINDER_ITEM) {
                    zoomer.setInventorySlotContents(0, single);
                    if (!playerIn.isCreative())
                        heldItem.shrink(1);
                }
                return true;
            }
        } else {
            if(itemOrEntity){
                if (worldIn.isRemote) {
                    WikiZoomerMod.PROXY.openItemZoomerGui((TileEntityZoomerBase) worldIn.getTileEntity(pos));
                }
            }else{
                if (worldIn.isRemote) {
                    WikiZoomerMod.PROXY.openEntityZoomerGui((TileEntityEntityZoomer) worldIn.getTileEntity(pos));
                }
            }
            return true;
        }
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return itemOrEntity ? new TileEntityItemZoomer() : new TileEntityEntityZoomer();
    }

    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Deprecated
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(itemOrEntity){
            tooltip.add(I18n.format("tile.wikizoomer.item_zoomer.desc0"));
            tooltip.add(I18n.format("tile.wikizoomer.item_zoomer.desc1"));
        }else{
            tooltip.add(I18n.format("tile.wikizoomer.entity_zoomer.desc0"));
            tooltip.add(I18n.format("tile.wikizoomer.entity_zoomer.desc1"));
        }

    }
}
