package net.saturn.murdermysteryfabric.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.saturn.murdermysteryfabric.block.ModBlocks;
import net.saturn.murdermysteryfabric.item.ModItems;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.createLogTexturePool(ModBlocks.REDWOOD_LOG).log(ModBlocks.REDWOOD_LOG).wood(ModBlocks.REDWOOD_WOOD);
        blockStateModelGenerator.createLogTexturePool(ModBlocks.STRIPPED_REDWOOD_LOG).log(ModBlocks.STRIPPED_REDWOOD_LOG).wood(ModBlocks.STRIPPED_REDWOOD_WOOD);

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.REDWOOD_PLANKS);
        blockStateModelGenerator.registerSingleton(ModBlocks.REDWOOD_LEAVES, TexturedModel.LEAVES);
        blockStateModelGenerator.registerTintableCrossBlockState(ModBlocks.REDWOOD_SAPLING, BlockStateModelGenerator.CrossType.NOT_TINTED);

        blockStateModelGenerator.registerParentedItemModel(ModBlocks.REDWOOD_PLANKS, ModelIds.getBlockModelId(ModBlocks.REDWOOD_PLANKS));
        blockStateModelGenerator.registerParentedItemModel(ModBlocks.REDWOOD_LEAVES, ModelIds.getBlockModelId(ModBlocks.REDWOOD_LEAVES));
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(ModItems.EVIDENCE_FILE, Models.GENERATED);
        generator.register(ModItems.KNIFE, Models.HANDHELD);


        }
    }
