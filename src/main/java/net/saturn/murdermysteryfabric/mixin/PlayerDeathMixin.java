package net.saturn.murdermysteryfabric.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.saturn.murdermysteryfabric.game.GameManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks into player death to trigger win condition checks.
 * In 1.21.11, ServerPlayerEntity has a public 'server' field (not getServer()).
 */
@Mixin(ServerPlayerEntity.class)
public class PlayerDeathMixin {

    // 'server' is a public field on ServerPlayerEntity in 1.21.11
    @Shadow public net.minecraft.server.MinecraftServer server;

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void onPlayerDeath(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (server != null) {
            GameManager.getInstance().onPlayerDeath(player, server);
        }
    }
}
