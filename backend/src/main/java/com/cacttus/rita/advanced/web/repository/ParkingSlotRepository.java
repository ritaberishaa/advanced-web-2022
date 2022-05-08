package com.cacttus.rita.advanced.web.repository;

import com.cacttus.rita.advanced.web.dto.parkingSlot.ParkingSlotDto;
import com.cacttus.rita.advanced.web.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    @Query("SELECT new com.cacttus.rita.advanced.web.dto.parkingSlot.ParkingSlotDto(p.id, p.parkingZone.id, p.isHandicap, p.isFree) FROM ParkingSlot as p where p.parkingZone.id = ?1")
    List<ParkingSlotDto> getAllSlotsAtZone(Long zoneId);
}
