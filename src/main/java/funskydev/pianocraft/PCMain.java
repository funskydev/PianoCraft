package funskydev.pianocraft;

import funskydev.pianocraft.registry.PCPayloads;
import funskydev.pianocraft.registry.PCBlocks;
import funskydev.pianocraft.registry.PCItems;
import funskydev.pianocraft.registry.PCScreenHandlers;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PCMain implements ModInitializer {

    public static final String MOD_ID = "pianocraft";
    public static final Logger LOGGER = LoggerFactory.getLogger("PianoCraft");

    @Override
    public void onInitialize() {

        LOGGER.debug("PianoCraft is initializing");

        PCBlocks.registerAllBlocks();
        PCItems.registerItems();
        PCScreenHandlers.registerScreenHandlers();
        PCPayloads.registerPayloads();

        LOGGER.debug("PianoCraft has been initialized");

    }

}