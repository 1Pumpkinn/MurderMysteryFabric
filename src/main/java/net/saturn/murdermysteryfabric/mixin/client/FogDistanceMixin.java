package net.saturn.murdermysteryfabric.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.AtmosphericFogModifier;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.world.ClientWorld;
import net.saturn.murdermysteryfabric.game.GameManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Clamps fog distances during an active Murder Mystery game.
 *
 * Actual FogData fields (from decompiled class):
 *   public float environmentalStart
 *   public float renderDistanceStart
 *   public float environmentalEnd
 *   public float renderDistanceEnd
 *   public float skyEnd
 *   public float cloudEnd
 *
 * We clamp both the environmental and render-distance pairs so fog kicks in
 * at 8 blocks and is fully opaque by 28 blocks regardless of render distance.
 */
@Environment(EnvType.CLIENT)
@Mixin(AtmosphericFogModifier.class)
public class FogDistanceMixin {

    private static final float GAME_FOG_START = 8.0f;
    private static final float GAME_FOG_END   = 28.0f;

    @Inject(
            method = "applyStartEndModifier",
            at = @At("TAIL")
    )
    private void clampFogDistances(
            FogData fogData,
            Camera camera,
            ClientWorld clientWorld,
            float viewDistance,
            RenderTickCounter renderTickCounter,
            CallbackInfo ci
    ) {
        if (!GameManager.getInstance().isGameRunning()) return;

        // Clamp start distances — fog begins fading in at GAME_FOG_START blocks
        fogData.environmentalStart  = Math.min(fogData.environmentalStart,  GAME_FOG_START);
        fogData.renderDistanceStart = Math.min(fogData.renderDistanceStart,  GAME_FOG_START);

        // Clamp end distances — fog is fully opaque at GAME_FOG_END blocks
        fogData.environmentalEnd    = Math.min(fogData.environmentalEnd,    GAME_FOG_END);
        fogData.renderDistanceEnd   = Math.min(fogData.renderDistanceEnd,   GAME_FOG_END);

        // Also pull sky and cloud fog in so distant sky isn't visible
        fogData.skyEnd   = Math.min(fogData.skyEnd,   GAME_FOG_END);
        fogData.cloudEnd = Math.min(fogData.cloudEnd, GAME_FOG_END);
    }
}