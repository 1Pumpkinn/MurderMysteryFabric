package net.saturn.murdermysteryfabric.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Exposes Camera's protected setRotation() and setPos() so our
 * horror camera mixin can call them from a sibling mixin class.
 */
@Environment(EnvType.CLIENT)
@Mixin(Camera.class)
public interface CameraAccessor {

    @Invoker("setRotation")
    void invokeSetRotation(float yaw, float pitch);

    @Invoker("setPos")
    void invokeSetPos(Vec3d pos);
}