package net.saturn.murdermysteryfabric.block;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;
import net.saturn.murdermysteryfabric.world.tree.ModSaplingGenerators;

public class ModBlocks {

    public static final Block REDWOOD_LOG = registerBlock("redwood_log",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.OAK_LOG)
                    .strength(3.0F)
                    .registryKey(key("redwood_log"))));

    public static final Block REDWOOD_WOOD = registerBlock("redwood_wood",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.OAK_WOOD)
                    .strength(3.0F)
                    .registryKey(key("redwood_wood"))));

    public static final Block STRIPPED_REDWOOD_LOG = registerBlock("stripped_redwood_log",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_OAK_LOG)
                    .strength(3.0F)
                    .registryKey(key("stripped_redwood_log"))));

    public static final Block STRIPPED_REDWOOD_WOOD = registerBlock("stripped_redwood_wood",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_OAK_WOOD)
                    .strength(3.0F)
                    .registryKey(key("stripped_redwood_wood"))));

    public static final Block REDWOOD_PLANKS = registerBlock("redwood_planks",
            new Block(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)
                    .strength(3.0F)
                    .registryKey(key("redwood_planks"))));

    public static final Block REDWOOD_LEAVES = registerBlock("redwood_leaves",
            new Block(AbstractBlock.Settings.copy(Blocks.OAK_LEAVES)
                    .registryKey(key("redwood_leaves"))));

    public static final Block REDWOOD_SAPLING = registerBlock("redwood_sapling",
            new SaplingBlock(ModSaplingGenerators.REDWOOD,
                    AbstractBlock.Settings.copy(Blocks.OAK_SAPLING)
                            .registryKey(key("redwood_sapling"))));

    private static RegistryKey<Block> key(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Murdermysteryfabric.MODID, name));
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Murdermysteryfabric.MODID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Murdermysteryfabric.MODID, name),
                new BlockItem(block, new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM,
                                Identifier.of(Murdermysteryfabric.MODID, name)))));
    }

    public static void registerModBlocks() {
        // Registration happens in static initializer
    }
}