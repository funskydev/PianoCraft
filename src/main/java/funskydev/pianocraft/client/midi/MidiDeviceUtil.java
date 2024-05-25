package funskydev.pianocraft.client.midi;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.client.midi.exception.PianoCraftMIDIException;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;

public class MidiDeviceUtil {


    public static void openAndPrepareMidiDevice(MidiDevice device) throws PianoCraftMIDIException {

        if (device == null) {
            throw new PianoCraftMIDIException(PianoCraftMIDIException.Cause.MIDI_DEVICE_NULL.getMessage());
        }

        try {
            device.open();
        } catch (MidiUnavailableException e) {
            throw new PianoCraftMIDIException(PianoCraftMIDIException.Cause.MIDI_DEVICE_OPEN_FAILED.getMessage().formatted(device.getDeviceInfo().getName()));
        }

        Transmitter transmitter;

        try {
            transmitter = device.getTransmitter();
        } catch (MidiUnavailableException e) {
            device.close();
            throw new PianoCraftMIDIException(PianoCraftMIDIException.Cause.MIDI_TRANSMITTER_GET_FAILED.getMessage().formatted(device.getDeviceInfo().getName()));
        }

        if (transmitter == null) {
            device.close();
            throw new PianoCraftMIDIException(PianoCraftMIDIException.Cause.MIDI_TRANSMITTER_NULL.getMessage().formatted(device.getDeviceInfo().getName()));
        }

        transmitter.setReceiver(new MidiInputReceiver());

        PCMain.LOGGER.debug("MIDI Device opened and ready : " + device.getDeviceInfo().getName());

    }

    public static void closeMidiDevice(MidiDevice device) {
        if (device != null) {
            device.close();
            PCMain.LOGGER.debug("MIDI Device closed : " + device.getDeviceInfo().getName());
        }
    }

    /**
     * Get a list of all available MIDI devices that can be opened and used as input
     *
     * @return A list of all available MIDI devices
     */
    public static List<MidiDevice> getAvailableMidiDevices() {

        List<MidiDevice> devices = new ArrayList<>();

        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos) {

            try {

                MidiDevice device = MidiSystem.getMidiDevice(info);

                // Check if the device is a real MIDI port
                if ((!(device instanceof Sequencer) && !(device instanceof Synthesizer))) {

                    // Check if the device has at least one transmitter
                    if (device.getMaxTransmitters() != 0) {

                        // Test if the device isn't already opened
                        if (!device.isOpen()) {
                            device.open();
                            device.close();
                        }

                        devices.add(device);
                    }

                }

            } catch (MidiUnavailableException e) {
                PCMain.LOGGER.warn("Skipping an unavailable MIDI device : " + info.getName());
            }

        }

        return devices;

    }

}
