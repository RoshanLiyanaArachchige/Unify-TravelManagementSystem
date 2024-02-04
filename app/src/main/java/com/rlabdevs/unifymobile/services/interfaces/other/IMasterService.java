package com.rlabdevs.unifymobile.services.interfaces.other;

import com.rlabdevs.unifymobile.models.master.NewCuisineTypeModel;
import com.rlabdevs.unifymobile.models.master.NewCurrencyModel;
import com.rlabdevs.unifymobile.models.master.NewLocationModel;
import com.rlabdevs.unifymobile.models.master.NewThingsToDoFilterModel;
import com.rlabdevs.unifymobile.models.master.NewThingsToDoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IMasterService {
    @GET("Master/GetIndexReferenceCode?")
    Call<String> getIndexReferenceCode(@Query("IndexReferenceID") Integer indexReferenceID);

    @GET("Master/GetThingsToDoList?")
    Call<NewThingsToDoFilterModel> getThingsToDoList(@Query("Latitude") Double latitude, @Query("Longitude") Double longitude);

    @GET("Master/GetLocations")
    Call<List<NewLocationModel>> getLocations();

    @GET("Master/GetCurrencies")
    Call<List<NewCurrencyModel>> getCurrencies();

    @GET("Master/GetCuisineTypes")
    Call<List<NewCuisineTypeModel>> getCuisineTypes();
}
