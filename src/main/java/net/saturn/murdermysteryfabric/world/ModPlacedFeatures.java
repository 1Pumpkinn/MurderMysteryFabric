package net.saturn.murdermysteryfabric.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;
import net.saturn.murdermysteryfabric.block.ModBlocks;
import net.saturn.murdermysteryfabric.world.ModConfiguredFeatures;

import java.util.List;

public class ModPlacedFeatures {

    public static final RegistryKey<PlacedFeature> REDWOOD_PLACED_KEY =
            registerKey("redwood_placed");

    public static void bootstrap(Registerable<PlacedFeature> context) {

        var configuredFeatures =
                context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(
                context,
                REDWOOD_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.REDWOOD_KEY),

                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        CountPlacementModifier.of(35),
                        ModBlocks.REDWOOD_SAPLING
                )
        );
    }

    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(
                RegistryKeys.PLACED_FEATURE,
                Identifier.of(Murdermysteryfabric.MODID, name)
        );
    }

    private static void register(
            Registerable<PlacedFeature> context,
            RegistryKey<PlacedFeature> key,
            RegistryEntry<ConfiguredFeature<?, ?>> configuration,
            List<PlacementModifier> modifiers
    ) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    @SafeVarargs
    private static void register(
            Registerable<PlacedFeature> context,
            RegistryKey<PlacedFeature> key,
            RegistryEntry<ConfiguredFeature<?, ?>> configuration,
            PlacementModifier... modifiers
    ) {
        register(context, key, configuration, List.of(modifiers));
    }
}