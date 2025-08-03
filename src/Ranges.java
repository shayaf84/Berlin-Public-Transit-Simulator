import java.util.Random;

public class Ranges {
    private int lower;
    private int higher;
    private Random rand;

    public Ranges(String lower, String higher) {
        check(lower, higher);
        rand = new Random();
    }

    private void check(String lower, String higher) {
        int l;
        int h;
        try {
            l = Integer.parseInt(lower);
            h = Integer.parseInt(higher);
        } catch (Exception e) {
            throw new IllegalArgumentException("Please enter valid integer");
        }

        if (l < 0 || h < 0) {
            throw new IllegalArgumentException("Please enter valid integers greater than 0");
        }

        if (l > h) {
            throw new IllegalArgumentException("Make sure that the lower range is not greater than the higher range.");
        }
        this.lower = l;
        this.higher = h;
    }

    public int uniformSelect() {
        return (rand.nextInt((higher - lower) + 1) + lower);
    }

    public void printRanges() {
        System.out.println("Lower: " + lower + " | Higher: " + higher);
    }
}