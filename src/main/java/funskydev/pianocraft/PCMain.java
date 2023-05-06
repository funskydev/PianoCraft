package funskydev.pianocraft;

import funskydev.pianocraft.network.PCPackets;
import funskydev.pianocraft.registry.PCBlocks;
import funskydev.pianocraft.registry.PCItems;
import funskydev.pianocraft.registry.PCScreenHandlers;
import funskydev.pianocraft.screen.PianoScreenHandler;
import funskydev.pianocraft.util.NoteUtil;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
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

        ServerPlayNetworking.registerGlobalReceiver(PCPackets.KEY_PRESSED_PACKET_ID, (server, player, handler, buf, responseSender) -> {

            if (player.currentScreenHandler instanceof PianoScreenHandler pianoScreenHandler) {

                int note = buf.readInt();
                int octave = buf.readInt();
                float volume = buf.readFloat();

                BlockPos pianoPos = pianoScreenHandler.getPianoPos();

                if (pianoPos == null) return;

                float pitch = NoteUtil.getPitchFromNoteAndOctave(note, octave);

                player.world.playSound(player, player.getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_HARP.value(), SoundCategory.RECORDS, volume, pitch);

            }

        });

    }

}