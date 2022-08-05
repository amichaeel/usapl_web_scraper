import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Scraper {
    public ArrayList<MeetResults> scrapeMeets(List<String> urls, int amount) throws Exception {
        ArrayList<MeetResults> records = new ArrayList<MeetResults>();
        int i = 1;
        for (String meet : urls) {
            if (i == amount + 1) break;
            try {
                Document doc = Jsoup.connect(meet).get();
                String[] urlSplit = meet.split("=", 2);
                String meetID = urlSplit[1];
                String meetName = doc.select("#content > h3:nth-child(3)").text();
                String meetDate = doc.select("#content > table:nth-child(4) > tbody > tr:nth-child(1) > td").text();
                String sanctionNumber = doc.select("#content > table:nth-child(4) > tbody > tr:nth-child(2) > td").text();
                String meetState = doc.select("#content > table:nth-child(4) > tbody > tr:nth-child(3) > td").text();
                String event = "";
                String sex = "";
                String equipment = "";
                String division = "";
                String weightClass = "";
                String placing = "";
                String lifterID = "";
                String name = "";
                String yob = "";
                String team = "";
                String lifterState = "";
                int lotNumber = 0;
                double weight = 0;
                double squat1 = 0, squat2 = 0, squat3 = 0;
                double bench1 = 0, bench2 = 0, bench3 = 0;
                double deadlift1 = 0, deadlift2 = 0, deadlift3 = 0;
                double total = 0;
                double points = 0;
                double bppoints = 0;
                String drugTested = "";
                Elements table = doc.select("#competition_view_results > tbody > tr");
                System.out.println("Processing meet: " + meetName + " || " + i + " out of " + amount);
                for (Element row : table) {

                    if (!row.select("th").isEmpty()){
                        if (!row.select("tr > th.competition_view_event").isEmpty())
                            event = row.select("tr > th.competition_view_event").text().trim();
                        else {
                            /* Obtain lifter sex */
                            String[] sexSplit = row.select("th").text().split("-");
                            sex = sexSplit[0];

                            /* Equipment Conditional */
                            String equip = row.select("th").text();
                            if (equip.contains("Raw With Wraps")) {
                                equipment = "Raw With Wraps";
                            } else if (equip.contains("Raw")) {
                                equipment = "Raw";
                            } else if (equip.contains("Equipped")) {
                                equipment = "Equipped";
                            } else if (equip.isEmpty()) {
                                equipment = "Unknown";
                            }

                            /* Division Conditional */
                            String[] divisionSplit = row.select("th").text().split("-");
                            if (divisionSplit[1].contains("Raw with Wraps")) {
                                // Split string after wraps and return all values afterwords
                                String[] split = divisionSplit[1].split("(?<=Wraps) ");
                                division = split[1];
                            } else if (divisionSplit[1].contains("Raw")) {
                                String[] split = divisionSplit[1].split("(?<=Raw) ");
                                division = split[1];
                            } else if (divisionSplit[1].contains("Equipped")) {
                                String[] split = divisionSplit[1].split("(?<=Eqiupped) ");
                                division = split[1];
                            }
                         }
                    } else {
                        weightClass = row.select("td:nth-of-type(1)").text();
                        placing = row.select("td:nth-of-type(2)").text();
                        // Parse lifer ID from string
                        String[] lifterIDSplit = row.select("td:nth-of-type(3)").attr("id").split("(?<=_)");
                        lifterID = lifterIDSplit[1];
                        name = row.select("td:nth-of-type(3)").text();
                        yob = row.select("td:nth-of-type(4)").text();
                        if (!row.select("td:nth-of-type(5)").text().isEmpty())
                            team = row.select("td:nth-of-type(5)").text();
                        else
                            team = "";
                        lifterState = row.select("td:nth-of-type(6)").text();
                        lotNumber = parseInt(row.select("td:nth-of-type(7)").text());
                        weight = Double.parseDouble(row.select("td:nth-of-type(8)").text());
                        if (!row.select("td:nth-of-type(9)").text().isEmpty())
                            squat1 = Double.parseDouble(row.select("td:nth-of-type(9)").text());
                        if (!row.select("td:nth-of-type(10)").text().isEmpty())
                            squat2 = Double.parseDouble(row.select("td:nth-of-type(10)").text());
                        if (!row.select("td:nth-of-type(11)").text().isEmpty())
                            squat3 = Double.parseDouble(row.select("td:nth-of-type(11)").text());
                        if (!row.select("td:nth-of-type(12)").text().isEmpty())
                            bench1 = Double.parseDouble(row.select("td:nth-of-type(12)").text());
                        if (!row.select("td:nth-of-type(13)").text().isEmpty())
                            bench2 = Double.parseDouble(row.select("td:nth-of-type(13)").text());
                        if (!row.select("td:nth-of-type(14)").text().isEmpty())
                            bench3 = Double.parseDouble(row.select("td:nth-of-type(14)").text());
                        if (!row.select("td:nth-of-type(15)").text().isEmpty())
                            deadlift1 = Double.parseDouble(row.select("td:nth-of-type(15)").text());
                        if (!row.select("td:nth-of-type(16)").text().isEmpty())
                            deadlift2 = Double.parseDouble(row.select("td:nth-of-type(16)").text());
                        if (!row.select("td:nth-of-type(17)").text().isEmpty())
                            deadlift3 = Double.parseDouble(row.select("td:nth-of-type(17)").text());
                        if (!row.select("td:nth-of-type(18)").text().isEmpty())
                            total = Double.parseDouble(row.select("td:nth-of-type(18)").text());
                        if (!row.select("td:nth-of-type(19)").text().isEmpty())
                            points = Double.parseDouble(row.select("td:nth-of-type(19)").text());
                        if (!row.select("td:nth-of-type(20)").text().isEmpty())
                            bppoints = Double.parseDouble(row.select("td:nth-of-type(20)").text());
                        drugTested = row.select("td:nth-of-type(21)").text().trim();

                        MeetResults lifterData = new MeetResults(meetID, meetName, meetDate, meetState, sanctionNumber, event, sex, equipment, division, weightClass, placing, lifterID, name, yob, team, lifterState, lotNumber, weight, squat1, squat2, squat3, bench1, bench2, bench3, deadlift1, deadlift2, deadlift3, total, points, bppoints, drugTested);
                        records.add(lifterData);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;
        }
        return records;
    }
}
