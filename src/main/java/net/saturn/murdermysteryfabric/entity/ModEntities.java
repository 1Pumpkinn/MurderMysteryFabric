package net.saturn.murdermysteryfabric.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;

public class ModEntities {
    
    public static final EntityType<BulletEntity> BULLET = Registry.register(
        Registries.ENTITY_TYPE,
        Identifier.of(Murdermysteryfabric.MODID, "bullet"),
        FabricEntityTypeBuilder.<BulletEntity>create(SpawnGroup.MISC, BulletEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // Small hitbox
            .trackRangeBlocks(64)
            .trackedUpdateRate(10)
            .build()
    );
    
    public static void initialize() {
        // Registration happens in static initializer
    }
}
