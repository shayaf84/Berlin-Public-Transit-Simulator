public class Point {

    private double x;
    private double y;

    public Point(String x, String y) {
        this.x = Double.parseDouble(checkX(x));
        this.y = Double.parseDouble(checkY(y));
    }

    private String checkX(String x) {
        if (!x.matches("^[+-]?\\d+$")) {
            throw new IllegalArgumentException("X-coordinate must be an integer");
        } else if (x == null || x.isEmpty()) {
            throw new IllegalArgumentException("X-coordinate cannot be null or empty");
        }
        return x;
    }

    private String checkY(String y) {
        if (!y.matches("^[+-]?\\d+$")) {
            throw new IllegalArgumentException("Y-coordinate must be an integer");
        } else if (y == null || y.isEmpty()) {
            throw new IllegalArgumentException("Y-coordinate cannot be null or empty");
        }
        return y;
    }

    public static String publicCheckX(String x) {
        if (!x.matches("^[+-]?\\d+$")) {
            throw new IllegalArgumentException("X-coordinate must be an integer");
        } else if (x == null || x.isEmpty()) {
            throw new IllegalArgumentException("X-coordinate cannot be null or empty");
        }
        return x;
    }

    public static String publicCheckY(String y) {
        if (!y.matches("^[+-]?\\d+$")) {
            throw new IllegalArgumentException("Y-coordinate must be an integer");
        } else if (y == null || y.isEmpty()) {
            throw new IllegalArgumentException("Y-coordinate cannot be null or empty");
        }
        return y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Distances between locations are in meters
    public static double calculateDistance(Point first, Point second) {
        double deltaX = first.x - second.x;
        double deltaY = first.y - second.y;
        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        return distance;
    }

    @Override
    public String toString() {
        return String.format("X: %.0f, Y: %.0f", x, y);
    }

}