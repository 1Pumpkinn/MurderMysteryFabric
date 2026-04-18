package net.saturn.murdermysteryfabric.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.saturn.murdermysteryfabric.block.ModBlocks;
import net.saturn.murdermysteryfabric.item.ModItems;
import net.saturn.murdermysteryfabric.sound.ModSounds;

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

        // Item Groups
        translationBuilder.add("itemGroup.murdermysteryfabric.murder_mystery", "Murder Mystery");

        // Sounds
        translationBuilder.add("murdermysteryfabric.sound.clue_found", "Clue Found");
        translationBuilder.add("murdermysteryfabric.sound.knife_stab", "Knife Stab");
        translationBuilder.add("murdermysteryfabric.sound.investigation_ambient", "Investigation Ambient");
    }
}
