package com.cacttus.rita.advanced.web.configuration;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@Configuration
public class LocaleConfiguration {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        System.out.println("Date in UTc: "+ new Date().toString());
    }
}
