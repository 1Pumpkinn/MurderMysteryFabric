package net.saturn.murdermysteryfabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.saturn.murdermysteryfabric.block.ModBlocks;
import net.saturn.murdermysteryfabric.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModLangProviderEN extends FabricLanguageProvider {

    public ModLangProviderEN(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        // Murderer items
        translationBuilder.add(ModItems.KNIFE, "Knife");// Detective items
        translationBuilder.add(ModItems.EVIDENCE_FILE, "Evidence File");

        // Blocks
        translationBuilder.add(ModBlocks.REDWOOD_LOG, "Redwood Log");
        translationBuilder.add(ModBlocks.REDWOOD_WOOD, "Redwood Wood");

        translationBuilder.add(ModBlocks.STRIPPED_REDWOOD_LOG, "Stripped Redwood Wood");
        translationBuilder.add(ModBlocks.STRIPPED_REDWOOD_WOOD, "Stripped Redwood Wood");
        translationBuilder.add(ModBlocks.REDWOOD_PLANKS, "Redwood Planks");

        translationBuilder.add(ModBlocks.REDWOOD_LEAVES, "Redwood Leaves");
        translationBuilder.add(ModBlocks.REDWOOD_SAPLING, "Redwood Sapling");



        // Item Groups
        translationBuilder.add("itemGroup.murdermysteryfabric.murder_mystery", "Murder Mystery");

        // Sounds
        translationBuilder.add("murdermysteryfabric.sound.clue_found", "Clue Found");
        translationBuilder.add("murdermysteryfabric.sound.knife_stab", "Knife Stab");
        translationBuilder.add("murdermysteryfabric.sound.investigation_ambient", "Investigation Ambient");
    }
}
