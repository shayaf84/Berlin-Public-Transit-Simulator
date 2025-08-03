public enum VehicleStatus {
    MOVING {

        public String toString() {
            return "currently moving";
        }
    },
    WAITING {

        public String toString() {
            return "just about to pull away from";
        }
    }
}