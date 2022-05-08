package com.cacttus.rita.advanced.web.dto.parkingSlot;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonProperty("isHandicap")
    public Boolean isHandicap() {
        return isHandicap;
    }
    @JsonProperty("isFree")
    public Boolean isFree() {
        return isFree;
    }
}
