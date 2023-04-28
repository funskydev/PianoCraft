package funskydev.pianocraft;

import funskydev.pianocraft.registry.PCBlocks;
import funskydev.pianocraft.registry.PCItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PCMain implements ModInitializer {

    public static final String MOD_ID = "pianocraft";
    public static final Logger LOGGER = LoggerFactory.getLogger("PianoCraft");

    @Override
    public void onInitialize() {

        LOGGER.info("PianoCraft is initializing");

        PCBlocks.registerBlocks();
        PCItems.registerItems();

        LOGGER.info("PianoCraft has been initialized");

    }

}