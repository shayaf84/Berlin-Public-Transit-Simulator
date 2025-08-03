import java.awt.Component;
import java.util.ArrayList;
import javax.swing.*;

public class RouteController {
    public void newRouteForm(JFrame parentFrame) {
        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();
        JComboBox<VehicleType> typeCombo = new JComboBox<>(VehicleType.values());

        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        idField.setAlignmentX(Component.LEFT_ALIGNMENT);
        typeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Route Name: "));
        panel.add(nameField);
        panel.add(new JLabel("Route ID:"));
        panel.add(idField);
        panel.add(new JLabel("Route Type:"));
        panel.add(typeCombo);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Create New Location", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().strip();
                String id = Route.publicCheckID(idField.getText().strip());
                VehicleType type = (VehicleType) typeCombo.getSelectedItem();

                Route route = new Route(name, id, type);
                JOptionPane.showMessageDialog(parentFrame, "Route Created:\n" + route);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
            }
        }
    }

    public void showRoutes(JFrame parentFrame) {
        ArrayList<Route> routes = Route.getAllRoutes();

        String[] columnNames = {"Name", "ID", "Type"};
        String[][] toDisplay = new String[routes.size()][columnNames.length];
        for (int i = 0; i < routes.size(); i++) {
            Route r = routes.get(i);
            toDisplay[i][0] = r.getName();
            toDisplay[i][1] = r.getRouteId();
            toDisplay[i][2] = r.getVehicleType().name();
        }
        JTable tableToShow = new JTable(toDisplay, columnNames);
        JScrollPane scrollPane = new JScrollPane(tableToShow);
        tableToShow.setFillsViewportHeight(true);
        JOptionPane.showMessageDialog(parentFrame, scrollPane);
    }


    public void addToRouteForm(JFrame parentFrame) {
        ArrayList<Location> locations = Location.getAllLocations();
        ArrayList<Route> routes = Route.getAllRoutes();

        if (locations.size() == 0) {
            JOptionPane.showMessageDialog(parentFrame, "There are no locations. Add some first.");
            return;
        }
        if (routes.size() == 0) {
            JOptionPane.showMessageDialog(parentFrame, "There are no routes. Add some first");
            return;
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JComboBox<Route> routeCombo = new JComboBox<>(routes.toArray(new Route[0]));
        JComboBox<Location> locationCombo = new JComboBox<>(locations.toArray(new Location[0]));
        JTextField indexField = new JTextField();

        routeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        locationCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        indexField.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(new JLabel("  Select route: "));
        panel.add(routeCombo);
        panel.add(new JLabel("  Select location: "));
        panel.add(locationCombo);
        panel.add(new JLabel("  Select index of route for location: "));
        panel.add(indexField);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Add location to Route", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                if (indexField.getText().isBlank()) throw new IllegalArgumentException("Empty argument, please enter a valid value");
                Route route = (Route) routeCombo.getSelectedItem();
                Location location = (Location) locationCombo.getSelectedItem();
                int index = Integer.parseInt(indexField.getText().strip());
                route.addStop(location, index);
                JOptionPane.showMessageDialog(parentFrame, "Successfully added location to route");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
            }
        }

    }

    public void showLocationOnRoute(JFrame parentFrame) {
        ArrayList<Route> routes = Route.getAllRoutes();

        if (routes.size() == 0) {
            JOptionPane.showMessageDialog(parentFrame, "There are no routes. Add some first");
            return;
        }
        JComboBox<Route> routeCombo = new JComboBox<>(routes.toArray(new Route[0]));
        routeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Choose Route to Display: "));
        panel.add(routeCombo);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Display Location on Route", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Route route = (Route) routeCombo.getSelectedItem();
                ArrayList<Location> stops = route.getStops();

                String[] columnNames = {"Name", "ID", "X-Coordinate", "Y-Coordinate", "Waiting Passengers"};
                String[][] toDisplay = new String[stops.size()][columnNames.length];
                for (int i = 0; i < stops.size(); i++) {
                    Location l = stops.get(i);
                    toDisplay[i][0] = l.getLocationName();
                    toDisplay[i][1] = l.getLocationID();
                    toDisplay[i][2] = String.valueOf(l.getPosition().getX());
                    toDisplay[i][3] = String.valueOf(l.getPosition().getY());
                    toDisplay[i][4] = String.valueOf(l.getNumRidersWaiting());


                }
                JTable tableToShow = new JTable(toDisplay, columnNames);
                JScrollPane scrollPane = new JScrollPane(tableToShow);
                tableToShow.setFillsViewportHeight(true);

                JOptionPane.showMessageDialog(parentFrame, scrollPane);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
            }
        }
    }

    public void removeFromRouteForm(JFrame parentFrame) {
        ArrayList<Route> routes = Route.getAllRoutes();
        if (routes.size() == 0) {
            JOptionPane.showMessageDialog(parentFrame, "No routes exist");
            return;
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JComboBox<Route> routeCombo = new JComboBox<>(routes.toArray(new Route[0]));
        JComboBox<Location> locationCombo = new JComboBox<>();

        routeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        locationCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        routeCombo.addActionListener(e -> {
            Route choice = (Route) routeCombo.getSelectedItem();

            locationCombo.removeAllItems();

            for (Location l : choice.getStops()) {
                locationCombo.addItem(l);
            }
        });
        routeCombo.setSelectedIndex(0);



        panel.add(new JLabel("  Select Route"));
        panel.add(routeCombo);
        panel.add(new JLabel("  Select Location to Remove"));
        panel.add(locationCombo);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Remove location from route", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Route chosenRoute = (Route) routeCombo.getSelectedItem();
            Location chosenLocation = (Location) locationCombo.getSelectedItem();
            if (chosenRoute == null || chosenLocation == null) {
                JOptionPane.showMessageDialog(parentFrame, "No stop selected", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int index = chosenRoute.getIndexbyLocationforRoute(chosenLocation);
                chosenRoute.removeVehiclesAtLocation(index);
                chosenRoute.removeStop(index);

                JOptionPane.showMessageDialog(parentFrame, "Successfully removed location from route");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
            }
        }



    }

        public void showVehicleOnRoute(JFrame parentFrame) {
        ArrayList<Route> routes = Route.getAllRoutes();

        if (routes.size() == 0) {
            JOptionPane.showMessageDialog(parentFrame, "There are no routes. Add some first");
            return;
        }
        JComboBox<Route> routeCombo = new JComboBox<>(routes.toArray(new Route[0]));
        routeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Choose Route to Display: "));
        panel.add(routeCombo);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Display Vehicle on Route", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Route route = (Route) routeCombo.getSelectedItem();
                ArrayList<Vehicle> vehicles = route.getVehiclesOnRoute();

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
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
            }
        }
    }

}