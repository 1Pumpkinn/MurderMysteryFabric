package net.saturn.murdermysteryfabric.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Custom bullet projectile for the gun.
 * - Invisible
 * - Not affected by gravity
 * - One-shots entities on hit
 * - Does nothing when hitting blocks
 */
public class BulletEntity extends ProjectileEntity {
    
    private static final double SPEED = 3.0;
    private int life = 0;
    private static final int MAX_LIFE = 100; // Despawn after 5 seconds (100 ticks)
    
    public BulletEntity(EntityType<? extends BulletEntity> entityType, World world) {
        super(entityType, world);
    }
    
    public BulletEntity(World world, LivingEntity owner) {
        super(ModEntities.BULLET, world);
        this.setOwner(owner);
        this.setPosition(owner.getEyePos());
        
        // Set velocity based on owner's look direction
        Vec3d lookVec = owner.getRotationVec(1.0F);
        this.setVelocity(lookVec.multiply(SPEED));
        
        // Make invisible
        this.setInvisible(true);
        this.setNoGravity(true);
    }
    
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        // No additional data to track
    }
    
    @Override
    public void tick() {
        super.tick();
        
        // Increment life counter and despawn if too old
        life++;
        if (life > MAX_LIFE) {
            this.discard();
            return;
        }
        
        // Check for collisions
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onCollision(hitResult);
        }
        
        // Update position based on velocity
        Vec3d velocity = this.getVelocity();
        double newX = this.getX() + velocity.x;
        double newY = this.getY() + velocity.y;
        double newZ = this.getZ() + velocity.z;
        this.setPosition(newX, newY, newZ);
        
        // Update rotation to match velocity direction
        this.setVelocity(velocity);
    }
    
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        
        Entity hitEntity = entityHitResult.getEntity();
        Entity owner = this.getOwner();
        
        // Don't hit the shooter
        if (hitEntity == owner) {
            return;
        }
        
        // One-shot kill any living entity
        if (hitEntity instanceof LivingEntity) {
            DamageSource damageSource = this.getDamageSources().mobProjectile(this, owner instanceof LivingEntity ? (LivingEntity) owner : null);
            hitEntity.damage(this.getServerWorld(), damageSource, 1000.0F); // Massive damage to ensure one-shot
        }
        
        // Remove bullet after hitting entity
        this.discard();
    }
    
    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        
        // Do nothing when hitting blocks, just remove the bullet
        this.discard();
    }
    
    @Override
    public boolean hasNoGravity() {
        return true;
    }
    
    @Override
    protected boolean canHit(Entity entity) {
        // Can hit any entity except the owner
        return super.canHit(entity) && entity != this.getOwner();
    }
    
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.life = nbt.getInt("Life");
    }
    
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Life", this.life);
    }
}
