public class MeetResults {
    String meetID;
    String meetName;
    String meetDate;
    String meetState;
    String sanctionNumber;
    String event;
    String sex;
    String equipment;
    String division;
    String weightClass;
    String placing;
    String lifterID;
    String name;
    String yob;
    String team;
    String lifterState;
    int lotNumber;
    double weight;
    double squat1, squat2, squat3;
    double bench1, bench2, bench3;
    double deadlift1, deadlift2, deadlift3;
    double total;
    double points;
    double bppoints;
    String drugTested;

    public MeetResults(String meetID, String meetName, String meetDate, String meetState, String sanctionNumber, String event, String sex, String equipment, String division, String weightClass, String placing, String lifterID, String name, String yob, String team, String lifterState, int lotNumber, double weight, double squat1, double squat2, double squat3, double bench1, double bench2, double bench3, double deadlift1, double deadlift2, double deadlift3, double total, double points, double bppoints, String drugTested) {
        this.meetID = meetID;
        this.meetName = meetName;
        this.meetDate = meetDate;
        this.meetState = meetState;
        this.sanctionNumber = sanctionNumber;
        this.event = event;
        this.sex = sex;
        this.equipment = equipment;
        this.division = division;
        this.weightClass = weightClass;
        this.placing = placing;
        this.lifterID = lifterID;
        this.name = name;
        this.yob = yob;
        this.team = team;
        this.lifterState = lifterState;
        this.lotNumber = lotNumber;
        this.weight = weight;
        this.squat1 = squat1;
        this.squat2 = squat2;
        this.squat3 = squat3;
        this.bench1 = bench1;
        this.bench2 = bench2;
        this.bench3 = bench3;
        this.deadlift1 = deadlift1;
        this.deadlift2 = deadlift2;
        this.deadlift3 = deadlift3;
        this.total = total;
        this.points = points;
        this.bppoints = bppoints;
        this.drugTested = drugTested;
    }

}


