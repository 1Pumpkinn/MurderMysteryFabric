package net.saturn.murdermysteryfabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.saturn.murdermysteryfabric.entity.ModEntities;

public class MurdermysteryfabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.BULLET, EmptyEntityRenderer::new);
    }
}