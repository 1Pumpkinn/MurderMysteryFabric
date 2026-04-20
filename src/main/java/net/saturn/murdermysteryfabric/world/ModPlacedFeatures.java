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

    public static final RegistryKey<PlacedFeature> FOREST_FLOOR_VEGETATION_PLACED_KEY =
            registerKey("forest_floor_vegetation_placed");
    public static final RegistryKey<PlacedFeature> LARGE_FERNS_PLACED_KEY =
            registerKey("large_ferns_placed");
    public static final RegistryKey<PlacedFeature> BROWN_MUSHROOMS_PLACED_KEY =
            registerKey("brown_mushrooms_placed");
    public static final RegistryKey<PlacedFeature> RED_MUSHROOMS_PLACED_KEY =
            registerKey("red_mushrooms_placed");
    public static final RegistryKey<PlacedFeature> FALLEN_LOG_PLACED_KEY =
            registerKey("fallen_log_placed");
    public static final RegistryKey<PlacedFeature> FOREST_ROCKS_PLACED_KEY =
            registerKey("forest_rocks_placed");

    public static void bootstrap(Registerable<PlacedFeature> context) {
        var configured = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(context, SMALL_REDWOOD_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.SMALL_REDWOOD_KEY),
                treeModifiers(6));

        register(context, MEDIUM_REDWOOD_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.MEDIUM_REDWOOD_KEY),
                treeModifiers(2));

        register(context, GIANT_SEQUOIA_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.GIANT_SEQUOIA_KEY),
                List.of(
                        RarityFilterPlacementModifier.of(8),
                        SquarePlacementModifier.of(),
                        HeightmapPlacementModifier.of(Heightmap.Type.WORLD_SURFACE_WG),
                        SurfaceWaterDepthFilterPlacementModifier.of(0),
                        BlockFilterPlacementModifier.of(
                                BlockPredicate.wouldSurvive(
                                        ModBlocks.REDWOOD_SAPLING.getDefaultState(),
                                        BlockPos.ORIGIN)),
                        BiomePlacementModifier.of()
                ));

        register(context, FOREST_FLOOR_VEGETATION_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.FOREST_FLOOR_VEGETATION_KEY),
                List.of(
                        CountPlacementModifier.of(2),
                        SquarePlacementModifier.of(),
                        HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES),
                        BiomePlacementModifier.of()
                ));

        register(context, LARGE_FERNS_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.LARGE_FERNS_KEY),
                List.of(
                        CountPlacementModifier.of(2),
                        SquarePlacementModifier.of(),
                        HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES),
                        BiomePlacementModifier.of()
                ));

        register(context, BROWN_MUSHROOMS_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.BROWN_MUSHROOMS_KEY),
                List.of(
                        RarityFilterPlacementModifier.of(2),
                        SquarePlacementModifier.of(),
                        HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES),
                        BiomePlacementModifier.of()
                ));

        register(context, RED_MUSHROOMS_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.RED_MUSHROOMS_KEY),
                List.of(
                        RarityFilterPlacementModifier.of(4),
                        SquarePlacementModifier.of(),
                        HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES),
                        BiomePlacementModifier.of()
                ));

        register(context, FALLEN_LOG_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.FALLEN_LOG_KEY),
                List.of(
                        RarityFilterPlacementModifier.of(4),
                        SquarePlacementModifier.of(),
                        HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES),
                        SurfaceWaterDepthFilterPlacementModifier.of(0),
                        BiomePlacementModifier.of()
                ));

        register(context, FOREST_ROCKS_PLACED_KEY,
                configured.getOrThrow(ModConfiguredFeatures.FOREST_ROCKS_KEY),
                List.of(
                        RarityFilterPlacementModifier.of(6),
                        SquarePlacementModifier.of(),
                        HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING),
                        SurfaceWaterDepthFilterPlacementModifier.of(0),
                        BiomePlacementModifier.of()
                ));
    }

    private static List<PlacementModifier> treeModifiers(int count) {
        return List.of(
                CountPlacementModifier.of(count),
                SquarePlacementModifier.of(),
                HeightmapPlacementModifier.of(Heightmap.Type.WORLD_SURFACE_WG),
                SurfaceWaterDepthFilterPlacementModifier.of(0),
                BlockFilterPlacementModifier.of(
                        BlockPredicate.wouldSurvive(
                                ModBlocks.REDWOOD_SAPLING.getDefaultState(),
                                BlockPos.ORIGIN)),
                BiomePlacementModifier.of()
        );
    }

    private static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE,
                Identifier.of(Murdermysteryfabric.MODID, name));
    }

    private static void register(Registerable<PlacedFeature> context,
                                 RegistryKey<PlacedFeature> key,
                                 RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}