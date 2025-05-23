package com.example.IntegracjaSystemow.users;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController( UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userdto) {
        try {
            String token = userService.login(userdto.getUsername(), userdto.getPassword());
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException ex){
            return ResponseEntity.status(401).body("Invalid login or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto){
        try {
            String token = userService.register(userDto.getUsername(), userDto.getPassword());
            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body("Username already in use!");
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> info(@RequestHeader(HttpHeaders.AUTHORIZATION) String header) {
        return ResponseEntity.ok(userService.getUserFromToken(header.replace("Bearer ", "")));
    }

    @GetMapping("/test")
    public ResponseEntity<?> testSecurity() {
        return ResponseEntity.ok().build();
    }
}
