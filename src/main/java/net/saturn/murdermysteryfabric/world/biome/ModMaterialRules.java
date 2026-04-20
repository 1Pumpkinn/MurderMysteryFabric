package net.saturn.murdermysteryfabric.world.biome;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

public class ModMaterialRules {

    private static final MaterialRules.MaterialRule PODZOL      = makeStateRule(Blocks.PODZOL);
    private static final MaterialRules.MaterialRule COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);
    private static final MaterialRules.MaterialRule DIRT        = makeStateRule(Blocks.DIRT);

    public static void register() {
        // Terraform's BiomeRemapper is for swapping biome IDs, not surface rules.
        // For surface rules without TerraBlender, use Fabric API:
        // net.fabricmc.fabric.api.biome.v1.TheEndBiomeData / overworld hooks
        // The correct API in Fabric 0.100+ is:
        // MaterialRuleContext — inject via mixin or use the datagen surface_rule approach.
        // Simplest: just add podzol generation via the biome's own feature list (already done
        // via forceDirt() in your tree features) and remove the surface rule entirely.
    }

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