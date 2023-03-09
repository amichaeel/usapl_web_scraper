public record MeetInfo(String id, String name, String date, String state, String sanctionNum) {
    @Override
    public String toString() {
        var strings = new String[]{id, name, date, state, sanctionNum};

        return String.join(", ", strings);
    }
}
