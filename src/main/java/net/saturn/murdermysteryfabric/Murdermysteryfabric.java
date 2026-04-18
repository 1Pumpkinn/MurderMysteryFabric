package net.saturn.murdermysteryfabric;

import net.fabricmc.api.ModInitializer;
import net.saturn.murdermysteryfabric.block.ModBlocks;
import net.saturn.murdermysteryfabric.command.ModCommands;
import net.saturn.murdermysteryfabric.item.ModItemGroups;
import net.saturn.murdermysteryfabric.item.ModItems;
import net.saturn.murdermysteryfabric.sound.ModSounds;

public class Murdermysteryfabric implements ModInitializer {

    public static final String MODID = "murdermysteryfabric";

    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModBlocks.initialize();
        ModItemGroups.initialize();
        ModSounds.initialize();
        ModCommands.register();
    }
}
