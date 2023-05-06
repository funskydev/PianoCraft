package funskydev.pianocraft.client.screen.widgets;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.registry.PCPackets;
import funskydev.pianocraft.util.NotesEnum;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class PianoKeyWidget extends PressableWidget {

    private NotesEnum note;
    private int octave;

    public PianoKeyWidget(int x, int y, NotesEnum note, int octave) {

        super(x, y, note.isSharp() ? 30 : 40, note.isSharp() ? 10 : 15, Text.of(note.getNoteName()));

        this.note = note;
        this.octave = octave;

    }

    @Override
    public void onPress() {
        PCMain.LOGGER.info("Key pressed : " + this.note.getNoteName() + " (octave " + this.octave + ")");

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(this.note.ordinal());
        buf.writeInt(this.octave);

        ClientPlayNetworking.send(PCPackets.KEY_PRESSED_PACKET_ID, buf);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

}
