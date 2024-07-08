package com.rlabdevs.unifymobile.services.interfaces.account;

import com.rlabdevs.unifymobile.models.account.NewLoginDetailModel;
import com.rlabdevs.unifymobile.models.account.NewUserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IUserService {
    @POST("User/Login")
    Call<NewUserModel> login(@Body NewLoginDetailModel model);

    @POST("User/Register")
    Call<NewUserModel> register(@Body NewUserModel model);

    @POST("User/UpdateProfile")
    Call<NewUserModel> updateProfile(@Body NewUserModel model);

    @GET("User/GetProfileDetails?")
    Call<NewUserModel> getProfileDetails(@Query("AuthToken") String authToken);
}
