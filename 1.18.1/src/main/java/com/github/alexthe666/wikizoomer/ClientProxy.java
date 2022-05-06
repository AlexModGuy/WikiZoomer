package com.github.alexthe666.wikizoomer;

import com.github.alexthe666.wikizoomer.client.*;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityEntityZoomer;
import com.github.alexthe666.wikizoomer.tileentity.TileEntityZoomerBase;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientProxy extends CommonProxy{
    public static Entity dataMimic = null;

    @Override
    public void setup() {
        BlockEntityRenderers.register(WikiZoomerMod.ITEM_ZOOMER_TE, manager -> new RenderItemZoomer(manager));
        BlockEntityRenderers.register(WikiZoomerMod.ENTITY_ZOOMER_TE, manager -> new RenderEntityZoomer(manager));
        ItemBlockRenderTypes.setRenderLayer(WikiZoomerMod.ITEM_ZOOMER_BLOCK, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WikiZoomerMod.ENTITY_ZOOMER_BLOCK, RenderType.cutout());
        ItemProperties.register(WikiZoomerMod.ENTITY_BINDER_ITEM, new ResourceLocation("bound"), (stack, a, b, c) -> {
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
