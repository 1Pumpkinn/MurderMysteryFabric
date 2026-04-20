package net.saturn.murdermysteryfabric.world;

import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.MegaPineFoliagePlacer;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.DarkOakTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;
import net.saturn.murdermysteryfabric.block.ModBlocks;

public class ModConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?, ?>> SMALL_REDWOOD_KEY =
            registerKey("small_redwood");

    public static final RegistryKey<ConfiguredFeature<?, ?>> MEDIUM_REDWOOD_KEY =
            registerKey("medium_redwood");

    public static final RegistryKey<ConfiguredFeature<?, ?>> GIANT_SEQUOIA_KEY =
            registerKey("giant_sequoia");

    public static final RegistryKey<ConfiguredFeature<?, ?>> FOREST_FLOOR_VEGETATION_KEY =
            registerKey("forest_floor_vegetation");

    public static final RegistryKey<ConfiguredFeature<?, ?>> LARGE_FERNS_KEY =
            registerKey("large_ferns");

    public static final RegistryKey<ConfiguredFeature<?, ?>> BROWN_MUSHROOMS_KEY =
            registerKey("brown_mushrooms");

    public static final RegistryKey<ConfiguredFeature<?, ?>> RED_MUSHROOMS_KEY =
            registerKey("red_mushrooms");

    public static final RegistryKey<ConfiguredFeature<?, ?>> FALLEN_LOG_KEY =
            registerKey("fallen_log");

    public static final RegistryKey<ConfiguredFeature<?, ?>> FOREST_ROCKS_KEY =
            registerKey("forest_rocks");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {

        // SMALL REDWOOD
        register(context, SMALL_REDWOOD_KEY, Feature.TREE,
                new TreeFeatureConfig.Builder(
                        BlockStateProvider.of(ModBlocks.REDWOOD_LOG),
                        new StraightTrunkPlacer(16, 3, 2),
                        BlockStateProvider.of(ModBlocks.REDWOOD_LEAVES),
                        new MegaPineFoliagePlacer(
                                ConstantIntProvider.create(0),
                                ConstantIntProvider.create(0),
                                UniformIntProvider.create(5, 7)
                        ),
                        new TwoLayersFeatureSize(1, 0, 1)
                )
                        .dirtProvider(BlockStateProvider.of(Blocks.PODZOL))
                        .forceDirt()
                        .ignoreVines()
                        .build()
        );

        // MEDIUM REDWOOD
        register(context, MEDIUM_REDWOOD_KEY, Feature.TREE,
                new TreeFeatureConfig.Builder(
                        BlockStateProvider.of(ModBlocks.REDWOOD_LOG),
                        new StraightTrunkPlacer(24, 4, 2),
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

        // GIANT SEQUOIA
        register(context, GIANT_SEQUOIA_KEY, Feature.TREE,
                new TreeFeatureConfig.Builder(
                        BlockStateProvider.of(ModBlocks.REDWOOD_LOG),
                        new DarkOakTrunkPlacer(16, 8, 4),
                        BlockStateProvider.of(ModBlocks.REDWOOD_LEAVES),
                        new SpruceFoliagePlacer(
                                UniformIntProvider.create(3, 5),
                                UniformIntProvider.create(0, 0),
                                UniformIntProvider.create(8, 12)
                        ),
                        new TwoLayersFeatureSize(1, 1, 2)
                )
                        .dirtProvider(BlockStateProvider.of(Blocks.PODZOL))
                        .forceDirt()
                        .ignoreVines()
                        .build()
        );

        // FOREST FLOOR (just configured feature, no placement!)
        register(context, FOREST_FLOOR_VEGETATION_KEY, Feature.RANDOM_PATCH,
                new RandomPatchFeatureConfig(
                        32,
                        4,
                        2,
                        PlacedFeatures.createEntry(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockFeatureConfig(
                                        BlockStateProvider.of(Blocks.SHORT_GRASS)
                                )
                        )
                )
        );

        // FERNS
        register(context, LARGE_FERNS_KEY, Feature.RANDOM_PATCH,
                new RandomPatchFeatureConfig(
                        8,  // reduced from 48
                        4,
                        1,
                        PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                                new SimpleBlockFeatureConfig(
                                        BlockStateProvider.of(Blocks.FERN)
                                ))
                )
        );

        // MUSHROOMS
        register(context, BROWN_MUSHROOMS_KEY, Feature.RANDOM_PATCH,
                new RandomPatchFeatureConfig(
                        8, 4, 1,
                        PlacedFeatures.createEntry(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockFeatureConfig(
                                        BlockStateProvider.of(Blocks.BROWN_MUSHROOM)
                                )
                        )
                )
        );

        register(context, RED_MUSHROOMS_KEY, Feature.RANDOM_PATCH,
                new RandomPatchFeatureConfig(
                        4, 4, 1,
                        PlacedFeatures.createEntry(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockFeatureConfig(
                                        BlockStateProvider.of(Blocks.RED_MUSHROOM)
                                )
                        )
                )
        );

        // FALLEN LOG
        register(context, FALLEN_LOG_KEY, Feature.RANDOM_PATCH,
                new RandomPatchFeatureConfig(
                        4, 1, 0,
                        PlacedFeatures.createEntry(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockFeatureConfig(
                                        BlockStateProvider.of(
                                                ModBlocks.REDWOOD_LOG.getDefaultState()
                                                        .with(net.minecraft.state.property.Properties.AXIS,
                                                                Direction.Axis.Z)
                                        )
                                )
                        )
                )
        );

        // ROCKS
        register(context, FOREST_ROCKS_KEY, Feature.FOREST_ROCK,
                new SingleStateFeatureConfig(
                        Blocks.MOSSY_COBBLESTONE.getDefaultState()
                )
        );
    }

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