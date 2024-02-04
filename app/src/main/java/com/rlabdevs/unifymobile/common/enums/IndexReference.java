package com.rlabdevs.unifymobile.common.enums;

public enum IndexReference {
    Hotels(1),
    Meals(2),
    Restaurants(3),
    RoomTypes(4),
    Rooms(5),
    MealTypes(6),
    Bookings(7),
    Users(8);

    private int value;

    private IndexReference(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
