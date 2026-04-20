package net.saturn.murdermysteryfabric.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import net.saturn.murdermysteryfabric.game.GameManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class PlayerDeathMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void clearInventoryOnDeath(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (GameManager.getInstance().isGameRunning()) {
            player.getInventory().clear();
        }
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void handleDeathOutcome(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        GameManager gm = GameManager.getInstance();

        if (gm.isGameRunning()) {
            try {
                player.changeGameMode(GameMode.SPECTATOR);
            } catch (Exception e) {
                System.err.println("[MurderMystery] Failed to set spectator for " + player.getName().getString() + ": " + e.getMessage());
            }
        }

        MinecraftServer srv = player.getEntityWorld().toServerWorld().getServer();
        if (srv != null) {
            gm.onPlayerDeath(player, srv);
        }
    }
}