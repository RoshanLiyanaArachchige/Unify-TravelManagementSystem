package com.rlabdevs.unifymobile.common.enums;

public enum Status {
    Active(1),
    Deactive (2),
    Deleted (3),
    Pending (4),
    Confirmed (5),
    Cancelled (6),
    Rejected (7),
    Success (8);

    private int value;

    private Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
