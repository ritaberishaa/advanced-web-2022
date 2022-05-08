package com.cacttus.rita.advanced.web.repository;

import com.cacttus.rita.advanced.web.model.City;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CityRepository extends CrudRepository<City, Long> {
    City findCityByName(String name);
    List<City> findAll();
}
