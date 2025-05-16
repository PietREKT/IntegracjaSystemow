package com.example.IntegracjaSystemow.Houses;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;

@Service
@Log4j2
public class HouseService {


    private final HouseRepository houseRepository;

    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    private House createHouse(String city, Double pricePerMeter, String quarter, String year){
        House house = new House();
        house.setCity(city);
        house.setPrice_per_meter(pricePerMeter);

        LocalDate date = LocalDate.of(Integer.parseInt(year), 1, 1);
        if (quarter.contains("V")){
            date = date.plusMonths(11);
        } else {
            date = date.plusMonths(3L * quarter.length() - 1);
        }
        house.setDate(date);

        return houseRepository.save(house);
    }

    //Apartment prices in Poland in 2006-2017
    public void getFileByApi() throws FileNotFoundException {
        RestTemplate template = new RestTemplate();
        String fileUrl = "https://ckan.multimediagdansk.pl/dataset/a8a4ad16-6983-493c-9bdc-015e2f124f00/resource/3c3db3c5-0e38-400c-a827-7742eba6c864/download/ceny-ofertowe-i-transakcyjne-w-wybranych-miastach-arkusz1.csv";

        ResponseEntity<byte[]> response = template.exchange(
                fileUrl,
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                byte[].class
        );

        byte[] file = response.getBody();
        if (file == null){
            throw new FileNotFoundException("Error occurred while getting the file");
        }

        try (InputStream inputStream = new ByteArrayInputStream(file)){
            CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            csvReader.readNext();
            String[] row = null;
            int counter = 0;
            while ((row = csvReader.readNext()) != null) {
                if (row[3].isEmpty()){
                    log.warn("No price info provided: {}", Arrays.toString(row));
                    continue;
                }
                String[] dateComponents = row[0].split("\\s+");
                createHouse(row[1], Double.parseDouble(row[3]), dateComponents[0], dateComponents[1]);
                counter++;
            }
            log.info("Total rows added: {}", counter);
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }


}
