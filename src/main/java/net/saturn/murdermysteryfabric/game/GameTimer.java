package net.saturn.murdermysteryfabric.game;

import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.saturn.murdermysteryfabric.Murdermysteryfabric;

/**
 * Manages the game countdown timer and displays it via a boss bar.
 * The timer counts down from a specified duration and notifies when time expires.
 */
public class GameTimer {
    
    private final MinecraftServer server;
    private final ServerBossBar bossBar;
    private int remainingSeconds;
    private final int totalSeconds;
    
    /**
     * Creates a new game timer with the specified duration.
     * 
     * @param server The Minecraft server instance
     * @param durationSeconds The duration of the timer in seconds
     * @throws IllegalArgumentException if server is null
     */
    public GameTimer(MinecraftServer server, int durationSeconds) {
        if (server == null) {
            throw new IllegalArgumentException("Server cannot be null");
        }
        
        this.server = server;
        this.totalSeconds = durationSeconds;
        this.remainingSeconds = durationSeconds;
        
        // Create boss bar directly (not through BossBarManager)
        Identifier barId = Identifier.of(Murdermysteryfabric.MODID, "game_timer");
        this.bossBar = new ServerBossBar(Text.literal("Time Remaining: " + formatTime(remainingSeconds)), BossBar.Color.YELLOW, BossBar.Style.PROGRESS);
        
        // Add all online players to the boss bar
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            this.bossBar.addPlayer(player);
        }
        
        // Initial update
        updateBossBar();
    }

    /**
     * Ticks the timer, decrementing the counter and updating the boss bar.
     * 
     * @return true if the timer has expired (reached zero), false otherwise
     */
    public boolean tick() {
        // Null check to prevent NPE after removal
        if (bossBar == null) {
            return false; // Safety check if timer was already removed
        }
        
        if (remainingSeconds > 0) {
            remainingSeconds--;
            updateBossBar();
            return false;
        }
        
        return true; // Time expired
    }
    
    /**
     * Adds a player to the boss bar display.
     * 
     * @param player The player to add
     */
    public void addPlayer(ServerPlayerEntity player) {
        if (bossBar != null && player != null) {
            bossBar.addPlayer(player);
        }
    }
    
    /**
     * Removes the boss bar from all players and cleans up resources.
     */
    public void remove() {
        if (bossBar != null) {
            bossBar.clearPlayers();
            // ServerBossBar doesn't need to be removed from BossBarManager
            // since we created it directly without registering it
        }
    }
    
    /**
     * Formats seconds as M:SS format.
     * 
     * @param seconds The number of seconds to format
     * @return Formatted time string (e.g., "5:00", "1:05")
     */
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%d:%02d", minutes, secs);
    }
    
    /**
     * Updates the boss bar text and progress percentage.
     * Also updates the color based on remaining time.
     */
    private void updateBossBar() {
        if (bossBar == null) {
            return;
        }
        
        // Update text
        bossBar.setName(Text.literal("Time Remaining: " + formatTime(remainingSeconds)));
        
        // Update progress percentage
        float percent = totalSeconds > 0 ? (float) remainingSeconds / totalSeconds : 0.0f;
        bossBar.setPercent(percent);
        
        // Update color based on remaining time
        if (remainingSeconds < 30) {
            bossBar.setColor(BossBar.Color.RED);
        } else {
            bossBar.setColor(BossBar.Color.YELLOW);
        }
    }
}
