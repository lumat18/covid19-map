package com.maps;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;

@Service
public class Covid19Confirmed {

    private DataRepository dataRepository;

    private static final String COVID_CONFIRMED_URL =
            "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";

    public Covid19Confirmed(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void get() throws IOException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        final String response = restTemplate.getForObject(COVID_CONFIRMED_URL, String.class);

        StringReader stringReader = new StringReader(response);
        CSVFormat cSVFormat;
        CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader);

        for (CSVRecord record : csvParser) {
            double lat = Double.parseDouble(record.get("Lat"));
            double lon = Double.parseDouble(record.get("Long"));
            String numberOfCases = record.get("3/15/20");

            if (Integer.valueOf(numberOfCases) > 0) {
                String country = record.get("Country/Region");
                dataRepository.addPoint(new Point(lat, lon, country + " : " + numberOfCases));
            }

        }
    }
}
