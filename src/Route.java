import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

public class Route implements Comparable<Route> {
    private ArrayList<Location> stops;
    private String name;
    private String id;
    private VehicleType type;

    private static ArrayList<Route> allRoutes = new ArrayList<>();
    private ArrayList<Vehicle> currentVehicles = new ArrayList<>();

    public Route(String name, String id, VehicleType typeIndicator) {
        this.name = name;
        this.id = UniqueIdChecker.checkId(id);
        allRoutes.add(this);
        Collections.sort(allRoutes);
        stops = new ArrayList<>();
        this.type = typeIndicator;
    }

    // need to add bounds checking
    public void addStop(Location loc, int i) {
        if (i < 0 || i > stops.size()) {
            throw new IndexOutOfBoundsException(
                    "Must add location at an integer between 0 and " + stops.size() + ", inclusive");
        }

        stops.add(i, loc);
    }

    // need to add bounds checking
    public void removeStop(int i) {
        if (i < 0 || i > stops.size()) {
            throw new IndexOutOfBoundsException(
                    "Must remove location using an integer between 0 and " + (stops.size() - 1) + ", inclusive");
        }
        stops.remove(i);
    }

    public void setAllStops(ArrayList<Location> stops) {
        this.stops = stops;
    }

    // ------------ Vehicle Display Methods ---------------

    /**
     * Adds Vehicle object into list of current vehicles on Route
     * 
     * Parameters:
     * - vehicle: vehicle to be added to current vehicle list
     */
    public void assignVehicle(Vehicle vehicle) {
        for (Vehicle v : currentVehicles) {
            if (v.getVehicleID().equals(vehicle.getVehicleID())) {
                throw new IllegalArgumentException("Vehicle is already assigned to Route.");
            }
        }
        currentVehicles.add(vehicle);
    }

    /**
     * Removes Vehicle object from list of current vehicles on Route
     * 
     * Parameters:
     * - vehicle: vehicle to be removed from vehicle list
     */
    public void removeVehicle(Vehicle vehicle) {
        for (Vehicle v : currentVehicles) {
            if (v.getVehicleID().equals(vehicle.getVehicleID())) {
                currentVehicles.remove(vehicle);
            }
        }
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle does not exist.");
        }
    }

    /**
     * Sorts and prints out all current vehicles
     */
    public void displayVehicles() {
        Collections.sort(currentVehicles);
        if (currentVehicles.size() > 0) {
            for (Vehicle v : currentVehicles) {
                System.out.println(v);
            }
        } else {
            System.out.println("        There are currently no vehicles assigned to this route.");
        }

    }
    // ------------------------------------------------------------

    /**
     * Prints out list of all current routes
     */
    public static void printRoutes() {
        int i = 0;
        for (Route r : allRoutes) {
            System.out.print("    " + i + " -> ");
            System.out.println(r);
            i++;
        }
    }

    /**
     * Prints out formatted list of current route stops/locations
     */
    public void printRouteStops() {
        if (stops.isEmpty()) {
            System.out.println("[]");
        } else {
            System.out.println("[");
            for (int i = 0; i < stops.size(); i++) {
                System.out.print("    " + i + " -> ");
                System.out.println(", " + stops.get(i));
            }
            System.out.println("]");
        }
    }

    /**
     * Return Route object corresponding to given id
     * 
     * Parameters:
     * - id: id of Route to be found
     */
    public static Route getRouteByID(String id) {
        for (Route r : allRoutes) {
            if (r.id.equals(id)) {
                return r;
            }
        }
        throw new NoSuchElementException("Route with given id does not ex ist.");
    }

    /**
     * Returns whether Route exists within allRoutes
     */
    public boolean isValidRoute() {
        for (int i = 0; i < stops.size(); i++) {
            if (stops.get(i).equals(this))
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Route))
            return false;

        Route route = (Route) o;
        return route.name.equals(this.name);
    }

    @Override
    public int compareTo(Route o) {
        return AlphanumericComparator.compare(this.id, o.id);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", name, id);
    }

    public static int getRoutesLength() {
        return allRoutes.size();
    }

    public ArrayList<Location> getStops() {
        return stops;
    }

    public static Route getRoutebyIndex(int index) {
        return allRoutes.get(index);
    }

    public int getSpecificRouteLength() {
        return stops.size();
    }

    public Location getLocationbyIndexforRoute(int index) {
        return stops.get(index);
    }

    public int getIndexbyLocationforRoute(Location loc) {
        return stops.indexOf(loc);
    }

    public static String publicCheckID(String id) {
        if (!id.matches("[A-Za-z0-9]+") || id.length() > 100) {
            throw new IllegalArgumentException("ID must be alphanumeric and less than 100 characters");
        }
        return id;
    }

    public static String publicCheckRouteValidity(String number) {
        if (!number.equals("1") && !number.equals("2") && !number.equals("3") && !number.equals("4")) {
            throw new IllegalArgumentException("Input must be 1, 2, 3, or 4");
        }
        return number;
    }

    public void publicVehicleTypeValidity(Vehicle v) {
        if (v.getVehicleType() != type) {
            throw new IllegalArgumentException("Vehicle and route type don't match");
        }
    }

    public static ArrayList<Route> getAllRoutes() {
        return allRoutes;
    }

    public String getName() { return name; }

    public VehicleType getVehicleType() {
        return type;
    }

    public boolean checkLocationExistenceInRoute(Location loc) {
        for (Location l : stops) {
            if (l == loc) {
                return true;
            }
        }
        return false;
    }

    public boolean checkEmptyRoute() {
        return stops.size() == 0;
    }

    public String getRouteId() {
        return id;
    }

    private Location getLocationByIndex(int i) {
        return stops.get(i);
    }

    public void removeVehiclesAtLocation(int idx) {
        Location loc = getLocationByIndex(idx);
        for (int i = currentVehicles.size() - 1; i >= 0; i--) {
            if (currentVehicles.get(i).getLocation() == loc) {
                currentVehicles.get(i).designateVehiclePosition(null, null);
                loc.removeVehicle(currentVehicles.get(i));
                currentVehicles.remove(i);
            }
        }
    }

    public int getNextLocationIndex(int index, boolean direction) {
        int newIndex;
        if (direction) {
            if (index + 1 == this.getSpecificRouteLength()) {
                newIndex = 0;
            } else {
                newIndex = index + 1;
            }
        } else {
            if (index == 0) {
                newIndex = this.getSpecificRouteLength() - 1;
            } else {
                newIndex = index - 1;
            }
        }

        return newIndex;
    }

    public ArrayList<Vehicle> getVehiclesOnRoute() {
        return currentVehicles;
    }

}
