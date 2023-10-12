package com.rlabdevs.unifymobile.activities;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.account.LoginActivity;
import com.rlabdevs.unifymobile.activities.explore.ExploreActivity;
import com.rlabdevs.unifymobile.activities.hotels.HotelFilterActivity;
import com.rlabdevs.unifymobile.activities.search.SearchResultsActivity;
import com.rlabdevs.unifymobile.activities.thingstodo.ThingsToDoActivity;
import com.rlabdevs.unifymobile.activities.thingstodo.ThingsToDoViewActivity;
import com.rlabdevs.unifymobile.activities.user.MenuActivity;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.CuisineTypeModel;
import com.rlabdevs.unifymobile.models.CurrencyModel;
import com.rlabdevs.unifymobile.models.LocationModel;
import com.rlabdevs.unifymobile.models.RoomModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cardViewExplore, cardViewThingsToDo, cardViewHotels;
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
                    if(!searchText.equals("")) {
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