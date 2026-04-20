package net.saturn.murdermysteryfabric.game;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.saturn.murdermysteryfabric.item.ModItems;
import net.minecraft.text.MutableText;

import java.util.*;

public class GameManager {

    private static final GameManager INSTANCE = new GameManager();

    private volatile boolean gameRunning = false;
    private boolean debugMode = false;
    private final Map<UUID, GameRole> playerRoles = new HashMap<>();
    private GameTimer gameTimer;
    private int tickCounter = 0;
    private boolean tickListenerRegistered = false;

    private GameManager() {}

    public static GameManager getInstance() {
        return INSTANCE;
    }

    public boolean isGameRunning() { return gameRunning; }
    public boolean isDebugMode() { return debugMode; }
    public void setDebugMode(boolean debug) { this.debugMode = debug; }

    public GameRole getRole(UUID uuid) {
        return playerRoles.getOrDefault(uuid, GameRole.NONE);
    }

    public GameRole getRole(PlayerEntity player) {
        return getRole(player.getUuid());
    }

    public void setRole(UUID uuid, GameRole role) {
        playerRoles.put(uuid, role);
    }

    public void setRoleWithItems(ServerPlayerEntity player, GameRole role) {
        playerRoles.put(player.getUuid(), role);
        player.getInventory().clear();
        switch (role) {
            case MURDERER -> giveKnife(player);
            case DETECTIVE -> giveGun(player);
            default -> {}
        }
    }

    public boolean startGame(MinecraftServer server) {
        if (gameRunning) return false;

        List<ServerPlayerEntity> players = new ArrayList<>(server.getPlayerManager().getPlayerList());
        if (players.isEmpty()) return false;
        if (!debugMode && players.size() < 3) return false;

        playerRoles.clear();
        Collections.shuffle(players);
        assignRoles(players);

        gameRunning = true;
        gameTimer = new GameTimer(server, 300);
        tickCounter = 0;

        if (!tickListenerRegistered) {
            ServerTickEvents.END_SERVER_TICK.register(this::onServerTick);
            tickListenerRegistered = true;
        }

        String suffix = debugMode ? " §e[DEBUG MODE]" : "";
        server.getPlayerManager().broadcast(
                Text.literal("=== Murder Mystery has started!" + suffix + " ===").formatted(Formatting.GOLD, Formatting.BOLD), false);

        return true;
    }

    private void assignRoles(List<ServerPlayerEntity> players) {
        if (players.size() == 1) {
            assign(players.get(0), GameRole.MURDERER, true);
        } else if (players.size() == 2) {
            assign(players.get(0), GameRole.MURDERER, true);
            assign(players.get(1), GameRole.DETECTIVE, true);
        } else {
            assign(players.get(0), GameRole.MURDERER, false);
            assign(players.get(1), GameRole.DETECTIVE, false);
            for (int i = 2; i < players.size(); i++) {
                assign(players.get(i), GameRole.INVESTIGATOR, false);
            }
        }
    }

    private void assign(ServerPlayerEntity player, GameRole role, boolean debug) {
        playerRoles.put(player.getUuid(), role);
        String debugTag = debug ? " §e[DEBUG MODE]" : "";
        switch (role) {
            case MURDERER -> {
                giveKnife(player);
                player.sendMessage(roleMessage("You are the ", "MURDERER",
                        debug ? "!" : "! Eliminate everyone without being caught.",
                        Formatting.RED, Formatting.DARK_RED).append(Text.literal(debugTag)), false);
            }
            case DETECTIVE -> {
                giveGun(player);
                player.sendMessage(roleMessage("You are the ", "DETECTIVE",
                        debug ? "!" : "! Find and eliminate the murderer.",
                        Formatting.BLUE, Formatting.AQUA).append(Text.literal(debugTag)), false);
            }
            case INVESTIGATOR -> {
                player.sendMessage(roleMessage("You are an ", "INVESTIGATOR", "! Collect evidence and survive.",
                        Formatting.GREEN, Formatting.DARK_GREEN), false);
            }
            default -> {}
        }
    }

    private static MutableText roleMessage(String prefix, String role, String suffix, Formatting prefixColor, Formatting roleColor) {
        return Text.literal(prefix).formatted(prefixColor)
                .append(Text.literal(role).formatted(roleColor, Formatting.BOLD))
                .append(Text.literal(suffix).formatted(prefixColor));
    }

    public synchronized void endGame(MinecraftServer server, String reason) {
        if (!gameRunning) return;
        gameRunning = false;

        if (gameTimer != null) {
            gameTimer.remove();
            gameTimer = null;
        }

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            try {
                player.changeGameMode(net.minecraft.world.GameMode.SURVIVAL);
            } catch (Exception e) {
                System.err.println("[MurderMystery] Failed to restore game mode for " + player.getName().getString() + ": " + e.getMessage());
            }
            if (playerRoles.containsKey(player.getUuid())) {
                player.getInventory().clear();
            }
        }

        playerRoles.clear();
        server.getPlayerManager().broadcast(
                Text.literal("=== Murder Mystery ended! " + reason + " ===").formatted(Formatting.GOLD, Formatting.BOLD), false);
    }

    public void onPlayerDeath(ServerPlayerEntity killed, MinecraftServer server) {
        if (!gameRunning) return;

        GameRole killedRole = getRole(killed.getUuid());

        if (killedRole == GameRole.MURDERER) {
            endGame(server, "The Murderer was eliminated! Investigators and Detective win!");
            return;
        }

        // Check the roles map directly rather than iterating getPlayerList(),
        // which may still include the just-killed player as a spectator.
        long aliveNonMurderers = server.getPlayerManager().getPlayerList().stream()
                .filter(p -> !p.getUuid().equals(killed.getUuid()))
                .filter(p -> {
                    GameRole role = getRole(p);
                    return role == GameRole.DETECTIVE || role == GameRole.INVESTIGATOR;
                })
                .count();

        if (aliveNonMurderers == 0) {
            endGame(server, "The Murderer has eliminated everyone! Murderer wins!");
        }
    }

    private void onServerTick(MinecraftServer server) {
        if (!gameRunning || gameTimer == null) return;

        if (++tickCounter >= 20) {
            tickCounter = 0;
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