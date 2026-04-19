package net.saturn.murdermysteryfabric;

import net.fabricmc.api.ModInitializer;
import net.saturn.murdermysteryfabric.block.ModBlocks;
import net.saturn.murdermysteryfabric.command.ModCommands;
import net.saturn.murdermysteryfabric.entity.ModEntities;
import net.saturn.murdermysteryfabric.event.ModEvents;
import net.saturn.murdermysteryfabric.item.ModItemGroups;
import net.saturn.murdermysteryfabric.item.ModItems;
import net.saturn.murdermysteryfabric.sound.ModSounds;
import net.saturn.murdermysteryfabric.world.gen.ModWorldGeneration;

public class Murdermysteryfabric implements ModInitializer {

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
}
