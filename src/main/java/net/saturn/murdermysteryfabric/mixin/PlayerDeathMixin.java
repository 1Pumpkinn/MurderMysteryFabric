package net.saturn.murdermysteryfabric.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
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

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onPlayerDeath(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        GameManager gm = GameManager.getInstance();
        
        // If game is running, clear inventory BEFORE death processing to prevent drops
        if (gm.isGameRunning()) {
            player.getInventory().clear();
        }
    }
    
    @Inject(method = "onDeath", at = @At("TAIL"))
    private void onPlayerDeathTail(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        GameManager gm = GameManager.getInstance();
        
        // If game is running, switch to spectator mode after death processing
        if (gm.isGameRunning()) {
            // Try-catch for changeGameMode failures
            try {
                player.changeGameMode(GameMode.SPECTATOR);
            } catch (Exception e) {
                // Log warning but continue with game logic
                System.err.println("Failed to change game mode for " + player.getName().getString() + ": " + e.getMessage());
            }
        }
        
        if (server != null) {
            gm.onPlayerDeath(player, server);
        }
    }
}
