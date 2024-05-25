package funskydev.pianocraft.client.midi.exception;

public class PianoCraftMIDIException extends Exception {

    public PianoCraftMIDIException(String message) {
        super(message);
    }

    public enum Cause {
        MIDI_DEVICE_NULL("MIDI Device is null"),
        MIDI_DEVICE_OPEN_FAILED("Failed to open MIDI Device : %s"),
        MIDI_TRANSMITTER_GET_FAILED("Failed to get MIDI Transmitter : %s"),
        MIDI_TRANSMITTER_NULL("MIDI Transmitter is null : %s");

        private final String message;

        Cause (String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

}
