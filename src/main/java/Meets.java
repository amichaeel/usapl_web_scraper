import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Meets {
    public List<String> getMeets() throws IOException {
        List<String> urls = new ArrayList<String>();
        try {
            Document doc = Jsoup.connect("https://www.usapl.liftingdatabase.com/competitions").get();
            for (Element row : doc.select("table.tabledata > tbody > tr > td > a")) {
                if (row.attr("href").contains("competitions-view")) {
                    String url = "https://usapl.liftingdatabase.com/" + row.attr("href");
                    urls.add(url);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Meets retrieved.");
        return urls;
    }
}

