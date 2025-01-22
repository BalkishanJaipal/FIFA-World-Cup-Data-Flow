package com.webscraper.service;


import com.webscraper.models.WorldCupMatch;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WikipediaScraperService {
    private static final String WIKI_URL = "https://en.wikipedia.org/wiki/List_of_FIFA_World_Cup_finals";

    public List<WorldCupMatch> scrapeWorldCupData() throws IOException {
        List<WorldCupMatch> matches = new ArrayList<>();

        // Connect to Wikipedia
        Document doc = Jsoup.connect(WIKI_URL).get();
        //System.out.println(doc.outerHtml());

        // Select the table - adjust selector based on actual page structure
        Element table = doc.select("table.sortable.plainrowheaders.wikitable").first();
        if (table == null) {
            throw new IOException("Table not found");
        }

        // Get rows
        Elements rows = table.select("tr");

        // Skip header row and process next 10 rows
        for (int i = 1; i <= 10 && i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements columns = row.select("td");

            if (columns.size() >= 4) {
                WorldCupMatch match = WorldCupMatch.builder()
                        .year(columns.get(0).text().trim())
                        .winner(columns.get(1).text().trim())
                        .score(columns.get(2).text().trim())
                        .runnerUp(columns.get(3).text().trim())
                        .build();

                matches.add(match);
            }
        }

        return matches;
    }
}
