import java.util.ArrayList;
import java.util.List;

class App {
    public static void main(String args[]) throws Exception{
        Meets meetURLS = new Meets();
        Scraper scraper = new Scraper();
        WriteToFile write = new WriteToFile();
        List<String> urls = meetURLS.getMeets();
        ArrayList<MeetResults> results = scraper.scrapeMeets(urls, 10);
        write.openFile();
        write.addRecords(results);
        write.closeFile();
    }
}
