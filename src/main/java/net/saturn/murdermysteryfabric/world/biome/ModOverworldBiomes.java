package net.saturn.murdermysteryfabric.world.biome;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.saturn.murdermysteryfabric.world.ModPlacedFeatures;

public class ModOverworldBiomes {

    public static Biome kaupenValley(
            RegistryEntryLookup<PlacedFeature> placedFeatureGetter,
            RegistryEntryLookup<ConfiguredCarver<?>> carverGetter) {

        // SPAWNS
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(spawnBuilder);

        // FEATURES
        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(
                        placedFeatureGetter,
                        carverGetter
                );

        DefaultBiomeFeatures.addLandCarvers(biomeBuilder);
        DefaultBiomeFeatures.addDungeons(biomeBuilder);
        DefaultBiomeFeatures.addMineables(biomeBuilder);
        DefaultBiomeFeatures.addDefaultOres(biomeBuilder);
        DefaultBiomeFeatures.addDefaultDisks(biomeBuilder);
        DefaultBiomeFeatures.addDefaultMushrooms(biomeBuilder);
        DefaultBiomeFeatures.addSprings(biomeBuilder);

        // ❌ DO NOT add default vegetation
        // DefaultBiomeFeatures.addDefaultVegetation(...);

        // 🌲 Your forest
        biomeBuilder.feature(
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.REDWOOD_PLACED_KEY
        );

        // EFFECTS (safe minimal)
        BiomeEffects effects = new BiomeEffects.Builder()
                .waterColor(0x3B6CD1)
                .grassColor(0x4A7A2E)
                .foliageColor(0x2D6B1A)
                .build();

        return new Biome.Builder()
                .precipitation(true)
                .temperature(0.7F)
                .downfall(0.8F)
                .effects(effects)
                .spawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build())
                .build();
    }

    public static int getSkyColor(float temperature) {
        float f = temperature / 3.0F;
        f = MathHelper.clamp(f, -1.0F, 1.0F);
        return MathHelper.hsvToRgb(
                0.62222224F - f * 0.05F,
                0.5F + f * 0.1F,
                1.0F
        );
    }
}