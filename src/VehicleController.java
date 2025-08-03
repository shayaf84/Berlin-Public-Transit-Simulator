import java.awt.Component;
import java.util.ArrayList;
import javax.swing.*;


public class VehicleController {

    public void newVehicleForm(JFrame parentFrame) {
        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();
        JTextField capacityField = new JTextField();
        JTextField numRidersField = new JTextField();
        JComboBox<VehicleType> typeCombo = new JComboBox<>(VehicleType.values());
        JTextField speedField = new JTextField();
        JComboBox<String> directionCombo = new JComboBox<>(new String[]{"Forward","Reverse"});

        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        idField.setAlignmentX(Component.LEFT_ALIGNMENT);
        capacityField.setAlignmentX(Component.LEFT_ALIGNMENT);
        numRidersField.setAlignmentX(Component.LEFT_ALIGNMENT);
        typeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        speedField.setAlignmentX(Component.LEFT_ALIGNMENT);
        directionCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("  Vehicle Name: "));
        panel.add(nameField);
        panel.add(new JLabel("  Vehicle ID:"));
        panel.add(idField);
        panel.add(new JLabel("  Max Capacity:"));
        panel.add(capacityField);
        panel.add(new JLabel("  Current Riders:"));
        panel.add(numRidersField);
        panel.add(new JLabel("  Vehicle Type:"));
        panel.add(typeCombo);
        panel.add(new JLabel("  Speed (km/h):"));
        panel.add(speedField);
        panel.add(new JLabel("  Direction:"));
        panel.add(directionCombo);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Create New Vehicle", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().strip();
                String id = Vehicle.publicCheckId(idField.getText().strip());
                String capacity = Vehicle.publicCheckCapacity(capacityField.getText().strip());
                String numRiders = Vehicle.publicCheckCapacity(numRidersField.getText().strip());
                VehicleType type = (VehicleType) typeCombo.getSelectedItem();
                String speed = Vehicle.publicCheckSpeed(speedField.getText().strip());
                String direction;
                if (directionCombo.getSelectedIndex() == 0) {
                    direction = "1";
                } else {
                    direction = "0";
                }

                Vehicle vehicle = new Vehicle(name, id, capacity, numRiders, type, speed, direction);
                JOptionPane.showMessageDialog(parentFrame, "Vehicle Created:\n" + vehicle);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
            }
        }

    }

    public void showVehicles(JFrame parentFrame) {
        ArrayList<Vehicle> vehicles = Vehicle.getAllVehicles();

        String[] columnNames = {"Name", "ID", "Capacity", "Riders", "Type", "Speed", "Direction"};
        String[][] toDisplay = new String[vehicles.size()][columnNames.length];
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle v = vehicles.get(i);
            toDisplay[i][0] = v.getName();
            toDisplay[i][1] = v.getVehicleID();
            toDisplay[i][2] = String.valueOf(v.getCapacity());
            toDisplay[i][3] = String.valueOf(v.getNumRiders());
            toDisplay[i][4] = v.getVehicleType().name();
            toDisplay[i][5] = String.format("%.2f", v.getSpeed());
            if (v.getDirection()) {
                toDisplay[i][6] = "Forward";
            } else {
                toDisplay[i][6] = "Reverse";
            }

        }
        JTable tableToShow = new JTable(toDisplay, columnNames);
        JScrollPane scrollPane = new JScrollPane(tableToShow);
        tableToShow.setFillsViewportHeight(true);

        JOptionPane.showMessageDialog(parentFrame, scrollPane);
    }

    public void assignVehicleForm(JFrame parentFrame) {
        ArrayList<Vehicle> vehicles = Vehicle.getAllVehicles();
        ArrayList<Route> routes = Route.getAllRoutes();

        if (routes.size() == 0) {
            JOptionPane.showMessageDialog(parentFrame, "No routes exist");
            return;
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JComboBox<Route> routeCombo = new JComboBox<>(routes.toArray(new Route[0]));
        JComboBox<Vehicle> vehicleCombo = new JComboBox<>();
        JComboBox<Location> locationCombo = new JComboBox<>();

        routeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        vehicleCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        locationCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        routeCombo.addActionListener(e -> {
            Route choice = (Route) routeCombo.getSelectedItem();

            vehicleCombo.removeAllItems();
            locationCombo.removeAllItems();

            VehicleType type = choice.getVehicleType();
            for (Vehicle v : vehicles) {
                if (type == v.getVehicleType()) {
                    vehicleCombo.addItem(v);
                }
            }

            for (Location l : choice.getStops()) {
                locationCombo.addItem(l);
            }
        });
        routeCombo.setSelectedIndex(0);

        panel.add(new JLabel("  Select Route:"));
        panel.add(routeCombo);
        panel.add(new JLabel("  Select Vehicle:"));
        panel.add(vehicleCombo);
        panel.add(new JLabel("  Select Location:"));
        panel.add(locationCombo);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Assign Vehicle to Route", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Route chosenRoute = (Route) routeCombo.getSelectedItem();
            Location chosenLocation = (Location) locationCombo.getSelectedItem();
            Vehicle chosenVehicle = (Vehicle) vehicleCombo.getSelectedItem();

            try {
                if (chosenVehicle.getRoute() != null) {
                    throw new IllegalArgumentException("Vehicle already assigned to route");
                }
                chosenRoute.publicVehicleTypeValidity(chosenVehicle);
                if (!chosenRoute.getStops().contains(chosenLocation)) {
                    throw new IllegalArgumentException("Location does not exist on route");
                }

                int index = chosenRoute.getIndexbyLocationforRoute(chosenLocation);
                chosenVehicle.setRouteIndex(index);
                chosenVehicle.designateVehiclePosition(chosenLocation, chosenRoute);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
            }
        }



    }

}