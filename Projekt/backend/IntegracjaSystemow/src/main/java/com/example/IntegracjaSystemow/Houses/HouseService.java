package com.example.IntegracjaSystemow.Houses;

import com.example.IntegracjaSystemow.users.User;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class HouseService {

    @Value("${spring.application.default_elements_per_page}")
    Integer defaultElsPerPage;

    @Value("${spring.application.min_year}")
    Long minYear;

    private final HouseRepository houseRepository;

    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    public boolean isRepositoryEmpty() {
        return houseRepository.count() == 0;
    }

    //Getters

    public Page<House> getPage(User user, int pageNo, Integer elsPerPage, String sortBy, String order) {
        if (elsPerPage == null)
            elsPerPage = defaultElsPerPage;
        if (sortBy != null && order == null) {
            throw new IllegalArgumentException("You must specify order if you want sorting!");
        }

        Pageable pageable = (sortBy != null) ? PageRequest.of(pageNo, elsPerPage, order.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending())
                : PageRequest.of(pageNo, elsPerPage);

        return user == null ? houseRepository.findDefault(pageable) : houseRepository.findAllByUser(user, pageable);
    }


    public HashMap<String, Double> getAveragePricePerCity(User user) {
        List<House> houses = (user == null) ? houseRepository.findDefault() : houseRepository.findAllByUser(user);
        HashMap<String, Double> priceInfo = new HashMap<>();
        Set<String> cities = houses.stream().map(House::getCity).collect(Collectors.toSet());
        cities.forEach(city -> {
            long len = houses.stream().filter(h -> h.getCity().equals(city) && h.getPriceTotal() != null).count();
            double total = houses.stream().filter(h -> h.getCity().equals(city) && h.getPriceTotal() != null).mapToDouble(House::getPriceTotal).sum();
            if (len > 0) {
                priceInfo.put(city, total / len);
            }
        });
        return priceInfo;
    }

    public HashMap<Integer, Double> getAveragePricePerYear(User user) {
        List<House> houses = (user == null) ? houseRepository.findDefault() : houseRepository.findAllByUser(user);
        HashMap<Integer, Double> priceInfo = new HashMap<>();
        Set<Integer> years = houses.stream().map(House::getTransactionYear).filter(y -> y > minYear && y <= LocalDate.now().getYear()).collect(Collectors.toSet());
        years.forEach(year -> {
            long len = houses.stream().filter(h -> h.getTransactionYear().equals(year) && h.getPriceTotal() != null).count();
            double total = houses.stream().filter(h -> h.getTransactionYear().equals(year) && h.getPriceTotal() != null).mapToDouble(House::getPriceTotal).sum();
            if (len > 0) {
                priceInfo.put(year, total / len);
            }
        });
        return priceInfo;
    }

    public HashMap<Integer, Long> getTransactionsPerYear(User user) {
        List<House> houses = (user == null) ? houseRepository.findDefault() : houseRepository.findAllByUser(user);
        HashMap<Integer, Long> priceInfo = new HashMap<>();
        houses.stream().map(House::getTransactionYear).filter(y -> y > minYear && y <= LocalDate.now().getYear()).collect(Collectors.toSet()).forEach(year -> {
            long len = houses.stream().filter(h -> h.getTransactionYear().equals(year)).count();
            priceInfo.put(year, len);
        });
        return priceInfo;
    }

    private House createHouse(String city, Double pricePerMeter, String year) {
        House house = new House();
        house.setCity(city);
        house.setPricePerMeter(pricePerMeter);
        house.setTransactionYear(Integer.parseInt(year));

        return house;
    }

    public House createUserHouse(HouseDto dto, User author) {
        House house = createHouse(dto.getCity(), dto.getPricePerMeter(), dto.getYear());
        house.setUser(author);
        house.setArea(dto.getArea());
        return houseRepository.save(house);
    }

    public void deleteHouse(Long id, User user) {
        Optional<House> houseOpt = houseRepository.findById(id);
        if (houseOpt.isPresent()) {
            if (houseOpt.get().getUser() != null && houseOpt.get().getUser().getId().equals(user.getId())) {
                houseRepository.deleteById(id);
            } else {
                throw new IllegalArgumentException("You can't delete houses that are not yours!");
            }
        }
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
                houseRepository.save(createHouse(row[1], Double.parseDouble(row[3]), dateComponents[1]));
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
        try (InputStream inputStream = new ClassPathResource("static/Houses.csv").getInputStream()) {
            log.info("Begin reading CSV File from resources.");
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream, Charset.forName("windows-1250")));
            reader.readNext();
            int counter = 0, error_counter = 0;
            while ((row = reader.readNext()) != null) {
                House house = createHouse(row[2], Double.parseDouble(row[7]) / Double.parseDouble(row[9]), row[10].substring(0, row[10].indexOf('.')));
                house.setArea(Double.parseDouble(row[9]));
                houseRepository.save(house);
                counter++;
            }
            log.info("Total rows added by resources' file: {}. {} had errors and were not added.", counter, error_counter);
        } catch (IOException | CsvValidationException | IllegalArgumentException e) {
                log.error(e.getMessage());
        }
    }
}
