package net.saturn.murdermysteryfabric.mixin;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Slows player walk and sprint speed to a more grounded, realistic pace.
 *
 * Vanilla defaults:
 *   Walk  = 0.10  (base movement speed attribute)
 *   Sprint multiplier = 1.30x applied on top
 *
 * We override the base speed attribute directly when the player spawns/loads,
 * and cap sprint by also reducing the sprint speed boost.
 *
 * Targeted values:
 *   Walk   = 0.065  (~35% slower than vanilla)
 *   Sprint = 0.080  (vanilla sprint would be ~0.13; we keep it only slightly faster than walk)
 */
@Mixin(PlayerEntity.class)
public class PlayerMovementSpeedMixin {

    /**
     * Applied once when the player entity ticks for the first time after joining/loading.
     * Sets the base movement speed attribute to our custom walk speed.
     * We inject at HEAD of tick() and use a flag approach via the attribute value itself
     * so it doesn't override every tick (attribute sets are cheap but needless).
     */
    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void applyCustomMovementSpeed(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;

        // Only apply server-side to avoid double application, OR apply client-side only for prediction.
        // For a Fabric mod, applying on both sides keeps client prediction accurate.
        var speedAttr = self.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
        if (speedAttr == null) return;

        // Vanilla base value is 0.10. We want 0.065 for walking.
        // We only update if it's still at (or near) vanilla default to avoid fighting other mods.
        double currentBase = speedAttr.getBaseValue();
        if (Math.abs(currentBase - 0.10) < 0.001) {
            speedAttr.setBaseValue(0.065);
        }
    }

    /**
     * Reduces the sprint speed multiplier.
     *
     * Vanilla applies a 0.30 bonus (so total = base * 1.30) via
     * PlayerEntity#getSprinting check in LivingEntity#travel().
     *
     * We cancel the vanilla sprint velocity boost and apply our own smaller one.
     */
    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void dampSprintBoost(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        if (!self.isSprinting()) return;
        if (self.getEntityWorld().isClient()) return; // let client handle prediction

        // Re-scale the horizontal velocity down so sprint ≈ 0.080 effective speed.
        // The sprint bonus has already been applied by vanilla by TAIL; we scale it back.
        // Target ratio: 0.080 / (0.065 * 1.30) ≈ 0.945
        var vel = self.getVelocity();
        self.setVelocity(vel.x * 0.945, vel.y, vel.z * 0.945);
    }
}