package funskydev.pianocraft.client;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.client.midi.MidiInputReceiver;
import funskydev.pianocraft.registry.PCScreenHandlers;
import funskydev.pianocraft.client.screen.PianoScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;

public class PCMainClient implements ClientModInitializer {

    private static MidiDevice midiDevice;

    @Override
    public void onInitializeClient() {

        PCMain.LOGGER.info("Starting PianoCraft client");

        setCurrentMidiDeviceToDefaultIfNull();

        HandledScreens.register(PCScreenHandlers.PIANO_SCREEN_HANDLER, PianoScreen::new);

    }

    public static MidiDevice getCurrentMidiDevice() {
        return midiDevice;
    }

    public static String getCurrentMidiDeviceName() {
        return midiDevice == null ? "None" : midiDevice.getDeviceInfo().getName();
    }

    public static void selectNextMidiDevice() {

        List<MidiDevice> devices = getAvailableMidiDevices();
        if (devices.isEmpty()) {
            setCurrentMidiDevice(null);
            return;
        }

        int index = devices.indexOf(midiDevice);
        if (index == -1) {
            setCurrentMidiDevice(devices.get(0));
        } else {
            index++;
            if (index >= devices.size()) index = 0;
            setCurrentMidiDevice(devices.get(index));
        }

    }

    public static void ensureCurrentMidiDeviceIsAvailableOrSetToDefault() {

        List<MidiDevice> devices = getAvailableMidiDevices();

        //if (devices.isEmpty()) setCurrentMidiDevice(null);
        if (!devices.contains(midiDevice)) setCurrentMidiDevice(null);

        setCurrentMidiDeviceToDefaultIfNull();

    }

    private static void setCurrentMidiDeviceToDefaultIfNull() {

        if (midiDevice != null) return;

        List<MidiDevice> devices = getAvailableMidiDevices();
        if (!devices.isEmpty()) {
            setCurrentMidiDevice(devices.get(0));
        } else {
            setCurrentMidiDevice(null);
        }

    }

    private static void setCurrentMidiDevice(MidiDevice device) {

        closeCurrentDevice();

        if (device == null) {
            closeCurrentDevice();
            return;
        }

        try {
            device.open();
        } catch (MidiUnavailableException e) {
            PCMain.LOGGER.error("Failed to open MIDI Device : " + device.getDeviceInfo().getName());
            return;
        }

        Transmitter transmitter;

        try {
            transmitter = device.getTransmitter();
        } catch (MidiUnavailableException e) {
            device.close();
            PCMain.LOGGER.error("Failed to get MIDI Transmitter : " + device.getDeviceInfo().getName());
            return;
        }

        if (transmitter == null) {
            device.close();
            PCMain.LOGGER.error("MIDI Transmitter is null : " + device.getDeviceInfo().getName());
            return;
        }

        transmitter.setReceiver(new MidiInputReceiver());

        midiDevice = device;

        PCMain.LOGGER.info("MIDI Device set to : " + midiDevice.getDeviceInfo().getName());

    }

    private static void closeCurrentDevice() {

        if (midiDevice == null) return;

        PCMain.LOGGER.info("Closing MIDI Device : " + midiDevice.getDeviceInfo().getName());

        midiDevice.close();
        midiDevice = null;

    }

    private static List<MidiDevice> getAvailableMidiDevices() {

        List<MidiDevice> devices = new ArrayList<>();

        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos) {

            try {

                MidiDevice device = MidiSystem.getMidiDevice(info);

                // Check if the device is a real MIDI port
                if ((!(device instanceof Sequencer) && !(device instanceof Synthesizer))) {

                    // Check if the device has at least one transmitter
                    if (device.getMaxTransmitters() != 0) {

                        device.open();
                        device.close();

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
