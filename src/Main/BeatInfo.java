package Main;

public class BeatInfo {
    private int lane;
    private Object noteType;
    private int timing;

    public BeatInfo(int lane, Object noteType, int timing) {
        this.lane = lane;
        this.noteType = noteType;
        this.timing = timing;
    }

    public int getLane() {
        return lane;
    }

    public Object getNoteType() {
        return noteType;
    }

    public int getTiming() {
        return timing;
    }
}
