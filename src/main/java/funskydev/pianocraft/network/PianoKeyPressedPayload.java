package funskydev.pianocraft.network;

import funskydev.pianocraft.PCMain;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record PianoKeyPressedPayload(int note,
                                     int octave,
                                     float volume) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PianoKeyPressedPayload> ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(PCMain.MOD_ID, "piano_key_pressed"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PianoKeyPressedPayload> CODEC =
            CustomPacketPayload.codec(PianoKeyPressedPayload::write, PianoKeyPressedPayload::new);

    PianoKeyPressedPayload(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readFloat());
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(note);
        buf.writeInt(octave);
        buf.writeFloat(volume);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
