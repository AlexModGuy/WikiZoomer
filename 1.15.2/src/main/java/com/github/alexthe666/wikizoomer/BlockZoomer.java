package com.github.alexthe666.wikizoomer;

import com.github.alexthe666.wikizoomer.tileentity.TileEntityEntityZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityItemZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityZoomerBase;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;

public class BlockZoomer extends ContainerBlock {
    private static final VoxelShape BASE_SHAPE = Block.makeCuboidShape(0, 0, 0, 16, 5, 16);
    private static final VoxelShape NECK_SHAPE = Block.makeCuboidShape(4, 5, 4, 12, 16, 12);
    private static final VoxelShape JOINED_SHAPE = VoxelShapes.combine(BASE_SHAPE, NECK_SHAPE, IBooleanFunction.OR);
    private boolean itemOrEntity = false;

    public BlockZoomer(boolean itemOrEntity) {
        super(Properties.create(Material.ROCK).harvestLevel(1).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).hardnessAndResistance(5, 20F).tickRandomly());
        this.setRegistryName("wikizoomer:" + (itemOrEntity ? "item_zoomer" : "entity_zoomer"));
        this.itemOrEntity = itemOrEntity;
    }

    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityZoomerBase) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityZoomerBase) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
    }


    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return JOINED_SHAPE;
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!player.isShiftKeyDown()) {
            if (worldIn.getTileEntity(pos) instanceof TileEntityZoomerBase) {
                TileEntityZoomerBase zoomer = (TileEntityZoomerBase) worldIn.getTileEntity(pos);
                if (!zoomer.getStackInSlot(0).isEmpty()) {
                    ItemEntity dropped = new ItemEntity(worldIn, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, zoomer.getStackInSlot(0).copy());
                    worldIn.addEntity(dropped);
                    zoomer.clear();
                }
                ItemStack heldItem = player.getHeldItem(handIn);
                ItemStack single = heldItem.copy();
                single.setCount(1);
                if (itemOrEntity || single.getItem() == WikiZoomerMod.ENTITY_BINDER_ITEM) {
                    zoomer.setInventorySlotContents(0, single);
                    if (!player.isCreative())
                        heldItem.shrink(1);
                }
                return ActionResultType.SUCCESS;
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
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.SUCCESS;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return itemOrEntity ? new TileEntityItemZoomer() : new TileEntityEntityZoomer();
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(itemOrEntity){
            tooltip.add(new TranslationTextComponent("block.wikizoomer.item_zoomer.desc0").applyTextStyle(TextFormatting.GRAY));
            tooltip.add(new TranslationTextComponent("block.wikizoomer.item_zoomer.desc1").applyTextStyle(TextFormatting.GRAY));
        }else{
            tooltip.add(new TranslationTextComponent("block.wikizoomer.entity_zoomer.desc0").applyTextStyle(TextFormatting.GRAY));
            tooltip.add(new TranslationTextComponent("block.wikizoomer.entity_zoomer.desc1").applyTextStyle(TextFormatting.GRAY));
        }

    }
}
