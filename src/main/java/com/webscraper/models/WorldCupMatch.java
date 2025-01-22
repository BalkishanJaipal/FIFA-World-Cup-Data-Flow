package com.webscraper.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Builder
public class WorldCupMatch {
    private String year;
    private String winner;
    private String score;
    private String runnerUp;


}
