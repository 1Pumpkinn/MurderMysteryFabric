package net.saturn.murdermysteryfabric.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import net.saturn.murdermysteryfabric.game.GameManager;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(FogRenderer.class)
public class DarkVisionMixin {

    private static final float FOG_R = 0.004f;
    private static final float FOG_G = 0.004f;
    private static final float FOG_B = 0.012f;

    @Inject(method = "applyFog", at = @At("RETURN"), cancellable = true)
    private static void forceMidnightFogColor(
            Camera camera, int viewDistance,
            RenderTickCounter renderTickCounter, float partialTick,
            ClientWorld clientWorld, CallbackInfoReturnable<Vector4f> cir) {
        if (!GameManager.getInstance().isGameRunning()) return;
        float alpha = cir.getReturnValue() != null ? cir.getReturnValue().w : 1.0f;
        cir.setReturnValue(new Vector4f(FOG_R, FOG_G, FOG_B, alpha));
    }
}