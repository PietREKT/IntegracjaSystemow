package com.example.IntegracjaSystemow.Houses;

import com.example.IntegracjaSystemow.users.User;
import com.example.IntegracjaSystemow.users.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/api/houses")
@Log4j2
public class HouseController {

    private final HouseService houseService;
    private final UserService userService;

    public HouseController(HouseService houseService, UserService userService) {
        this.houseService = houseService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addHouse(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody @Valid HouseDto dto){
        User user = userService.getUserFromToken(authorization);
        House house = houseService.createUserHouse(dto, user);
        log.info("Added new house: {}", house.toString());
        return ResponseEntity.ok(house);
    }

    @GetMapping
    public ResponseEntity<?> getHousesPaged(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String auth, @RequestParam int page, @RequestParam(required = false) Integer elementsPerPage, @RequestParam(required = false) String sorting, @RequestParam(required = false) String order){
        User user = userService.getUserFromToken(auth);
        return ResponseEntity.ok(houseService.getPage(user, page, elementsPerPage, sorting, order));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteHouse(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String auth){
        try {
            User user = userService.getUserFromToken(auth);
            houseService.deleteHouse(id, user);
            log.info("Deleted house with ID: {}", id);
        } catch (IllegalArgumentException ex){
            return ResponseEntity.status(403).body(ex.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats/price")
    public ResponseEntity<?> getAveragePricesAndYears(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String auth){
        User user = userService.getUserFromToken(auth);
        return ResponseEntity.ok(houseService.getAveragePricePerYear(user));
    }

    @GetMapping("/stats/count")
    public ResponseEntity<?> getTransactionsPerYear(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String auth){
        User user = userService.getUserFromToken(auth);
        return ResponseEntity.ok(houseService.getTransactionsPerYear(user));
    }

    @GetMapping("/stats/cities")
    public ResponseEntity<?> getAveragePriceByCity(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String auth){
        User user = userService.getUserFromToken(auth);
        return ResponseEntity.ok(houseService.getAveragePricePerCity(user));
    }
}
