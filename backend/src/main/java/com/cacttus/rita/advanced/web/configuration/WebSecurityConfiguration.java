package com.cacttus.rita.advanced.web.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers("/api/auth/login", "/api/auth/register","/api/docs/**").permitAll().
                antMatchers(HttpMethod.GET, "/api/zone/*", "/api/zone/*/slots").permitAll().
                antMatchers(HttpMethod.POST, "/api/zone").hasAuthority("ADMIN").
                antMatchers(HttpMethod.PUT, "/api/zone/**").hasAuthority("ADMIN").
                antMatchers(HttpMethod.DELETE, "/api/zone/**").hasAuthority("ADMIN").
                antMatchers(HttpMethod.GET, "/api/slot/*", "/api/slot").permitAll().
                antMatchers(HttpMethod.POST, "/api/slot").hasAuthority("ADMIN").
                antMatchers(HttpMethod.PUT, "/api/slot/**").hasAuthority("ADMIN").
                antMatchers(HttpMethod.DELETE, "/api/slot/**").hasAuthority("ADMIN").
                antMatchers(HttpMethod.GET, "/api/city/*").permitAll().

                anyRequest().authenticated().and().exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint).
                and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
