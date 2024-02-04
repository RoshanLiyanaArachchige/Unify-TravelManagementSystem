package com.rlabdevs.unifymobile.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rlabdevs.unifymobile.common.Constants;
import com.rlabdevs.unifymobile.utilities.CustomDateDeserializer;
import com.rlabdevs.unifymobile.utilities.CustomTimeDeserializer;

import java.time.LocalTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            createClient();
        }

        return retrofit;
    }

    public static void createClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new CustomDateDeserializer())
                .registerTypeAdapter(LocalTime.class, new CustomTimeDeserializer())
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.WEB_API_CONNECTION_ADDRESS)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
