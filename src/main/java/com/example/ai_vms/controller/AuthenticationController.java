// package com.example.ai_vms.controller;

// import com.example.ai_vms.util.JwtUtil;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.web.bind.annotation.*;

// import com.example.ai_vms.model.AuthenticationRequest;

// @RestController
// public class AuthenticationController {

//     @Autowired
//     private AuthenticationManager authenticationManager;

//     @Autowired
//     private UserDetailsService userDetailsService;

//     @Autowired
//     private JwtUtil jwtUtil;

//     @PostMapping("/authenticate")
//     public String createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
//         try {
//             authenticationManager.authenticate(
//                     new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
//             );
//         } catch (Exception e) {
//             throw new Exception("Incorrect username or password", e);
//         }

//         final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
//         final String jwt = jwtUtil.generateToken(userDetails); 

//         return jwt;
//     }
// }
