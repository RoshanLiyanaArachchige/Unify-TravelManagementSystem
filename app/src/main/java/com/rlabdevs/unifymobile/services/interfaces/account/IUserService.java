package com.rlabdevs.unifymobile.services.interfaces.account;

import com.rlabdevs.unifymobile.models.account.NewLoginDetailModel;
import com.rlabdevs.unifymobile.models.account.NewUserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IUserService {
    @POST("User/Login")
    Call<NewUserModel> login(@Body NewLoginDetailModel model);

    @POST("User/Register")
    Call<NewUserModel> register(@Body NewUserModel model);

    @POST("User/UpdateProfile")
    Call<NewUserModel> updateProfile(@Body NewUserModel model);
}
