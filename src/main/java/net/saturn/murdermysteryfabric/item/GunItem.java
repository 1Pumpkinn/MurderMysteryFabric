package net.saturn.murdermysteryfabric.item;

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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GunItem extends Item {

    public GunItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        
        if (!world.isClient()) {
            // Shoot a projectile (using arrow entity for simplicity)
            Vec3d lookVec = user.getRotationVec(1.0F);
            Vec3d startPos = user.getEyePos();
            
            // Create an arrow projectile
            net.minecraft.entity.projectile.ArrowEntity arrow = new net.minecraft.entity.projectile.ArrowEntity(world, user, stack, null);
            arrow.setPosition(startPos);
            arrow.setVelocity(lookVec.x, lookVec.y, lookVec.z, 3.0F, 0.0F);
            arrow.setDamage(20.0); // High damage to kill in one hit
            
            world.spawnEntity(arrow);
            
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
