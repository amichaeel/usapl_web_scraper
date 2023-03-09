public record Vector3(double first, double second, double third) {
    @Override
    public String toString() {
        return first + ", " + second + ", " + third;
    }
}
