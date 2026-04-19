package net.saturn.murdermysteryfabric;

import net.fabricmc.api.ModInitializer;
import net.saturn.murdermysteryfabric.block.ModBlocks;
import net.saturn.murdermysteryfabric.command.ModCommands;
import net.saturn.murdermysteryfabric.entity.ModEntities;
import net.saturn.murdermysteryfabric.event.ModEvents;
import net.saturn.murdermysteryfabric.item.ModItemGroups;
import net.saturn.murdermysteryfabric.item.ModItems;
import net.saturn.murdermysteryfabric.sound.ModSounds;
import net.saturn.murdermysteryfabric.world.biome.ModBiomes;
import net.saturn.murdermysteryfabric.world.biome.ModMaterialRules;
import net.saturn.murdermysteryfabric.world.gen.ModWorldGeneration;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

public class Murdermysteryfabric implements ModInitializer, TerraBlenderApi {

    public static final String MODID = "murdermysteryfabric";

    @Override
    public void onInitialize() {
        ModEntities.initialize();
        ModItems.initialize();
        ModBlocks.registerModBlocks();
        ModItemGroups.initialize();
        ModSounds.initialize();
        ModCommands.register();
        ModEvents.register();
        ModWorldGeneration.generateModWorldGeneration();
    }

    @Override
    public void onTerraBlenderInitialized() {
        ModBiomes.registerBiomes();

        // Register our surface rules
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, ModMaterialRules.makeKaupenValleyRules());
    }
}
