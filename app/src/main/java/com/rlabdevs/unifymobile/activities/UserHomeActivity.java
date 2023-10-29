package com.rlabdevs.unifymobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.account.LoginActivity;
import com.rlabdevs.unifymobile.activities.explore.ExploreActivity;
import com.rlabdevs.unifymobile.activities.hotels.HotelFilterActivity;
import com.rlabdevs.unifymobile.activities.restaurants.RestaurantFilterActivity;
import com.rlabdevs.unifymobile.activities.search.SearchResultsActivity;
import com.rlabdevs.unifymobile.activities.thingstodo.ThingsToDoActivity;
import com.rlabdevs.unifymobile.activities.user.MenuActivity;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.CuisineTypeModel;
import com.rlabdevs.unifymobile.models.CurrencyModel;
import com.rlabdevs.unifymobile.models.LocationModel;

import java.util.List;

public class UserHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cardViewExplore, cardViewThingsToDo, cardViewHotels, cardViewFlights, cardViewRestaurant;
    private ImageView imgViewAccount;
    private EditText txtSearch;

    public static List<CurrencyModel> currencyList;
    public static List<LocationModel> locationList;
    public static List<CuisineTypeModel> cuisineTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        InitUI();

        Functions.getCurrencyTypeList();
        Functions.getLocationList();
        Functions.getCuisineTypeList();

        txtSearch = findViewById(R.id.txtSearch);
        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String searchText = (txtSearch.getText().toString() + "").trim();
                    if (!searchText.equals("")) {
                        txtSearch.setText("");
                        Intent intent = new Intent(UserHomeActivity.this, SearchResultsActivity.class);
                        intent.putExtra("SearchText", searchText);
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void InitUI() {
        cardViewExplore = findViewById(R.id.cardViewExplore);
        cardViewThingsToDo = findViewById(R.id.cardViewThingsToDo);
        cardViewHotels = findViewById(R.id.cardViewHotels);
        cardViewFlights = findViewById(R.id.cardViewFlights);
        cardViewRestaurant = findViewById(R.id.cardViewRestaurant);
        imgViewAccount = findViewById(R.id.imgViewAccount);

        cardViewExplore.setOnClickListener(this);
        cardViewThingsToDo.setOnClickListener(this);
        cardViewHotels.setOnClickListener(this);
        cardViewFlights.setOnClickListener(this);
        cardViewRestaurant.setOnClickListener(this);
        imgViewAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardViewExplore: {
                new Functions().StartActivity(UserHomeActivity.this, ExploreActivity.class);
                break;
            }
            case R.id.cardViewThingsToDo: {
                new Functions().StartActivity(UserHomeActivity.this, ThingsToDoActivity.class);
                break;
            }
            case R.id.cardViewHotels: {
                new Functions().StartActivity(UserHomeActivity.this, HotelFilterActivity.class);
                break;
            }
            case R.id.cardViewRestaurant: {
                new Functions().StartActivity(UserHomeActivity.this, RestaurantFilterActivity.class);
                break;
            }
            case R.id.cardViewFlights: {
                break;
            }
            case R.id.imgViewAccount: {
                ConfigureAccount();
                break;
            }
        }
    }

    private void ConfigureAccount() {
        boolean isUserLoggedIn = MainActivity.sharedPref.getBoolean("IsUserLoggedIn", false);
        if (isUserLoggedIn)
            new Functions().StartActivity(UserHomeActivity.this, MenuActivity.class);
        else
            new Functions().StartActivity(UserHomeActivity.this, LoginActivity.class);
    }
}