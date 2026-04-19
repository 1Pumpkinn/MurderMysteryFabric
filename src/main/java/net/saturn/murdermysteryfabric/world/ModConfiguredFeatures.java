package net.saturn.murdermysteryfabric.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.CherryFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;
import net.saturn.murdermysteryfabric.block.ModBlocks;

public class ModConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> REDWOOD_KEY = registryKey("redwood");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        register(context, REDWOOD_KEY, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.REDWOOD_LOG),
                new StraightTrunkPlacer(5, 6, 3),

                BlockStateProvider.of(ModBlocks.REDWOOD_LEAVES),
                new CherryFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(1), ConstantIntProvider.create(3),
                        0.25f, 0.5f, 0.15f, 0.05f),

                new TwoLayersFeatureSize(1, 0, 2)).build());

    }

    public static RegistryKey<ConfiguredFeature<?, ?>>registryKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(Murdermysteryfabric.MODID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ? >> context,
                                                                                  RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

}
