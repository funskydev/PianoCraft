package funskydev.pianocraft.network;

import funskydev.pianocraft.screen.PianoScreenHandler;
import funskydev.pianocraft.util.NoteUtil;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class KeyPressedChannelHandler implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        if (player.currentScreenHandler instanceof PianoScreenHandler pianoScreenHandler) {

            int note = buf.readInt();
            int octave = buf.readInt();
            float volume = buf.readFloat();

            BlockPos pianoPos = pianoScreenHandler.getPianoPos();

            if (pianoPos == null) return;

            float pitch = NoteUtil.getPitchFromNoteAndOctave(note, octave);

            player.world.playSound(player, player.getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_HARP.value(), SoundCategory.RECORDS, volume, pitch);

        }

    }

}
