package net.saturn.murdermysteryfabric.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public abstract class BloodWaterFluid extends FlowableFluid {

    @Override public Fluid getFlowing() { return ModFluids.FLOWING_BLOOD_WATER; }
    @Override public Fluid getStill()   { return ModFluids.STILL_BLOOD_WATER; }

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == ModFluids.STILL_BLOOD_WATER || fluid == ModFluids.FLOWING_BLOOD_WATER;
    }

    @Override public Item getBucketItem() { return ModFluids.BLOOD_WATER_BUCKET; }

    @Override protected boolean isInfinite(ServerWorld world) { return false; }
    @Override protected int getLevelDecreasePerBlock(WorldView world) { return 1; }
    @Override public int getTickRate(WorldView world) { return 5; }
    @Override protected float getBlastResistance() { return 100f; }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropStacks(state, world, pos, blockEntity);
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockView world,
                                        BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !matchesType(fluid);
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return ModFluids.BLOOD_WATER_BLOCK.getDefaultState()
                .with(Properties.LEVEL_15, getBlockStateLevel(state));
    }

    /** Register right-click-empty-cauldron behaviour. Call from ModFluids.registerFluids(). */
    public static void registerCauldronBehavior() {
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(
                ModFluids.BLOOD_WATER_BUCKET,
                (state, world, pos, player, hand, stack) -> {
                    if (!world.isClient()) {
                        player.setStackInHand(hand, ItemStack.EMPTY);
                        player.getInventory().insertStack(new ItemStack(Items.BUCKET));
                        world.setBlockState(pos,
                                Blocks.WATER_CAULDRON.getDefaultState()
                                        .with(LeveledCauldronBlock.LEVEL, 3));
                        world.playSound(null, pos,
                                SoundEvents.ITEM_BUCKET_EMPTY,
                                SoundCategory.BLOCKS, 1.0f, 1.0f);
                    }
                    return ActionResult.SUCCESS;
                }
        );
    }

    // ---- Subclasses ----

    public static class Flowing extends BloodWaterFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }
        @Override protected int getMaxFlowDistance(WorldView world) { return 5; }
        @Override public int getLevel(FluidState state) { return state.get(LEVEL); }
        @Override public boolean isStill(FluidState state) { return false; }
    }

    public static class Still extends BloodWaterFluid {
        @Override protected int getMaxFlowDistance(WorldView world) { return 4; }
        @Override public int getLevel(FluidState state) { return 8; }
        @Override public boolean isStill(FluidState state) { return true; }
    }
}