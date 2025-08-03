import java.util.PriorityQueue;
import java.util.ArrayList;

public class Simulation {
    // k value in requirements

    private int currentTime;
    private PriorityQueue<Event> eventQueue;
    private Ranges[] ranges;

    public Simulation() {
        this.currentTime = 0;
        this.eventQueue = new PriorityQueue<Event>();
        initializeQueue();
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public int getEventQueueSize() {
        return eventQueue.size();
    }

    public PriorityQueue<Event> getEventQueue() { return eventQueue;}

    public void initializeQueue() {
        ArrayList<Vehicle> vehicles = Vehicle.getAllVehicles();
        if (vehicles.size() == 0) {
            throw new IllegalArgumentException("There are no vehicles with which to run the simulation");
        }
        ArrayList<Route> routes = Route.getAllRoutes();
        boolean unassignedVehicles = true;
        for (Route r : routes) {
            if (r.getVehiclesOnRoute().size() != 0) {
                unassignedVehicles = false;
            }
        }

        boolean unassignedVehiclesSUSSY = true;
        for (Vehicle v : vehicles) {
            if (v.getCurrentLocation() == null) {
                unassignedVehiclesSUSSY = false;
            }
        }

        if (unassignedVehicles) {
            throw new IllegalArgumentException(
                    "There are vehicles, but none are assigned to a route. Please assign all vehicles to a route.");
        }

        if (!unassignedVehiclesSUSSY) {
            throw new IllegalArgumentException(
                    "There is at least one unassigned vehicle. Please assign all vehicles to a route.");
        }
        for (Vehicle v : vehicles) {
            if (v.getEventTime() != 0) {
                if (v.getDepartStatus()) {
                    eventQueue.add(new DepartureEvent(v, v.getCurrentLocation(), v.getEventTime()));
                } else {
                    eventQueue.add(new ArrivalEvent(v, v.getCurrentLocation(), v.getEventTime(), v.getRoute().getNextLocationIndex(v.getIndexOnRoute(), v.getDirection())));
                }
            } else {
                DepartureEvent toEnqueue = new DepartureEvent(v, v.getCurrentLocation(),
                        ArrivalEvent.calculateDelay(v, v.getCurrentLocation()));
                eventQueue.add(toEnqueue);
            }
        }
    }

    public String[] runForTime(int relativeTime) {
        ArrayList<String> output = new ArrayList<>();
        boolean done = false;
        while (!done) {
            Event e = eventQueue.peek();
            Event toAdd;
            int t = e.getEventTime();
            if (t - currentTime <= relativeTime) {
                if (e instanceof DepartureEvent) {
                    DepartureEvent toDepart = (DepartureEvent) eventQueue.poll();
                    output.add("Processed: " + toDepart);
                    System.out.println();
                    toAdd = toDepart.processDeparture(toDepart.getEventTime());
                    output.add("\n");

                } else {
                    ArrivalEvent toArrive = (ArrivalEvent) eventQueue.poll();
                    output.add("Processed: " + toArrive);
                    System.out.println();
                    toAdd = toArrive.processArrival(toArrive.getEventTime(), this, output);
                    output.add("\n");
                }
                eventQueue.add(toAdd);

            } else {
                done = true;
                currentTime = relativeTime + currentTime;
            }

        }
        return output.toArray(new String[0]);
    }

    public String[] runSingleIteration() {
        ArrayList<String> output = new ArrayList<>();
        Event e = eventQueue.poll();
        if (e instanceof DepartureEvent) {
            DepartureEvent toDepart = (DepartureEvent) e;
            output.add("Processed: " + toDepart);
            System.out.println();
            Event next = eventQueue.peek();
            if (eventQueue.size() == 0 || next.getEventTime() != toDepart.getEventTime()) {
                currentTime = toDepart.getEventTime();
            }
            ArrivalEvent nextArrival = toDepart.processDeparture(toDepart.getEventTime());
            eventQueue.add(nextArrival);
        } else if (e instanceof ArrivalEvent) {
            ArrivalEvent toArrive = (ArrivalEvent) e;
            output.add("Processed: " + toArrive);
            System.out.println();
            Event next = eventQueue.peek();
            if (eventQueue.size() == 0 || next.getEventTime() != toArrive.getEventTime()) {
                currentTime = toArrive.getEventTime();
            }
            DepartureEvent nextDeparture = toArrive.processArrival(toArrive.getEventTime(), this, output);
            eventQueue.add(nextDeparture);
        }
        return output.toArray(new String[0]);
    }

    public ArrayList<String> printStatus() {
        ArrayList<String> status = new ArrayList<>();
        ArrayList<Vehicle> vehicles = Vehicle.getAllVehicles();
        System.out.println(vehicles.size());
        status.add("Simulation Time: " + currentTime);
        for (Vehicle v : vehicles) {
            if (v.getRoute() != null) {
                if (v.getStatus() == VehicleStatus.MOVING) {

                    status.add(
                            "(NAME: " + v.getName() + ", ID: " + v.getVehicleID() + ") is "
                                    + v.getStatus().toString()
                                    + " from " + v.getCurrentLocation() + " to "
                                    + v.getRoute().getLocationbyIndexforRoute(
                                            v.getRoute().getNextLocationIndex(v.getIndexOnRoute(), v.getDirection()))
                                    + " on " + v.getRoute());
                } else if (v.getStatus() == VehicleStatus.WAITING) {
                    status.add(
                            "(NAME: " + v.getName() + ", ID: " + v.getVehicleID() + ") is "
                                    + v.getStatus().toString()
                                    + " " + v.getCurrentLocation() + " to "
                                    + v.getRoute().getLocationbyIndexforRoute(
                                            v.getRoute().getNextLocationIndex(v.getIndexOnRoute(), v.getDirection()))
                                    + " on " + v.getRoute());
                }
            }
        }

        return status;

    }

    public void printEvents() {
        PriorityQueue<Event> copy = new PriorityQueue<>(eventQueue);
        while (!copy.isEmpty()) {
            Event toPrint = copy.poll();
            System.out.println(toPrint);
        }
        System.out.println("Simulation Time: " + currentTime);
    }

    public void resetCurrentTime() {
        currentTime = 0;
    }

    public void setRanges(Ranges[] ranges) {
        this.ranges = ranges;
    }

    public int getDebarkingNum() {
        return ranges[0].uniformSelect();
    }

    public int getTransferringNum() {
        return ranges[1].uniformSelect();
    }

    public int getEmbarkingNum() {
        return ranges[2].uniformSelect();
    }

}