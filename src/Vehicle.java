import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class Vehicle implements Comparable<Vehicle> {
    private final int WAITING_TIME = 1;
    private String name;
    private String id;
    private VehicleType type;
    private Route route;
    private Location currentLocation;
    private int capacity;
    private double speed;
    private boolean direction;
    private int numRiders; // <------------------------------------------------------------------------
    private VehicleStatus status;
    private int routeIndex = -1;
    
    private boolean departStatus = true;
    private int eventTime = 0;

    private static ArrayList<Vehicle> allVehicles = new ArrayList<>();

    public Vehicle(String name, String id, String capacity, String numRiders, VehicleType type, String speed,
                   String direction) {
        this.name = name;
        this.id = UniqueIdChecker.checkId(id);
        this.capacity = Integer.parseInt(checkCapacity(capacity));
        this.numRiders = Integer.parseInt(checkCapacity(numRiders));
        this.speed = Double.parseDouble(checkSpeed(speed));
        this.status = VehicleStatus.WAITING;
        this.type = type;
        allVehicles.add(this);
        Collections.sort(allVehicles);
        if (direction.equals("1")) {
            this.direction = true;
        } else {
            this.direction = false;
        }
    }

    // constructor only for hardcoding/preinitialization
    public Vehicle(String name, String id, String capacity, String numRiders, VehicleType type, String speed,
                   String direction, boolean departStatus, int eventTime) {
        this.name = name;
        this.id = UniqueIdChecker.checkId(id);
        this.capacity = Integer.parseInt(checkCapacity(capacity));
        this.numRiders = Integer.parseInt(checkCapacity(numRiders));
        this.speed = Double.parseDouble(checkSpeed(speed));
        this.status = VehicleStatus.WAITING;
        this.type = type;
        allVehicles.add(this);
        Collections.sort(allVehicles);
        if (direction.equals("1")) {
            this.direction = true;
        } else {
            this.direction = false;
        }
        this.departStatus = departStatus;
        this.eventTime = eventTime;
    }

    private String checkSpeed(String speed) {
        if (!speed.matches("^[+-]?(\\d+(\\.\\d*)?|\\.\\d+)$")) {
            throw new IllegalArgumentException("Vehicle speed must be a real number");
        } else if (speed == null || speed.isEmpty()) {
            throw new IllegalArgumentException("Vehicle speed cannot be null or empty");
        }
        return speed;
    }

    private String checkCapacity(String capacity) {
        if (!capacity.matches("^[0-9]+$")) {
            throw new IllegalArgumentException("Maximum capacity must be a non-negative integer");
        }
        return capacity;
    }

    /**
     * Assigns vehicle to given location and route.
     * <p>
     * Parameters:
     * - loc: location to assign vehicle to
     * - route: route to assign vehicle to
     */
    public void designateVehiclePosition(Location loc, Route route) {
        this.route = route;
        this.currentLocation = loc;
        if (!(loc == null || route == null)) {
            route.assignVehicle(this);
            loc.assignVehicle(this);
        }

    }

    public String getName() {
        return this.name;
    }

    /**
     * Return Vehicle object corresponding to given id
     * <p>
     * Parameters:
     * - id: id of Vehicle to be found
     */
    public static Vehicle getVehicleByID(String id) {
        for (Vehicle v : allVehicles) {
            if (v.id.equals(id)) {
                return v;
            }
        }
        throw new NoSuchElementException("Vehicle with given id does not exist.");
    }

    /**
     * Prints out formatted list of all Vehicle objects.
     */
    public static void printVehicles() {

        int i = 0;
        for (Vehicle v : allVehicles) {
            System.out.print("    " + i + " -> ");
            System.out.println(v);
            i++;
        }
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", name, id);
    }

    @Override
    public int compareTo(Vehicle o) {
        return AlphanumericComparator.compare(this.id, o.id);
    }

    public String getVehicleID() {
        return id;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getSpeed() {return speed;}

    public int getWAITING_TIME() {
        return WAITING_TIME;
    }

    public static int getVehiclesLength() {
        return allVehicles.size();
    }

    public static ArrayList<Vehicle> getAllVehicles() {
        return allVehicles;
    }

    public static Vehicle getVehiclebyIndex(int index) {
        return allVehicles.get(index);
    }

    public VehicleType getVehicleType() {
        return type;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public Route getRoute() {
        return route;
    }

    public void setCurrentLocation(Location loc) {
        this.currentLocation = loc;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public static String publicCheckId(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (!id.matches("[A-Za-z0-9]+")) {
            throw new IllegalArgumentException("ID must be alphanumeric");
        }
        if (id.length() >= 100) {
            throw new IllegalArgumentException("ID must be less than 100 characters");
        }
        for (Vehicle v : allVehicles) {
            if (v.id.equals(id)) {
                throw new IllegalArgumentException("ID must be unique");
            }
        }
        return id;
    }

    public static String publicCheckCapacity(String capacity) {
        if (!capacity.matches("^[0-9]+$")) {
            throw new IllegalArgumentException("Maximum capacity must be a non-negative integer");
        }
        return capacity;
    }

    public static String publicCheckSpeed(String speed) {
        if (!speed.matches("^[+-]?(\\d+(\\.\\d*)?|\\.\\d+)$")) {
            throw new IllegalArgumentException("Vehicle speed must be a real number");
        } else if (speed == null || speed.isEmpty()) {
            throw new IllegalArgumentException("Vehicle speed cannot be null or empty");
        } else if (Integer.parseInt(speed) < 0) {
            throw new IllegalArgumentException("Vehicle speed cannot be negative");
        }
        return speed;
    }

    public Location getLocation() {
        return currentLocation;
    }

    // Time in minutes
    public int calculateNextLocationTime() {
        int nextRouteIndex = this.route.getNextLocationIndex(routeIndex, direction);
        Location nextLocation = route.getLocationbyIndexforRoute(nextRouteIndex);
        double distance = currentLocation.calculateDistanceTo(nextLocation);
        double time = (distance / speed) * 60;

        HashMap<Location, Hazard> hazards = currentLocation.getHazardsBetweenLocations();
        if (hazards.containsKey(nextLocation)) {
            Hazard hazard = hazards.get(nextLocation);

            if (hazard.getDuration()) {
                time *= hazard.getTimeMultiplier();
            } else {
                time += hazard.getTimeMultiplier();
            }
        }

        return Integer.max((int) time, 1);

    }

    public boolean getDirection() {
        return direction;
    }

    public void setRouteIndex(int i) {
        routeIndex = i;
    }

    public int getIndexOnRoute() {
        return routeIndex;
    }

    public static void resetVehicles() {
        for (Vehicle v : allVehicles) {
            if (v.getStatus() == VehicleStatus.MOVING) {
                v.setStatus(VehicleStatus.WAITING);
            }
        }
    }

    public int getNumRiders() {
        return numRiders;
    }

    public void setNumRiders(int numRiders) {
        this.numRiders = numRiders;
    }

    public boolean getDepartStatus() {
        return departStatus;
    }

    public int getEventTime() {
        return eventTime;
    }
}