import javax.swing.*;


public class TransitSimulator {
    private static JFrame frame;
    private static JPanel mainPanel;

    private final VehicleController vehicleController;
    private final LocationController locationController;
    private final RouteController routeController;
    private final SimulationController simulationController;

    public TransitSimulator() {
        InitializeState.clientState();
        
        this.vehicleController = new VehicleController();
        this.locationController = new LocationController();
        this.routeController = new RouteController();
        this.simulationController = new SimulationController();

        this.frame = new JFrame("Berlin Mass Transit Simulator");
        frame.setSize(400,400);



        this.mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainMenuPanel();

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel center(JComponent component) {
        JPanel wrapper = new JPanel();
        wrapper.add(component);
        return wrapper;
    }

    private void mainMenuPanel() {
        mainPanel.removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());

        JLabel title = new JLabel("Berlin Public Transit Simulation System");
        JButton vehicleButton = new JButton("Vehicles");
        JButton locationButton = new JButton("Locations");
        JButton routesButton = new JButton("Routes");
        JButton simButton = new JButton("Simulation");

        vehicleButton.addActionListener(e -> vehiclesPanel());
        locationButton.addActionListener(e -> locationsPanel());
        routesButton.addActionListener(e -> routesPanel());
        simButton.addActionListener(e -> {
            try {
                Simulation sim = new Simulation();
                simulationPanel(sim);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        panel.add(center(title));
        panel.add(center(vehicleButton));
        panel.add(center(locationButton));
        panel.add(center(routesButton));
        panel.add(center(simButton));
        panel.add(Box.createVerticalGlue());

        mainPanel.add(panel);
        mainPanel.revalidate();
        mainPanel.repaint();

    }


    private void vehiclesPanel() {
        mainPanel.removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());

        JLabel title = new JLabel("Vehicle Menu");
        JButton createVehicle = new JButton("Create a Vehicle");
        JButton viewVehicle = new JButton("View all Vehicles");
        JButton assignVehicle = new JButton("Assign Vehicle to Route");
        JButton backButton = new JButton("Back");

        createVehicle.addActionListener(e -> vehicleController.newVehicleForm(frame));
        viewVehicle.addActionListener(e -> vehicleController.showVehicles(frame));
        assignVehicle.addActionListener(e -> vehicleController.assignVehicleForm(frame));
        backButton.addActionListener(e -> mainMenuPanel());

        panel.add(center(title));
        panel.add(center(createVehicle));
        panel.add(center(viewVehicle));
        panel.add(center(assignVehicle));
        panel.add(center(backButton));
        panel.add(Box.createVerticalGlue());

        mainPanel.add(panel);
        mainPanel.revalidate();
        mainPanel.repaint();

    }

    private void locationsPanel() {
        mainPanel.removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());

        JLabel title = new JLabel("Location Menu");
        JButton createLocation = new JButton("Create a Location");
        JButton viewLocation = new JButton("View all Locations");
        JButton viewVehicles = new JButton("View Vehicles at Location");
        JButton changeRange = new JButton("Change Passenger Ranges");
        JButton backButton = new JButton("Back");

        createLocation.addActionListener(e -> locationController.newLocationForm(frame));
        viewLocation.addActionListener(e -> locationController.showLocations(frame));
        viewVehicles.addActionListener(e -> locationController.showVehiclesAtLocation(frame));
        changeRange.addActionListener(e -> locationController.changePassengerRanges(frame));
        backButton.addActionListener(e -> mainMenuPanel());

        panel.add(center(title));
        panel.add(center(createLocation));
        panel.add(center(viewLocation));
        panel.add(center(viewVehicles));
        panel.add(center(changeRange));
        panel.add(center(backButton));
        panel.add(Box.createVerticalGlue());

        mainPanel.add(panel);
        mainPanel.revalidate();
        mainPanel.repaint();

    }

    private void routesPanel() {
        mainPanel.removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());

        JLabel title = new JLabel("Route Menu");
        JButton createRoute = new JButton("Create a Route");
        JButton viewRoute = new JButton("View all Routes");
        JButton addLoc = new JButton("Add Location to Route");
        JButton removeLoc = new JButton("Remove Location from Route");
        JButton displayLoc = new JButton("View Location on a Route");
        JButton displayVeh = new JButton("View Vehicle on a Route");
        JButton backButton = new JButton("Back");

        createRoute.addActionListener(e -> routeController.newRouteForm(frame));
        viewRoute.addActionListener(e -> routeController.showRoutes(frame));
        addLoc.addActionListener(e -> routeController.addToRouteForm(frame));
        removeLoc.addActionListener(e -> routeController.removeFromRouteForm(frame));
        displayLoc.addActionListener(e -> routeController.showLocationOnRoute(frame));
        displayVeh.addActionListener(e -> routeController.showVehicleOnRoute(frame));
        backButton.addActionListener(e -> mainMenuPanel());

        panel.add(center(title));
        panel.add(center(createRoute));
        panel.add(center(viewRoute));
        panel.add(center(addLoc));
        panel.add(center(removeLoc));
        panel.add(center(displayLoc));
        panel.add(center(displayVeh));
        panel.add(center(backButton));

        panel.add(Box.createVerticalGlue());

        mainPanel.add(panel);
        mainPanel.revalidate();
        mainPanel.repaint();

    }

    private void simulationPanel(Simulation newSim) {
        mainPanel.removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());

        JLabel title = new JLabel("Simulation Menu");
        JButton showQueue = new JButton("Display Next Events");
        JButton runNext = new JButton("Run Next Event");
        JButton runForTime = new JButton("Run For A Set Time");
        JButton viewStatus = new JButton("Show Simulation Status");
        JButton createHazard = new JButton("Create a Hazard");
        JButton removeHazard = new JButton("Remove a Hazard");
        JButton backButton = new JButton("Back");

        showQueue.addActionListener(e -> simulationController.showQueue(frame, newSim));
        runNext.addActionListener(e -> simulationController.runNext(frame, newSim));
        runForTime.addActionListener(e -> simulationController.runForTime(frame, newSim));
        viewStatus.addActionListener(e -> simulationController.showVehicleStatus(frame, newSim));
        createHazard.addActionListener(e -> simulationController.createHazard(frame));
        removeHazard.addActionListener(e -> simulationController.removeHazard(frame));
        backButton.addActionListener(e -> mainMenuPanel());

        panel.add(center(title));
        panel.add(center(showQueue));
        panel.add(center(runNext));
        panel.add(center(runForTime));
        panel.add(center(viewStatus));
        panel.add(center(viewStatus));
        panel.add(center(createHazard));
        panel.add(center(removeHazard));
        panel.add(center(backButton));
        panel.add(Box.createVerticalGlue());

        mainPanel.add(panel);
        mainPanel.revalidate();
        mainPanel.repaint();

    }


    public static void main(String[] args) {
        new TransitSimulator();

    }




}