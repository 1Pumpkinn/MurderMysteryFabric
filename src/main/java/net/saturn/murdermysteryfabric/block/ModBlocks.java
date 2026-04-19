package net.saturn.murdermysteryfabric.block;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;
import net.saturn.murdermysteryfabric.world.tree.ModSaplingGenerators;

public class ModBlocks {

    public static final Block REDWOOD_LOG = registerBlock("redwood_log",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.OAK_LOG).strength(3.0F)));

    public static final Block REDWOOD_WOOD = registerBlock("redwood_wood",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.OAK_WOOD).strength(3.0F)));

    public static final Block STRIPPED_REDWOOD_LOG = registerBlock("stripped_redwood_log",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_OAK_LOG).strength(3.0F)));

    public static final Block STRIPPED_REDWOOD_WOOD = registerBlock("stripped_redwood_wood",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_OAK_WOOD).strength(3.0F)));

    public static final Block REDWOOD_PLANKS = registerBlock("redwood_planks",
            new Block(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).strength(3.0F)));

    public static final Block REDWOOD_LEAVES =
            registerBlock("redwood_leaves",
                    new Block(AbstractBlock.Settings.copy(Blocks.OAK_LEAVES)));

        public static final Block REDWOOD_SAPLING = registerBlock("redwood_sapling",
            new SaplingBlock(ModSaplingGenerators.REDWOOD ,AbstractBlock.Settings.copy(Blocks.OAK_SAPLING)));


                private static Block registerBlock(String name, Block block) {
                    registerBlockItem(name, block);
                    return Registry.register(Registries.BLOCK, Identifier.of(Murdermysteryfabric.MODID, name), block);
                }



                private static void registerBlockItem(String name, Block block) {
                    Registry.register(Registries.ITEM, Identifier.of(Murdermysteryfabric.MODID, name),
                            new BlockItem(block, new Item.Settings()));
                }

                public static void registerModBlocks() {
                    // registers ModBlocks
                }
            }

