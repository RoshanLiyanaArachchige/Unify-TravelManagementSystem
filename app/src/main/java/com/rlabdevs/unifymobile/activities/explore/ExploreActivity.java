package com.rlabdevs.unifymobile.activities.explore;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.github.ybq.android.spinkit.SpinKitView;

import android.location.Location;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.hotels.HotelFilterActivity;
import com.rlabdevs.unifymobile.adapters.HotelFilterAdapter;
import com.rlabdevs.unifymobile.common.Constants;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.HotelModel;

import java.util.ArrayList;
import java.util.List;

public class ExploreActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private MapView mapViewCurrentLocation;
    private GoogleMap googleMapCurrentLocation;
    private RelativeLayout relativeLayoutHotels, relativeLayoutRestaurants;
    private LinearLayout linearLayoutHotelNoResults, linearLayoutRestaurantNoResults;
    private RecyclerView recyclerViewHotels, recyclerViewRestaurants;
    private SpinKitView spinKitProgressHotels, spinKitProgressRestaurants;

    private LatLng currentLatLng;
    private boolean isViewsInitialized = false;

    private CollectionReference hotelReference;
    private List<HotelModel> hotelFilterList;
    private HotelFilterAdapter hotelFilterAdapter;
    private LinearLayoutManager linearLayoutManagerHotels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        mapViewCurrentLocation = findViewById(R.id.mapViewCurrentLocation);
        mapViewCurrentLocation.onCreate(savedInstanceState);
        mapViewCurrentLocation.getMapAsync(this);

        hotelReference = firestoreDB.collection("Hotels");

        initializeExploreNearby();
    }

    private void initializeExploreNearby() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void initFeaturedHotels() {
        if (!isViewsInitialized) {
            relativeLayoutHotels = findViewById(R.id.relativeLayoutHotels);
            spinKitProgressHotels = findViewById(R.id.spinKitProgressHotels);
            linearLayoutHotelNoResults = findViewById(R.id.linearLayoutHotelNoResults);
            recyclerViewHotels = findViewById(R.id.recyclerViewHotels);
        }

        relativeLayoutHotels.setVisibility(View.VISIBLE);
        spinKitProgressHotels.setVisibility(View.VISIBLE);
        linearLayoutHotelNoResults.setVisibility(View.GONE);

        getFeaturedHotels();
    }

    private void getFeaturedHotels() {
        double searchRadius = 0.1;

        double minLatitude = currentLatLng.latitude - searchRadius;
        double maxLatitude = currentLatLng.latitude + searchRadius;
        double minLongitude = currentLatLng.longitude - searchRadius;
        double maxLongitude = currentLatLng.longitude + searchRadius;

        new Thread(new Runnable() {
            @Override
            public void run() {
                hotelReference
                        .whereGreaterThan("latitude", minLatitude)
                        .whereLessThan("latitude", maxLatitude)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    hotelFilterList = new ArrayList<>();
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                                        double longitude = documentSnapshot.getDouble("longitude");
                                        if(longitude >= minLongitude && longitude <= maxLongitude) {
                                            hotelFilterList.add(documentSnapshot.toObject(HotelModel.class));
                                        }
                                    }
                                } else {
                                    linearLayoutHotelNoResults.setVisibility(View.VISIBLE);
                                }

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        spinKitProgressHotels.setVisibility(View.GONE);

                                        hotelFilterAdapter = new HotelFilterAdapter(ExploreActivity.this, hotelFilterList);
                                        linearLayoutManagerHotels = new LinearLayoutManager(ExploreActivity.this);
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
                                        new Functions().ShowErrorDialog("Error Occurred", "Try Again", ExploreActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    private void initFeaturedRestaurants() {
        if (!isViewsInitialized) {
            relativeLayoutRestaurants = findViewById(R.id.relativeLayoutRestaurants);
            spinKitProgressRestaurants = findViewById(R.id.spinKitProgressRestaurants);
            linearLayoutRestaurantNoResults = findViewById(R.id.linearLayoutRestaurantNoResults);
            recyclerViewRestaurants = findViewById(R.id.recyclerViewRestaurants);
        }

        relativeLayoutRestaurants.setVisibility(View.VISIBLE);
        spinKitProgressRestaurants.setVisibility(View.VISIBLE);
        linearLayoutRestaurantNoResults.setVisibility(View.GONE);
    }

    private void loadCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMapCurrentLocation.setMyLocationEnabled(true);

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMapCurrentLocation.addMarker(new MarkerOptions().position(currentLatLng).title("My Location"));
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLatLng).zoom(16).build();
                            googleMapCurrentLocation.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            initFeaturedHotels();
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadCurrentLocation();
            } else {
            }
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMapCurrentLocation = googleMap;
        loadCurrentLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapViewCurrentLocation.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapViewCurrentLocation.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapViewCurrentLocation.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapViewCurrentLocation.onLowMemory();
    }
}