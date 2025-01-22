package com.webscraper.controller;

import com.webscraper.models.WorldCupMatch;
import com.webscraper.service.GoogleSheetsService;
import com.webscraper.service.WikipediaScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/worldcup")
public class WorldCupMatchController {

    @Value("${google.sheets.spreadsheet.id}")
    private String SPREADSHEET_ID;

    @Value("${google.sheets.range}")
    private String RANGE;

    private final WikipediaScraperService scraperService;
    private final GoogleSheetsService sheetsService;

    @Autowired
    public WorldCupMatchController(WikipediaScraperService scraperService, GoogleSheetsService sheetsService) {
        this.scraperService = scraperService;
        this.sheetsService = sheetsService;
    }

    @GetMapping("/matches")
    public ResponseEntity<List<WorldCupMatch>> getWorldCupMatches() {
        try {
            List<WorldCupMatch> matches = scraperService.scrapeWorldCupData();
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/update-sheets")
    public ResponseEntity<Map<String, String>> updateGoogleSheets() {
        Map<String, String> response = new HashMap<>();

        try {
            List<WorldCupMatch> matches = scraperService.scrapeWorldCupData();
            sheetsService.appendData(SPREADSHEET_ID, RANGE, matches);

            response.put("status", "success");
            response.put("message", "Successfully scraped and updated " + matches.size() + " matches");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
