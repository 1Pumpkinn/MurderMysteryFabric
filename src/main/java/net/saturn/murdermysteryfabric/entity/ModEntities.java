package net.saturn.murdermysteryfabric.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;
import net.saturn.murdermysteryfabric.entity.custom.BulletEntity;
import net.saturn.murdermysteryfabric.entity.custom.TomahawkProjectileEntity;

public class ModEntities {

    private static final RegistryKey<EntityType<?>> TOMAHAWK_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Murdermysteryfabric.MODID, "tomahawk"));

    public static final EntityType<BulletEntity> BULLET = register("bullet",
            EntityType.Builder.<BulletEntity>create(BulletEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25F, 0.25F)
                    .maxTrackingRange(64)
                    .trackingTickInterval(10)
    );


    public static final EntityType<TomahawkProjectileEntity> TOMAHAWK = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(Murdermysteryfabric.MODID, "tomahawk"),
            EntityType.Builder.<TomahawkProjectileEntity>create(TomahawkProjectileEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 1.15f).build(TOMAHAWK_KEY));

    private static <T extends net.minecraft.entity.Entity> EntityType<T> register(
            String name, EntityType.Builder<T> builder) {
        RegistryKey<EntityType<?>> key = RegistryKey.of(
                RegistryKeys.ENTITY_TYPE,
                Identifier.of(Murdermysteryfabric.MODID, name)
        );
        return Registry.register(Registries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void registerModEntities() {
    }
}