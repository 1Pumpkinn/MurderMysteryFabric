package net.saturn.murdermysteryfabric.mixin;

import net.minecraft.entity.Entity;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class PlayerAttackMixin {

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void onAttack(Entity target, CallbackInfo ci) {
        ServerPlayerEntity attacker = (ServerPlayerEntity) (Object) this;
        if (!(target instanceof ServerPlayerEntity targetPlayer)) return;

        GameManager gm = GameManager.getInstance();
        if (!gm.isGameRunning()) return;

        GameRole attackerRole = gm.getRole(attacker);
        GameRole targetRole = gm.getRole(targetPlayer);

        // Spectator players (dead) cannot attack
        if (attacker.isSpectator()) {
            ci.cancel();
            return;
        }

        switch (attackerRole) {
            case INVESTIGATOR -> {
                attacker.sendMessage(Text.literal("Investigators cannot attack!").formatted(Formatting.RED), true);
                ci.cancel();
            }
            case MURDERER -> {
                // Allowed — knife's postHit handles kill logic
            }
            case DETECTIVE -> {
                if (targetRole == GameRole.INVESTIGATOR) {
                    penaliseDetective(attacker, targetPlayer);
                    ci.cancel();
                }
                // Attacking MURDERER is the correct play — allow it
            }
            default -> {
                // NONE role during a game shouldn't be able to attack
                ci.cancel();
            }
        }
    }

    private static void penaliseDetective(ServerPlayerEntity detective, ServerPlayerEntity innocent) {
        detective.sendMessage(
                Text.literal("You shot an innocent! You have been eliminated.").formatted(Formatting.DARK_RED), false);
        innocent.sendMessage(
                Text.literal("The Detective mistook you for the murderer!").formatted(Formatting.YELLOW), false);

        ItemStack held = detective.getMainHandStack();
        if (!held.isEmpty() && held.getItem() == ModItems.GUN) {
            detective.dropItem(held, false, false);
            detective.setStackInHand(detective.getActiveHand(), ItemStack.EMPTY);
        }

        detective.kill(detective.getEntityWorld().toServerWorld());
    }
}