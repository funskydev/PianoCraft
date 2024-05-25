package funskydev.pianocraft.network;

import funskydev.pianocraft.PCMain;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record PianoKeyPressedPayload(int note,
                                     int octave,
                                     float volume) implements CustomPayload {

    public static final CustomPayload.Id<PianoKeyPressedPayload> ID = new CustomPayload.Id<>(new Identifier(PCMain.MOD_ID, "piano_key_pressed"));

    public static final PacketCodec<RegistryByteBuf, PianoKeyPressedPayload> CODEC =
            CustomPayload.codecOf(PianoKeyPressedPayload::write, PianoKeyPressedPayload::new);

    PianoKeyPressedPayload(PacketByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readFloat());
    }

    private void write(RegistryByteBuf buf) {
        buf.writeInt(note);
        buf.writeInt(octave);
        buf.writeFloat(volume);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
