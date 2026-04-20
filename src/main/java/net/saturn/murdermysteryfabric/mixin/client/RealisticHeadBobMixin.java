package net.saturn.murdermysteryfabric.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Camera.class)
public class RealisticHeadBobMixin {

    @Unique private static final PerlinNoiseSampler SAMPLER = new PerlinNoiseSampler(Random.create());

    @Unique private float hrb_groundBlend = 0.0f;
    @Unique private float hrb_bobPhase = 0.0f;
    @Unique private long hrb_lastNano = -1L;

    @Unique
    private static float slowSample(float gameTime, float intensity, int offset) {
        return (float) SAMPLER.sample(gameTime / intensity, offset, 0) * 1.5f;
    }

    @Inject(method = "update(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;ZZF)V", at = @At("RETURN"))
    private void horrorCameraUpdate(World area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.player == null || mc.world == null || thirdPerson) return;

        Camera camera = (Camera) (Object) this;
        CameraAccessor access = (CameraAccessor) (Object) this;

        // --- Delta Time & Ground Blending (Anti-Jitter) ---
        long nowNano = System.nanoTime();
        if (hrb_lastNano < 0L) hrb_lastNano = nowNano;
        float dt = Math.min((float)((nowNano - hrb_lastNano) / 1_000_000_000.0), 0.05f);
        hrb_lastNano = nowNano;

        ClientPlayerEntity player = mc.player;
        float speed = (float) player.getVelocity().horizontalLength();
        boolean onGround = player.isOnGround();
        boolean moving = onGround && speed > 0.04f;

        float groundTarget = onGround ? 1.0f : 0.0f;
        float groundLerpK = onGround ? 1.0f - (float) Math.exp(-dt / 0.08f) : 1.0f - (float) Math.exp(-dt / 0.05f);
        hrb_groundBlend += (groundTarget - hrb_groundBlend) * groundLerpK;

        float phaseSpeed = moving ? (0.35f + speed * 1.2f) : 0f;
        hrb_bobPhase += phaseSpeed * dt * 20f;

        float gameTime = (mc.world.getTime() % 24000L) + tickDelta;

        // ── 1. LABORED BREATHING ──────────────────────────────────────────
        // Instead of a perfect sine wave, we mix two noise frequencies.
        // This creates a slightly erratic, "heavy" chest heave.
        float breathCore = slowSample(gameTime, 12f, 0);
        float breathWheeze = slowSample(gameTime, 4f, 100) * 0.3f; // Slight secondary shudder

        float breathY = (breathCore + breathWheeze) * 0.012f;
        float breathPitch = (breathCore - breathWheeze) * 0.50f;

        // ── 2. CREEPING DRIFT (Unease) ────────────────────────────────────
        // Very slow, wide arcs that make the camera feel slightly detached
        // from the player's control, simulating dizziness or paranoia.
        float driftYaw = slowSample(gameTime, 30f, 40) * 0.25f;
        float driftPitch = slowSample(gameTime, 35f, 50) * 0.15f;

        // ── 3. PARANOIA TWITCH ────────────────────────────────────────────
        // Raises noise to the 5th power to isolate only the extreme peaks.
        // This causes the camera to occasionally do a sharp, tiny "flinch".
        float rawTwitch = slowSample(gameTime, 6f, 200);
        float twitchMultiplier = (float) Math.pow(Math.abs(rawTwitch), 5.0);
        float paranoiaPitch = twitchMultiplier * Math.signum(rawTwitch) * 0.4f;
        float paranoiaYaw = twitchMultiplier * slowSample(gameTime, 2f, 250) * 0.3f;

        // ── 4. LUMBERING FOOTSTEPS ────────────────────────────────────────
        // Slower, heavier impacts.
        float stepLurchY = 0.0f;
        float stepYawLean = 0.0f;
        if (moving) {
            // Drop the camera sharply on the downstep, recover slowly
            float stepSine = (float)Math.sin(hrb_bobPhase);
            float stepCos = (float)Math.cos(hrb_bobPhase);

            stepLurchY = -Math.abs(stepCos) * 0.018f * hrb_groundBlend; // Heavier Y drop
            stepYawLean = stepSine * 0.40f * hrb_groundBlend;           // Heavier side-to-side sway
        }

        // ── APPLY HORROR ROTATION ─────────────────────────────────────────
        access.invokeSetRotation(
                camera.getYaw() + driftYaw + stepYawLean + paranoiaYaw,
                camera.getPitch() + breathPitch + driftPitch + paranoiaPitch
        );

        // ── APPLY CAMERA POSITION ─────────────────────────────────────────
        double yOffset = breathY + stepLurchY;

        if (Math.abs(yOffset) > 0.00001f) {
            access.invokeSetPos(
                    player.getCameraPosVec(tickDelta).add(0.0, yOffset, 0.0)
            );
        }
    }
}