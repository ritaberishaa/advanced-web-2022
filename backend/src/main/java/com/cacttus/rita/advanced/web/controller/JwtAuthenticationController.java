package com.cacttus.rita.advanced.web.controller;

import com.cacttus.rita.advanced.web.configuration.JwtTokenUtil;
import com.cacttus.rita.advanced.web.dto.http.GenericJsonResponse;
import com.cacttus.rita.advanced.web.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

}
