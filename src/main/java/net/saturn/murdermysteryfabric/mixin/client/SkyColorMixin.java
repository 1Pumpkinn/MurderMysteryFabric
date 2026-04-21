package net.saturn.murdermysteryfabric.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.saturn.murdermysteryfabric.game.GameManager;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class SkyColorMixin {

    @Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
    private void cancelSkyRender(CallbackInfo ci) {
        if (!GameManager.getInstance().isGameRunning()) return;
        ci.cancel();
    }
}