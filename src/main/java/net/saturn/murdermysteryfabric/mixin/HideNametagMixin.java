package net.saturn.murdermysteryfabric.mixin;

import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.PlayerLikeEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Client-side mixin that hides all player nametags.
 * In 1.21.11, hasLabel takes (PlayerLikeEntity, double).
 */
@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public class HideNametagMixin {

    @Inject(method = "hasLabel(Lnet/minecraft/entity/PlayerLikeEntity;D)Z", at = @At("HEAD"), cancellable = true)
    private void hideAllNametags(PlayerLikeEntity entity, double distance, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
