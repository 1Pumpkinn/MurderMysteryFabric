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
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.MAGNIFYING_GLASS))
                    .displayName(Text.translatable("itemGroup.murdermysteryfabric.murder_mystery"))
                    .entries((context, entries) -> {
                        // Murderer
                        entries.add(ModItems.KNIFE);
                        entries.add(ModItems.POISON);
                        // Detective
                        entries.add(ModItems.MAGNIFYING_GLASS);
                        entries.add(ModItems.EVIDENCE_FILE);
                        // Blocks

                    })
                    .build());

    public static void initialize() {
        // Ensure class is loaded
    }
}
