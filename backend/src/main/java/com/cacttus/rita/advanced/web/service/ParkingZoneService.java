package com.cacttus.rita.advanced.web.service;

import com.cacttus.rita.advanced.web.dto.parkingZone.ParkingZoneDto;
import com.cacttus.rita.advanced.web.exception.CityWithIdDoesNotExistException;
import com.cacttus.rita.advanced.web.exception.ParkingZoneWithIdDoesNotExistException;
import com.cacttus.rita.advanced.web.model.City;
import com.cacttus.rita.advanced.web.model.ParkingZone;
import com.cacttus.rita.advanced.web.repository.CityRepository;
import com.cacttus.rita.advanced.web.repository.ParkingZoneRepository;

import java.util.List;
import java.util.Optional;

public class ParkingZoneService {
    private final CityRepository cityRepository;
    private final ParkingZoneRepository parkingZoneRepository;

    public ParkingZoneService(CityRepository cityRepository, ParkingZoneRepository parkingZoneRepository) {
        this.cityRepository = cityRepository;
        this.parkingZoneRepository = parkingZoneRepository;
    }

    public ParkingZoneDto saveParkingZone(ParkingZoneDto data) throws CityWithIdDoesNotExistException {
        ParkingZone zone = new ParkingZone();

        Optional<City> city = cityRepository.findById(data.getCityId());

        if(city.isEmpty())
            throw new CityWithIdDoesNotExistException(data.getCityId());

        zone.setCity(city.get());

        if(data.getName() != null)
            zone.setName(data.getName());
        if(data.getLocationLatitude() != null)
            zone.setLocationLat(data.getLocationLatitude());
        if(data.getLocationLongitude() != null)
            zone.setLocationLng(data.getLocationLongitude());
        if(data.getOperating() != null)
            zone.setOperating(data.getOperating());
        else
            zone.setOperating(true);

        zone.setActive(true);

        parkingZoneRepository.save(zone);

        ParkingZoneDto response = new ParkingZoneDto();
        response.setId(zone.getId());
        response.setCityId(city.get().getId());
        response.setName(zone.getName());
        response.setLocationLatitude(zone.getLocationLat());
        response.setLocationLongitude(zone.getLocationLng());
        response.setId(zone.getId());
        response.setOperating(zone.getOperating());
        return response;
    }
    public List<ParkingZoneDto> getAllParkingZones() {
        return parkingZoneRepository.getAllParkingZonesAsDto();
    }

    public ParkingZone getParkingZoneWithId(Long id) throws ParkingZoneWithIdDoesNotExistException {
        Optional<ParkingZone> zone = parkingZoneRepository.findById(id);

        if(zone.isEmpty())
            throw new ParkingZoneWithIdDoesNotExistException(id);

        return zone.get();
    }

    public ParkingZoneDto editParkingZone(Long id, ParkingZoneDto newZone) throws ParkingZoneWithIdDoesNotExistException, CityWithIdDoesNotExistException {
        Optional<ParkingZone> zone = parkingZoneRepository.findById(id);

        if(zone.isEmpty())
            throw new ParkingZoneWithIdDoesNotExistException(id);

        if(newZone.getName() != null)
            zone.get().setName(newZone.getName());

        if(newZone.getCityId() != null) {
            Optional<City> city = cityRepository.findById(newZone.getCityId());
            if(city.isEmpty())
                throw new CityWithIdDoesNotExistException(newZone.getCityId());
            zone.get().setCity(city.get());
        }

        if(newZone.getLocationLatitude() != null){
            zone.get().setLocationLat(newZone.getLocationLatitude());
        }

        if(newZone.getLocationLongitude() != null){
            zone.get().setLocationLng(newZone.getLocationLongitude());
        }

        if(newZone.getOperating() != null){
            zone.get().setOperating(newZone.getOperating());
        }

        parkingZoneRepository.save(zone.get());

        return parkingZoneRepository.getParkingZoneDtoBy(id);
    }

    public void deleteParkingZoneWithId(Long id) throws ParkingZoneWithIdDoesNotExistException {
        Optional<ParkingZone> zone = parkingZoneRepository.findById(id);

        if(zone.isEmpty())
            throw new ParkingZoneWithIdDoesNotExistException(id);

        zone.get().setActive(false);

        parkingZoneRepository.save(zone.get());
    }

}
