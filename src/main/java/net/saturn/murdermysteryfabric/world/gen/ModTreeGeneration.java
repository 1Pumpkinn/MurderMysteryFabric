package net.saturn.murdermysteryfabric.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.saturn.murdermysteryfabric.world.ModPlacedFeatures;

public class ModTreeGeneration {
    public static void generateTrees() {
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.PLAINS, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA, BiomeKeys.TAIGA),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.REDWOOD_PLACED_KEY);
    }
}
