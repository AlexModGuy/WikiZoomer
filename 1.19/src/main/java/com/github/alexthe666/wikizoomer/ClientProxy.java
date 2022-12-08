package com.github.alexthe666.wikizoomer;

import com.github.alexthe666.wikizoomer.client.GuiEntityZoomer;
import com.github.alexthe666.wikizoomer.client.GuiItemZoomer;
import com.github.alexthe666.wikizoomer.client.RenderEntityZoomer;
import com.github.alexthe666.wikizoomer.client.RenderItemZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityEntityZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityRegistry;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityZoomerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientProxy extends CommonProxy{
    public static Entity dataMimic = null;

    @Override
    public void setup() {
        BlockEntityRenderers.register(TileEntityRegistry.ITEM_ZOOMER_TE.get(), RenderItemZoomer::new);
        BlockEntityRenderers.register(TileEntityRegistry.ENTITY_ZOOMER_TE.get(), RenderEntityZoomer::new);
        ItemProperties.register(ItemAndBlockRegistry.ENTITY_BINDER_ITEM.get(), new ResourceLocation("bound"), (stack, a, b, c) -> {
            return ItemEntityBinder.isEntityBound(stack) ? 1 : 0;
        });
    }

    @Override
    public void openItemZoomerGui(TileEntityZoomerBase tileEntity) {
        Minecraft.getInstance().setScreen(new GuiItemZoomer(tileEntity));
    }

    @Override
    public void openEntityZoomerGui(TileEntityEntityZoomer tileEntity) {
        Minecraft.getInstance().setScreen(new GuiEntityZoomer(tileEntity));
    }

    @Override
    public void onDataCopierUse(LivingEntity target) {
        this.dataMimic = target;
    }

}
