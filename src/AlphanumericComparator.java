public class AlphanumericComparator {
    public static int compare(String s1, String s2) {
        int i = 0, j = 0;
        while (i < s1.length() && j < s2.length()) {
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(j);

            StringBuilder chunk1 = new StringBuilder();
            StringBuilder chunk2 = new StringBuilder();

            // Collect characters of same type
            while (i < s1.length() && Character.isDigit(c1) == Character.isDigit(s1.charAt(i))) {
                chunk1.append(s1.charAt(i++));
            }
            while (j < s2.length() && Character.isDigit(c2) == Character.isDigit(s2.charAt(j))) {
                chunk2.append(s2.charAt(j++));
            }

            String part1 = chunk1.toString();
            String part2 = chunk2.toString();

            int result;
            if (Character.isDigit(part1.charAt(0)) && Character.isDigit(part2.charAt(0))) {
                // Compare as numbers
                result = Integer.compare(Integer.parseInt(part1), Integer.parseInt(part2));
            } else {
                // Compare as strings
                result = part1.compareTo(part2);
            }

            if (result != 0) {
                return result;
            }
        }

        return Integer.compare(s1.length(), s2.length());
    }
}
