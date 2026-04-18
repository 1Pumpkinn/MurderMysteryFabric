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

    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        // KNIFE model is custom (manually created in resources) - skipped in datagen
        // Custom models with elements and complex display transforms aren't supported by Fabric's datagen API
        // The knife.json file is maintained manually in src/main/resources/assets/murdermysteryfabric/models/item/
        
        generator.register(ModItems.EVIDENCE_FILE, Models.GENERATED);
    }
}
