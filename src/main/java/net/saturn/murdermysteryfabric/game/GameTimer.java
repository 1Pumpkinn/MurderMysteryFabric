package net.saturn.murdermysteryfabric.game;

import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class GameTimer {

    private final ServerBossBar bossBar;
    private int remainingSeconds;
    private final int totalSeconds;

    public GameTimer(MinecraftServer server, int durationSeconds) {
        this.totalSeconds = durationSeconds;
        this.remainingSeconds = durationSeconds;

        this.bossBar = new ServerBossBar(
                Text.literal("Time Remaining: " + formatTime(remainingSeconds)),
                BossBar.Color.YELLOW,
                BossBar.Style.PROGRESS
        );

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            bossBar.addPlayer(player);
        }

        updateBossBar();
    }

    public boolean tick() {
        if (remainingSeconds <= 0) return true;
        remainingSeconds--;
        updateBossBar();
        return remainingSeconds <= 0;
    }

    public void addPlayer(ServerPlayerEntity player) {
        bossBar.addPlayer(player);
    }

    public void remove() {
        bossBar.clearPlayers();
    }

    private String formatTime(int seconds) {
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }

    private void updateBossBar() {
        bossBar.setName(Text.literal("Time Remaining: " + formatTime(remainingSeconds)));
        bossBar.setPercent(totalSeconds > 0 ? (float) remainingSeconds / totalSeconds : 0f);
        bossBar.setColor(remainingSeconds < 30 ? BossBar.Color.RED : BossBar.Color.YELLOW);
    }
}