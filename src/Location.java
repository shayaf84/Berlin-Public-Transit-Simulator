import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class Location implements Comparable<Location> {
    private String name;
    private String id;
    private Point position;
    private int numRidersWaiting; //<------------------------------------------------------------------------
    private Hazard hazardAtLocation;
    private HashMap<Location, Hazard> hazardsBetweenLocations = new HashMap<>(); //<------------------------------------------------------------------------

    private static ArrayList<Location> allLocations = new ArrayList<>();
    private ArrayList<Vehicle> currentVehicles = new ArrayList<>();
    private HashMap<String, Ranges> passengerDist = new HashMap<>();

    public Location(String name, String id, String x, String y, String numPassengers) {
        this.name = name;
        this.id = UniqueIdChecker.checkId(id);
        this.position = new Point(x, y);
        numRidersWaiting = Integer.parseInt(checkCapacity(numPassengers));
        allLocations.add(this);
        Collections.sort(allLocations);
        passengerDist.put("debarking", new Ranges("0", "10"));
        passengerDist.put("transferring", new Ranges("0", "10"));
        passengerDist.put("embarking", new Ranges("0", "10"));
    }

    public Location(String name, String id, String x, String y, String numPassengers, String alow, String ahigh, String tlow, String thigh, String dlow, String dhigh) {
        this.name = name;
        this.id = UniqueIdChecker.checkId(id);
        this.position = new Point(x, y);
        numRidersWaiting = Integer.parseInt(checkCapacity(numPassengers));
        allLocations.add(this);
        Collections.sort(allLocations);
        changePassengerRange("debarking", new Ranges(alow, ahigh));
        changePassengerRange("transferring", new Ranges(tlow, thigh));
        changePassengerRange("embarking", new Ranges(dlow, dhigh));
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
        for (Location l : allLocations) {
            if (l.id.equals(id)) {
                throw new IllegalArgumentException("ID must be unique");
            }
        }
        return id;
    }

    // ------------ Vehicle Display Methods ---------------

    /**
     * Adds Vehicle object into list of current vehicles at Location
     * 
     * Parameters:
     * - vehicle: vehicle to be added to current vehicle list
     */
    public void assignVehicle(Vehicle vehicle) {
        for (Vehicle v : currentVehicles) {
            if (v.getVehicleID().equals(vehicle.getVehicleID())) {
                throw new IllegalArgumentException("Vehicle is already assigned to Location.");
            }
        }
        currentVehicles.add(vehicle);
    }

    /**
     * Removes Vehicle object from list of current vehicles at Location
     * 
     * Parameters:
     * - vehicle: vehicle to be removed from vehicle list
     */
    public void removeVehicle(Vehicle vehicle) {
        currentVehicles.remove(vehicle);
    }

    /**
     * Sorts and prints out all current vehicles
     */
    public void displayVehicles() {
        Collections.sort(currentVehicles);
        for (Vehicle v : currentVehicles) {
            System.out.println(v);
        }
    }
    // ------------------------------------------------------------


    // ------------ Hazard Methods ---------------

    /**
     * Adds hazard to location and links to other locations if hazard is in between.
     * 
     * Parameters:
     * - hazardType: name of the hazard
     * - duration: short or long term 
     * - timeMultipler: impact on travel time
     * - hazardLocationIndex: relative hazard location
     * - prev: location before this in route
     * - next: location after this in route
     */
    public void addHazard(String hazardType, int duration, double timeMultiplier, int hazardLocationIndex, Location prev, Location next) {
        Hazard h = new Hazard(hazardType, duration, timeMultiplier);

        switch (hazardLocationIndex) {
            case 0:
                hazardsBetweenLocations.put(prev, h);
                prev.hazardsBetweenLocations.put(this, h);
                break;
            case 1:
                hazardAtLocation = h;
                break;
            case 2:
                hazardsBetweenLocations.put(next, h);
                next.hazardsBetweenLocations.put(this, h);
                break;
         // Todo: Add default case
        }
    }

    /**
     * Removes hazard to location and revmoes links to other locations if hazard is in between.
     * 
     * Parameters:
     * - hazardLocationIndex: relative hazard location
     * - prev: location before this in route
     * - next: location after this in route
     */
    public void removeHazard(int hazardLocationIndex, Location prev, Location next) {
        switch (hazardLocationIndex) {
            case 0:
                if (hazardsBetweenLocations.get(prev) == null) {
                    throw new NullPointerException("Location does not have hazard to remove.");
                }   
                hazardsBetweenLocations.remove(prev);
                prev.hazardsBetweenLocations.remove(this);
                break;
            case 1:
                if (hazardAtLocation == null) {
                    throw new NullPointerException("Location does not have hazard to remove.");
                }
                hazardAtLocation = null;
                break;
            case 2:
                if (hazardsBetweenLocations.get(next) == null) {
                    throw new NullPointerException("Location does not have hazard to remove.");
                }   
                hazardsBetweenLocations.remove(next);
                next.hazardsBetweenLocations.remove(this);
                break;
        }
    }

    // -------------------------------------------

    /**
     * Prints out list of all current locations
     */
    public static void printLocations() {
        int i = 0;
        for (Location l : allLocations) {
            System.out.print("    " + i + " -> ");
            System.out.println(l);
            i++;
        }
    }

    /**
     * Return Location object corresponding to given id
     * 
     * Parameters:
     * - id: id of Route to be found
     */
    public static Location getLocationByID(String id) {
        for (Location l : allLocations) {
            if (l.id.equals(id)) {
                return l;
            }
        }
        throw new NoSuchElementException("Location with given id does not exist.");
    }

    /**
     * Returns whether Location exists within allLocations
     */
    public boolean isValidLocation() {
        for (int i = 0; i < allLocations.size(); i++) {
            if (allLocations.get(i).equals(this))
                return true;
        }
        return false;
    }

    public double calculateDistanceTo(Location destination) {
        return Point.calculateDistance(position, destination.getPosition());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Location))
            return false;

        Location loc = (Location) o;
        return loc.name.equals(this.name) && loc.id.equals(this.id);
    }

    @Override
    public int compareTo(Location o) {
        return AlphanumericComparator.compare(this.id, o.id);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", name, id);
        // return String.format(
        //     "Name - %s                  %n" +
        //     "ID - %s                    %n" +
        //     "Coordinates - %s           %n" +
        //     "People Waiting at Stop - %d",
        //     name, id, position, numRidersWaiting
        // );
    }

    public static int getLocationsLength() {
        return allLocations.size();
    }

    public static Location getLocationbyIndex(int index) {
        return allLocations.get(index);
    }

    public static ArrayList<Location> getAllLocations() {
        return allLocations;
    }

    public String getLocationName() {
        return name;
    }

    public String getLocationID() {
        return id;
    }

    public int getSpecificLocationLength() {
        return currentVehicles.size();
    }

    public ArrayList<Vehicle> getCurrentVehicles() {return currentVehicles;}

    public Point getPosition() {
        return position;
    }

    public Hazard getHazardAtLocation() {
        return hazardAtLocation;
    }

    public HashMap<Location, Hazard> getHazardsBetweenLocations() {
        return hazardsBetweenLocations;
    }

    public static String publicCheckCapacity(String capacity) { //<------------------------------------------------------------------
        if (!capacity.matches("^[0-9]+$")) {
            throw new IllegalArgumentException("Maximum capacity must be a non-negative integer");
        }
        return capacity;
    } //<------------------------------------------------------------------

    private String checkCapacity(String capacity) { //<------------------------------------------------------------------
        if (!capacity.matches("^[0-9]+$")) {
            throw new IllegalArgumentException("Maximum capacity must be a non-negative integer");
        }
        return capacity;
    } //<------------------------------------------------------------------

    public int getNumRidersWaiting() {
        return numRidersWaiting;
    }

    public void setNumRidersWaiting(int numRidersWaiting) {
        this.numRidersWaiting = numRidersWaiting;
    }

    public void changePassengerRange(String key, Ranges newRange) {
        passengerDist.put(key, newRange);
    }

    public int getDebarkingNum() {
        return passengerDist.get("debarking").uniformSelect();
    }

    public int getTransferringNum() {
        return passengerDist.get("transferring").uniformSelect();
    }

    public int getEmbarkingNum() {
        return passengerDist.get("embarking").uniformSelect();
    }

    //Solely for debugging purposes
    public void printRanges() {
        System.out.println("- - - - - - - - - - -");
        passengerDist.get("debarking").printRanges();
        passengerDist.get("transferring").printRanges();
        passengerDist.get("embarking").printRanges();
        System.out.println("- - - - - - - - - - -");
    }


}