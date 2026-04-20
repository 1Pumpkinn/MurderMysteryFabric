package net.saturn.murdermysteryfabric.game;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.saturn.murdermysteryfabric.item.ModItems;

import java.util.*;

public class GameManager {

    private static GameManager instance;

    private boolean gameRunning = false;
    private boolean debugMode = false;
    private final Map<UUID, GameRole> playerRoles = new HashMap<>();
    private GameTimer gameTimer;
    private int tickCounter = 0;
    private boolean tickListenerRegistered = false;

    private GameManager() {}

    public static GameManager getInstance() {
        if (instance == null) instance = new GameManager();
        return instance;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debug) {
        this.debugMode = debug;
    }

    public GameRole getRole(UUID uuid) {
        return playerRoles.getOrDefault(uuid, GameRole.NONE);
    }

    public GameRole getRole(PlayerEntity player) {
        return getRole(player.getUuid());
    }

    public void setRole(UUID uuid, GameRole role) {
        playerRoles.put(uuid, role);
    }

    /**
     * Sets a player's role and gives them the appropriate items.
     * Clears their inventory first to remove old role items.
     */
    public void setRoleWithItems(ServerPlayerEntity player, GameRole role) {
        playerRoles.put(player.getUuid(), role);
        
        // Clear inventory to remove old role items
        player.getInventory().clear();
        
        // Give items based on new role
        switch (role) {
            case MURDERER:
                giveKnife(player);
                break;
            case DETECTIVE:
                giveGun(player);
                break;
            case INVESTIGATOR:
            case NONE:
                // No items for investigators or none
                break;
        }
    }

    /**
     * Starts the game, randomly assigns roles to all online players.
     * Requires at least 3 players (1 murderer, 1 detective, rest investigators).
     * In debug mode, can start with fewer players.
     */
    public boolean startGame(MinecraftServer server) {
        if (gameRunning) return false;

        List<ServerPlayerEntity> players = new ArrayList<>(server.getPlayerManager().getPlayerList());
        
        // Check minimum player count (skip in debug mode)
        if (!debugMode && players.size() < 3) return false;
        if (players.isEmpty()) return false;

        playerRoles.clear();
        Collections.shuffle(players);

        // In debug mode with 1 player, make them murderer
        if (players.size() == 1) {
            ServerPlayerEntity player = players.get(0);
            playerRoles.put(player.getUuid(), GameRole.MURDERER);
            giveKnife(player);
            player.sendMessage(Text.literal("You are the ").formatted(Formatting.RED)
                    .append(Text.literal("MURDERER").formatted(Formatting.DARK_RED, Formatting.BOLD))
                    .append(Text.literal("! [DEBUG MODE]").formatted(Formatting.YELLOW)), false);
        }
        // In debug mode with 2 players, make one murderer and one detective
        else if (players.size() == 2) {
            ServerPlayerEntity murderer = players.get(0);
            playerRoles.put(murderer.getUuid(), GameRole.MURDERER);
            giveKnife(murderer);
            murderer.sendMessage(Text.literal("You are the ").formatted(Formatting.RED)
                    .append(Text.literal("MURDERER").formatted(Formatting.DARK_RED, Formatting.BOLD))
                    .append(Text.literal("! [DEBUG MODE]").formatted(Formatting.YELLOW)), false);

            ServerPlayerEntity detective = players.get(1);
            playerRoles.put(detective.getUuid(), GameRole.DETECTIVE);
            giveGun(detective);
            detective.sendMessage(Text.literal("You are the ").formatted(Formatting.BLUE)
                    .append(Text.literal("DETECTIVE").formatted(Formatting.AQUA, Formatting.BOLD))
                    .append(Text.literal("! [DEBUG MODE]").formatted(Formatting.YELLOW)), false);
        }
        // Normal mode: 3+ players
        else {
            // Assign murderer
            ServerPlayerEntity murderer = players.get(0);
            playerRoles.put(murderer.getUuid(), GameRole.MURDERER);
            giveKnife(murderer);
            murderer.sendMessage(Text.literal("You are the ").formatted(Formatting.RED)
                    .append(Text.literal("MURDERER").formatted(Formatting.DARK_RED, Formatting.BOLD))
                    .append(Text.literal("! Eliminate everyone without being caught.").formatted(Formatting.RED)), false);

            // Assign detective
            ServerPlayerEntity detective = players.get(1);
            playerRoles.put(detective.getUuid(), GameRole.DETECTIVE);
            giveGun(detective);
            detective.sendMessage(Text.literal("You are the ").formatted(Formatting.BLUE)
                    .append(Text.literal("DETECTIVE").formatted(Formatting.AQUA, Formatting.BOLD))
                    .append(Text.literal("! Find and eliminate the murderer.").formatted(Formatting.BLUE)), false);

            // Assign investigators
            for (int i = 2; i < players.size(); i++) {
                ServerPlayerEntity inv = players.get(i);
                playerRoles.put(inv.getUuid(), GameRole.INVESTIGATOR);
                inv.sendMessage(Text.literal("You are an ").formatted(Formatting.GREEN)
                        .append(Text.literal("INVESTIGATOR").formatted(Formatting.DARK_GREEN, Formatting.BOLD))
                );
            }
        }

        gameRunning = true;

        // Create and start the game timer (300 seconds = 5 minutes)
        gameTimer = new GameTimer(server, 300);
        tickCounter = 0;
        
        // Register server tick listener to update timer (only once)
        if (!tickListenerRegistered) {
            ServerTickEvents.END_SERVER_TICK.register(this::onServerTick);
            tickListenerRegistered = true;
        }

        // Broadcast start
        String debugTag = debugMode ? " [DEBUG MODE]" : "";
        server.getPlayerManager().broadcast(
                Text.literal("=== Murder Mystery has started!" + debugTag + " ===").formatted(Formatting.GOLD, Formatting.BOLD), false);

        return true;
    }

