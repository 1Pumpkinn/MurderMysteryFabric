package net.saturn.murdermysteryfabric.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.*;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;
import net.saturn.murdermysteryfabric.block.ModBlocks;

import java.util.List;

public class ModPlacedFeatures {

    public static final RegistryKey<PlacedFeature> SMALL_REDWOOD_PLACED_KEY =
            registerKey("small_redwood_placed");
    public static final RegistryKey<PlacedFeature> MEDIUM_REDWOOD_PLACED_KEY =
            registerKey("medium_redwood_placed");
    public static final RegistryKey<PlacedFeature> GIANT_SEQUOIA_PLACED_KEY =
            registerKey("giant_sequoia_placed");

    public static void bootstrap(Registerable<PlacedFeature> context) {
        var configured = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        // Small redwoods – common
        register(
                context,
                SMALL_REDWOOD_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.SMALL_REDWOOD_KEY),
                baseTreeModifiers(1) // very common
        );

        // Medium redwoods – less common
        register(
                context,
                MEDIUM_REDWOOD_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.MEDIUM_REDWOOD_KEY),
                baseTreeModifiers(3) // moderate
        );

        // Giant sequoias – very rare and spaced far apart to avoid overlap
        register(
                context,
                GIANT_SEQUOIA_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.GIANT_SEQUOIA_KEY),
                baseTreeModifiers(50) // rare: ~1 per 50 chunks
        );
    }

    private static List<PlacementModifier> baseTreeModifiers(int rarity) {
        return List.of(
                // Rarity controls how often attempts are made across chunks
                RarityFilterPlacementModifier.of(rarity),

                // Spread attempts inside the chunk
                SquarePlacementModifier.of(),

                // Place on surface
                HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING),

                // Prevent water placement
                SurfaceWaterDepthFilterPlacementModifier.of(0),

                // Ensure sapling would survive at the placement position
                BlockFilterPlacementModifier.of(BlockPredicate.wouldSurvive(
                        ModBlocks.REDWOOD_SAPLING.getDefaultState(),
                        BlockPos.ORIGIN
                )),

                // Biome restriction
                BiomePlacementModifier.of()
        );
    }

    private static RegistryKey<PlacedFeature> registerKey(String name) {
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
}
