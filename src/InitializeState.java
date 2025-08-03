import java.util.ArrayList;
import java.util.Arrays;


/**
 * TODO:
 * - departing/arriving
 * - event time
 */


public class InitializeState {
    public static void clientState() {
        // -------------------- LOCATIONS --------------------
        // Location(name, id, x, y, numPassenger, arriveLow, ariveHigh, transLow, transHigh, depLow, depHigh)                             
        Location stadium = new Location("stadium", "stadium", "0","5","8","2","6","2", "3","1","1");
        Location west = new Location("west", "west", "1","3","2","1","2","1", "1","1","1" );
        Location castle = new Location("castle", "castle", "2","0","5","1","2","1", "3","1","1" );
        Location port = new Location("port", "port", "3","10","3","1","1","1", "1","1","2" );
        Location bahnhof = new Location("bahnhof", "bahnhof", "4","5","10","1","4","1", "6","3","6" );
        Location north = new Location("north", "north", "5","8","3","1","2","1", "2","1","1" );
        Location museum = new Location("museum", "museum", "5","3","8","2","4","1", "4","2","6" );
        Location south = new Location("south", "south", "6","1","3","1","2","1", "1","1","1" );
        Location tower = new Location("tower", "tower", "7","4","5","1","1","1", "2","1","3" );
        Location east = new Location("east", "east", "8","4","2","1","2","1", "1","1","2" );
        Location airport = new Location("airport", "airport", "10","2","10","3","8","2", "5","1","2" );
        Location gate = new Location("gate", "gate", "9","9","5","1","1","1", "2","1","3" );

        // -------------------- Routes --------------------
        // Route(name, id, type)
        Route line_1 = new Route("line_1", "line_1", VehicleType.SBAHN);
        Route line_2 = new Route("line_2", "line_2", VehicleType.UBAHN);
        Route line_3 = new Route("line_3", "line_3", VehicleType.UBAHN);
        Route ring_1 = new Route("ring_1", "ring_1", VehicleType.SBAHN);
        Route ring_2 = new Route("ring_2", "ring_2", VehicleType.BUS);
        Route ring_3 = new Route("ring_3", "ring_3", VehicleType.TRAM);

        line_1.setAllStops(new ArrayList<>(Arrays.asList(stadium, west, bahnhof, tower, east, airport, east, tower, bahnhof, west)));
        line_2.setAllStops(new ArrayList<>(Arrays.asList(port, north, bahnhof, west, castle, west, bahnhof, north)));
        line_3.setAllStops(new ArrayList<>(Arrays.asList(gate, north, bahnhof, tower, south, tower, bahnhof, north)));
        ring_1.setAllStops(new ArrayList<>(Arrays.asList(south, castle, west, bahnhof, tower)));
        ring_2.setAllStops(new ArrayList<>(Arrays.asList(bahnhof, museum, tower)));
        ring_3.setAllStops(new ArrayList<>(Arrays.asList(north, bahnhof, north, port, north, gate)));

        // -------------------- VEHICLES --------------------
        // Vehicle(name, id, capacity, numriders, type, speed, direction, departing?, eventTime)
        Vehicle sbahn_A = new Vehicle("sbahn_A", "sbahn_A", "6", "2", VehicleType.SBAHN, "60", "1", false, 0);
        Vehicle ubahn_A = new Vehicle("ubahn_A", "ubahn_A", "5", "3", VehicleType.UBAHN, "45", "0", true, 4);
        Vehicle bus_A = new Vehicle("bus_A", "bus_A", "3", "3", VehicleType.BUS, "35", "1", true, 3);
        Vehicle sbahn_C = new Vehicle("sbahn_C", "sbahn_C", "8", "7", VehicleType.SBAHN, "60", "0", false, 4);
        Vehicle tram_A = new Vehicle("tram_A", "tram_A", "4", "4", VehicleType.TRAM, "40", "1", false, 10);
        Vehicle sbahn_B = new Vehicle("sbahn_B", "sbahn_B", "6", "3", VehicleType.SBAHN, "60", "0", true , 2);
        Vehicle ubahn_C = new Vehicle("ubahn_C", "ubahn_C", "5", "5", VehicleType.UBAHN, "45", "1", true, 8);
        Vehicle ubahn_B = new Vehicle("ubahn_B", "ubahn_B", "5", "2", VehicleType.UBAHN, "45", "1", false, 5);

        // designateVehiclePostion(location, route)
        sbahn_A.designateVehiclePosition(stadium, line_1);
        ubahn_A.designateVehiclePosition(port, line_2);
        bus_A.designateVehiclePosition(museum, ring_2);
        sbahn_C.designateVehiclePosition(west, ring_1);
        tram_A.designateVehiclePosition(north, ring_3);
        sbahn_B.designateVehiclePosition(east, line_1);
        ubahn_C.designateVehiclePosition(bahnhof, line_2);
        ubahn_B.designateVehiclePosition(north, line_3);

        // set route index :DDDDD
        sbahn_A.setRouteIndex(line_1.getIndexbyLocationforRoute(stadium));
        ubahn_A.setRouteIndex(line_2.getIndexbyLocationforRoute(port));
        bus_A.setRouteIndex(ring_2.getIndexbyLocationforRoute(museum));
        sbahn_C.setRouteIndex(ring_1.getIndexbyLocationforRoute(west));
        tram_A.setRouteIndex(ring_3.getIndexbyLocationforRoute(north));
        sbahn_B.setRouteIndex(line_1.getIndexbyLocationforRoute(east));
        ubahn_C.setRouteIndex(line_2.getIndexbyLocationforRoute(bahnhof));
        ubahn_B.setRouteIndex(line_3.getIndexbyLocationforRoute(north));
    }
}
