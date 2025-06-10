package pheological.noghost;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource; // The correct CLIENT-SIDE import
import net.minecraft.command.CommandRegistryAccess; // The correct import
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

// NOTICE: There are NO imports from net.minecraft.server.* or for the generic CommandSource

public class CommandRegistration {

    public static void initialize() {
        ClientCommandRegistrationCallback.EVENT.register(CommandRegistration::registerCommands);
    }

    private static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(ClientCommandManager.literal("noghost")
                .then(ClientCommandManager.literal("item")
                        .then(ClientCommandManager.literal("enable").executes(context -> {
                            GhostItemHandler.setEnabled(true);
                            context.getSource().sendFeedback(Text.literal("NoGhost Item fix is now ").append(Text.literal("ENABLED").formatted(Formatting.GREEN)));
                            return 1;
                        }))
                        .then(ClientCommandManager.literal("disable").executes(context -> {
                            GhostItemHandler.setEnabled(false);
                            context.getSource().sendFeedback(Text.literal("NoGhost Item fix is now ").append(Text.literal("DISABLED").formatted(Formatting.RED)));
                            return 1;
                        }))
                        .executes(context -> {
                            boolean isEnabled = GhostItemHandler.isEnabled();
                            Text status = isEnabled
                                    ? Text.literal("ENABLED").formatted(Formatting.GREEN)
                                    : Text.literal("DISABLED").formatted(Formatting.RED);
                            context.getSource().sendFeedback(Text.literal("NoGhost Item fix is currently ").append(status));
                            return 1;
                        })
                )
                .then(ClientCommandManager.literal("block").executes(context -> {
                    GhostBlockHandler.requestBlockUpdates();
                    return 1;
                }))
        );
    }
}