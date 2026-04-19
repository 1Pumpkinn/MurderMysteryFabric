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
import net.saturn.murdermysteryfabric.Murdermysteryfabric;
import net.saturn.murdermysteryfabric.block.ModBlocks;

public class ModConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?, ?>> REDWOOD_KEY =
            registerKey("redwood");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {

        register(context, REDWOOD_KEY, Feature.TREE,
                new TreeFeatureConfig.Builder(
                        BlockStateProvider.of(ModBlocks.REDWOOD_LOG),

                        new DarkOakTrunkPlacer(12, 6, 4),

                        BlockStateProvider.of(ModBlocks.REDWOOD_LEAVES),

                        new CherryFoliagePlacer(
                                ConstantIntProvider.create(5), // radius
                                ConstantIntProvider.create(1), // offset
                                ConstantIntProvider.create(6), // height

                                0.2f, // wide bottom layer hole chance
                                0.4f, // corner hole chance
                                0.1f, // hanging leaves chance
                                0.05f // hanging leaves extension chance
                        ),

                        new TwoLayersFeatureSize(1, 0, 2)
                )
                        .dirtProvider(BlockStateProvider.of(Blocks.PODZOL))
                        .forceDirt()
                        .build()
        );
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
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