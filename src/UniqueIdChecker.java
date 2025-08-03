

public class UniqueIdChecker {
    public static String checkId(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (!id.matches("[A-Za-z0-9_]+")) {
            throw new IllegalArgumentException("ID must be alphanumeric");
        }
        if (id.length() >= 100) {
            throw new IllegalArgumentException("ID must be less than 100 characters");
        }

        for (Route r : Route.getAllRoutes()) {
            if (r.getRouteId().equals(id)) {
                throw new IllegalArgumentException("ID must be unique");
            }
        }

        for (Location l : Location.getAllLocations()) {
            if (l.getLocationID().equals(id)) {
                throw new IllegalArgumentException("ID must be unique");
            }
        }

        for (Vehicle v : Vehicle.getAllVehicles()) {
            if (v.getVehicleID().equals(id)) {
                throw new IllegalArgumentException("ID must be unique");
            }
        }

        return id;
    }
}