    public void endGame(MinecraftServer server, String reason) {
        // Synchronization to prevent race conditions in timer expiration
        synchronized (this) {
            if (!gameRunning) return;
            
            // Set gameRunning to false immediately to prevent re-entry
            gameRunning = false;
            
            // Remove the game timer first (null-safe)
            if (gameTimer != null) {
                gameTimer.remove();
                gameTimer = null;
            }
        }
        
        // Restore all players to survival mode and clear inventories
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            try {
                player.changeGameMode(net.minecraft.world.GameMode.SURVIVAL);
            } catch (Exception e) {
                // Log error for specific player, continue with others
                System.err.println("Failed to restore game mode for " + player.getName().getString() + ": " + e.getMessage());
            }
            
            if (playerRoles.containsKey(player.getUuid())) {
                player.getInventory().clear();
            }
        }
        
        playerRoles.clear();

        server.getPlayerManager().broadcast(
                Text.literal("=== Murder Mystery ended! " + reason + " ===").formatted(Formatting.GOLD, Formatting.BOLD), false);
    }

    /** Called when a player is killed — checks win conditions. */
    public void onPlayerDeath(ServerPlayerEntity killed, MinecraftServer server) {
        if (!gameRunning) return;

        GameRole killedRole = getRole(killed.getUuid());

        // If the Murderer dies, Investigators and Detective win
        if (killedRole == GameRole.MURDERER) {
            endGame(server, "The Murderer was eliminated! Investigators and Detective win!");
            return;
        }

        // Check if all non-murderers are dead (Murderer wins)
        long aliveNonMurderers = server.getPlayerManager().getPlayerList().stream()
                .filter(p -> !p.getUuid().equals(killed.getUuid())) // Exclude the just-killed player
                .filter(p -> {
                    GameRole role = getRole(p);
                    return role == GameRole.DETECTIVE || role == GameRole.INVESTIGATOR;
                })
                .count();

        if (aliveNonMurderers == 0) {
            endGame(server, "The Murderer has eliminated everyone! Murderer wins!");
        }
    }

    /**
     * Called every server tick to update the game timer.
     * Ticks the timer every 20 ticks (once per second).
     */
    private void onServerTick(MinecraftServer server) {
        // State checks to prevent race conditions
        if (!gameRunning || gameTimer == null) {
            return;
        }
        
        tickCounter++;
        
        // Tick the timer every 20 ticks (once per second)
        if (tickCounter >= 20) {
            tickCounter = 0;
            
            // Check if timer has expired
            // Double-check gameRunning to prevent race condition where endGame was already called
            if (gameRunning && gameTimer != null && gameTimer.tick()) {
                endGame(server, "Time's up! The Murderer wins!");
            }
        }
    }

    private void giveKnife(ServerPlayerEntity player) {
        player.getInventory().clear();
        player.getInventory().insertStack(new ItemStack(ModItems.KNIFE));
    }

    private void giveGun(ServerPlayerEntity player) {
        player.getInventory().clear();
        player.getInventory().insertStack(new ItemStack(ModItems.GUN));
    }
}
