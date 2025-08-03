import java.util.ArrayList;
public class DepartureEvent extends Event {

    public DepartureEvent(Vehicle vehicle, Location currentLocation, int departureTime) {
        super(vehicle, currentLocation, departureTime);
    }

    public ArrivalEvent processDeparture(int currentTime) {

        int newTime = currentTime + vehicle.calculateNextLocationTime();
        vehicle.setStatus(VehicleStatus.MOVING);
        int nextRouteIndex = vehicle.getRoute().getNextLocationIndex(vehicle.getIndexOnRoute(), vehicle.getDirection());
        Location nextLocation = vehicle.getRoute().getLocationbyIndexforRoute(
                nextRouteIndex);
        ArrivalEvent toReturn = new ArrivalEvent(vehicle, nextLocation, newTime, nextRouteIndex);
        return toReturn;
    }

    public Event process(Simulation sim, ArrayList<String> logs) {
        return processDeparture(sim.getCurrentTime());
    }

    public String toString() {
        return String.format("Departure: %s is departing from %s at time: %d", vehicle.getName(),
                currentLocation.getLocationName(), eventTime);
    }

}