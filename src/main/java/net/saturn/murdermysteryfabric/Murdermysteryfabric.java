package net.saturn.murdermysteryfabric;
import net.fabricmc.api.ModInitializer;
import net.saturn.murdermysteryfabric.block.ModBlocks;
import net.saturn.murdermysteryfabric.command.ModCommands;
import net.saturn.murdermysteryfabric.entity.ModEntities;
import net.saturn.murdermysteryfabric.event.ModEvents;
import net.saturn.murdermysteryfabric.fluid.ModFluids;
import net.saturn.murdermysteryfabric.item.ModItemGroups;
import net.saturn.murdermysteryfabric.item.ModItems;
import net.saturn.murdermysteryfabric.sound.ModSounds;
import net.saturn.murdermysteryfabric.world.biome.ModBiomes;
import net.saturn.murdermysteryfabric.world.gen.ModWorldGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Murdermysteryfabric implements ModInitializer {

    public static final String MODID = "murdermysteryfabric";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        ModEntities.registerModEntities();
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModItemGroups.registerModItemGroups();
        ModFluids.registerFluids();
        ModSounds.registerModSounds();
        ModCommands.registerModCommands();
        ModEvents.registerModEvents();
        ModWorldGeneration.generateModWorldGeneration();
        ModBiomes.registerModBiomes();
    }
}