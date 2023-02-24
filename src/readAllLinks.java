package webCrawler;
import java.io.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.json.simple.JsonObject;
import java.util.*;

public class readAllLinks {
    public static Set < String > uniqueURL = new HashSet < String > ();
    public static int _Counter;
    public static Elements links;
    public static ArrayList < String > AllLinks = new ArrayList < String > ();
    public static String my_site;
    public static void main(String[] args) {
        readAllLinks obj = new readAllLinks();
        obj.get_links("https://en.wikipedia.org/wiki/Help:Introduction", 1);
        obj.ProcessJSON();
    }

    private void get_links(String url, int validInteger) {
        try {
            _Counter = validInteger;
            if (validInteger != (int) validInteger)
                throw new ArithmeticException("Not a Integer");
            if (_Counter == 0) {
                System.out.println("Conter is Zero");
                return;
            }
            System.out.println("Connecting to chrome...");
            Document doc = Jsoup.connect(url).userAgent("chrome").get();
            links = doc.select("a");

            if (links.isEmpty())
                return;

            links.stream().map((link) -> link.attr("abs:href")).forEachOrdered((this_url) -> {
                if (_Counter == 0)
                    return;

                boolean add = uniqueURL.add(this_url);
                if (add && this_url.contains("wikipedia")) {
                    System.out.println(this_url);
                    _Counter--;
                    get_links(this_url, _Counter);}
            });
        } catch (Exception ex) {
            System.out.println("Catch exception found in get_links");
        }
    }
    public void ProcessJSON() {
        try {
            JsonObject WikiLinks = new JsonObject();
            links.stream().map((link) -> link.attr("abs:href")).forEachOrdered((this_url) -> {
                if (this_url != null && !this_url.trim().isEmpty())
                    AllLinks.add(this_url);
            });
            HashSet < String > hashSetNumbers = new HashSet < String > (AllLinks);
            WikiLinks.put("AllFoundLinks", AllLinks);
            WikiLinks.put("TotalCount", links.size());
            WikiLinks.put("UniqueCount", hashSetNumbers.size());
            FileWriter file = new FileWriter("Wiki.json");
            file.write(WikiLinks.toJson());
            file.flush();

        } catch (Exception e) {
            System.out.println("Exepction found while writing to JSON");
        }
    }
}