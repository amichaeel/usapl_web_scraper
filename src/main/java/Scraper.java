import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scraper {
    public ArrayList<MeetResults> scrapeMeets(List<String> urls, int amount) {
        ArrayList<MeetResults> records = new ArrayList<>();
        int i = 1;
        for (String meet : urls) {
            if (i == amount + 1) break;
            try {
                Document doc = Jsoup.connect(meet).get();
                MeetInfo info = new MeetInfo(
                        meet.split("=", 2)[1],
                        doc.select("#content > h3:nth-child(3)").text(),
                        doc.select("#content > table:nth-child(4) > tbody > tr:nth-child(1) > td").text(),
                        doc.select("#content > table:nth-child(4) > tbody > tr:nth-child(3) > td").text(),
                        doc.select("#content > table:nth-child(4) > tbody > tr:nth-child(2) > td").text()
                );

                System.out.println("Processing meet: " + info.name() + " || " + i + " out of " + amount);
                getRecordsFromMeet(records, doc, info);
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;
        }
        return records;
    }

    private void getRecordsFromMeet(ArrayList<MeetResults> records, Document doc, MeetInfo info) throws IOException {
        String event = "";
        String sex = "";
        String equipment = "";
        String division = "";
        String weightClass;
        String placing;
        String lifterID;
        String name;
        String yob;
        String team;
        String lifterState;
        int lotNumber;
        double weight;
        double total;
        double points;
        double bppoints;
        String drugTested;
        Elements table = doc.select("#competition_view_results > tbody > tr");
        for (Element row : table) {
            if (!row.select("th").isEmpty()) {
                if (!row.select("tr > th.competition_view_event").isEmpty()) {
                    event = row.select("tr > th.competition_view_event").text().trim();
                } else {
                    /* Obtain lifter sex */
                    sex = row.select("th").text().split("-")[0];
                    equipment = equipmentConditional(row);
                    division = divisionConditional(row);
                }
            } else {
                weightClass = row.select("td:nth-of-type(1)").text();
                placing = row.select("td:nth-of-type(2)").text();
                // Parse lifer ID from string
                String[] lifterIDSplit = row.select("td:nth-of-type(3)").attr("id").split("(?<=_)");
                lifterID = lifterIDSplit[1];
                name = row.select("td:nth-of-type(3)").text();
                yob = row.select("td:nth-of-type(4)").text();
                team = row.select("td:nth-of-type(5)").text();
                lifterState = row.select("td:nth-of-type(6)").text();
                lotNumber = (int) doubleFromRow(row, "td:nth-of-type(7)");
                weight = doubleFromRow(row, "td:nth-of-type(8)");
                Vector3 squats = new Vector3(
                        doubleFromRow(row, "td:nth-of-type(9)"),
                        doubleFromRow(row, "td:nth-of-type(10)"),
                        doubleFromRow(row, "td:nth-of-type(11)")
                );
                Vector3 benches = new Vector3(
                        doubleFromRow(row, "td:nth-of-type(12)"),
                        doubleFromRow(row, "td:nth-of-type(13)"),
                        doubleFromRow(row, "td:nth-of-type(14)")
                );
                Vector3 deadlifts = new Vector3(
                        doubleFromRow(row, "td:nth-of-type(15)"),
                        doubleFromRow(row, "td:nth-of-type(16)"),
                        doubleFromRow(row, "td:nth-of-type(17)")
                );
                total = doubleFromRow(row, "td:nth-of-type(18)");
                points = doubleFromRow(row, "td:nth-of-type(19)");
                bppoints = doubleFromRow(row, "td:nth-of-type(20)");
                drugTested = row.select("td:nth-of-type(21)").text().trim();

                MeetResults lifterData = new MeetResults(
                        info, event, sex, equipment,
                        division, weightClass, placing, lifterID,
                        name, yob, team, lifterState, lotNumber,
                        weight, squats, benches, deadlifts,
                        total, points, bppoints, drugTested
                );
                records.add(lifterData);
            }
        }
    }

    private String equipmentConditional(Element row) {
        /* Equipment Conditional */
        String[] equip = row.select("th").text().split("-");

        if (equip.length > 1) {
            if (equip[1].contains("Raw With Wraps")) {
                return "Raw With Wraps";
            } else if (equip[1].contains("Raw")) {
                return "Raw";
            } else if (equip[1].contains("Equipped")) {
                return "Equipped";
            } else if (equip[1].isEmpty()) {
                return "Unknown";
            }
        }

        return "";
    }

    private String divisionConditional(Element row) {
        /* Division Conditional */
        String[] divisionSplit = row.select("th").text().split("-");

        if (divisionSplit.length > 1) {
            if (divisionSplit[1].contains("Raw with Wraps")) {
                // Split string after wraps and return all values afterwords
                String[] split = divisionSplit[1].split("(?<=Wraps) ");
                return split[1];
            } else if (divisionSplit[1].contains("Raw")) {
                String[] split = divisionSplit[1].split("(?<=Raw) ");
                return split[1];
            } else if (divisionSplit[1].contains("Equipped")) {
                String[] split = divisionSplit[1].split("(?<=Eqiupped) ");
                return split[1];
            }
        }

        return "";
    }

    private double doubleFromRow(Element row, String title) {
        if (!row.select(title).text().isEmpty())
            return Double.parseDouble(row.select(title).text());

        return 0.0;
    }
}
