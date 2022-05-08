package com.cacttus.rita.advanced.web.repository;

import com.cacttus.rita.advanced.web.dto.parkingZone.ParkingZoneDto;
import com.cacttus.rita.advanced.web.model.ParkingZone;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ParkingZoneRepository extends CrudRepository<ParkingZone, Long> {
    @Query("SELECT new com.cacttus.rita.advanced.web.dto.parkingZone.ParkingZoneDto(p.id, p.name,  p.city.id, p.locationLat, p.locationLng, p.isOperating) from ParkingZone as p")
    List<ParkingZoneDto> getAllParkingZonesAsDto();

    @Query("SELECT new com.cacttus.rita.advanced.web.dto.parkingZone.ParkingZoneDto(p.id, p.name,  p.city.id, p.locationLat, p.locationLng, p.isOperating) from ParkingZone as p WHERE p.id = ?1")
    ParkingZoneDto getParkingZoneDtoBy(Long id);
}
