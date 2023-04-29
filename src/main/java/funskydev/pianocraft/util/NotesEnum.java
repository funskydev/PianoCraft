package funskydev.pianocraft.util;

public enum NotesEnum {

    C,
    C_SHARP(true),
    D,
    D_SHARP(true),
    E,
    F,
    F_SHARP(true),
    G,
    G_SHARP(true),
    A,
    A_SHARP(true),
    B;

    private boolean isSharp;

    NotesEnum() {
        this.isSharp = false;
    }

    NotesEnum(boolean isSharp) {
        this.isSharp = isSharp;
    }

    public boolean isSharp() {
        return this.isSharp;
    }

    public String getNoteName() {
        return this.name().replace("_SHARP", "#");
    }

    public static NotesEnum getNote(int index) {
        return NotesEnum.values()[index];
    }

}
