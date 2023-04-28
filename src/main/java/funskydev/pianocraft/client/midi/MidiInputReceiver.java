package funskydev.pianocraft.client.midi;

import funskydev.pianocraft.PCMain;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
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
                PCMain.LOGGER.info("NOTE ON message received: " + sm.getCommand() + " " + sm.getChannel() + " " + sm.getData1() + " " + sm.getData2());

                // Default Minecraft piano sound is tuned to F#4 (midi note 66)
                float pitch = (float) Math.pow(2.0, (sm.getData1() - 66) / 12.0);
                float volume = sm.getData2() / 127.0f * 2;
                //PCMain.LOGGER.info("Pitch: " + pitch + " (" + (float) (440 * Math.pow(2.0, (sm.getData1() - 69) / 12.0)) + " Hz)");
                // 1.0f is F#4 and 440 Hz should be A4
                //PCMain.LOGGER.info("Pitch in Hz: " + );
                //PCMain.LOGGER.info("Volume: " + volume);

                PCMain.LOGGER.info("Note played - Pitch : " + pitch + " (" + (float) (440 * Math.pow(2.0, (sm.getData1() - 69) / 12.0)) + " Hz)" + " - Volume : " + volume + " - Note timestamp : " + timeStamp/1000000 + " - Actual timestamp : " + new Timestamp(System.currentTimeMillis()));
                System.out.println("Note played - Pitch : " + pitch + " (" + (float) (440 * Math.pow(2.0, (sm.getData1() - 69) / 12.0)) + " Hz)" + " - Volume : " + volume + " - Note timestamp : " + timeStamp/1000000 + " - Actual timestamp : " + new Timestamp(System.currentTimeMillis()));

                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                if(player != null) MinecraftClient.getInstance().execute(() -> {
                    player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), volume, pitch);
                    PCMain.LOGGER.info("Sound played at " + new Timestamp(System.currentTimeMillis()));
                    System.out.println("Sound played at " + new Timestamp(System.currentTimeMillis()));
                });

            } else if (sm.getCommand() == ShortMessage.NOTE_OFF || (sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() == 0)) {

                //PCMain.LOGGER.info("NOTE OFF message received: " + sm.getCommand() + " " + sm.getChannel() + " " + sm.getData1() + " " + sm.getData2());

            }

        }

    }

    @Override
    public void close() {

    }

}
