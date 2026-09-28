package com.trading.authservice.services;


import com.trading.authservice.dtos.LoginRequest;
import com.trading.authservice.dtos.LoginResponse;
import com.trading.authservice.dtos.RegisterRequest;
import com.trading.authservice.models.Role;
import com.trading.authservice.models.User;
import com.trading.authservice.respositories.RoleRepository;
import com.trading.authservice.respositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
    }

    public void register(RegisterRequest request){
        if (userRepository.existsByUsername(
                request.getUsername())) {

            throw new RuntimeException(
                    "Username already exists");
        }



        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUsername(request.getUsername());
        Set<Role> roles = request.getRole()
                .stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Role not found: " + roleName
                                )
                        ))
                .collect(Collectors.toSet());

        user.setRoles(roles);

        user.setRoles(roles);

        userRepository.save(user);
    }



    public LoginResponse login(LoginRequest request){
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();
        String token =
                jwtService.generateToken(userDetails);

        return new LoginResponse(
                token,
                "Bearer",
                900
        );


    }

}
