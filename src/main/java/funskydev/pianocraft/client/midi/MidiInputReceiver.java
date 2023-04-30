package funskydev.pianocraft.client.midi;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.client.screen.PianoScreen;
import funskydev.pianocraft.screen.PianoScreenHandler;
import funskydev.pianocraft.util.NoteUtil;
import funskydev.pianocraft.util.NotesEnum;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundEvents;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.sql.Timestamp;

public class MidiInputReceiver implements Receiver {

    @Override
    public void send(MidiMessage message, long timeStamp) {

        if (message instanceof ShortMessage sm) {

            if (sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() != 0) {

                int noteId = sm.getData1();
                NotesEnum note = NoteUtil.getNoteFromId(noteId);
                int octave = NoteUtil.getOctaveFromId(noteId);

                float volume = sm.getData2() / 127.0f * 2;

                MinecraftClient client = MinecraftClient.getInstance();
                client.execute(() -> {

                    if (client.currentScreen instanceof PianoScreen pianoScreen) {
                        pianoScreen.playNote(note, octave, volume);
                    }

                });

            } else if (sm.getCommand() == ShortMessage.NOTE_OFF || (sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() == 0)) {

                // Note OFF

            }

        }

    }

    @Override
    public void close() {

    }

}
