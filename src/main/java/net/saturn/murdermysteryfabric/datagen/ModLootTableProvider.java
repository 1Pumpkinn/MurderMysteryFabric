package net.saturn.murdermysteryfabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;
import net.saturn.murdermysteryfabric.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {

    public ModLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.REDWOOD_LOG);
        addDrop(ModBlocks.REDWOOD_WOOD);
        addDrop(ModBlocks.STRIPPED_REDWOOD_LOG);
        addDrop(ModBlocks.STRIPPED_REDWOOD_WOOD);
        addDrop(ModBlocks.REDWOOD_PLANKS);
        addDrop(ModBlocks.REDWOOD_SAPLING);

        addDrop(ModBlocks.REDWOOD_LEAVES, leavesDrops(ModBlocks.REDWOOD_LEAVES, ModBlocks.REDWOOD_SAPLING, 0.0625f));
    }
}