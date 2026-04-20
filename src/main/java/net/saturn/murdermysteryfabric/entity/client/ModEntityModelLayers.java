package net.saturn.murdermysteryfabric.entity.client;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;

public class ModEntityModelLayers {
    public static final EntityModelLayer TOMAHAWK =
            new EntityModelLayer(Identifier.of(Murdermysteryfabric.MODID, "tomahawk"), "main");
}