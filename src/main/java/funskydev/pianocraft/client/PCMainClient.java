package funskydev.pianocraft.client;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.client.midi.exception.PianoCraftMIDIException;
import funskydev.pianocraft.client.midi.MidiDeviceUtil;
import funskydev.pianocraft.registry.PCScreenHandlers;
import funskydev.pianocraft.client.screen.PianoScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

import javax.sound.midi.*;
import java.util.List;

public class PCMainClient implements ClientModInitializer {

    private static MidiDevice currentMidiDevice;

    @Override
    public void onInitializeClient() {

        PCMain.LOGGER.info("Starting PianoCraft client");

        HandledScreens.register(PCScreenHandlers.PIANO_SCREEN_HANDLER, PianoScreen::new);

    }

    public static String getCurrentMidiDeviceName() {
        return currentMidiDevice == null ? null : currentMidiDevice.getDeviceInfo().getName();
    }

    /**
     * Select the next available MIDI device in the list of available devices
     * <p>If no device is available, the current device will be set to null</p>
     */
    public static void selectNextMidiDevice() {

        List<MidiDevice> devices = MidiDeviceUtil.getAvailableMidiDevices();
        if (devices.isEmpty()) {
            setCurrentMidiDevice(null);
            return;
        }

        int index = devices.indexOf(currentMidiDevice);
        if (index == -1) {
            setCurrentMidiDevice(devices.get(0));
        } else {
            index++;
            if (index >= devices.size()) index = 0;
            setCurrentMidiDevice(devices.get(index));
        }

    }

    /**
     * Search for a default MIDI device if none is selected
     */
    public static void searchForMidiDeviceIfNoneSelected() {

        if (currentMidiDevice != null) return;
        setCurrentMidiDevice(MidiDeviceUtil.getAvailableMidiDevices().stream().findFirst().orElse(null));
    }

    /**
     * If present, ensure the current MIDI device is available and ready
     *
     * <p>If the current device is not available anymore, it will be set to null</p>
     * <p>If the current device is not open, it will be opened</p>
     */
    public static void ensureCurrentMidiDeviceIsAvailableAndReady() {

        if (currentMidiDevice == null) return;

        if (!MidiDeviceUtil.getAvailableMidiDevices().contains(currentMidiDevice)) {
            // If the current device is not available anymore, set it to null
            setCurrentMidiDevice(null);
            return;
        }

        // If the current device is not open, open it
        if (currentMidiDevice.isOpen()) return;

        try {
            MidiDeviceUtil.openAndPrepareMidiDevice(currentMidiDevice);
        } catch (PianoCraftMIDIException e) {
            PCMain.LOGGER.error("Error preparing current device : " + e.getMessage());
            setCurrentMidiDevice(null);
        }
    }

    public static void closeCurrentMidiDevice() {
        MidiDeviceUtil.closeMidiDevice(currentMidiDevice);
        currentMidiDevice = null;
    }

    /**
     * <ul>
     *     <li>Close the current device if present</li>
     *     <li>Open the given device</li>
     *     <li>Set the current device to the given device</li>
     * </ul>
     * <p>If any error, the current device will be set to null</p>
     *
     * @param newMidiDevice The device to set as the current device
     */
    private static void setCurrentMidiDevice(MidiDevice newMidiDevice) {

        closeCurrentMidiDevice();

        // If no device to set, keep the current device as null
        if (newMidiDevice == null) return;

        try {
            MidiDeviceUtil.openAndPrepareMidiDevice(newMidiDevice);
        } catch (PianoCraftMIDIException e) {
            PCMain.LOGGER.error("Error setting new device : " + e.getMessage());
            return;
        }

        // If no error, set the new device as the current device
        currentMidiDevice = newMidiDevice;

        PCMain.LOGGER.info("MIDI Device set to : " + currentMidiDevice.getDeviceInfo().getName());

    }

}
