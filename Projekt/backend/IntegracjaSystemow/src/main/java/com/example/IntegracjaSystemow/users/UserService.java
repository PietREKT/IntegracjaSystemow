package com.example.IntegracjaSystemow.users;

import com.example.IntegracjaSystemow.configs.JWTService;
import org.hibernate.NonUniqueObjectException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jWTService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jWTService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jWTService = jWTService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Couldn't find user " + username));
    }

    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public String login(String username, String password) throws BadCredentialsException {
        User user = getUserByUsername(username);
        if (passwordEncoder.matches(password, user.getPassword())) {
            return jWTService.generateToken(user);
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public String register(String username, String password) throws NonUniqueObjectException {
        password = passwordEncoder.encode(password);
        User user = new User(username, password);
        try {
            getUserByUsername(username);
            throw new IllegalArgumentException();
        } catch (UsernameNotFoundException ex) {
            userRepository.save(user);
            return jWTService.generateToken(user);
        }
    }

    public User getUserFromToken(String token) throws UsernameNotFoundException {
        if (token == null) {
            return null;
        }
        try {
            String username = jWTService.getUsername(token.replace("Bearer ", ""));
            return getUserByUsername(username);
        } catch (Exception e) {
            return null;
        }
    }
}
