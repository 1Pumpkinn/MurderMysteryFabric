package net.saturn.murdermysteryfabric.world.biome;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

public class ModMaterialRules {

    private static final MaterialRules.MaterialRule PODZOL      = makeStateRule(Blocks.PODZOL);
    private static final MaterialRules.MaterialRule COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);
    private static final MaterialRules.MaterialRule DIRT        = makeStateRule(Blocks.DIRT);

    public static MaterialRules.MaterialRule makeMixedRedwoodForestRules() {
        return MaterialRules.sequence(
                MaterialRules.condition(
                        MaterialRules.biome(ModBiomes.MIXED_REDWOOD_FOREST),
                        MaterialRules.sequence(
                                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, PODZOL),
                                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH, COARSE_DIRT),
                                DIRT
                        )
                )
        );
    }

    private static MaterialRules.MaterialRule makeStateRule(Block block) {
        return MaterialRules.block(block.getDefaultState());
    }
}
