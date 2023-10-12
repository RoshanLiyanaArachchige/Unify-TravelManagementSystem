package com.rlabdevs.unifymobile.activities.search;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.QuerySnapshot;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.explore.ExploreActivity;
import com.rlabdevs.unifymobile.adapters.HotelFilterAdapter;
import com.rlabdevs.unifymobile.adapters.RestaurantFilterAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.RestaurantModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private RelativeLayout relativeLayoutHotels, relativeLayoutRestaurants;
    private LinearLayout linearLayoutHotelNoResults, linearLayoutRestaurantNoResults;
    private RecyclerView recyclerViewHotels, recyclerViewRestaurants;
    private SpinKitView spinKitProgressHotels, spinKitProgressRestaurants;
    private TextView tvSearchResultsHeading;

    private String searchText;

    private List<HotelModel> hotelFilterList = new ArrayList<>();
    private HotelFilterAdapter hotelFilterAdapter;
    private List<RestaurantModel> restaurantFilterList = new ArrayList<>();
    private RestaurantFilterAdapter restaurantFilterAdapter;
    private CollectionReference hotelReference, restaurantReference;
    private LinearLayoutManager linearLayoutManagerHotels, linearLayoutManagerRestaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        hotelReference = firestoreDB.collection("Hotels");
        restaurantReference = firestoreDB.collection("Restaurants");

        searchText = getIntent().getStringExtra("SearchText");
        tvSearchResultsHeading = findViewById(R.id.tvSearchResultsHeading);
        tvSearchResultsHeading.setText("Search Results For '" + searchText + "'");

        initSearchHotels();
        initSearchRestaurants();
    }

    private void initSearchHotels() {
        relativeLayoutHotels = findViewById(R.id.relativeLayoutHotels);
        spinKitProgressHotels = findViewById(R.id.spinKitProgressHotels);
        linearLayoutHotelNoResults = findViewById(R.id.linearLayoutHotelNoResults);
        recyclerViewHotels = findViewById(R.id.recyclerViewHotels);

        relativeLayoutHotels.setVisibility(View.VISIBLE);
        spinKitProgressHotels.setVisibility(View.VISIBLE);
        linearLayoutHotelNoResults.setVisibility(View.GONE);

        searchHotels();
    }

    private void searchHotels() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                hotelReference
                        .whereGreaterThanOrEqualTo("hotelName", searchText)
                        .whereLessThanOrEqualTo("hotelName", searchText + "\uf8ff")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                                        hotelFilterList.add(documentSnapshot.toObject(HotelModel.class));
                                    }
                                } else {
                                    linearLayoutHotelNoResults.setVisibility(View.VISIBLE);
                                }

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        spinKitProgressHotels.setVisibility(View.GONE);

                                        hotelFilterAdapter = new HotelFilterAdapter(SearchResultsActivity.this, hotelFilterList);
                                        linearLayoutManagerHotels = new LinearLayoutManager(SearchResultsActivity.this);
                                        recyclerViewHotels.setLayoutManager(linearLayoutManagerHotels);
                                        recyclerViewHotels.setAdapter(hotelFilterAdapter);

                                        hotelFilterAdapter.notifyItemRangeInserted(0, hotelFilterList.size());
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        spinKitProgressHotels.setVisibility(View.GONE);
                                        //new Functions().ShowErrorDialog("Error Occurred", "Try Again", SearchResultsActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }



    private void initSearchRestaurants() {
        relativeLayoutRestaurants = findViewById(R.id.relativeLayoutRestaurants);
        spinKitProgressRestaurants = findViewById(R.id.spinKitProgressRestaurants);
        linearLayoutRestaurantNoResults = findViewById(R.id.linearLayoutRestaurantNoResults);
        recyclerViewRestaurants = findViewById(R.id.recyclerViewRestaurants);

        relativeLayoutRestaurants.setVisibility(View.VISIBLE);
        spinKitProgressRestaurants.setVisibility(View.VISIBLE);
        linearLayoutRestaurantNoResults.setVisibility(View.GONE);

        searchRestaurants();
    }

    private void searchRestaurants() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                restaurantReference
                        .whereGreaterThanOrEqualTo("restaurantName", searchText)
                        .whereLessThanOrEqualTo("restaurantName", searchText + "\uf8ff")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                                        restaurantFilterList.add(documentSnapshot.toObject(RestaurantModel.class));
                                    }
                                } else {
                                    linearLayoutRestaurantNoResults.setVisibility(View.VISIBLE);
                                }

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        spinKitProgressRestaurants.setVisibility(View.GONE);

                                        restaurantFilterAdapter = new RestaurantFilterAdapter(SearchResultsActivity.this, restaurantFilterList);
                                        linearLayoutManagerRestaurants = new LinearLayoutManager(SearchResultsActivity.this);
                                        recyclerViewRestaurants.setLayoutManager(linearLayoutManagerRestaurants);
                                        recyclerViewRestaurants.setAdapter(restaurantFilterAdapter);

                                        restaurantFilterAdapter.notifyItemRangeInserted(0, restaurantFilterList.size());
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        spinKitProgressRestaurants.setVisibility(View.GONE);
                                        //new Functions().ShowErrorDialog("Error Occurred", "Try Again", SearchResultsActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }
}