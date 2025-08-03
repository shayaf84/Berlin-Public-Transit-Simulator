import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArrivalEvent extends Event {
    private static final String GOLD = "\033[38;5;220m";
    private static final String RESET = "\033[0m";
    public static int totalDelayed = 0;
    private static Map<Integer, Integer> delayedDictionary = new HashMap<>();

    private int newRouteIndex;

    public ArrivalEvent(Vehicle vehicle, Location currentLocation, int arrivalTime, int newRouteIndex) {
        super(vehicle, currentLocation, arrivalTime);
        this.newRouteIndex = newRouteIndex;
    }

    public Event process(Simulation sim, ArrayList<String> logs) {
        return processArrival(sim.getCurrentTime(), sim, logs);
    }

    public DepartureEvent processArrival(int currentTime, Simulation currentSimulation, ArrayList<String> logs) {
        int timeModifier = calculateDelay(vehicle, currentLocation);
        int newTime = currentTime + timeModifier;
        vehicle.setStatus(VehicleStatus.WAITING);
        vehicle.getCurrentLocation().removeVehicle(vehicle);
        vehicle.setCurrentLocation(currentLocation);
        vehicle.setRouteIndex(newRouteIndex);
        currentLocation.assignVehicle(vehicle);
        // currentLocation.printRanges(); // use this for testing ranges
        // <---------------------------------------------------------------------------

        logs.add("Current Statistics: ");
        logs.add("    Number of people on " + vehicle.getName() + ": " + vehicle.getNumRiders());
        logs.add("    Number of people at " + currentLocation.getLocationName() + ": "
                + currentLocation.getNumRidersWaiting());

        logs.add("Exchanging....");
        int debark = currentLocation.getDebarkingNum();
        if (debark > vehicle.getNumRiders()) {
            debark = vehicle.getNumRiders();
        }
        vehicle.setNumRiders(vehicle.getNumRiders() - debark);
        // System.out.println("Number debarking: " + debark);
        // System.out.println("Number remaining on vehicle: " + vehicle.getNumRiders());

        int transfer = currentLocation.getTransferringNum();
        if (transfer > debark) {
            transfer = debark;
        }
        // System.out.println("Number transferring: " + transfer);

        int embark = currentLocation.getEmbarkingNum();
        if (embark > currentLocation.getNumRidersWaiting()) {
            embark = currentLocation.getNumRidersWaiting();
        }
        // System.out.println("Number embarking: " + embark);
        int delayed = 0;

        if (embark > (vehicle.getCapacity() - vehicle.getNumRiders())) {
            // System.out.println(GOLD + "Number wanting to embark is too much" + RESET);
            delayed = embark - (vehicle.getCapacity() - vehicle.getNumRiders());
            embark = (vehicle.getCapacity() - vehicle.getNumRiders());
            // System.out.println("New embarking number: " + embark);
            // System.out.println("Delayed number: " + delayed);
        }

        logs.add("    Number of people debarking: " + debark);
        logs.add("    Number of people 'home': " + (debark - transfer));
        logs.add("    Number of people transferring: " + transfer);
        logs.add("    Number of people embarking: " + embark);
        logs.add("    Number of people delayed: " + delayed);
        vehicle.setNumRiders(vehicle.getNumRiders() + embark);
        currentLocation.setNumRidersWaiting(currentLocation.getNumRidersWaiting() - embark + transfer);
        if (delayed != 0) {
            delayedDictionary.put(currentTime, delayed);
        }

        logs.add("New Statistics: ");
        logs.add("    Number of people on " + vehicle.getName() + ": " + vehicle.getNumRiders());
        logs.add("    Number of people at " + currentLocation.getLocationName() + ": "
                + currentLocation.getNumRidersWaiting());

        DepartureEvent toReturn = new DepartureEvent(vehicle, currentLocation, newTime);
        return toReturn;
    }

    public String toString() {
        return String.format("Arrival: %s is arriving at %s at time: %d", vehicle.getName(),
                currentLocation.getLocationName(), eventTime);
    }

    public static int calculateDelay(Vehicle v, Location l) {
        double timeModifier = v.getWAITING_TIME();
        Hazard hazard = l.getHazardAtLocation();
        if (hazard != null) {
            System.out.println(hazard); // moved print statement here to see if it get rids of the null being printed.
            if (hazard.getDuration()) {
                timeModifier *= hazard.getTimeMultiplier();
            } else {
                timeModifier += hazard.getTimeMultiplier();
            }
        }

        return (int) timeModifier;
    }

    public static void printDelayed() {
        if (delayedDictionary.isEmpty()) {
            System.out.println("There have been no delays so far.");
            return;
        }
        int total = 0;
        for (Map.Entry<Integer, Integer> entry : delayedDictionary.entrySet()) {
            System.out.println("Time " + entry.getKey() + ": " + entry.getValue() + " Passengers Delayed");
            total += entry.getValue();
        }
        System.out.println("Total Passengers Delayed: " + total);

    }

}