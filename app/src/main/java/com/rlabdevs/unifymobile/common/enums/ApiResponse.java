package com.rlabdevs.unifymobile.common.enums;

public enum ApiResponse {
    Success(200),
    InternalServerError (500),
    AuthenticationFailure (401),
    ResourceNotFound (404),
    Gone (410),
    TooManyRequests (429),
    MissingParameters (601),
    InvalidParameters (602),
    InsufficientPermission (603),
    NoAttemptsExceeded (604),
    DuplicateResource (605);

    private int value;

    private ApiResponse(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
