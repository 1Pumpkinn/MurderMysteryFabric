package net.saturn.murdermysteryfabric.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.saturn.murdermysteryfabric.game.GameManager;
import net.saturn.murdermysteryfabric.game.GameRole;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ModCommands {

    /**
     * OP level check (level 2+)
     * - Allows console
     * - Allows ops
     */
    private static boolean isOp(ServerCommandSource src) {
        if (src.getEntity() instanceof ServerPlayerEntity player) {
            return src.getServer()
                    .getPlayerManager()
                    .isOperator(player.getPlayerConfigEntry());
        }
        return true;
    }

    public static void registerModCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(literal("mm")

                        // /mm start
                        .then(literal("start")
                                .requires(ModCommands::isOp)
                                .executes(ctx -> {
                                    ServerCommandSource src = ctx.getSource();
                                    GameManager gm = GameManager.getInstance();

                                    if (gm.isGameRunning()) {
                                        src.sendError(Text.literal("A game is already running!"));
                                        return 0;
                                    }

                                    if (gm.startGame(src.getServer())) {
                                        src.sendFeedback(() -> Text.literal("Game started!").formatted(Formatting.GREEN), true);
                                        return 1;
                                    }

                                    src.sendError(Text.literal("Need at least 3 players to start! (Use /mm debug to bypass)"));
                                    return 0;
                                }))

                        // /mm end
                        .then(literal("end")
                                .requires(ModCommands::isOp)
                                .executes(ctx -> {
                                    ServerCommandSource src = ctx.getSource();

                                    if (!GameManager.getInstance().isGameRunning()) {
                                        src.sendError(Text.literal("No game is currently running."));
                                        return 0;
                                    }

                                    GameManager.getInstance().endGame(src.getServer(), "Game force-ended by admin.");
                                    src.sendFeedback(() -> Text.literal("Game ended.").formatted(Formatting.YELLOW), true);
                                    return 1;
                                }))

                        // /mm status
                        .then(literal("status")
                                .requires(ModCommands::isOp)
                                .executes(ctx -> {
                                    ServerCommandSource src = ctx.getSource();
                                    GameManager gm = GameManager.getInstance();

                                    boolean running = gm.isGameRunning();

                                    src.sendFeedback(() -> Text.literal("Game running: " + running)
                                            .formatted(running ? Formatting.GREEN : Formatting.RED), false);

                                    if (running) {
                                        src.getServer().getPlayerManager().getPlayerList().forEach(p -> {
                                            GameRole role = gm.getRole(p);
                                            src.sendFeedback(() -> Text.literal("  " + p.getName().getString() + " -> " + role)
                                                    .formatted(Formatting.GRAY), false);
                                        });
                                    }

                                    return 1;
                                }))

                        // /mm role <player> <role>
                        .then(literal("role")
                                .requires(ModCommands::isOp)
                                .then(argument("player", EntityArgumentType.player())
                                        .then(argument("role", StringArgumentType.word())
                                                .suggests((ctx, builder) -> {
                                                    builder.suggest("MURDERER");
                                                    builder.suggest("DETECTIVE");
                                                    builder.suggest("INVESTIGATOR");
                                                    return builder.buildFuture();
                                                })
                                                .executes(ctx -> {
                                                    ServerCommandSource src = ctx.getSource();
                                                    ServerPlayerEntity target = EntityArgumentType.getPlayer(ctx, "player");
                                                    String roleStr = StringArgumentType.getString(ctx, "role").toUpperCase();

                                                    try {
                                                        GameRole role = GameRole.valueOf(roleStr);
                                                        GameManager.getInstance().setRoleWithItems(target, role);

                                                        src.sendFeedback(() ->
                                                                        Text.literal("Set " + target.getName().getString() + "'s role to " + role)
                                                                                .formatted(Formatting.GREEN),
                                                                true
                                                        );

                                                        target.sendMessage(
                                                                Text.literal("Your role has been set to: " + role)
                                                                        .formatted(Formatting.AQUA),
                                                                false
                                                        );

                                                    } catch (IllegalArgumentException e) {
                                                        src.sendError(Text.literal("Invalid role. Use MURDERER, DETECTIVE, or INVESTIGATOR."));
                                                        return 0;
                                                    }

                                                    return 1;
                                                }))))


                        // /mm debug
                        .then(literal("debug")
                                .requires(ModCommands::isOp)
                                .executes(ctx -> {
                                    ServerCommandSource src = ctx.getSource();
                                    GameManager gm = GameManager.getInstance();

                                    boolean newDebug = !gm.isDebugMode();
                                    gm.setDebugMode(newDebug);

                                    src.sendFeedback(() ->
                                                    Text.literal("Debug mode: " + (newDebug ? "ON" : "OFF"))
                                                            .formatted(newDebug ? Formatting.GREEN : Formatting.RED),
                                            true
                                    );

                                    return 1;
                                }))
                )
        );
    }
}