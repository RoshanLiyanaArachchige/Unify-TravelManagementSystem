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
import com.rlabdevs.unifymobile.activities.user.MenuActivity;
import com.rlabdevs.unifymobile.common.Functions;

public class UserHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cardViewExplore, cardViewHotels;
    private ImageView imgViewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        InitUI();
    }

    private void InitUI() {
        cardViewExplore = findViewById(R.id.cardViewExplore);
        cardViewHotels = findViewById(R.id.cardViewHotels);
        imgViewAccount = findViewById(R.id.imgViewAccount);

        cardViewExplore.setOnClickListener(this);
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