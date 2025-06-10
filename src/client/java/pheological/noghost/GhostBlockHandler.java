package pheological.noghost;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class GhostBlockHandler {

    public static void requestBlockUpdates() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayNetworkHandler connection = client.getNetworkHandler();
        if (connection == null || client.player == null) {
            return;
        }

        BlockPos playerPos = client.player.getBlockPos();
        for (int dx = -4; dx <= 4; dx++) {
            for (int dy = -4; dy <= 4; dy++) {
                for (int dz = -4; dz <= 4; dz++) {
                    BlockPos targetPos = playerPos.add(dx, dy, dz);
                    PlayerActionC2SPacket packet = new PlayerActionC2SPacket(
                            PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK,
                            targetPos,
                            Direction.UP
                    );
                    connection.sendPacket(packet);
                }
            }
        }
        // Use the new translation key for feedback
        client.player.sendMessage(Text.translatable("msg.noghost.request"), false);
    }
}