package net.saturn.murdermysteryfabric.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Amplifies the camera head-bob effect to feel more realistic and weighty.
 *
 * Layers three motions on top of vanilla bobbing:
 *   - Extra vertical dip on each footstep
 *   - Left/right roll (camera tilts into each step)
 *   - Subtle forward pitch sway
 *
 * Uses MinecraftClient.getInstance() instead of an accessor — GameRenderer
 * already has a public getClient() in 1.21.11 which conflicts with @Accessor.
 */
@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class RealisticHeadBobMixin {

    @Inject(method = "bobView", at = @At("TAIL"))
    private void applyRealisticBob(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        PlayerEntity player = client.player;

        // Only bob when on the ground and actually moving
        if (!player.isOnGround()) return;

        float speed = (float) player.getVelocity().horizontalLength();
        if (speed < 0.01f) return;

        // Scale intensity with movement speed (walk ~0.065, sprint ~0.080)
        float bobStrength = MathHelper.clamp(speed / 0.065f, 0.0f, 1.8f);

        // Use horizontalSpeed as the bob phase — it increments each ground tick
        float bobPhase = player.speed + tickDelta;

        // Vertical dip — always downward, peaks at heel strike
        float verticalBob = MathHelper.sin(bobPhase * (float) Math.PI) * bobStrength * 0.018f;
        matrices.translate(0.0, -Math.abs(verticalBob), 0.0);

        // Side roll — camera tilts left/right with each step
        float rollAngle = MathHelper.sin(bobPhase * (float) Math.PI * 0.5f) * bobStrength * 1.8f;
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Z.rotationDegrees(rollAngle));

        // Pitch sway — subtle forward lean on each step
        float pitchSway = Math.abs(MathHelper.sin(bobPhase * (float) Math.PI)) * bobStrength * 0.4f;
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotationDegrees(-pitchSway));
    }
}