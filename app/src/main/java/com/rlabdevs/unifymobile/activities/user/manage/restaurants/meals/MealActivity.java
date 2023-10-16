package com.rlabdevs.unifymobile.activities.user.manage.restaurants.meals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.rlabdevs.unifymobile.R;

public class MealActivity extends AppCompatActivity {

    public static TextView tvCurrency;
    public static String currencyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
    }
}