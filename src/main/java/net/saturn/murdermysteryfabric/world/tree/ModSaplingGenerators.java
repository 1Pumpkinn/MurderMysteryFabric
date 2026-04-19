package net.saturn.murdermysteryfabric.world.tree;

import net.minecraft.block.SaplingGenerator;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;
import net.saturn.murdermysteryfabric.world.ModConfiguredFeatures;

import java.util.Optional;

public class ModSaplingGenerators {
    public static final SaplingGenerator REDWOOD = new SaplingGenerator(Murdermysteryfabric.MODID + ":blackwood",
            Optional.empty(), Optional.of(ModConfiguredFeatures.REDWOOD_KEY), Optional.empty());
}