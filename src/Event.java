import java.util.ArrayList;
public abstract class Event implements Comparable<Event> {
    protected Vehicle vehicle;
    protected Location currentLocation;
    protected int eventTime;


    public Event(Vehicle vehicle, Location currentLocation, int eventTime) {
        this.vehicle = vehicle;
        this.currentLocation = currentLocation;
        this.eventTime = eventTime;
    }

    public abstract Event process(Simulation newSim, ArrayList<String> logs);


    public int compareTo(Event other) {
        return this.eventTime - other.eventTime;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public int getEventTime() {
        return eventTime;
    }

    public String toString() {
        return String.format("Vehicle: %s, Location: %s, Time: %d", vehicle, currentLocation, eventTime);
    }

}
