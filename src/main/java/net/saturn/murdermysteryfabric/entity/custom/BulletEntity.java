package net.saturn.murdermysteryfabric.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.saturn.murdermysteryfabric.entity.ModEntities;

public class BulletEntity extends ProjectileEntity {

    private static final double SPEED = 3.0;
    private int life = 0;
    private static final int MAX_LIFE = 100;

    public BulletEntity(EntityType<? extends BulletEntity> entityType, World world) {
        super(entityType, world);
    }

    public BulletEntity(World world, LivingEntity owner) {
        super(ModEntities.BULLET, world);
        this.setOwner(owner);
        this.setPosition(owner.getEyePos());

        Vec3d lookVec = owner.getRotationVec(1.0F);
        this.setVelocity(lookVec.multiply(SPEED));

        this.setInvisible(true);
        this.setNoGravity(true);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        // No custom tracked data
    }

    @Override
    public void tick() {
        super.tick();

        life++;
        if (life > MAX_LIFE) {
            this.discard();
            return;
        }

        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onCollision(hitResult);
        }

        Vec3d velocity = this.getVelocity();
        this.setPosition(
                this.getX() + velocity.x,
                this.getY() + velocity.y,
                this.getZ() + velocity.z
        );
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        Entity hitEntity = entityHitResult.getEntity();
        Entity owner = this.getOwner();

        if (hitEntity == owner) return;
        if (!(this.getEntityWorld() instanceof ServerWorld serverWorld)) return;

        if (hitEntity instanceof LivingEntity) {
            hitEntity.damage(serverWorld,
                    serverWorld.getDamageSources().mobProjectile(this, owner instanceof LivingEntity le ? le : null),
                    1000.0F);
        }

        this.discard();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.discard();
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && entity != this.getOwner();
    }

    @Override
    protected void readCustomData(ReadView view) {
        this.life = view.getInt("Life", 0);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        view.putInt("Life", this.life);
    }
}