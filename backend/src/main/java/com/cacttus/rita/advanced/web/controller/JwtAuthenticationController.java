package com.cacttus.rita.advanced.web.controller;

import com.cacttus.rita.advanced.web.configuration.JwtTokenUtil;
import com.cacttus.rita.advanced.web.dto.auth.LoginRequest;
import com.cacttus.rita.advanced.web.dto.auth.LoginResponse;
import com.cacttus.rita.advanced.web.dto.auth.RegisterRequest;
import com.cacttus.rita.advanced.web.dto.auth.RegisterResponse;
import com.cacttus.rita.advanced.web.dto.http.ErrorResponse;
import com.cacttus.rita.advanced.web.dto.http.GenericJsonResponse;
import com.cacttus.rita.advanced.web.exception.UserWithEmailAlreadyExistException;
import com.cacttus.rita.advanced.web.exception.UserWithUsernameAlreadyExistsException;
import com.cacttus.rita.advanced.web.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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

    @PostMapping("/login")
    public GenericJsonResponse<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest, HttpServletResponse response) throws Exception {
        try{
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        }
        catch (BadCredentialsException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return new GenericJsonResponse<>(false, new ErrorResponse(e.getMessage()));
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return new GenericJsonResponse<>(true, new LoginResponse(token));
    }

    private void authenticate(String username, String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @PostMapping("/register")
    public GenericJsonResponse<?> saveUser(@RequestBody RegisterRequest user, HttpServletResponse response){
        try {
            userDetailsService.save(user);
        }
        catch (UserWithUsernameAlreadyExistsException | UserWithEmailAlreadyExistException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new GenericJsonResponse<ErrorResponse>(false, new ErrorResponse(e.getMessage()));
        }

        response.setStatus(HttpServletResponse.SC_CREATED);
        return new GenericJsonResponse<RegisterResponse>(true, null);
    }
}
