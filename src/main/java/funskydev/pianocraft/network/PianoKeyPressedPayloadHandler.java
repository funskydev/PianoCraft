package funskydev.pianocraft.network;

import funskydev.pianocraft.screen.PianoScreenHandler;
import funskydev.pianocraft.util.NoteUtil;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class PianoKeyPressedPayloadHandler implements ServerPlayNetworking.PlayPayloadHandler<PianoKeyPressedPayload> {

    @Override
    public void receive(PianoKeyPressedPayload payload, ServerPlayNetworking.Context context) {

        if (context.player() == null) return;

        ServerPlayer player = context.player();

        if (player.containerMenu instanceof PianoScreenHandler pianoScreenHandler) {

            BlockPos pianoPos = pianoScreenHandler.getPianoPos();

            if (pianoPos == null) return;

            float pitch = NoteUtil.getPitchFromNoteAndOctave(payload.note(), payload.octave());

            player.level().playSound(player, pianoPos, SoundEvents.NOTE_BLOCK_HARP.value(),
                    SoundSource.RECORDS, payload.volume(), pitch);

        }

    }
}
