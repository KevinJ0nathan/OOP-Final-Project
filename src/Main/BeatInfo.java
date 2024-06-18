package Main;

/**
 * This class represents the information of a beat in a rhythm game.
 * Each beat has properties such as lane, note type, and timing.
 */
public class BeatInfo {
    // The lane in which the beat appears
    private int lane;
    // The type of note (note/slider)
    private Object noteType;
    // The timing of the beat (when it should spawn)
    private int timing;

    /**
     * Constructs a BeatInfo object with the specified properties.
     *
     * @param lane     The lane in which the beat appears.
     * @param noteType The type of note (e.g., regular note, slider note).
     * @param timing   The timing of the beat in the game timeline.
     */
    public BeatInfo(int lane, Object noteType, int timing) {
        this.lane = lane;
        this.noteType = noteType;
        this.timing = timing;
    }

    /**
     * Returns the lane in which the beat appears.
     *
     * @return The lane number.
     */
    public int getLane() {
        return lane;
    }

    /**
     * Returns the type of note.
     *
     * @return The note type.
     */
    public Object getNoteType() {
        return noteType;
    }

    /**
     * Returns the timing of the beat.
     *
     * @return The timing value.
     */
    public int getTiming() {
        return timing;
    }
}
