import java.awt.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import javax.swing.*;

public class SimulationController {
    public void showQueue(JFrame parentFrame, Simulation sim) {
        String[] columnNames = {"Time", "Vehicle", "Event Type", "Location"};
        PriorityQueue<Event> eventQueue = new PriorityQueue<>(sim.getEventQueue());
        String[][] toPrint = new String[eventQueue.size()][columnNames.length];

        int i = 0;
        while (!eventQueue.isEmpty()) {
            Event e = eventQueue.poll();
            toPrint[i][0] = String.valueOf(e.getEventTime());
            toPrint[i][1] = String.valueOf(e.getVehicle().getVehicleID());
            if (e instanceof DepartureEvent) {
                toPrint[i][2] = "Departure";
            } else {
                toPrint[i][2] = "Arrival";
            }
            toPrint[i][3] = String.valueOf(e.getCurrentLocation().getLocationID());
            i++;
        }
        JTable table = new JTable(toPrint, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        JOptionPane.showMessageDialog(parentFrame, scrollPane);

    }

    public void runNext(JFrame parentFrame, Simulation newSim) {
        try {
            String[] logs = newSim.runSingleIteration();
            JTextArea textArea = new JTextArea(String.join("\n", logs));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(parentFrame, scrollPane,
                    "Successfully processed the next event.\n\nCurrent time: " + newSim.getCurrentTime(), JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
        }
    }

    public void runForTime(JFrame parentFrame, Simulation newSim) {
        String input = JOptionPane.showInputDialog(parentFrame, "Enter the time you want the simulation to run for (minutes): ");
        if (input == null) {
            return;
        }
        try {
            if (input.isBlank()) throw new IllegalArgumentException("Empty argument, please enter a valid value");
            int time = Integer.parseInt(input);
            if (time <= 0) {
                throw new IllegalArgumentException("Time should be positive integer");
            }
            String[] logs = newSim.runForTime(time);
            JTextArea textArea = new JTextArea(String.join("\n", logs));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            JOptionPane.showMessageDialog(parentFrame, scrollPane, "Simulation ran for " + time + "minutes. \nCurrent time: " + newSim.getCurrentTime(), JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
        }
    }

    public void showVehicleStatus(JFrame parentFrame, Simulation newSim) {
        ArrayList<String> status = newSim.printStatus();
        JTextArea textArea = new JTextArea(String.join("\n\n", status));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JOptionPane.showMessageDialog(parentFrame, scrollPane);
    }

    public void createHazard(JFrame parentFrame) {
        ArrayList<Route> routes = Route.getAllRoutes();

        if (routes.size() == 0) {
            JOptionPane.showMessageDialog(parentFrame, "There are no routes. Add some first");
            return;
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JComboBox<Route> routeCombo = new JComboBox<>(routes.toArray(new Route[0]));
        JComboBox<Location> locationCombo = new JComboBox<>();
        JComboBox<String> relLocCombo = new JComboBox<>(new String[]{"Before", "At", "After"});
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Short", "Long"});
        JTextField multiplier = new JTextField();
        JTextField name = new JTextField();

        routeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        locationCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        relLocCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        typeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        multiplier.setAlignmentX(Component.LEFT_ALIGNMENT);
        name.setAlignmentX(Component.LEFT_ALIGNMENT);

        routeCombo.addActionListener(e -> {
            Route choice = (Route) routeCombo.getSelectedItem();

            locationCombo.removeAllItems();

            for (Location l : choice.getStops()) {
                locationCombo.addItem(l);
            }
        });
        routeCombo.setSelectedIndex(0);

        panel.add(new JLabel("  Enter name of hazard: "));
        panel.add(name);
        panel.add(new JLabel("  Select route: "));
        panel.add(routeCombo);
        panel.add(new JLabel("  Select location: "));
        panel.add(locationCombo);
        panel.add(new JLabel("  Select where to place hazard relative to location: "));
        panel.add(relLocCombo);
        panel.add(new JLabel("  Select long or short term: "));
        panel.add(typeCombo);
        panel.add(new JLabel("  Enter severity multiplier of hazard: "));
        panel.add(multiplier);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Add hazard to location", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Route route = (Route) routeCombo.getSelectedItem();
                Location location = (Location) locationCombo.getSelectedItem();
                int locationIndex = route.getIndexbyLocationforRoute(location);
                Location nextLocation = route.getLocationbyIndexforRoute(route.getNextLocationIndex(locationIndex, true));
                Location prevLocation = route.getLocationbyIndexforRoute(route.getNextLocationIndex(locationIndex, false));
                double mult = Double.parseDouble(multiplier.getText().strip());
                String hazardType = name.getText().strip();
                int duration = (typeCombo.getSelectedItem() == "Short") ? 0 : 1;
                int hazardIndex = (relLocCombo.getSelectedItem() == "Before") ? 0 : (relLocCombo.getSelectedItem() == "At" ? 1 : 2);
                location.addHazard(hazardType, duration, mult, hazardIndex, prevLocation, nextLocation);
                JOptionPane.showMessageDialog(parentFrame, "Successfully added hazard to location");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
            }
        }
    }

    public void removeHazard(JFrame parentFrame) {
        ArrayList<Route> routes = Route.getAllRoutes();

        if (routes.size() == 0) {
            JOptionPane.showMessageDialog(parentFrame, "There are no routes. Add some first");
            return;
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JComboBox<Route> routeCombo = new JComboBox<>(routes.toArray(new Route[0]));
        JComboBox<Location> locationCombo = new JComboBox<>();
        JComboBox<String> relLocCombo = new JComboBox<>(new String[]{"Before", "At", "After"});

        routeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        locationCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        relLocCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        routeCombo.addActionListener(e -> {
            Route choice = (Route) routeCombo.getSelectedItem();

            locationCombo.removeAllItems();

            for (Location l : choice.getStops()) {
                locationCombo.addItem(l);
            }
        });
        routeCombo.setSelectedIndex(0);

        panel.add(new JLabel("  Select route: "));
        panel.add(routeCombo);
        panel.add(new JLabel("  Select location: "));
        panel.add(locationCombo);
        panel.add(new JLabel("  Select where to remove hazard relative to location: "));
        panel.add(relLocCombo);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Add hazard to location", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Route route = (Route) routeCombo.getSelectedItem();
                Location location = (Location) locationCombo.getSelectedItem();
                int locationIndex = route.getIndexbyLocationforRoute(location);
                Location nextLocation = route.getLocationbyIndexforRoute(route.getNextLocationIndex(locationIndex, true));
                Location prevLocation = route.getLocationbyIndexforRoute(route.getNextLocationIndex(locationIndex, false));
                int hazardIndex = (relLocCombo.getSelectedItem() == "Before") ? 0 : (relLocCombo.getSelectedItem() == "At" ? 1 : 2);
                location.removeHazard(hazardIndex, prevLocation, nextLocation);
                JOptionPane.showMessageDialog(parentFrame, "Successfully removed hazard to location");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame, "Error: " + ex.getMessage());
            }
        }
    }

}