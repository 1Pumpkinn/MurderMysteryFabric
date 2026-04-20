package net.saturn.murdermysteryfabric.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = Camera.class, priority = 500) // lower priority = runs before Camera Overhaul
public class BreathingCameraMixin {

    @Unique
    private double breathPhase = 0.0;

    @Inject(
            method = "update(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;ZZF)V",
            at = @At("RETURN")
    )
    private void applyBreathing(World world, Entity focusedEntity,
                                boolean thirdPerson, boolean inverseView,
                                float tickDelta, CallbackInfo ci) {

        if (thirdPerson) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.player == null) return;

        Camera camera = (Camera) (Object) this;
        CameraAccessor access = (CameraAccessor) camera;

        // Advance breath phase — one full breath cycle every ~4 seconds
        // 0.016 ≈ 1/60 second per frame; adjust speed to taste
        breathPhase += 0.016 * (2 * Math.PI / 4.0);
        if (breathPhase > 2 * Math.PI) breathPhase -= 2 * Math.PI;

        // Gentle sine wave — only Y offset, no rotation
        // Amplitude 0.004 = ~0.3cm, barely perceptible but present
        double breathY = Math.sin(breathPhase) * 0.004;

        Vec3d currentPos = camera.getCameraPos();
        access.invokeSetPos(currentPos.add(0.0, breathY, 0.0));
    }
}