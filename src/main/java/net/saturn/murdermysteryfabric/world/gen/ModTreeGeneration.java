package net.saturn.murdermysteryfabric.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.saturn.murdermysteryfabric.world.ModPlacedFeatures;
import net.saturn.murdermysteryfabric.world.biome.ModBiomes;

public class ModTreeGeneration {
    public static void generateTrees() {

        // Add small redwoods to old-growth taiga biomes as a guest appearance
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(
                        BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA,
                        BiomeKeys.OLD_GROWTH_PINE_TAIGA
                ),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.SMALL_REDWOOD_PLACED_KEY
        );

        // Add medium redwoods to old-growth taiga only
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(
                        BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA
                ),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.MEDIUM_REDWOOD_PLACED_KEY
        );

        // Giant sequoias only spawn in the custom biome — they're the star feature
        // (already added via ModOverworldBiomes, no need to add to vanilla biomes)
    }
}