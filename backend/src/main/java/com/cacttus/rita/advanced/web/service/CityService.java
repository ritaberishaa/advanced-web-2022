package com.cacttus.rita.advanced.web.service;

import com.cacttus.rita.advanced.web.exception.CityWithIdDoesNotExistException;
import com.cacttus.rita.advanced.web.model.City;
import com.cacttus.rita.advanced.web.repository.CityRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CityService {
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) { this.cityRepository = cityRepository; }

    public List<City> getAllCities() { return cityRepository.findAll(); }

    public City getCityWithId(Long id) throws CityWithIdDoesNotExistException {
        Optional<City> city = cityRepository.findById(id);
        if (city.isEmpty())
            throw new CityWithIdDoesNotExistException(id);
        return city.get();
    }

}
