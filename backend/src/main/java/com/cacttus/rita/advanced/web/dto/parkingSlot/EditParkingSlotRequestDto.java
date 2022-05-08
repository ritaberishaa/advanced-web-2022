package com.cacttus.rita.advanced.web.dto.parkingSlot;

public class EditParkingSlotRequestDto {
    private Long parkingZoneId;
    private Boolean isHandicap;
    private Boolean isFree;

    public Long getParkingZoneId() {
        return parkingZoneId;
    }

    public void setParkingZoneId(Long parkingZoneId) {
        this.parkingZoneId = parkingZoneId;
    }

    public void setHandicap(Boolean handicap) {
        isHandicap = handicap;
    }

    public void setFree(Boolean free) {
        isFree = free;
    }
}
