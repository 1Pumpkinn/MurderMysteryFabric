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
@Mixin(value = Camera.class, priority = 500)
public class BreathingCameraMixin {

    private static final double BREATH_CYCLE_SECONDS = 4.0;
    private static final double BREATH_AMPLITUDE     = 0.004;
    private static final double FRAME_TIME           = 0.016;

    @Unique private double breathPhase = 0.0;

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

        breathPhase = (breathPhase + FRAME_TIME * (2 * Math.PI / BREATH_CYCLE_SECONDS)) % (2 * Math.PI);

        Camera camera = (Camera) (Object) this;
        CameraAccessor access = (CameraAccessor) camera;
        Vec3d pos = camera.getCameraPos();
        access.invokeSetPos(pos.add(0.0, Math.sin(breathPhase) * BREATH_AMPLITUDE, 0.0));
    }
}