package net.saturn.murdermysteryfabric.event;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;
import net.saturn.murdermysteryfabric.game.GameManager;

public class ModEvents {

    public static void registerModEvents() {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
            GameManager gm = GameManager.getInstance();
            if (!gm.isGameRunning()) return true;

            GameMode mode = sender.interactionManager.getGameMode();
            if (mode == GameMode.SPECTATOR) {
                sender.sendMessage(Text.literal("Dead players cannot chat.").formatted(Formatting.RED), true);
                return false;
            }

            return true;
        });
    }
}