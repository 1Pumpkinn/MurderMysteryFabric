package net.saturn.murdermysteryfabric.event;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;
import net.saturn.murdermysteryfabric.game.GameManager;

public class ModEvents {
    
    /**
     * Registers all event listeners for the mod.
     * Should be called during mod initialization.
     */
    public static void register() {
        // Register chat message filter to prevent dead players from chatting
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
            try {
                GameManager gm = GameManager.getInstance();
                
                // Allow all messages if game is not running
                if (!gm.isGameRunning()) {
                    return true;
                }
                
                // Null check on getGameMode
                GameMode mode = sender.interactionManager.getGameMode();
                if (mode == null) {
                    // Treat as non-spectator (allow message)
                    return true;
                }
                
                // Check if sender is in spectator mode (dead player)
                if (mode == GameMode.SPECTATOR) {
                    // Try to send feedback to dead player via actionbar
                    try {
                        sender.sendMessage(
                            Text.literal("Dead players cannot chat.")
                                .formatted(Formatting.RED),
                            true // actionbar
                        );
                    } catch (Exception e) {
                        // Log error but still block the message
                        System.err.println("Failed to send actionbar message: " + e.getMessage());
                    }
                    return false; // Block message
                }
                
                return true; // Allow message
            } catch (Exception e) {
                // Prevent chat system from breaking - allow message on error
                System.err.println("Error in chat event handler: " + e.getMessage());
                return true;
            }
        });
    }
}
