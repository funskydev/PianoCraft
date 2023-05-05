package funskydev.pianocraft.util;

public enum BlockPosEnum {

    EAST,
    WEST,
    TOP(true),
    TOP_EAST(true),
    TOP_WEST(true);

    private boolean isTop;

    private BlockPosEnum() {
        this.isTop = false;
    }

    private BlockPosEnum(boolean isTop) {
        this.isTop = isTop;
    }

    public boolean isTop() {
        return this.isTop;
    }

}
