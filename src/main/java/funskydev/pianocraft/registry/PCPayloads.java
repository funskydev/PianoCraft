package funskydev.pianocraft.registry;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.network.PianoKeyPressedPayloadHandler;
import funskydev.pianocraft.network.PianoKeyPressedPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class PCPayloads {

    public static final CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, PianoKeyPressedPayload> PIANO_KEY_PRESSED_PAYLOAD = PayloadTypeRegistry.playC2S().register(PianoKeyPressedPayload.ID, PianoKeyPressedPayload.CODEC);

    public static void registerPayloads() {

        ServerPlayNetworking.registerGlobalReceiver(PianoKeyPressedPayload.ID, new PianoKeyPressedPayloadHandler());

        PCMain.LOGGER.debug("Payload registered");
    }

}
