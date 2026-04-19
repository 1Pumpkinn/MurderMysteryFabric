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

    // ── Tree placed feature keys ─────────────────────────────────────────────
    public static final RegistryKey<PlacedFeature> SMALL_REDWOOD_PLACED_KEY =
            registerKey("small_redwood_placed");
    public static final RegistryKey<PlacedFeature> MEDIUM_REDWOOD_PLACED_KEY =
            registerKey("medium_redwood_placed");
    public static final RegistryKey<PlacedFeature> GIANT_SEQUOIA_PLACED_KEY =
            registerKey("giant_sequoia_placed");

    // ── Forest floor detail keys ─────────────────────────────────────────────
    public static final RegistryKey<PlacedFeature> FOREST_FLOOR_VEGETATION_PLACED_KEY =
            registerKey("forest_floor_vegetation_placed");
    public static final RegistryKey<PlacedFeature> FALLEN_LOG_PLACED_KEY =
            registerKey("fallen_log_placed");
    public static final RegistryKey<PlacedFeature> FOREST_ROCKS_PLACED_KEY =
            registerKey("forest_rocks_placed");

    public static void bootstrap(Registerable<PlacedFeature> context) {
        var configured = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        // ── Small Redwood ────────────────────────────────────────────────────
        // Common — appears throughout the forest. Rarity 1 = attempt every chunk.
        // CountPlacementModifier(6) = up to 6 attempts per chunk section.
        register(
                context,
                SMALL_REDWOOD_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.SMALL_REDWOOD_KEY),
                treeModifiers(6)
        );

        // ── Medium Redwood ───────────────────────────────────────────────────
        // Moderate — roughly 1–2 per chunk.
        register(
                context,
                MEDIUM_REDWOOD_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.MEDIUM_REDWOOD_KEY),
                treeModifiers(2)
        );

        // ── Giant Sequoia ────────────────────────────────────────────────────
        // Rare — roughly 1 per 8 chunks to prevent overlap.
        register(
                context,
                GIANT_SEQUOIA_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.GIANT_SEQUOIA_KEY),
                List.of(
                        // Rare: only ~1 in 8 chunks gets an attempt
                        RarityFilterPlacementModifier.of(8),
                        SquarePlacementModifier.of(),
                        // Use WORLD_SURFACE heightmap — ensures the trunk starts on actual ground
                        HeightmapPlacementModifier.of(Heightmap.Type.WORLD_SURFACE_WG),
                        // Never place in or next to water
                        SurfaceWaterDepthFilterPlacementModifier.of(0),
                        // Sapling must be able to survive at position (solid ground, no water)
                        BlockFilterPlacementModifier.of(
                                BlockPredicate.wouldSurvive(
                                        ModBlocks.REDWOOD_SAPLING.getDefaultState(),
                                        BlockPos.ORIGIN
                                )
                        ),
                        BiomePlacementModifier.of()
                )
        );

        // ── Forest Floor Vegetation ──────────────────────────────────────────
        // Lots of ferns on the forest floor — dense and atmospheric.
        register(
                context,
                FOREST_FLOOR_VEGETATION_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.FOREST_FLOOR_VEGETATION_KEY),
                List.of(
                        CountPlacementModifier.of(8),       // 8 patches per chunk
                        SquarePlacementModifier.of(),
                        HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES),
                        BiomePlacementModifier.of()
                )
        );

        // ── Fallen Logs ──────────────────────────────────────────────────────
        // Occasional fallen log on the ground — adds to the forest atmosphere.
        register(
                context,
                FALLEN_LOG_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.FALLEN_LOG_KEY),
                List.of(
                        RarityFilterPlacementModifier.of(4),  // ~1 per 4 chunks
                        SquarePlacementModifier.of(),
                        HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES),
                        SurfaceWaterDepthFilterPlacementModifier.of(0),
                        BiomePlacementModifier.of()
                )
        );

        // ── Forest Rocks ─────────────────────────────────────────────────────
        // Mossy cobblestone boulders scattered around — rare, atmospheric.
        register(
                context,
                FOREST_ROCKS_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.FOREST_ROCKS_KEY),
                List.of(
                        RarityFilterPlacementModifier.of(6),  // ~1 per 6 chunks
                        SquarePlacementModifier.of(),
                        HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING),
                        SurfaceWaterDepthFilterPlacementModifier.of(0),
                        BiomePlacementModifier.of()
                )
        );
    }

    // ── Shared tree placement modifiers ──────────────────────────────────────
    // count  = how many placement attempts per chunk
    // Uses WORLD_SURFACE_WG heightmap (more accurate than MOTION_BLOCKING for
    // tree generation — avoids trees spawning on top of tall grass or snow layers)
    private static List<PlacementModifier> treeModifiers(int count) {
        return List.of(
                CountPlacementModifier.of(count),
                SquarePlacementModifier.of(),
                // WORLD_SURFACE_WG = solid ground surface ignoring non-solid blocks
                HeightmapPlacementModifier.of(Heightmap.Type.WORLD_SURFACE_WG),
                // Block any placement where water is at or near the surface
                SurfaceWaterDepthFilterPlacementModifier.of(0),
                // Sapling survival check: ensures the ground block below is valid
                // (not air, not water, not floating) — this is the key anti-float guard
                BlockFilterPlacementModifier.of(
                        BlockPredicate.wouldSurvive(
                                ModBlocks.REDWOOD_SAPLING.getDefaultState(),
                                BlockPos.ORIGIN
                        )
                ),
                BiomePlacementModifier.of()
        );
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

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