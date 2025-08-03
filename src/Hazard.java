public class Hazard { // <------------------------------------------------------------------------

    private boolean duration;
    // true for longTerm, false for shortTerm
    private String hazardType;
    private double timeMultiplier;

    public Hazard(String hazard, int term, double timeMultiplier) {
        if (timeMultiplier < 0) {
            throw new IllegalArgumentException("Hazard cannot decrease travel time.");
        }
        this.hazardType = hazard;
        this.duration = term != 0;
        this.timeMultiplier = timeMultiplier;
    }

    public boolean getDuration() {
        return duration;
    }

    public double getTimeMultiplier() {
        return timeMultiplier;
    }
}