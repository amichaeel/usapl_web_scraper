import java.util.ArrayList;
import java.util.Formatter;

public class WriteToFile {

    private Formatter x;

    public void openFile() {
        try {
            x = new Formatter("data/results.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRecords(ArrayList<MeetResults> data) {
        x.format("meetID, meetName, meetDate, meetState, sanctionNumber, event, sex, equipment, division, weightClass, placing, lifterID, name, yob, team, lifterState, lotNumber, weight, squat1, squat2, squat3, bench1, bench2, bench3, deadlift1, deadlift2, deadlift3, total, points, bppoints, drugTested\n");
        for (MeetResults lifter : data) {
            x.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s\n",
                    lifter.meetInfo(),
                    lifter.event(),
                    lifter.sex(),
                    lifter.equipment(),
                    lifter.division(),
                    lifter.weightClass(),
                    lifter.placing(),
                    lifter.lifterID(),
                    lifter.name(),
                    lifter.yob(),
                    lifter.team(),
                    lifter.lifterState(),
                    lifter.lotNumber(),
                    lifter.weight(),
                    lifter.squats(),
                    lifter.benches(),
                    lifter.deadlifts(),
                    lifter.total(),
                    lifter.points(),
                    lifter.bppoints(),
                    lifter.drugTested());
        }
    }

    public void closeFile() {
        x.close();
    }
}
