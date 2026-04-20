package net.saturn.murdermysteryfabric.mixin;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerMovementSpeedMixin {

    private static final double VANILLA_WALK_SPEED = 0.10;
    private static final double CUSTOM_WALK_SPEED  = 0.065;
    private static final double SPRINT_DAMPEN      = 0.945;

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void applyWalkSpeed(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        var attr = self.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
        if (attr == null) return;
        if (Math.abs(attr.getBaseValue() - VANILLA_WALK_SPEED) < 0.001) {
            attr.setBaseValue(CUSTOM_WALK_SPEED);
        }
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void dampSprintSpeed(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        if (!self.isSprinting() || self.getEntityWorld().isClient()) return;
        var vel = self.getVelocity();
        self.setVelocity(vel.x * SPRINT_DAMPEN, vel.y, vel.z * SPRINT_DAMPEN);
    }
}