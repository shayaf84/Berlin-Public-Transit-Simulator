import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class LocationController {
    public void newLocationForm(JFrame parentFrame) {
        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();
        JTextField xField = new JTextField();
        JTextField yField = new JTextField();
        JTextField numRidersField = new JTextField();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Location name: "));
        panel.add(nameField);
        panel.add(new JLabel("Location ID: "));
        panel.add(idField);
        panel.add(new JLabel("X-coordinate: "));
        panel.add(xField);
        panel.add(new JLabel("Y-coordinate: "));
        panel.add(yField);
        panel.add(new JLabel("Number of waiting passengers: "));
        panel.add(numRidersField);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Create New Location", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().strip();
                String id = Location.publicCheckId(idField.getText().strip());
                String x = Point.publicCheckX(xField.getText().strip());
                String y = Point.publicCheckY(yField.getText().strip());
                String numRidersWaiting = Location.publicCheckCapacity(numRidersField.getText().strip());

                Location location = new Location(name, id, x, y, numRidersWaiting);
                JOptionPane.showMessageDialog(parentFrame, "Location Created:\n" + location);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
            }
        }

    }

    public void showLocations(JFrame parentFrame) {
        ArrayList<Location> locations = Location.getAllLocations();

        String[] columnNames = {"Name", "ID", "X-Coordinate", "Y-Coordinate", "Waiting Passengers"};
        String[][] toDisplay = new String[locations.size()][columnNames.length];
        for (int i = 0; i < locations.size(); i++) {
            Location l = locations.get(i);
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

    }

    public void showVehiclesAtLocation(JFrame parentFrame) {
        if (Location.getLocationsLength() == 0) {
            JOptionPane.showMessageDialog(parentFrame, "No Locations Exist");
            return;
        }
        ArrayList<Location> locations = Location.getAllLocations();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JComboBox<Location> locationCombo = new JComboBox<>(locations.toArray(new Location[0]));
        locationCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(new JLabel("  Select Location: "));
        panel.add(locationCombo);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Show Vehicles", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Location chosenLocation = (Location) locationCombo.getSelectedItem();
            ArrayList<Vehicle> vehicles = chosenLocation.getCurrentVehicles();
            Collections.sort(vehicles);
            if (vehicles.isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame, "No vehicles at this location");
                return;
            }
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
    }

    public void changePassengerRanges(JFrame parentFrame) {
        if (Location.getLocationsLength() == 0) {
                JOptionPane.showMessageDialog(parentFrame, "No Locations Exist");
                return;
            }

            ArrayList<Location> locations = Location.getAllLocations();
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JComboBox<Location> locationCombo = new JComboBox<>(locations.toArray(new Location[0]));
            locationCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextField embLow = new JTextField(10);
            JTextField embHigh = new JTextField(10);
            JTextField transfLow = new JTextField(10);
            JTextField transfHigh = new JTextField(10);
            JTextField debLow = new JTextField(10);
            JTextField debHigh = new JTextField(10);

            panel.add(new JLabel("  Select Location:"));
            panel.add(locationCombo);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));

            panel.add(createLowHighPanel(
                "Enter embarking lower bound:", embLow,
                "Enter embarking upper bound:", embHigh));

            panel.add(Box.createRigidArea(new Dimension(0, 10)));

            panel.add(createLowHighPanel(
                "Enter transferring lower bound:", transfLow,
                "Enter transferring upper bound:", transfHigh));

            panel.add(Box.createRigidArea(new Dimension(0, 10)));

            panel.add(createLowHighPanel(
                "Enter debarking lower bound:", debLow,
                "Enter debarking upper bound:", debHigh));

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Add hazard to location", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Location location = (Location) locationCombo.getSelectedItem();
                String eLow = embLow.getText().strip();
                String eHigh = embHigh.getText().strip();
                String tLow = transfLow.getText().strip();
                String tHigh = transfHigh.getText().strip();
                String dLow = debLow.getText().strip();
                String dHigh = debHigh.getText().strip();
                location.changePassengerRange("embarking", new Ranges(eLow, eHigh));
                location.changePassengerRange("transferring", new Ranges(tLow, tHigh));
                location.changePassengerRange("debarking", new Ranges(dLow, dHigh));
                JOptionPane.showMessageDialog(parentFrame, "Successfully adjusted passenger ranges");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
            }
        }
    }

    private JPanel createLowHighPanel(String label1Text, JTextField field1,
                                    String label2Text, JTextField field2) {
        JPanel pairPanel = new JPanel();
        pairPanel.setLayout(new BoxLayout(pairPanel, BoxLayout.X_AXIS));
        pairPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(new JLabel(label1Text));
        leftPanel.add(field1);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(new JLabel(label2Text));
        rightPanel.add(field2);

        pairPanel.add(leftPanel);
        pairPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        pairPanel.add(rightPanel);

        return pairPanel;
    }

}