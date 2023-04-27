package funskydev.pianocraft;

import funskydev.pianocraft.registry.PCBlocks;
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

        LOGGER.info("PianoCraft has been initialized");

    }

}