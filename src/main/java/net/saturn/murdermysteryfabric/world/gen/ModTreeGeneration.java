package net.saturn.murdermysteryfabric.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.saturn.murdermysteryfabric.world.ModPlacedFeatures;
import net.saturn.murdermysteryfabric.world.biome.ModBiomes;

public class ModTreeGeneration {
    public static void generateTrees() {

        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(
                        BiomeKeys.PLAINS,
                        BiomeKeys.TAIGA,
                        BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA,
                        ModBiomes.MIXED_REDWOOD_FOREST
                ),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.SMALL_REDWOOD_PLACED_KEY
        );

        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(
                        BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA
                ),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.MEDIUM_REDWOOD_PLACED_KEY
        );

        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(
                        BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA
                ),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.GIANT_SEQUOIA_PLACED_KEY
        );
    }
}
