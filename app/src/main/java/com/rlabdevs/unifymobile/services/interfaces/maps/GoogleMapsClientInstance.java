package com.rlabdevs.unifymobile.services.interfaces.maps;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleMapsClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://maps.googleapis.com/maps/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
