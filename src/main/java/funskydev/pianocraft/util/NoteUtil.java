package funskydev.pianocraft.util;

public class NoteUtil {

    public static float getPitchFromNoteAndOctave(int note, int octave) {
        return (float) (Math.pow(2.0D, (note - 6) / 12.0D) * Math.pow(2.0D, octave - 4));
    }

    public static float getPitchFromNoteAndOctave(NotesEnum note, int octave) {
        return getPitchFromNoteAndOctave(note.ordinal(), octave);
    }

    public static int convertNoteAndOctaveToId(int note, int octave) {
        return octave * 12 + note;
    }

    public static int convertNoteAndOctaveToId(NotesEnum note, int octave) {
        return convertNoteAndOctaveToId(note.ordinal(), octave);
    }

    public static NotesEnum getNoteFromId(int id) {
        return NotesEnum.getNote(id % 12);
    }

    public static int getOctaveFromId(int id) {
        return id / 12;
    }

}
