package net.saturn.murdermysteryfabric.world.biome.region;

import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.saturn.murdermysteryfabric.world.biome.ModBiomes;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;

import java.util.function.Consumer;

import static terrablender.api.ParameterUtils.*;

public class OverworldRegion extends Region {

    public OverworldRegion(Identifier name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry,
                          Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mapper) {

        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();

        new ParameterPointListBuilder()
                .temperature(Temperature.span(Temperature.NEUTRAL, Temperature.WARM))
                .humidity(Humidity.span(Humidity.WET, Humidity.HUMID))
                .continentalness(Continentalness.span(Continentalness.INLAND, Continentalness.FAR_INLAND))
                .erosion(Erosion.EROSION_0, Erosion.EROSION_1, Erosion.EROSION_2)
                .depth(Depth.SURFACE, Depth.FLOOR)
                .weirdness(
                        Weirdness.LOW_SLICE_NORMAL_DESCENDING,
                        Weirdness.MID_SLICE_NORMAL_ASCENDING,
                        Weirdness.MID_SLICE_NORMAL_DESCENDING,
                        Weirdness.HIGH_SLICE_NORMAL_ASCENDING
                )
                .build()
                .forEach(point -> builder.add(point, ModBiomes.MIXED_REDWOOD_FOREST));

        builder.build().forEach(mapper);
    }
}
