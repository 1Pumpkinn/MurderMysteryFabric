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
    private static final int MAX_LIFE = 100;

    private int life = 0;

    public BulletEntity(EntityType<? extends BulletEntity> entityType, World world) {
        super(entityType, world);
    }

    public BulletEntity(World world, LivingEntity owner) {
        super(ModEntities.BULLET, world);
        setOwner(owner);
        setPosition(owner.getEyePos());
        setVelocity(owner.getRotationVec(1.0F).multiply(SPEED));
        setInvisible(true);
        setNoGravity(true);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}

    @Override
    public void tick() {
        super.tick();

        if (++life > MAX_LIFE) {
            discard();
            return;
        }

        HitResult hit = ProjectileUtil.getCollision(this, this::canHit);
        if (hit.getType() != HitResult.Type.MISS) {
            onCollision(hit);
            return;
        }

        Vec3d vel = getVelocity();
        setPosition(getX() + vel.x, getY() + vel.y, getZ() + vel.z);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        Entity hit = entityHitResult.getEntity();
        Entity owner = getOwner();
        if (hit == owner) return;
        if (!(getEntityWorld() instanceof ServerWorld serverWorld)) return;

        if (hit instanceof LivingEntity living) {
            living.damage(serverWorld,
                    serverWorld.getDamageSources().mobProjectile(this, owner instanceof LivingEntity le ? le : null),
                    1000.0F);
        }

        discard();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        discard();
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && entity != getOwner();
    }

    @Override
    protected void readCustomData(ReadView view) {
        life = view.getInt("Life", 0);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        view.putInt("Life", life);
    }
}