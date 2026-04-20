package net.saturn.murdermysteryfabric.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.saturn.murdermysteryfabric.game.GameManager;
import net.saturn.murdermysteryfabric.game.GameRole;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ModCommands {

    // OP level 2 = moderator/gamemaster
    private static boolean isOp(ServerCommandSource src) {
        return src.getPermissions().hasPermission(new Permission.Level(PermissionLevel.fromLevel(2)));
    }

    public static void registerModCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            dispatcher.register(literal("mm")

                // /mm start  (op level 2+)
                .then(literal("start")
                    .requires(ModCommands::isOp)
                    .executes(ctx -> {
                        ServerCommandSource src = ctx.getSource();
                        GameManager gm = GameManager.getInstance();
                        if (gm.isGameRunning()) {
                            src.sendError(Text.literal("A game is already running!"));
                            return 0;
                        }
                        boolean started = gm.startGame(src.getServer());
                        if (started) {
                            src.sendFeedback(() -> Text.literal("Game started!").formatted(Formatting.GREEN), true);
                        } else {
                            src.sendError(Text.literal("Need at least 3 players to start!"));
                        }
                        return 1;
                    }))

                // /mm end  (op level 2+)
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

                // /mm status  (op level 2+)
                .then(literal("status")
                    .requires(ModCommands::isOp)
                    .executes(ctx -> {
                        ServerCommandSource src = ctx.getSource();
                        boolean running = GameManager.getInstance().isGameRunning();
                        src.sendFeedback(() -> Text.literal("Game running: " + running)
                                .formatted(running ? Formatting.GREEN : Formatting.RED), false);
                        if (running) {
                            src.getServer().getPlayerManager().getPlayerList().forEach(p -> {
                                GameRole role = GameManager.getInstance().getRole(p);
                                src.sendFeedback(() -> Text.literal("  " + p.getName().getString() + " -> " + role)
                                        .formatted(Formatting.GRAY), false);
                            });
                        }
                        return 1;
                    }))

                // /mm role <player> <role>  (op level 2+)
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
                                    src.sendFeedback(() -> Text.literal(
                                            "Set " + target.getName().getString() + "'s role to " + role)
                                            .formatted(Formatting.GREEN), true);
                                    target.sendMessage(Text.literal("Your role has been set to: " + role)
                                            .formatted(Formatting.AQUA), false);
                                } catch (IllegalArgumentException e) {
                                    src.sendError(Text.literal("Invalid role. Use MURDERER, DETECTIVE, or INVESTIGATOR."));
                                }
                                return 1;
                            }))))

                // /mm debug  (op level 2+)
                .then(literal("debug")
                    .requires(ModCommands::isOp)
                    .executes(ctx -> {
                        ServerCommandSource src = ctx.getSource();
                        GameManager gm = GameManager.getInstance();
                        boolean newDebug = !gm.isDebugMode();
                        gm.setDebugMode(newDebug);
                        src.sendFeedback(() -> Text.literal("Debug mode: " + (newDebug ? "ON" : "OFF"))
                                .formatted(newDebug ? Formatting.GREEN : Formatting.RED), true);
                        if (newDebug) {
                            src.sendFeedback(() -> Text.literal("You can now start games with fewer than 3 players.")
                                    .formatted(Formatting.GRAY), false);
                        }
                        return 1;
                    }))
            )
        );
    }
}
