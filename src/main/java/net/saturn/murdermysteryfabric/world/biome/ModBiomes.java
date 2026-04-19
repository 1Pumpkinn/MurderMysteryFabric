package net.saturn.murdermysteryfabric.world.biome;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;
import net.saturn.murdermysteryfabric.world.biome.region.OverworldRegion;
import terrablender.api.Regions;

public class ModBiomes {

    public static final RegistryKey<Biome> MIXED_REDWOOD_FOREST =
            registerBiomeKey("mixed_redwood_forest");

    public static void registerBiomes() {
        Regions.register(new OverworldRegion(
                Identifier.of(Murdermysteryfabric.MODID, "murdermysteryfabric_overworld"),
                20
        ));
    }

    public static void bootstrap(Registerable<Biome> context) {
        var carver = context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER);
        var placedFeatures = context.getRegistryLookup(RegistryKeys.PLACED_FEATURE);

        context.register(MIXED_REDWOOD_FOREST,
                ModOverworldBiomes.mixedRedwoodForest(placedFeatures, carver));
    }

    private static void register(Registerable<Biome> context, RegistryKey<Biome> key, Biome biome) {
        context.register(key, biome);
    }

    private static RegistryKey<Biome> registerBiomeKey(String name) {
        return RegistryKey.of(RegistryKeys.BIOME,
                Identifier.of(Murdermysteryfabric.MODID, name));
    }
}
