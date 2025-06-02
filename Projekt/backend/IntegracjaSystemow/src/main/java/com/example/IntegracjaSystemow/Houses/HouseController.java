package com.example.IntegracjaSystemow.Houses;

import com.example.IntegracjaSystemow.users.User;
import com.example.IntegracjaSystemow.users.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/houses")
public class HouseController {

    private final HouseService houseService;
    private final UserService userService;

    public HouseController(HouseService houseService, UserService userService) {
        this.houseService = houseService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addHouse(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestBody HouseDto dto){
        User user = userService.getUserFromToken(authorization.replace("Bearer ", ""));
        House house = houseService.createUserHouse(dto, user);

        return ResponseEntity.ok(house);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteHouse(@PathVariable String id){
        houseService.deleteHouse(id);
        return ResponseEntity.ok().build();
    }
}
