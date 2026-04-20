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

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Shadow @Final public PlayerEntity player;

    @Inject(method = "insertStack(Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void onPickupGun(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() != ModItems.GUN) return;
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;

        GameManager gm = GameManager.getInstance();
        if (!gm.isGameRunning()) return;

        GameRole role = gm.getRole(serverPlayer);

        if (role == GameRole.MURDERER) {
            serverPlayer.sendMessage(Text.literal("The Murderer cannot pick up the gun!").formatted(Formatting.RED), true);
            cir.setReturnValue(false);
            return;
        }

        if (role == GameRole.INVESTIGATOR) {
            gm.setRole(serverPlayer.getUuid(), GameRole.DETECTIVE);
            serverPlayer.sendMessage(
                    Text.literal("You picked up the gun! You are now the ")
                            .formatted(Formatting.YELLOW)
                            .append(Text.literal("DETECTIVE").formatted(Formatting.AQUA, Formatting.BOLD))
                            .append(Text.literal("!").formatted(Formatting.YELLOW)),
                    false);
            serverPlayer.getEntityWorld().toServerWorld().getServer().getPlayerManager().broadcast(
                    Text.literal(serverPlayer.getName().getString() + " is now the Detective!")
                            .formatted(Formatting.AQUA, Formatting.BOLD),
                    false);
        }
        // DETECTIVE picking up a second gun is fine — just let it happen
    }
}