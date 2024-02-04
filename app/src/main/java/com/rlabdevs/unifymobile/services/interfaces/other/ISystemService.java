package com.rlabdevs.unifymobile.services.interfaces.other;

import com.rlabdevs.unifymobile.models.service.NewServiceModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ISystemService {
    @POST("Service/SaveHotel")
    Call<NewServiceModel> saveHotel(@Body NewServiceModel model);


}
