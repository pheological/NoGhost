package pheological.noghost;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.util.ActionResult; // <-- CORRECTED IMPORT
import net.minecraft.util.Hand;
// import net.minecraft.util.TypedActionResult; // <-- REMOVED OLD IMPORT
import net.minecraft.world.World;

public class GhostItemHandler {

    private static boolean enabled = true;

    private static final int OFFHAND_INVENTORY_SLOT = 40;
    private static final int OFFHAND_PACKET_SLOT = 45;

    public static void initialize() {
        UseItemCallback.EVENT.register(GhostItemHandler::onUseItem);
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (!enabled || client.player == null) return;
            syncSlot(OFFHAND_PACKET_SLOT, client.player.getInventory().getStack(OFFHAND_INVENTORY_SLOT));
        });
    }

    // --- METHOD SIGNATURE AND RETURN STATEMENTS CORRECTED FOR 1.21.4 ---
    private static ActionResult onUseItem(PlayerEntity player, World world, Hand hand) {
        if (!enabled || !world.isClient()) {
            return ActionResult.PASS; // Return the simple ActionResult
        }

        ItemStack heldStack = player.getStackInHand(hand);
        boolean shouldSync = false;

        if (heldStack.isOf(Items.WATER_BUCKET) || heldStack.isOf(Items.LAVA_BUCKET)) {
            shouldSync = true;
        } else if (heldStack.isOf(Items.CROSSBOW) && CrossbowItem.isCharged(heldStack)) {
            shouldSync = true;
        }

        if (shouldSync && hand == Hand.MAIN_HAND) {
            int selectedSlot = player.getInventory().selectedSlot;
            int packetSlotId = 36 + selectedSlot;
            syncSlot(packetSlotId, heldStack);
        }

        // We are not changing the outcome, so we "pass" the event to let the game handle it.
        return ActionResult.PASS;
    }

    private static void syncSlot(int slotId, ItemStack stack) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.getNetworkHandler() != null) {
            client.getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(slotId, stack));
        }
    }

    public static void setEnabled(boolean state) {
        enabled = state;
    }

    public static boolean isEnabled() {
        return enabled;
    }
}