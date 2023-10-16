package com.rlabdevs.unifymobile.activities.user.manage.restaurants;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.MainActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.HotelActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.MyHotelsActivity;
import com.rlabdevs.unifymobile.adapters.MyHotelsAdapter;
import com.rlabdevs.unifymobile.adapters.MyRestaurantsAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.RestaurantModel;

import java.util.ArrayList;
import java.util.List;

public class MyRestaurantsActivity extends AppCompatActivity implements View.OnClickListener {

    private MyRestaurantsActivity myRestaurantsActivity;

    private RelativeLayout relativeLayoutMyRestaurants;
    private RecyclerView recyclerViewMyRestaurants;
    private TextView tvNoAddRestaurants;
    private ImageView imgViewAddNewRestaurant;
    private SpinKitView spinKitProgress;

    private CollectionReference restaurantReference;
    private List<RestaurantModel> myRestaurantsList;
    private MyRestaurantsAdapter myRestaurantsAdapter;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_restaurants);
        restaurantReference = firestoreDB.collection("Restaurants");
        myRestaurantsActivity = this;
        InitUI();
    }

    private void InitUI() {
        relativeLayoutMyRestaurants = findViewById(R.id.relativeLayoutMyRestaurants);
        tvNoAddRestaurants = findViewById(R.id.tvNoAddRestaurants);
        imgViewAddNewRestaurant = findViewById(R.id.imgViewAddNewRestaurant);
        spinKitProgress = findViewById(R.id.spinKitProgress);

        relativeLayoutMyRestaurants.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        tvNoAddRestaurants.setOnClickListener(this);
        imgViewAddNewRestaurant.setOnClickListener(this);
    }

    private void InitRecyclerViewMyRestaurantsList() {
        recyclerViewMyRestaurants = findViewById(R.id.recyclerViewMyRestaurants);
        myRestaurantsList = new ArrayList<>();
        myRestaurantsAdapter = new MyRestaurantsAdapter(this, myRestaurantsList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewMyRestaurants.setLayoutManager(linearLayoutManager);
        recyclerViewMyRestaurants.setAdapter(myRestaurantsAdapter);

        FetchMyRestaurants();
    }

    private void FetchMyRestaurants() {

        String UserDetailsCode = MainActivity.sharedPref.getString("UserDetailsCode", "");

        new Thread(new Runnable() {
            @Override
            public void run() {

                myRestaurantsActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        spinKitProgress.setVisibility(View.VISIBLE);
                        new Functions().ShowProgressBar(MyRestaurantsActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                restaurantReference.whereEqualTo("userDetailsCode", UserDetailsCode).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(!queryDocumentSnapshots.isEmpty())
                                {
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot documentSnapshot: documentSnapshotList) {
                                        RestaurantModel restaurant = documentSnapshot.toObject(RestaurantModel.class);
                                        restaurant.setID(documentSnapshot.getId());
                                        myRestaurantsList.add(restaurant);
                                        myRestaurantsAdapter.notifyDataSetChanged();
                                    }
                                    imgViewAddNewRestaurant.setVisibility(View.VISIBLE);
                                    spinKitProgress.setVisibility(View.GONE);
                                }
                                else
                                {
                                    myRestaurantsActivity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            spinKitProgress.setVisibility(View.GONE);
                                            tvNoAddRestaurants.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }

                                myRestaurantsActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                        spinKitProgress.setVisibility(View.GONE);
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                myRestaurantsActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                        new Functions().ShowErrorDialog("Error Occurred", "Try Again", MyRestaurantsActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imgViewAddNewRestaurant :
            case R.id.tvNoAddRestaurants: {
                new Functions().StartActivity(MyRestaurantsActivity.this, RestaurantActivity.class);
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        InitRecyclerViewMyRestaurantsList();
    }
}