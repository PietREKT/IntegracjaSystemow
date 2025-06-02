package com.example.IntegracjaSystemow.configs;

import com.example.IntegracjaSystemow.Houses.HouseService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PostStartup {


    private final HouseService houseService;

    public PostStartup(HouseService houseService) {
        this.houseService = houseService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void run() throws Exception {
        if (houseService.isRepositoryEmpty()) {
            houseService.readFileFromApi();
            houseService.readFile();
        }
    }
}
