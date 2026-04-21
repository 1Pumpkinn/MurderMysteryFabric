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

@Environment(EnvType.CLIENT)
@Mixin(AtmosphericFogModifier.class)
public class FogDistanceMixin {

    private static final float GAME_FOG_START = 2.0f;
    private static final float GAME_FOG_END   = 14.0f;

    @Inject(method = "applyStartEndModifier", at = @At("TAIL"))
    private void clampFogDistances(FogData fogData, Camera camera,
                                   ClientWorld clientWorld, float viewDistance,
                                   RenderTickCounter renderTickCounter, CallbackInfo ci) {
        if (!GameManager.getInstance().isGameRunning()) return;
        fogData.environmentalStart  = Math.min(fogData.environmentalStart,  GAME_FOG_START);
        fogData.renderDistanceStart = Math.min(fogData.renderDistanceStart,  GAME_FOG_START);
        fogData.environmentalEnd    = Math.min(fogData.environmentalEnd,    GAME_FOG_END);
        fogData.renderDistanceEnd   = Math.min(fogData.renderDistanceEnd,   GAME_FOG_END);
        fogData.skyEnd              = Math.min(fogData.skyEnd,              GAME_FOG_END);
        fogData.cloudEnd            = Math.min(fogData.cloudEnd,            GAME_FOG_END);
    }
}