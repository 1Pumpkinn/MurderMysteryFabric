package net.saturn.murdermysteryfabric.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;

public class ModBlocks {




    private static Block register(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Murdermysteryfabric.MODID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Murdermysteryfabric.MODID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void initialize() {
        // Ensure class is loaded
    }
}
