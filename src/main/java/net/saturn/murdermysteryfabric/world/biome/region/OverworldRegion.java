package net.saturn.murdermysteryfabric.world.biome.region;

import com.terraformersmc.biolith.api.biome.BiomePlacement;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.saturn.murdermysteryfabric.world.biome.ModBiomes;

public class OverworldRegion {

    public static void register() {
        BiomePlacement.addOverworld(
                ModBiomes.MIXED_REDWOOD_FOREST,
                MultiNoiseUtil.createNoiseHypercube(
                        MultiNoiseUtil.ParameterRange.of(0.0F, 0.55F),   // temperature: NEUTRAL to WARM
                        MultiNoiseUtil.ParameterRange.of(0.35F, 1.0F),   // humidity: WET to HUMID
                        MultiNoiseUtil.ParameterRange.of(0.03F, 1.0F),   // continentalness: INLAND to FAR_INLAND
                        MultiNoiseUtil.ParameterRange.of(-1.0F, 0.45F),  // erosion: EROSION_0 to EROSION_2
                        MultiNoiseUtil.ParameterRange.of(0.0F, 0.0F),    // depth: SURFACE
                        MultiNoiseUtil.ParameterRange.of(-0.93F, 0.4F),  // weirdness: normal slices
                        0.0F                                              // offset
                )
        );
    }
}