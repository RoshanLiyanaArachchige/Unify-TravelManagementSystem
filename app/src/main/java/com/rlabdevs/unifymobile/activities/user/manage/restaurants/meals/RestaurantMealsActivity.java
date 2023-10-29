package com.rlabdevs.unifymobile.activities.user.manage.restaurants.meals;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.content.Intent;
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
import com.google.gson.Gson;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.rooms.HotelRoomsActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.rooms.RoomActivity;
import com.rlabdevs.unifymobile.adapters.HotelRoomsAdapter;
import com.rlabdevs.unifymobile.adapters.RestaurantMealsAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.MealModel;
import com.rlabdevs.unifymobile.models.MealTypesModel;
import com.rlabdevs.unifymobile.models.RestaurantModel;
import com.rlabdevs.unifymobile.models.RoomModel;
import com.rlabdevs.unifymobile.models.RoomTypesModel;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMealsActivity extends AppCompatActivity implements View.OnClickListener {

    private RestaurantMealsActivity restaurantMealsActivity;

    private RelativeLayout relativeLayoutRestaurantMeals;
    private RecyclerView recyclerViewRestaurantMeals;
    private TextView tvNoAddMeals;
    private ImageView imgViewAddNewMeal;
    private SpinKitView spinKitProgress;

    private CollectionReference mealsReference, mealTypesReference;
    private List<MealModel> restaurantMealsList;
    private RestaurantMealsAdapter restaurantMealsAdapter;

    private List<MealTypesModel> mealTypesList;

    public RestaurantModel restaurant;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_meals);
        restaurant = new Gson().fromJson(getIntent().getStringExtra("Restaurant"), RestaurantModel.class);
        mealsReference = firestoreDB.collection("Meals");
        mealTypesReference = firestoreDB.collection("MealTypes");
        restaurantMealsActivity = this;
        InitUI();
    }

    private void InitUI() {
        relativeLayoutRestaurantMeals = findViewById(R.id.relativeLayoutRestaurantMeals);
        tvNoAddMeals = findViewById(R.id.tvNoAddMeals);
        imgViewAddNewMeal = findViewById(R.id.imgViewAddNewMeal);
        spinKitProgress = findViewById(R.id.spinKitProgress);

        relativeLayoutRestaurantMeals.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        tvNoAddMeals.setOnClickListener(this);
        imgViewAddNewMeal.setOnClickListener(this);
    }

    private void InitRecyclerViewRestaurantsMealsList() {
        recyclerViewRestaurantMeals = findViewById(R.id.recyclerViewRestaurantMeals);
        restaurantMealsList = new ArrayList<>();
        mealTypesList = new ArrayList<>();
        restaurantMealsAdapter = new RestaurantMealsAdapter(this, restaurantMealsList, mealTypesList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewRestaurantMeals.setLayoutManager(linearLayoutManager);
        recyclerViewRestaurantMeals.setAdapter(restaurantMealsAdapter);

        FetchRestaurantMealsAndTypes();
    }

    private void FetchRestaurantMealsAndTypes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                restaurantMealsActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        spinKitProgress.setVisibility(View.VISIBLE);
                        new Functions().ShowProgressBar(RestaurantMealsActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                mealTypesReference.whereEqualTo("restaurantCode", restaurant.getRestaurantCode()).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                                        MealTypesModel mealType = documentSnapshot.toObject(MealTypesModel.class);
                                        mealType.setID(documentSnapshot.getId());
                                        mealTypesList.add(mealType);
                                    }

                                    mealsReference.whereEqualTo("restaurantCode", restaurant.getRestaurantCode()).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    if (!queryDocumentSnapshots.isEmpty()) {
                                                        List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                                        for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                                                            MealModel meal = documentSnapshot.toObject(MealModel.class);
                                                            meal.setID(documentSnapshot.getId());
                                                            restaurantMealsList.add(meal);
                                                            restaurantMealsAdapter.notifyDataSetChanged();
                                                        }
                                                        spinKitProgress.setVisibility(View.GONE);
                                                        Functions.HideProgressBar();
                                                        tvNoAddMeals.setVisibility(View.GONE);
                                                    } else {
                                                        restaurantMealsActivity.runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                spinKitProgress.setVisibility(View.GONE);
                                                                Functions.HideProgressBar();
                                                                tvNoAddMeals.setVisibility(View.VISIBLE);
                                                            }
                                                        });
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    spinKitProgress.setVisibility(View.GONE);
                                                    Functions.HideProgressBar();
                                                    new Functions().ShowErrorDialog("Meals Load Failure !", "Try Again", RestaurantMealsActivity.this);
                                                }
                                            });
                                } else {
                                    restaurantMealsActivity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            spinKitProgress.setVisibility(View.GONE);
                                            Functions.HideProgressBar();
                                        }
                                    });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                restaurantMealsActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                        new Functions().ShowErrorDialog("Error Occurred", "Try Again", RestaurantMealsActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgViewAddNewMeal: {
                NewRestaurantMeal();
                break;
            }
            case R.id.tvNoAddMeals: {
                NewRestaurantMeal();
                break;
            }
        }
    }

    private void NewRestaurantMeal() {
        Intent restaurantMealIntent = new Intent(restaurantMealsActivity, MealActivity.class);
        restaurantMealIntent.putExtra("Restaurant", new Gson().toJson(restaurant));
        //finish();
        startActivity(restaurantMealIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        InitRecyclerViewRestaurantsMealsList();
    }
}