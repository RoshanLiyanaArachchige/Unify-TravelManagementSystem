package com.rlabdevs.unifymobile.activities.restaurants.meals;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.adapters.MealReviewAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.enums.StatusCode;
import com.rlabdevs.unifymobile.models.MealModel;
import com.rlabdevs.unifymobile.models.MealReviewModel;
import com.rlabdevs.unifymobile.models.MealTypesModel;
import com.rlabdevs.unifymobile.models.RestaurantModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class MealViewActivity extends AppCompatActivity {

    private com.rlabdevs.unifymobile.activities.restaurants.meals.MealViewActivity mealViewActivity;

    private ImageView imgViewMealImage;
    private TextView tvMealType, tvMealName, tvMealPrice, tvNoOfPersons, tvNoMealReviews;
    private EditText txtMealDescription;
    private RecyclerView recyclerViewMealReviews;
    private SpinKitView spinKitProgress;

    private CollectionReference restaurantReference, mealTypesReference;
    private List<MealReviewModel> mealReviewList;
    private MealReviewAdapter mealReviewAdapter;

    private LinearLayoutManager linearLayoutManager;

    private RestaurantModel restaurantModel;
    private MealModel mealModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_view);
        mealViewActivity= this;
        mealTypesReference = firestoreDB.collection("MealTypes");
        InitUI();
        InitRecyclerViewMealReviews();
        InitMealView();
        FetchMealReviews();
    }

    private void InitUI() {
        imgViewMealImage = findViewById(R.id.imgViewMealImage);

        tvMealName = findViewById(R.id.tvMealName);
        tvMealType = findViewById(R.id.tvMealType);
        tvMealPrice = findViewById(R.id.tvMealPrice);
        tvNoOfPersons = findViewById(R.id.tvNoOfPersons);
        tvNoMealReviews = findViewById(R.id.tvNoMealReviews);

        txtMealDescription = findViewById(R.id.txtMealDescription);
        spinKitProgress = findViewById(R.id.spinKitProgress);
    }

    private void InitRecyclerViewMealReviews() {
        recyclerViewMealReviews = findViewById(R.id.recyclerViewMealReviews);
        mealReviewList = new ArrayList<>();
        mealReviewAdapter = new MealReviewAdapter(this, mealReviewList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewMealReviews.setLayoutManager(linearLayoutManager);
        recyclerViewMealReviews.setAdapter(mealReviewAdapter);
    }

    private void InitMealView() {
        restaurantModel = new Gson().fromJson(getIntent().getStringExtra("Restaurant"), RestaurantModel.class);
        mealModel = new Gson().fromJson(getIntent().getStringExtra("Meal"), MealModel.class);

        Picasso.with(com.rlabdevs.unifymobile.activities.restaurants.meals.MealViewActivity.this).load(mealModel.getMealImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imgViewMealImage.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });

        txtMealDescription.setText(Html.fromHtml(mealModel.getMealDescription()));
        tvMealPrice.setText("Price: " + mealModel.getMealPrice() + Functions.GetCurrencyNameFromCode(mealModel.getCurrencyCode()));
        tvNoOfPersons.setText("Portion: " + (mealModel.getPortionSize() > 1 ? mealModel.getPortionSize() + " Persons" : mealModel.getPortionSize() + " Person"));

        mealTypesReference.whereEqualTo("restaurantCode", restaurantModel.getRestaurantCode()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                                MealTypesModel mealType = documentSnapshot.toObject(MealTypesModel.class);
                                if(mealType.getMealTypeCode().equals(mealModel.getMealTypeCode())) {
                                    tvMealType.setText("Meal Type: " + mealType.getMealTypeName());
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void FetchMealReviews() {
        CollectionReference mealReviewReference = firestoreDB.collection( "MealReviews");
        mealReviewReference.whereEqualTo("mealCode", mealModel.getMealCode()).whereEqualTo("statusCode", StatusCode.Active.getStatusCode()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                                mealReviewList.add(documentSnapshot.toObject(MealReviewModel.class));
                            }
                            mealReviewAdapter.notifyDataSetChanged();
                        } else {
                            tvNoMealReviews.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}