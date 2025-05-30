package com.example.IntegracjaSystemow.Houses;

import com.example.IntegracjaSystemow.users.User;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
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

    private House createHouse(String city, Double pricePerMeter, String quarter, String year) {
        House house = new House();
        house.setCity(city);
        house.setPrice_per_meter(pricePerMeter);

        LocalDate date = LocalDate.of(Integer.parseInt(year), 1, 1);
        if (quarter.contains("V")) {
            date = date.plusMonths(11);
        } else {
            date = date.plusMonths(3L * quarter.length() - 1);
        }
        house.setDate(date);

        return house;
    }

    public House createUserHouse(HouseDto dto, User author){
        House house = createHouse(dto.getCity(), dto.getPrice_per_meter(), dto.getQuarter(), dto.getYear());
        house.setUser(author);
        house.setArea(dto.getArea());
        return houseRepository.save(house);
    }

    public void deleteHouse(String id){
        houseRepository.deleteById(Long.parseLong(id));
    }

    //Apartment prices in Poland in 2006-2017
    public void readFileFromApi() throws FileNotFoundException {
        RestTemplate template = new RestTemplate();
        String fileUrl = "https://ckan.multimediagdansk.pl/dataset/a8a4ad16-6983-493c-9bdc-015e2f124f00/resource/3c3db3c5-0e38-400c-a827-7742eba6c864/download/ceny-ofertowe-i-transakcyjne-w-wybranych-miastach-arkusz1.csv";

        ResponseEntity<byte[]> response = template.exchange(
                fileUrl,
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                byte[].class
        );

        byte[] file = response.getBody();
        if (file == null) {
            throw new FileNotFoundException("Error occurred while getting the file");
        }

        try (InputStream inputStream = new ByteArrayInputStream(file)) {
            CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            csvReader.readNext();
            String[] row;
            log.info("Begin reading CSV File from API.");
            int counter = 0;
            while ((row = csvReader.readNext()) != null) {
                if (row[3].isEmpty()) {
                    log.warn("No price info provided: {}", Arrays.toString(row));
                    continue;
                }
                String[] dateComponents = row[0].split("\\s+");
                houseRepository.save(createHouse(row[1], Double.parseDouble(row[3]), dateComponents[0], dateComponents[1]));
                counter++;
            }
            log.info("Total rows added by API file: {}.", counter);
        } catch (IOException | CsvValidationException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void readFile() {
        String[] row = null;
        try (InputStream inputStream = new ClassPathResource("static/olx_house_price_Q122.csv").getInputStream()) {
            log.info("Begin reading CSV File from resources.");
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            reader.readNext();
            int counter = 0, error_counter = 0;
            while ((row = reader.readNext()) != null) {
                House house = createHouse(row[9], Double.parseDouble(row[2]), "1", row[12]);
                String type = row[7].toUpperCase().trim().replace(" ", "_");
                if (!Arrays.asList("HOUSING_BLOCK", "APARTMENT_BUILDING", "TENEMENT").contains(type)){
                    error_counter++;
                    continue;
                }
                house.setHouse_type(House.Type.valueOf(type));
                house.setArea(Double.parseDouble(row[5]));
                houseRepository.save(house);
                counter++;
            }
            log.info("Total rows added by resources' file: {}. {} had errors and were not added.", counter, error_counter);
        } catch (IOException | CsvValidationException | IllegalArgumentException e) {
            if (e instanceof IllegalArgumentException && row != null){
                log.error("Bad type: {}", row[7]);
            } else {
                log.error(e.getMessage());
            }
        }
    }
}
