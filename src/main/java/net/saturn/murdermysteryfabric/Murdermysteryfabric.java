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

public class Murdermysteryfabric implements ModInitializer {

    public static final String MODID = "murdermysteryfabric";

    @Override
    public void onInitialize() {
        ModEntities.registerModEntities();
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModItemGroups.registerModItemGroups();
        ModSounds.registerModSounds();
        ModCommands.registerModCommands();
        ModEvents.registerModEvents();
        ModWorldGeneration.generateModWorldGeneration();
        ModBiomes.registerModBiomes();
    }
}