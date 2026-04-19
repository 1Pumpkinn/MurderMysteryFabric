package net.saturn.murdermysteryfabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.saturn.murdermysteryfabric.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        valueLookupBuilder(BlockTags.PICKAXE_MINEABLE);
        valueLookupBuilder(BlockTags.NEEDS_STONE_TOOL);

        valueLookupBuilder(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.REDWOOD_LOG,
                        ModBlocks.REDWOOD_WOOD,
                        ModBlocks.STRIPPED_REDWOOD_LOG,
                        ModBlocks.STRIPPED_REDWOOD_WOOD);

    }
}