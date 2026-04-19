package net.saturn.murdermysteryfabric.world.tree;

import net.minecraft.block.SaplingGenerator;
import net.saturn.murdermysteryfabric.world.ModConfiguredFeatures;

import java.util.Optional;

public class ModSaplingGenerators {

    public static final SaplingGenerator REDWOOD = new SaplingGenerator(
            "redwood",
            // common
            Optional.of(ModConfiguredFeatures.SMALL_REDWOOD_KEY),
            // uncommon
            Optional.of(ModConfiguredFeatures.MEDIUM_REDWOOD_KEY),
            // rare
            Optional.of(ModConfiguredFeatures.GIANT_SEQUOIA_KEY)
    );
}
