package funskydev.pianocraft.client.screen;

import funskydev.pianocraft.util.NotesEnum;

public enum KeysEnum {

    Q("key.keyboard.q"),
    N2("key.keyboard.2"),
    W("key.keyboard.w"),
    N3("key.keyboard.3"),
    E("key.keyboard.e"),

    R("key.keyboard.r"),
    N5("key.keyboard.5"),
    T("key.keyboard.t"),
    N6("key.keyboard.6"),
    Y("key.keyboard.y"),
    N7("key.keyboard.7"),
    U("key.keyboard.u"),

    I("key.keyboard.i"),
    N9("key.keyboard.9"),
    O("key.keyboard.o"),
    N0("key.keyboard.0"),
    P("key.keyboard.p"),

    Z("key.keyboard.z"),
    S("key.keyboard.s"),
    X("key.keyboard.x"),
    D("key.keyboard.d"),
    C("key.keyboard.c"),
    F("key.keyboard.f"),
    V("key.keyboard.v"),

    B("key.keyboard.b"),
    H("key.keyboard.h"),
    N("key.keyboard.n"),
    J("key.keyboard.j"),
    M("key.keyboard.m");

    private String translationKey;

    private NotesEnum note;
    private int octave;

    KeysEnum(String translationKey) {
        this.translationKey = translationKey;

        this.note = NotesEnum.values()[this.ordinal() % 12];
        this.octave = this.ordinal() / 12 + 3;
    }

    public NotesEnum getNote() {
        return this.note;
    }

    public int getOctave() {
        return this.octave;
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

}
