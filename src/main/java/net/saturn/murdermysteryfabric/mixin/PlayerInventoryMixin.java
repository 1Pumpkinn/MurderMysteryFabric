package net.saturn.murdermysteryfabric.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.saturn.murdermysteryfabric.game.GameManager;
import net.saturn.murdermysteryfabric.game.GameRole;
import net.saturn.murdermysteryfabric.item.ModItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Prevents the Murderer from picking up the gun.
 * Makes anyone else who picks up the gun become the new Detective.
 */
@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Shadow @Final public PlayerEntity player;

    @Inject(method = "insertStack(Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void onPickupGun(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() == ModItems.GUN && player instanceof ServerPlayerEntity serverPlayer) {
            GameManager gm = GameManager.getInstance();
            if (!gm.isGameRunning()) return;
            
            GameRole currentRole = gm.getRole(serverPlayer);
            
            // Murderer cannot pick up the gun
            if (currentRole == GameRole.MURDERER) {
                serverPlayer.sendMessage(Text.literal("The Murderer cannot pick up the gun!").formatted(Formatting.RED), true);
                cir.setReturnValue(false);
                return;
            }
            
            // If an Investigator picks up the gun, they become the new Detective
            if (currentRole == GameRole.INVESTIGATOR) {
                gm.setRole(serverPlayer.getUuid(), GameRole.DETECTIVE);
                serverPlayer.sendMessage(Text.literal("You picked up the gun! You are now the ").formatted(Formatting.YELLOW)
                        .append(Text.literal("DETECTIVE").formatted(Formatting.AQUA, Formatting.BOLD))
                        .append(Text.literal("!").formatted(Formatting.YELLOW)), false);
                
                // Broadcast to all players - getEntityWorld() returns ServerWorld for ServerPlayerEntity
                serverPlayer.getEntityWorld().getServer().getPlayerManager().broadcast(
                        Text.literal(serverPlayer.getName().getString() + " is now the Detective!").formatted(Formatting.AQUA, Formatting.BOLD), false);
            }
        }
    }
}

