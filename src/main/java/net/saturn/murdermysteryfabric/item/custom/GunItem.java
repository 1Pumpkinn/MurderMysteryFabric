package net.saturn.murdermysteryfabric.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.saturn.murdermysteryfabric.entity.BulletEntity;

public class GunItem extends Item {

    public GunItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            // Create and spawn custom bullet projectile
            BulletEntity bullet = new BulletEntity(world, user);
            world.spawnEntity(bullet);
            
            // Play gun sound
            world.playSound(null, user.getX(), user.getY(), user.getZ(), 
                    SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.5F, 1.5F);
        }
        
        return ActionResult.SUCCESS;
    }

    @Override
    public boolean canMine(ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity miner) {
        return false;
    }
}
