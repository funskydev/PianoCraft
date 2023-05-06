package funskydev.pianocraft;

import funskydev.pianocraft.network.KeyPressedChannelHandler;
import funskydev.pianocraft.registry.PCPackets;
import funskydev.pianocraft.registry.PCBlocks;
import funskydev.pianocraft.registry.PCItems;
import funskydev.pianocraft.registry.PCScreenHandlers;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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
        PCScreenHandlers.registerScreenHandlers();

        LOGGER.info("PianoCraft has been initialized");

        ServerPlayNetworking.registerGlobalReceiver(PCPackets.KEY_PRESSED_PACKET_ID, new KeyPressedChannelHandler());

    }

}