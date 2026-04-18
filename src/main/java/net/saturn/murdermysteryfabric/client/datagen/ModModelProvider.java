package net.saturn.murdermysteryfabric.client.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.saturn.murdermysteryfabric.block.ModBlocks;
import net.saturn.murdermysteryfabric.item.ModItems;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        generator.registerSimpleCubeAll(ModBlocks.EVIDENCE_BOARD);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(ModItems.KNIFE, Models.HANDHELD);
        generator.register(ModItems.POISON, Models.GENERATED);
        generator.register(ModItems.MAGNIFYING_GLASS, Models.GENERATED);
        generator.register(ModItems.EVIDENCE_FILE, Models.GENERATED);
    }
}
