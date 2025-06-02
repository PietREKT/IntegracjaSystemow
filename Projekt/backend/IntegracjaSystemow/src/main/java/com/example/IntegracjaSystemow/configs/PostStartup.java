package com.example.IntegracjaSystemow.configs;

import com.example.IntegracjaSystemow.Houses.HouseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PostStartup implements CommandLineRunner {


    private final HouseService houseService;

    public PostStartup(HouseService houseService) {
        this.houseService = houseService;
    }

    @Override
    public void run(String... args) throws Exception {
        houseService.readFileFromApi();
        houseService.readFile();
    }
}
