package net.saturn.murdermysteryfabric.world;

import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.MegaPineFoliagePlacer;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.DarkOakTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import net.minecraft.world.gen.trunk.MegaJungleTrunkPlacer;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;
import net.saturn.murdermysteryfabric.block.ModBlocks;

public class ModConfiguredFeatures {

    // ── Tree keys ────────────────────────────────────────────────────────────
    public static final RegistryKey<ConfiguredFeature<?, ?>> SMALL_REDWOOD_KEY =
            registerKey("small_redwood");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MEDIUM_REDWOOD_KEY =
            registerKey("medium_redwood");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GIANT_SEQUOIA_KEY =
            registerKey("giant_sequoia");

    // ── Forest floor detail keys ─────────────────────────────────────────────
    public static final RegistryKey<ConfiguredFeature<?, ?>> FOREST_FLOOR_VEGETATION_KEY =
            registerKey("forest_floor_vegetation");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FALLEN_LOG_KEY =
            registerKey("fallen_log");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FOREST_ROCKS_KEY =
            registerKey("forest_rocks");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {

        // ── Small Redwood ────────────────────────────────────────────────────
        // 10-14 blocks tall, narrow conical pine crown.
        // PineFoliagePlacer gives the classic tapered cone look.
        register(context, SMALL_REDWOOD_KEY, Feature.TREE,
                new TreeFeatureConfig.Builder(
                        BlockStateProvider.of(ModBlocks.REDWOOD_LOG),
                        new StraightTrunkPlacer(16, 3, 2),  // 16-21 blocks tall bare trunk
                        BlockStateProvider.of(ModBlocks.REDWOOD_LEAVES),
                        new MegaPineFoliagePlacer(
                                ConstantIntProvider.create(0),
                                ConstantIntProvider.create(0),
                                UniformIntProvider.create(5, 7)  // small crown, only at the top
                        ),
                        new TwoLayersFeatureSize(1, 0, 1)
                )
                        .dirtProvider(BlockStateProvider.of(Blocks.PODZOL))
                        .forceDirt()
                        .ignoreVines()
                        .build()
        );

        // ── Medium Redwood ───────────────────────────────────────────────────
        // 18-22 blocks tall, wider pine crown.
        register(context, MEDIUM_REDWOOD_KEY, Feature.TREE,
                new TreeFeatureConfig.Builder(
                        BlockStateProvider.of(ModBlocks.REDWOOD_LOG),
                        new StraightTrunkPlacer(24, 4, 2),  // 24-30 blocks tall bare trunk
                        BlockStateProvider.of(ModBlocks.REDWOOD_LEAVES),
                        new MegaPineFoliagePlacer(
                                ConstantIntProvider.create(0),
                                ConstantIntProvider.create(0),
                                UniformIntProvider.create(6, 9)
                        ),
                        new TwoLayersFeatureSize(1, 0, 2)
                )
                        .dirtProvider(BlockStateProvider.of(Blocks.PODZOL))
                        .forceDirt()
                        .ignoreVines()
                        .build()
        );

        // ── Giant Sequoia ────────────────────────────────────────────────────
        // 30-42 blocks tall, massive 2x2 trunk, dense mega-pine crown.
        // MegaJungleTrunkPlacer produces the thick 2x2 trunk used by jungle trees.
        // MegaPineFoliagePlacer gives the layered, dense canopy.
        register(context, GIANT_SEQUOIA_KEY, Feature.TREE,
                new TreeFeatureConfig.Builder(
                        BlockStateProvider.of(ModBlocks.REDWOOD_LOG),
                        new DarkOakTrunkPlacer(16, 8, 4),                        BlockStateProvider.of(ModBlocks.REDWOOD_LEAVES),
                        new SpruceFoliagePlacer(
                                UniformIntProvider.create(3, 5),  // radius — wide layers
                                UniformIntProvider.create(0, 0),  // offset
                                UniformIntProvider.create(8, 12)  // trunk height (controls where leaves start)
                        ),
                        new TwoLayersFeatureSize(1, 1, 2)
                )
                        .dirtProvider(BlockStateProvider.of(Blocks.PODZOL))
                        .forceDirt()
                        .ignoreVines()
                        .build()
        );

        // ── Forest Floor Vegetation ──────────────────────────────────────────
        // Dense ferns, grass, and dead bushes scattered across the forest floor.
        register(context, FOREST_FLOOR_VEGETATION_KEY, Feature.RANDOM_PATCH,
                new RandomPatchFeatureConfig(
                        64, // tries
                        7,  // xz spread
                        3,  // y spread
                        PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                                new SimpleBlockFeatureConfig(
                                        BlockStateProvider.of(Blocks.FERN)
                                ))
                )
        );

        // ── Fallen Log ───────────────────────────────────────────────────────
        // A single horizontal redwood log sitting on the ground.
        // We use FOREST_ROCK as a structural analogue — it places a single block.
        // For a "fallen log" we place a horizontal log via simple block feature.
        register(context, FALLEN_LOG_KEY, Feature.RANDOM_PATCH,
                new RandomPatchFeatureConfig(
                        4,  // tries
                        1,  // xz spread — very tight, forces them close in a line
                        0,  // no y spread
                        PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                                new SimpleBlockFeatureConfig(
                                        BlockStateProvider.of(
                                                ModBlocks.REDWOOD_LOG.getDefaultState()
                                                        .with(net.minecraft.state.property.Properties.AXIS,
                                                                net.minecraft.util.math.Direction.Axis.Z)
                                        )
                                ))
                )
        );

        // ── Forest Rocks (mossy cobblestone boulders) ────────────────────────
        register(context, FOREST_ROCKS_KEY, Feature.FOREST_ROCK,
                new SingleStateFeatureConfig(Blocks.MOSSY_COBBLESTONE.getDefaultState())
        );
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(
                RegistryKeys.CONFIGURED_FEATURE,
                Identifier.of(Murdermysteryfabric.MODID, name)
        );
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(
            Registerable<ConfiguredFeature<?, ?>> context,
            RegistryKey<ConfiguredFeature<?, ?>> key,
            F feature,
            FC config
    ) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }
}