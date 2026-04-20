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

/**
 * Darkens the fog/sky colour during an active Murder Mystery game.
 *
 * Targets FogRenderer (net.minecraft.client.render.fog) in 1.21.11 Yarn mappings.
 * applyFog returns a Vector4f (RGBA) fog colour; we intercept it at RETURN
 * and multiply RGB channels to near-black.
 *
 * Fog start/end distances are clamped in FogDistanceMixin (AtmosphericFogModifier).
 */
@Environment(EnvType.CLIENT)
@Mixin(FogRenderer.class)
public class DarkVisionMixin {

    @Inject(
            method = "applyFog",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void darkenFogColor(
            Camera camera,
            int viewDistance,
            RenderTickCounter renderTickCounter,
            float partialTick,
            ClientWorld clientWorld,
            CallbackInfoReturnable<Vector4f> cir
    ) {
        if (!GameManager.getInstance().isGameRunning()) return;

        Vector4f original = cir.getReturnValue();
        if (original == null) return;

        // Darken to near-black with a faint blue tint for a horror atmosphere.
        // RGB multiplied by ~0.18; alpha (w) left unchanged.
        cir.setReturnValue(new Vector4f(
                original.x * 0.18f,
                original.y * 0.18f,
                original.z * 0.22f,
                original.w
        ));
    }
}