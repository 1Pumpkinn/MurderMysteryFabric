package net.saturn.murdermysteryfabric.world;

import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.CherryFoliagePlacer;
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

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {

        // Small redwood – common, narrow crown using CherryFoliagePlacer
        register(context, SMALL_REDWOOD_KEY, Feature.TREE,
                new TreeFeatureConfig.Builder(
                        BlockStateProvider.of(ModBlocks.REDWOOD_LOG),
                        new StraightTrunkPlacer(10, 4, 2),
                        BlockStateProvider.of(ModBlocks.REDWOOD_LEAVES),
                        new CherryFoliagePlacer(
                                ConstantIntProvider.create(2), // radius
                                ConstantIntProvider.create(0), // offset
                                ConstantIntProvider.create(3), // height
                                0.12f, // wide bottom layer hole chance
                                0.18f, // corner hole chance
                                0.06f, // hanging leaves chance
                                0.03f  // hanging leaves extension chance
                        ),
                        new TwoLayersFeatureSize(1, 0, 1)
                )
                        .dirtProvider(BlockStateProvider.of(Blocks.PODZOL))
                        .forceDirt()
                        .build()
        );

        // Medium redwood – taller, fuller crown but still conical
        register(context, MEDIUM_REDWOOD_KEY, Feature.TREE,
                new TreeFeatureConfig.Builder(
                        BlockStateProvider.of(ModBlocks.REDWOOD_LOG),
                        new StraightTrunkPlacer(16, 5, 3),
                        BlockStateProvider.of(ModBlocks.REDWOOD_LEAVES),
                        new CherryFoliagePlacer(
                                ConstantIntProvider.create(3), // radius
                                ConstantIntProvider.create(0), // offset
                                ConstantIntProvider.create(5), // height
                                0.10f,
                                0.15f,
                                0.08f,
                                0.04f
                        ),
                        new TwoLayersFeatureSize(1, 0, 2)
                )
                        .dirtProvider(BlockStateProvider.of(Blocks.PODZOL))
                        .forceDirt()
                        .build()
        );

        // Giant sequoia – massive trunk using DarkOakTrunkPlacer (2x2 trunk) and large Cherry foliage
        register(context, GIANT_SEQUOIA_KEY, Feature.TREE,
                new TreeFeatureConfig.Builder(
                        BlockStateProvider.of(ModBlocks.REDWOOD_LOG),
                        // DarkOakTrunkPlacer produces a 2x2 trunk; parameters tuned for very tall trunk
                        new DarkOakTrunkPlacer(24, 10, 6),
                        BlockStateProvider.of(ModBlocks.REDWOOD_LEAVES),
                        new CherryFoliagePlacer(
                                ConstantIntProvider.create(5), // radius
                                ConstantIntProvider.create(1), // offset
                                ConstantIntProvider.create(8), // height
                                0.08f,
                                0.12f,
                                0.10f,
                                0.06f
                        ),
                        // larger size to allow a big crown
                        new TwoLayersFeatureSize(2, 1, 3)
                )
                        .dirtProvider(BlockStateProvider.of(Blocks.PODZOL))
                        .forceDirt()
                        .build()
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
            FC configuration
    ) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
