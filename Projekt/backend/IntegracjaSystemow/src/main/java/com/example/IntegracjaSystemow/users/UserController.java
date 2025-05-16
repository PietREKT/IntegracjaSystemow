package com.example.IntegracjaSystemow.users;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final AuthenticationManager authenticationManager;

      public UserController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userdto, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userdto.getUsername(), userdto.getPassword());
        try {
            Authentication auth = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);

            request.getSession(true)
                    .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                            SecurityContextHolder.getContext());

            return ResponseEntity.ok("Logged in as " + auth.getName());
        } catch (AuthenticationException ex){
            return ResponseEntity.status(401).body("Bad credentials");
        }
    }
    @GetMapping("")
    public ResponseEntity<?> test() {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok("Hello " + auth.getUsername());
    }

    @GetMapping("/test")
    public ResponseEntity<?> testSecurity() {
        return ResponseEntity.ok().build();
    }
}
