package net.saturn.murdermysteryfabric.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.saturn.murdermysteryfabric.game.GameManager;
import net.saturn.murdermysteryfabric.game.GameRole;
import net.saturn.murdermysteryfabric.sound.ModSounds;

public class KnifeItem extends Item {

    public KnifeItem(Settings settings) {
        super(settings);
    }

    /**
     * In 1.21.11 postHit returns void.
     * We use this to enforce 1-shot kills and suppress subtitles.
     */
    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof ServerPlayerEntity attackerPlayer)) return;
        if (!(target instanceof ServerPlayerEntity targetPlayer)) return;

        // getEntityWorld() on ServerPlayerEntity returns ServerWorld in 1.21.11
        World world = attacker.getEntityWorld();
        if (world.isClient()) return;

        GameManager gm = GameManager.getInstance();

        // Only the murderer can kill with the knife
        if (gm.getRole(attackerPlayer) != GameRole.MURDERER) {
            targetPlayer.setHealth(Math.min(targetPlayer.getHealth() + 20f, targetPlayer.getMaxHealth()));
            attackerPlayer.sendMessage(Text.literal("Only the Murderer can use the knife!"), true);
            return;
        }

        // 1-shot kill
        targetPlayer.setHealth(0f);

        // Play on MASTER category — subtitles only show for VOICE/AMBIENT/RECORD/WEATHER/BLOCK/HOSTILE/NEUTRAL/PLAYER/MUSIC
        // MASTER bypasses subtitle rendering entirely
        world.playSound(
                null,
                attacker.getX(), attacker.getY(), attacker.getZ(),
                ModSounds.KNIFE_STAB,
                SoundCategory.MASTER,
                1.0f, 1.0f
        );
    }

    @Override
    public boolean canMine(ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity miner) {
        return false;
    }
}
