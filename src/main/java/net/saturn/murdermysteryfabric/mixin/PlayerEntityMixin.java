package net.saturn.murdermysteryfabric.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.saturn.murdermysteryfabric.game.GameManager;
import net.saturn.murdermysteryfabric.game.GameRole;
import net.saturn.murdermysteryfabric.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"), cancellable = true)
    private void preventDetectiveDropGun(ItemStack stack, boolean throwRandomly, CallbackInfoReturnable<net.minecraft.entity.ItemEntity> cir) {
        if (stack.getItem() != ModItems.GUN) return;
        if (!(((PlayerEntity)(Object)this) instanceof ServerPlayerEntity serverPlayer)) return;

        GameManager gm = GameManager.getInstance();
        if (gm.isGameRunning() && gm.getRole(serverPlayer) == GameRole.DETECTIVE) {
            serverPlayer.sendMessage(
                    Text.literal("You cannot drop the gun!").formatted(Formatting.RED), true);
            cir.setReturnValue(null);
        }
    }
}