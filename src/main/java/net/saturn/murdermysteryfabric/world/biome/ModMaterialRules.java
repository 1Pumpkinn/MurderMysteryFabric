package net.saturn.murdermysteryfabric.world.biome;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

public class ModMaterialRules {

    private static final MaterialRules.MaterialRule PODZOL      = makeStateRule(Blocks.PODZOL);
    private static final MaterialRules.MaterialRule COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);
    private static final MaterialRules.MaterialRule DIRT        = makeStateRule(Blocks.DIRT);
    private static final MaterialRules.MaterialRule STONE       = makeStateRule(Blocks.STONE);

    public static MaterialRules.MaterialRule makeKaupenValleyRules() {
        return MaterialRules.sequence(
                MaterialRules.condition(
                        MaterialRules.biome(ModBiomes.KAUPEN_VALLEY),
                        MaterialRules.sequence(
                                // Top surface block: podzol (old-growth forest floor)
                                MaterialRules.condition(
                                        MaterialRules.STONE_DEPTH_FLOOR,
                                        PODZOL
                                ),
                                // A few blocks below surface: coarse dirt
                                MaterialRules.condition(
                                        MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH,
                                        COARSE_DIRT
                                ),
                                // Deeper still: regular dirt
                                DIRT
                        )
                )
        );
    }

    private static MaterialRules.MaterialRule makeStateRule(Block block) {
        return MaterialRules.block(block.getDefaultState());
    }
}