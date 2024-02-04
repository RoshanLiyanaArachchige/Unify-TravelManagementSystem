package com.rlabdevs.unifymobile.common.enums;

public enum Amenity {
    FreeWifi(1),
    AirConditioned (2),
    FreeBreakfast (3),
    TeaCoffee (4),
    Bar (5),
    RoomServices (6),
    Television (7),
    Pool (8),
    Spa (9),
    Parking (10),
    Beverages (11),
    TakeAway (12);

    private int value;

    private Amenity(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
