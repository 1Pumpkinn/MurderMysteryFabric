package net.saturn.murdermysteryfabric.world.biome;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.OceanPlacedFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.saturn.murdermysteryfabric.world.ModPlacedFeatures;

public class ModOverworldBiomes {

    private static void addFeature(GenerationSettings.LookupBackedBuilder builder,
                                   GenerationStep.Feature step,
                                   RegistryKey<PlacedFeature> feature) {
        builder.feature(step, feature);
    }

    public static Biome kaupenValley(
            RegistryEntryLookup<PlacedFeature> placedFeatureGetter,
            RegistryEntryLookup<ConfiguredCarver<?>> carverGetter) {

        // -------------------
        // SPAWNS
        // -------------------
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();

        DefaultBiomeFeatures.addFarmAnimals(spawnBuilder);


        // -------------------
        // FEATURES
        // -------------------
        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(placedFeatureGetter, carverGetter);

        DefaultBiomeFeatures.addLandCarvers(biomeBuilder);
        DefaultBiomeFeatures.addMineables(biomeBuilder);
        DefaultBiomeFeatures.addDefaultOres(biomeBuilder);

        addFeature(biomeBuilder, GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.REDWOOD_PLACED_KEY);

        addFeature(biomeBuilder, GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.REDWOOD_PLACED_KEY);

        addFeature(biomeBuilder, GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.REDWOOD_PLACED_KEY);

        // -------------------
        // BIOME EFFECTS (SAFE VERSION)
        // -------------------
        BiomeEffects effects = new BiomeEffects.Builder()
                .waterColor(0x93CAFD)
                .build();

        // -------------------
        // BIOME
        // -------------------
        return new Biome.Builder()
                .precipitation(false)
                .temperature(-1.0F)
                .downfall(0.0F)
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