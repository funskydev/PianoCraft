package funskydev.pianocraft.client;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.client.midi.MidiInputReceiver;
import net.fabricmc.api.ClientModInitializer;

import javax.sound.midi.*;

public class PCMainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        //MidiDevice keyboard =

        PCMain.LOGGER.info("Starting PianoCraft client");
        try {

            MidiDevice device = getKeyboardDevice();
            device.open();

            Receiver receiver = new MidiInputReceiver();
            Transmitter transmitter = device.getTransmitter();
            transmitter.setReceiver(receiver);

            PCMain.LOGGER.info("MIDI receiver set / Device opened : " + device.isOpen());

        } catch (MidiUnavailableException e) {
            PCMain.LOGGER.error(e.getMessage());
        }

    }

    private static MidiDevice getKeyboardDevice() throws MidiUnavailableException {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos) {
            MidiDevice device = MidiSystem.getMidiDevice(info);

            // Check if the device is a real MIDI port
            if ((!(device instanceof Sequencer) && !(device instanceof Synthesizer))) {

                // Check if the device has at least one transmitter
                if (device.getMaxTransmitters() != 0) {

                    PCMain.LOGGER.info("--------------------");
                    PCMain.LOGGER.info("Found MIDI compatible device: " + info.getName());
                    PCMain.LOGGER.info("Max transmitters: " + device.getMaxTransmitters());
                    PCMain.LOGGER.info("Max receivers: " + device.getMaxReceivers());
                    PCMain.LOGGER.info("Opened: " + device.isOpen());
                    PCMain.LOGGER.info("Object: " + device.toString());
                    PCMain.LOGGER.info("--------------------");

                    return device;

                }

            }


        }
        throw new MidiUnavailableException("No MIDI input devices found");
    }

}
