import java.util.List;

class App {
    public static void main(String args[]) throws Exception{
        Meets meetURLS = new Meets();
        Scraper scraper = new Scraper();
        List<String> urls = meetURLS.getMeets();
        scraper.scrapeMeets(urls, 5);
    }
}
