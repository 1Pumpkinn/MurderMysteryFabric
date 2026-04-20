package net.saturn.murdermysteryfabric.world.biome;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.saturn.murdermysteryfabric.world.ModPlacedFeatures;

public class ModOverworldBiomes {

    public static Biome mixedRedwoodForest(
            RegistryEntryLookup<PlacedFeature> placedFeatureGetter,
            RegistryEntryLookup<ConfiguredCarver<?>> carverGetter) {

        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(spawnBuilder);

        GenerationSettings.LookupBackedBuilder gen =
                new GenerationSettings.LookupBackedBuilder(placedFeatureGetter, carverGetter);

        DefaultBiomeFeatures.addLandCarvers(gen);
        DefaultBiomeFeatures.addAmethystGeodes(gen);
        DefaultBiomeFeatures.addDungeons(gen);
        DefaultBiomeFeatures.addMineables(gen);
        DefaultBiomeFeatures.addDefaultOres(gen);
        DefaultBiomeFeatures.addDefaultDisks(gen);
        DefaultBiomeFeatures.addSprings(gen);

        gen.feature(GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.FOREST_FLOOR_VEGETATION_PLACED_KEY);
        gen.feature(GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.LARGE_FERNS_PLACED_KEY);
        gen.feature(GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.BROWN_MUSHROOMS_PLACED_KEY);
        gen.feature(GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.RED_MUSHROOMS_PLACED_KEY);
        gen.feature(GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.FALLEN_LOG_PLACED_KEY);
        gen.feature(GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.FOREST_ROCKS_PLACED_KEY);
        gen.feature(GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.SMALL_REDWOOD_PLACED_KEY);
        gen.feature(GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.MEDIUM_REDWOOD_PLACED_KEY);
        gen.feature(GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.GIANT_SEQUOIA_PLACED_KEY);

        BiomeEffects effects = new BiomeEffects.Builder()
                .waterColor(0x3B6CD1)
                .foliageColor(0x2D6B1A)
                .grassColor(0x4A7A2E)
                .build();

        return new Biome.Builder()
                .precipitation(true)
                .temperature(0.6F)
                .downfall(0.9F)
                .effects(effects)
                .spawnSettings(spawnBuilder.build())
                .generationSettings(gen.build())
                .build();
    }
}