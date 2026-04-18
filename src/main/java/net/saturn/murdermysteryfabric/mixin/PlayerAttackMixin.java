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

/**
 * Enforces Murder Mystery combat rules:
 * 1. Investigators cannot attack anyone.
 * 2. Murderer can attack anyone.
 * 3. Detective can attack anyone, but shooting an Investigator → detective drops gun and dies.
 */
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

        // Investigators are civilians — they cannot attack
        if (attackerRole == GameRole.INVESTIGATOR) {
            attacker.sendMessage(Text.literal("Investigators cannot attack!").formatted(Formatting.RED), true);
            ci.cancel();
            return;
        }

        // Murderer can attack anyone - allow the attack to proceed
        if (attackerRole == GameRole.MURDERER) {
            // Don't cancel - let the knife's postHit handle the kill
            return;
        }

        // Detective shoots an Investigator (wrong target) → detective dies, drops gun
        if (attackerRole == GameRole.DETECTIVE && targetRole == GameRole.INVESTIGATOR) {
            attacker.sendMessage(
                    Text.literal("You shot an innocent Investigator! You have been eliminated.").formatted(Formatting.DARK_RED), false);
            targetPlayer.sendMessage(
                    Text.literal("The Detective mistook you for the murderer!").formatted(Formatting.YELLOW), false);

            // Drop the gun from main hand
            ItemStack held = attacker.getMainHandStack();
            if (!held.isEmpty() && held.getItem() == ModItems.GUN) {
                attacker.dropItem(held, false, false);
                attacker.setStackInHand(attacker.getActiveHand(), ItemStack.EMPTY);
            }

            // Kill the detective — getEntityWorld() returns ServerWorld on ServerPlayerEntity
            attacker.kill(attacker.getEntityWorld());

            ci.cancel();
            return;
        }

        // Detective attacking Murderer - allow the attack to proceed
        // (This is the correct action)
    }
}
