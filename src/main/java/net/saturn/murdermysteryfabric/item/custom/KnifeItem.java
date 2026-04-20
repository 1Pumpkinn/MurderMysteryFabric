package net.saturn.murdermysteryfabric.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.saturn.murdermysteryfabric.game.GameManager;
import net.saturn.murdermysteryfabric.game.GameRole;
import net.saturn.murdermysteryfabric.sound.ModSounds;

public class KnifeItem extends Item {

    public KnifeItem(Settings settings) {
        super(settings);
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof ServerPlayerEntity attackerPlayer)) return;
        if (!(attacker.getEntityWorld() instanceof ServerWorld serverWorld)) return;

        GameManager gm = GameManager.getInstance();
        if (!gm.isGameRunning()) return;

        if (gm.getRole(attackerPlayer) != GameRole.MURDERER) {
            if (target instanceof ServerPlayerEntity targetPlayer) {
                targetPlayer.setHealth(Math.min(targetPlayer.getHealth() + 20f, targetPlayer.getMaxHealth()));
            }
            attackerPlayer.sendMessage(
                    Text.literal("Only the Murderer can use the knife!").formatted(Formatting.RED), true);
            return;
        }

        serverWorld.playSound(null,
                attacker.getX(), attacker.getY(), attacker.getZ(),
                ModSounds.KNIFE_STAB,
                SoundCategory.MASTER,
                1.0f, 1.0f);

        if (target instanceof ServerPlayerEntity targetPlayer) {
            // Use proper damage source so the full death pipeline fires
            targetPlayer.damage(serverWorld,
                    serverWorld.getDamageSources().playerAttack(attackerPlayer),
                    Float.MAX_VALUE);
        } else {
            target.damage(serverWorld,
                    serverWorld.getDamageSources().playerAttack(attackerPlayer),
                    Float.MAX_VALUE);
        }
    }


    @Override
    public boolean canMine(ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity miner) {
        return false;
    }
}