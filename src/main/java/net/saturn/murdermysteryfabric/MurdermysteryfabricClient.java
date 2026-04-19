package net.saturn.murdermysteryfabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.saturn.murdermysteryfabric.block.ModBlocks;
import net.saturn.murdermysteryfabric.entity.ModEntities;

public class MurdermysteryfabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.BULLET, EmptyEntityRenderer::new);

        BlockRenderLayerMap.putBlock(ModBlocks.REDWOOD_SAPLING, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModBlocks.REDWOOD_LEAVES, BlockRenderLayer.CUTOUT);

    }
}