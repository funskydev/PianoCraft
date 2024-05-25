package funskydev.pianocraft.network;

import funskydev.pianocraft.screen.PianoScreenHandler;
import funskydev.pianocraft.util.NoteUtil;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class PianoKeyPressedPayloadHandler implements ServerPlayNetworking.PlayPayloadHandler<PianoKeyPressedPayload> {

    @Override
    public void receive(PianoKeyPressedPayload payload, ServerPlayNetworking.Context context) {

        if (context.player() == null) return;

        ServerPlayerEntity player = context.player();

        if (player.currentScreenHandler instanceof PianoScreenHandler pianoScreenHandler) {

            BlockPos pianoPos = pianoScreenHandler.getPianoPos();

            if (pianoPos == null) return;

            float pitch = NoteUtil.getPitchFromNoteAndOctave(payload.note(), payload.octave());

            player.getServerWorld().playSound(player, player.getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_HARP.value(),
                    SoundCategory.RECORDS, payload.volume(), pitch);

        }

    }
}
