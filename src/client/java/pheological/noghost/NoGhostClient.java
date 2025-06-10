package pheological.noghost;

import net.fabricmc.api.ClientModInitializer;

/**
 * The main client initializer for the NoGhost mod.
 * Its only responsibility is to initialize the different feature handlers.
 */
public class NoGhostClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		// Initialize the event listeners for the item fix.
		GhostItemHandler.initialize();

		// Initialize and register the command structure.
		CommandRegistration.initialize();
	}
}