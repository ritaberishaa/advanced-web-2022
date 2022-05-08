package com.cacttus.rita.advanced.web.service;


import com.cacttus.rita.advanced.web.dto.auth.RegisterRequest;
import com.cacttus.rita.advanced.web.exception.UserWithEmailAlreadyExistException;
import com.cacttus.rita.advanced.web.exception.UserWithUsernameAlreadyExistsException;
import com.cacttus.rita.advanced.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.cacttus.rita.advanced.web.model.User user = userRepository.getUserByUsername(username);

        if(user != null){
            ArrayList<SimpleGrantedAuthority> role = new ArrayList();
            role.add(new SimpleGrantedAuthority(user.getRole()));
            return new User(user.getUsername(), user.getPassword(), role);
        }
        else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

    }
    public void save(RegisterRequest user) throws UserWithUsernameAlreadyExistsException, UserWithEmailAlreadyExistException {
        if(userRepository.getUserByUsername(user.getUsername()) != null){
            throw new UserWithUsernameAlreadyExistsException();
        }

        if(userRepository.getUserByEmail(user.getEmail()) != null){
            throw new UserWithEmailAlreadyExistException();
        }

        com.cacttus.rita.advanced.web.model.User newUser = new com.cacttus.rita.advanced.web.model.User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setRole("USER");
        userRepository.save(newUser);
    }

}
