package net.saturn.murdermysteryfabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.saturn.murdermysteryfabric.block.ModBlocks;
import net.saturn.murdermysteryfabric.entity.ModEntities;
import net.saturn.murdermysteryfabric.entity.client.ModEntityModelLayers;
import net.saturn.murdermysteryfabric.entity.client.TomahawkProjectileModel;
import net.saturn.murdermysteryfabric.entity.client.TomahawkProjectileRenderer;

public class MurdermysteryfabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        BlockRenderLayerMap.putBlock(ModBlocks.REDWOOD_SAPLING, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModBlocks.REDWOOD_LEAVES, BlockRenderLayer.CUTOUT);

        EntityRendererRegistry.register(ModEntities.BULLET, EmptyEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.TOMAHAWK, TomahawkProjectileModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.TOMAHAWK, TomahawkProjectileRenderer::new);
    }
}