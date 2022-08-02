import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Scraper {
    public List<Object> scrapeMeets(List<String> urls, int amount) throws Exception {
        List<Object> data = new ArrayList<Object>();
        int i = 1;
        for (String meet : urls) {
            if (i == amount + 1) break;
            MeetResults results = new MeetResults();
            try {
                Document doc = Jsoup.connect(meet).get();

                String[] urlSplit = meet.split("=", 2);
                results.meetID = urlSplit[1];
                results.meetName = doc.select("#content > h3:nth-child(3)").text();
                results.meetDate = doc.select("#content > table:nth-child(4) > tbody > tr:nth-child(1) > td").toString();
                results.sanctionNumber = doc.select("#content > table:nth-child(4) > tbody > tr:nth-child(2) > td").toString();
                results.meetState = doc.select("#content > table:nth-child(4) > tbody > tr:nth-child(3) > td").toString();
                System.out.println("Processing meet: " + results.meetName + " || " + i + " out of " + amount);

                Elements table = doc.select("#competition_view_results > tbody > tr");
                for (Element row : table) {
                    if (!row.select("th").isEmpty()){
                        if (!row.select("tr > th.competition_view_event").isEmpty())
                            results.event = row.select("tr > th.competition_view_event").text().trim();
                        else {
                            /* Obtain lifter sex */
                            String sexSplit[] = row.select("th").text().split("-");
                            results.sex = sexSplit[0];

                            /* Equipment Conditional */
                            String equip = row.select("th").text();
                            if (equip.contains("Raw With Wraps")) {
                                results.equipment = "Raw With Wraps";
                            } else if (equip.contains("Raw")) {
                                results.equipment = "Raw";
                            } else if (equip.contains("Equipped")) {
                                results.equipment = "Equipped";
                            } else if (equip.isEmpty()) {
                                results.equipment = "Unknown";
                            }

                            /* Division Conditional */
                            String division[] = row.select("th").text().split("-");
                            if (division[1].contains("Raw with Wraps")) {
                                // Split string after wraps and return all values afterwords
                                String[] split = division[1].split("(?<=Wraps) ");
                                results.division = split[1];
                            } else if (division[1].contains("Raw")) {
                                String[] split = division[1].split("(?<=Raw) ");
                                results.division = split[1];
                            } else if (division[1].contains("Equipped")) {
                                String [] split = division[1].split("(?<=Eqiupped) ");
                                results.division = split[1];
                            }
                         }
                    } else {
                        results.weightclass = row.select("td:nth-of-type(1)").text();
                        results.placing = row.select("td:nth-of-type(2)").text();
                        // Parse lifer ID from string
                        results.lifterID = row.select("td:nth-of-type(3)").attr("id");
                        results.name = row.select("td:nth-of-type(3)").text();
                        results.yob = row.select("td:nth-of-type(4)").text();
                        if (!row.select("td:nth-of-type(5)").text().isEmpty())
                            results.team = row.select("td:nth-of-type(5)").text();
                        results.lifterState = row.select("td:nth-of-type(6)").text();
                        results.lotNumber = parseInt(row.select("td:nth-of-type(7)").text());
                        results.weight = Double.parseDouble(row.select("td:nth-of-type(8)").text());
                        if (!row.select("td:nth-of-type(9)").text().isEmpty())
                            results.squat1 = Double.parseDouble(row.select("td:nth-of-type(9)").text());
                        if (!row.select("td:nth-of-type(10)").text().isEmpty())
                            results.squat2 = Double.parseDouble(row.select("td:nth-of-type(10)").text());
                        if (!row.select("td:nth-of-type(11)").text().isEmpty())
                            results.squat3 = Double.parseDouble(row.select("td:nth-of-type(11)").text());
                        if (!row.select("td:nth-of-type(12)").text().isEmpty())
                            results.bench1 = Double.parseDouble(row.select("td:nth-of-type(12)").text());
                        if (!row.select("td:nth-of-type(13)").text().isEmpty())
                            results.bench2 = Double.parseDouble(row.select("td:nth-of-type(13)").text());
                        if (!row.select("td:nth-of-type(14)").text().isEmpty())
                            results.bench3 = Double.parseDouble(row.select("td:nth-of-type(14)").text());
                        if (!row.select("td:nth-of-type(15)").text().isEmpty())
                            results.deadlift1 = Double.parseDouble(row.select("td:nth-of-type(15)").text());
                        if (!row.select("td:nth-of-type(16)").text().isEmpty())
                            results.deadlift2 = Double.parseDouble(row.select("td:nth-of-type(16)").text());
                        if (!row.select("td:nth-of-type(17)").text().isEmpty())
                            results.deadlift3 = Double.parseDouble(row.select("td:nth-of-type(17)").text());
                        if (!row.select("td:nth-of-type(18)").text().isEmpty())
                            results.total = Double.parseDouble(row.select("td:nth-of-type(18)").text());
                        if (!row.select("td:nth-of-type(19)").text().isEmpty())
                            results.points = Double.parseDouble(row.select("td:nth-of-type(19)").text());
                        if (!row.select("td:nth-of-type(20)").text().isEmpty())
                            results.bppoints = Double.parseDouble(row.select("td:nth-of-type(20)").text());
                        if (!row.select("td:nth-of-type(21)").text().isEmpty())
                            results.drugTested = row.select("td:nth-of-type(21)").text();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            data.add(results);
            i++;
        }
        return data;
    }

    @Override
    public String toString() {
        return "Scraper [getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
                + "]";
    }
}
