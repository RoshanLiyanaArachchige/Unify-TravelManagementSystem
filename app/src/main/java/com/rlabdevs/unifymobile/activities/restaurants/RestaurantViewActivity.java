package com.rlabdevs.unifymobile.activities.restaurants;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.models.RestaurantModel;

public class RestaurantViewActivity extends AppCompatActivity {

    public static RestaurantModel restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_view);
    }
}