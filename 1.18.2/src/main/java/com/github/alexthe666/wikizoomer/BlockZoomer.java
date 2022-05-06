package com.github.alexthe666.wikizoomer;

import com.github.alexthe666.wikizoomer.tileentity.TileEntityEntityZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityItemZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityRegistry;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityZoomerBase;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockZoomer extends BaseEntityBlock {
    private static final VoxelShape BASE_SHAPE = Block.box(0, 0, 0, 16, 5, 16);
    private static final VoxelShape NECK_SHAPE = Block.box(4, 5, 4, 12, 16, 12);
    private static final VoxelShape JOINED_SHAPE = Shapes.join(BASE_SHAPE, NECK_SHAPE, BooleanOp.OR);
    private boolean itemOrEntity = false;

    public BlockZoomer(boolean itemOrEntity) {
        super(Properties.of(Material.STONE).sound(SoundType.METAL).strength(5, 20F).randomTicks());
        this.itemOrEntity = itemOrEntity;
    }

    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof TileEntityZoomerBase) {
            Containers.dropContents(worldIn, pos, (TileEntityZoomerBase) tileentity);
            worldIn.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    public RenderShape getRenderShape(BlockState p_149645_1_) {
        return RenderShape.MODEL;
    }

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return JOINED_SHAPE;
    }

    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!player.isShiftKeyDown()) {
            if (worldIn.getBlockEntity(pos) instanceof TileEntityZoomerBase) {
                TileEntityZoomerBase zoomer = (TileEntityZoomerBase) worldIn.getBlockEntity(pos);
                if (!zoomer.getItem(0).isEmpty()) {
                    ItemEntity dropped = new ItemEntity(worldIn, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, zoomer.getItem(0).copy());
                    worldIn.addFreshEntity(dropped);
                    zoomer.clearContent();
                }
                ItemStack heldItem = player.getItemInHand(handIn);
                ItemStack single = heldItem.copy();
                single.setCount(1);
                if (itemOrEntity || single.getItem() == ItemAndBlockRegistry.ENTITY_BINDER_ITEM.get()) {
                    zoomer.setItem(0, single);
                    if (!player.isCreative())
                        heldItem.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        } else {
            if(itemOrEntity){
                if (worldIn.isClientSide) {
                    WikiZoomerMod.PROXY.openItemZoomerGui((TileEntityZoomerBase) worldIn.getBlockEntity(pos));
                }
            }else{
                if (worldIn.isClientSide) {
                    WikiZoomerMod.PROXY.openEntityZoomerGui((TileEntityEntityZoomer) worldIn.getBlockEntity(pos));
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flags) {
        if(itemOrEntity){
            tooltip.add(new TranslatableComponent("block.wikizoomer.item_zoomer.desc0").withStyle(ChatFormatting.GRAY));
            tooltip.add(new TranslatableComponent("block.wikizoomer.item_zoomer.desc1").withStyle(ChatFormatting.GRAY));
        }else{
            tooltip.add(new TranslatableComponent("block.wikizoomer.entity_zoomer.desc0").withStyle(ChatFormatting.GRAY));
            tooltip.add(new TranslatableComponent("block.wikizoomer.entity_zoomer.desc1").withStyle(ChatFormatting.GRAY));
        }

    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return itemOrEntity ? new TileEntityItemZoomer(pos, state) : new TileEntityEntityZoomer(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152180_, BlockState p_152181_, BlockEntityType<T> p_152182_) {
        return itemOrEntity ? createTickerHelper(p_152182_, TileEntityRegistry.ITEM_ZOOMER_TE.get(), TileEntityItemZoomer::tick) : createTickerHelper(p_152182_, TileEntityRegistry.ENTITY_ZOOMER_TE.get(), TileEntityEntityZoomer::tick);
    }
}
