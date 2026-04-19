package net.saturn.murdermysteryfabric.world.biome;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

public class ModMaterialRules {
    private static final MaterialRules.MaterialRule GREEN_TERRACOTTA = makeStateRule(Blocks.GREEN_TERRACOTTA);
    private static final MaterialRules.MaterialRule GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final MaterialRules.MaterialRule STONE = makeStateRule(Blocks.STONE);



    public static MaterialRules.MaterialRule makeKaupenValleyRules() {
        return MaterialRules.sequence(
                MaterialRules.condition(MaterialRules.biome(ModBiomes.KAUPEN_VALLEY),
                        MaterialRules.sequence(MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH, GRASS_BLOCK), STONE)),
                // Default to green terracotta
                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, GREEN_TERRACOTTA)
        );
    }


    private static MaterialRules.MaterialRule makeStateRule(Block block) {
        return MaterialRules.block(block.getDefaultState());
    }
}