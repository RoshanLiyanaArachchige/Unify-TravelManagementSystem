package com.rlabdevs.unifymobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.account.LoginActivity;
import com.rlabdevs.unifymobile.activities.explore.ExploreActivity;
import com.rlabdevs.unifymobile.activities.hotels.HotelFilterActivity;
import com.rlabdevs.unifymobile.activities.thingstodo.ThingsToDoActivity;
import com.rlabdevs.unifymobile.activities.thingstodo.ThingsToDoViewActivity;
import com.rlabdevs.unifymobile.activities.user.MenuActivity;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.CuisineTypeModel;
import com.rlabdevs.unifymobile.models.CurrencyModel;

import java.util.List;

public class UserHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cardViewExplore, cardViewThingsToDo, cardViewHotels;
    private ImageView imgViewAccount;

    public static List<CurrencyModel> currencyList;
    public static List<CuisineTypeModel> cuisineTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        InitUI();

        Functions.getCurrencyTypeList();
        Functions.getCuisineTypeList();
    }

    private void InitUI() {
        cardViewExplore = findViewById(R.id.cardViewExplore);
        cardViewThingsToDo = findViewById(R.id.cardViewThingsToDo);
        cardViewHotels = findViewById(R.id.cardViewHotels);
        imgViewAccount = findViewById(R.id.imgViewAccount);

        cardViewExplore.setOnClickListener(this);
        cardViewThingsToDo.setOnClickListener(this);
        cardViewHotels.setOnClickListener(this);
        imgViewAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.cardViewExplore:
            {
                new Functions().StartActivity(UserHomeActivity.this, ExploreActivity.class);
                break;
            }
            case R.id.cardViewThingsToDo:
            {
                new Functions().StartActivity(UserHomeActivity.this, ThingsToDoActivity.class);
                break;
            }
            case R.id.cardViewHotels:
            {
                new Functions().StartActivity(UserHomeActivity.this, HotelFilterActivity.class);
                break;
            }
            case R.id.imgViewAccount:
            {
                ConfigureAccount();
                break;
            }
        }
    }

    private void ConfigureAccount() {
        boolean isUserLoggedIn = MainActivity.sharedPref.getBoolean("IsUserLoggedIn", false);
        if(isUserLoggedIn)
            new Functions().StartActivity(UserHomeActivity.this, MenuActivity.class);
        else
            new Functions().StartActivity(UserHomeActivity.this, LoginActivity.class);
    }
}