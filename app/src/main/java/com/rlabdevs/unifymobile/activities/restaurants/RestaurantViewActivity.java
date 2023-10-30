package com.rlabdevs.unifymobile.activities.restaurants;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.bookings.restaurant.ResturantBookingActivity;
import com.rlabdevs.unifymobile.activities.directions.GetDirectionsActivity;
import com.rlabdevs.unifymobile.adapters.RestaurantMealsAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.enums.StatusCode;
import com.rlabdevs.unifymobile.models.MealModel;
import com.rlabdevs.unifymobile.models.MealTypesModel;
import com.rlabdevs.unifymobile.models.RestaurantModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class RestaurantViewActivity extends AppCompatActivity {

    private RestaurantViewActivity restaurantViewActivity;

    private RelativeLayout relativeLytNoMeals, relativeLayoutRestaurant;
    private TextView tvRestaurantName, tvRestaurantClass;
    private EditText txtRestaurantDescription, txtRestaurantLocation, txtTelephoneNo, txtCheckInOut;
    private ImageView imgViewRestaurantImage;
    private SpinKitView spinKitProgress;
    private RecyclerView recyclerViewRestaurantMeals;
    private MaterialButtonToggleGroup toggleButtonCuisineTypes;
    private Button btnGetDirections, btnBookNow;

    private CollectionReference mealsReference, mealTypesReference, cuisineTypesReference;
    private List<MealModel> restaurantMealsList;
    private RestaurantMealsAdapter restaurantMealsAdapter;

    private List<MealTypesModel> mealTypesList;

    public RestaurantModel restaurant;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_view);
        restaurant = new Gson().fromJson(getIntent().getStringExtra("Restaurant"), RestaurantModel.class);
        mealsReference = firestoreDB.collection("Meals");
        mealTypesReference = firestoreDB.collection("MealTypes");
        cuisineTypesReference = firestoreDB.collection("CuisineTypes");
        restaurantViewActivity = this;
        InitUI();
        InitRecyclerViewRestaurantMeals();
        FetchRestaurantMealsAndTypes();
    }

    private void InitUI() {
        relativeLayoutRestaurant = findViewById(R.id.relativeLayoutRestaurant);
        relativeLytNoMeals = findViewById(R.id.relativeLytNoMeals);
        tvRestaurantName = findViewById(R.id.tvRestaurantName);
        tvRestaurantClass = findViewById(R.id.tvRestaurantClass);
        txtRestaurantDescription = findViewById(R.id.txtRestaurantDescription);
        txtRestaurantLocation = findViewById(R.id.txtRestaurantLocation);
        txtTelephoneNo = findViewById(R.id.txtTelephoneNo);
        txtCheckInOut = findViewById(R.id.txtCheckInOut);
        imgViewRestaurantImage = findViewById(R.id.imgViewRestaurantImage);
        spinKitProgress = findViewById(R.id.spinKitProgress);
        toggleButtonCuisineTypes = findViewById(R.id.toggleButtonCuisineTypes);

        btnBookNow = findViewById(R.id.btnBookNow);
        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantViewActivity.this, ResturantBookingActivity.class);
                intent.putExtra("Restaurant", new Gson().toJson(restaurant));
                startActivity(intent);
            }
        });

        btnGetDirections = findViewById(R.id.btnGetDirections);
        btnGetDirections.setOnClickListener(getDirectionsOnClickListener);

        Picasso.with(restaurantViewActivity).load(restaurant.getRestaurantImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                //linearLytImageProgress.setVisibility(View.GONE);
                imgViewRestaurantImage.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                //linearLytImageProgress.setVisibility(View.GONE);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });

        tvRestaurantName.setText(restaurant.getRestaurantName());
        txtRestaurantDescription.setText(Html.fromHtml(restaurant.getRestaurantDescription()));
        txtRestaurantLocation.setText("Location: " + restaurant.getRestaurantLocation());
        tvRestaurantClass.setText("Class: " + restaurant.getRestaurantClass() + " Star");
        txtTelephoneNo.setText("Tel No: " + restaurant.getRestaurantTelNo());
        txtCheckInOut.setText("Open/Close: " + restaurant.getOpeningHour() + "/" + restaurant.getClosingHour());
    }

    private View.OnClickListener getDirectionsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent directionsIntent = new Intent(RestaurantViewActivity.this, GetDirectionsActivity.class);
            directionsIntent.putExtra("DesLatitude", restaurant.getLatitude());
            directionsIntent.putExtra("DesLongitude", restaurant.getLongitude());
            startActivity(directionsIntent);
        }
    };

    private void InitRecyclerViewRestaurantMeals() {
        recyclerViewRestaurantMeals = findViewById(R.id.recyclerViewRestaurantMeals);
        restaurantMealsList = new ArrayList<>();
        mealTypesList = new ArrayList<>();
        restaurantMealsAdapter = new RestaurantMealsAdapter(this, restaurantMealsList, mealTypesList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewRestaurantMeals.setLayoutManager(linearLayoutManager);
        recyclerViewRestaurantMeals.setAdapter(restaurantMealsAdapter);
    }

    private void FetchRestaurantMealsAndTypes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                restaurantViewActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        spinKitProgress.setVisibility(View.VISIBLE);
                        new Functions().ShowProgressBar(RestaurantViewActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                cuisineTypesReference.whereEqualTo("statusCode", StatusCode.Active.getStatusCode()).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                        String cuisineTypeCode = documentSnapshot.get("cuisineTypeCode", String.class);
                                        if (restaurant.getCuisineTypes().stream().anyMatch(a -> a.equals(cuisineTypeCode))) {
                                            MaterialButton materialButton = new MaterialButton(RestaurantViewActivity.this, null, R.attr.materialButtonOutlinedStyle);
                                            materialButton.setTag(cuisineTypeCode);
                                            materialButton.setText(documentSnapshot.get("cuisineTypeName", String.class));
                                            materialButton.setPadding(50, 0, 50, 0);
                                            materialButton.setCheckable(true);
                                            materialButton.setEnabled(false);
                                            toggleButtonCuisineTypes.addView(materialButton);
                                        }
                                    }
                                }
                            }});

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
                                                    } else {
                                                        restaurantViewActivity.runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                spinKitProgress.setVisibility(View.GONE);
                                                                Functions.HideProgressBar();
                                                                relativeLytNoMeals.setVisibility(View.VISIBLE);
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
                                                    new Functions().ShowErrorDialog("Meals Load Failure !", "Try Again", RestaurantViewActivity.this);
                                                }
                                            });
                                } else {
                                    restaurantViewActivity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            spinKitProgress.setVisibility(View.GONE);
                                            Functions.HideProgressBar();
                                            relativeLytNoMeals.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                restaurantViewActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                        new Functions().ShowErrorDialog("Error Occurred", "Try Again", RestaurantViewActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }
}