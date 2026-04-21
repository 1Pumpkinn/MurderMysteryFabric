package net.saturn.murdermysteryfabric.fluid;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;

public class ModFluids {

    public static final FlowableFluid STILL_BLOOD_WATER = Registry.register(
            Registries.FLUID,
            Identifier.of(Murdermysteryfabric.MODID, "blood_water"),
            new BloodWaterFluid.Still()
    );

    public static final FlowableFluid FLOWING_BLOOD_WATER = Registry.register(
            Registries.FLUID,
            Identifier.of(Murdermysteryfabric.MODID, "flowing_blood_water"),
            new BloodWaterFluid.Flowing()
    );

    public static final Block BLOOD_WATER_BLOCK = Registry.register(
            Registries.BLOCK,
            Identifier.of(Murdermysteryfabric.MODID, "blood_water_block"),
            new FluidBlock(
                    STILL_BLOOD_WATER,
                    AbstractBlock.Settings.copy(Blocks.WATER)
                            .registryKey(blockKey("blood_water_block")) // 🔥 REQUIRED
                            .replaceable()
                            .liquid()
            )
    );

    public static final Item BLOOD_WATER_BUCKET = Registry.register(
            Registries.ITEM,
            Identifier.of(Murdermysteryfabric.MODID, "blood_water_bucket"),
            new BucketItem(
                    STILL_BLOOD_WATER,
                    new Item.Settings()
                            .registryKey(itemKey("blood_water_bucket")) // ✅ correct
                            .recipeRemainder(Items.BUCKET)
                            .maxCount(1)
            )
    );

    private static RegistryKey<Block> blockKey(String name) {
        return RegistryKey.of(
                RegistryKeys.BLOCK,
                Identifier.of(Murdermysteryfabric.MODID, name)
        );
    }

    private static RegistryKey<Item> itemKey(String name) {
        return RegistryKey.of(
                RegistryKeys.ITEM,
                Identifier.of(Murdermysteryfabric.MODID, name)
        );
    }

    public static void registerFluids() {
        Murdermysteryfabric.LOGGER.info("Registering Fluids for " + Murdermysteryfabric.MODID);
    }
}