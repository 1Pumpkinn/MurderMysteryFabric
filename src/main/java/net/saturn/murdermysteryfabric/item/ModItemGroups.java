package net.saturn.murdermysteryfabric.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;
import net.saturn.murdermysteryfabric.block.ModBlocks;

public class ModItemGroups {

    public static final ItemGroup MURDER_MYSTERY = Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of(Murdermysteryfabric.MODID, "murder_mystery"),
            FabricItemGroup.
                    builder().displayName(Text.translatable("itemGroup.murdermysteryfabric.murder_mystery"))
                    .icon(() -> new ItemStack(ModItems.KNIFE))
                    .entries((context, entries) -> {
                        // Items
                        entries.add(ModItems.KNIFE);
                        entries.add(ModItems.GUN);
                        entries.add(ModItems.EVIDENCE_FILE);
                        entries.add(ModItems.TOMAHAWK);



                       // Blocks
                        entries.add(ModBlocks.REDWOOD_LOG);
                        entries.add(ModBlocks.REDWOOD_WOOD);
                        entries.add(ModBlocks.STRIPPED_REDWOOD_LOG);
                        entries.add(ModBlocks.STRIPPED_REDWOOD_WOOD);
                        entries.add(ModBlocks.REDWOOD_PLANKS);
                        entries.add(ModBlocks.REDWOOD_LEAVES);
                        entries.add(ModBlocks.REDWOOD_SAPLING);



                    })
                    .build());

    public static void registerModItemGroups() {
        // Ensure class is loaded
    }
}
