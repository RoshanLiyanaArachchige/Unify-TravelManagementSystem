package com.rlabdevs.unifymobile.activities.thingstodo;

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
import android.widget.RelativeLayout;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.explore.ExploreActivity;
import com.rlabdevs.unifymobile.adapters.HotelFilterAdapter;
import com.rlabdevs.unifymobile.adapters.RestaurantFilterAdapter;
import com.rlabdevs.unifymobile.adapters.ThingsToDoAdapter;
import com.rlabdevs.unifymobile.common.Constants;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.RestaurantModel;
import com.rlabdevs.unifymobile.models.ThingsToDoModel;

import java.util.ArrayList;
import java.util.List;

public class ThingsToDoActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LinearLayout linearLayoutThingsToDoNoResults;
    private SpinKitView spinKitProgressThingsToDo;
    private RecyclerView recyclerViewThingsToDo;
    private RelativeLayout relativeLayoutThingsToDo;

    public static LatLng currentLatLng;
    private double searchRadius = 0.1;
    private double minLatitude, maxLatitude, minLongitude, maxLongitude;

    private List<ThingsToDoModel> thingsToDoList = new ArrayList<>();
    private ThingsToDoAdapter thingsToDoAdapter;
    private CollectionReference thingsToDoReference;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_things_to_do);

        thingsToDoReference = firestoreDB.collection("ThingsToDo");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        loadCurrentLocation();
    }

    private void initThingsToDo() {
        relativeLayoutThingsToDo = findViewById(R.id.relativeLayoutThingsToDo);
        spinKitProgressThingsToDo = findViewById(R.id.spinKitProgressThingsToDo);
        linearLayoutThingsToDoNoResults = findViewById(R.id.linearLayoutThingsToDoNoResults);
        recyclerViewThingsToDo = findViewById(R.id.recyclerViewThingsToDo);

        spinKitProgressThingsToDo.setVisibility(View.VISIBLE);
        linearLayoutThingsToDoNoResults.setVisibility(View.GONE);

        getThingsToDo();
    }

    private void getThingsToDo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                thingsToDoReference
                        .whereGreaterThan("latitude", minLatitude)
                        .whereLessThan("latitude", maxLatitude)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                                        double longitude = documentSnapshot.getDouble("longitude");
                                        if(longitude >= minLongitude && longitude <= maxLongitude) {
                                            thingsToDoList.add(documentSnapshot.toObject(ThingsToDoModel.class));
                                        }
                                    }
                                } else {
                                    linearLayoutThingsToDoNoResults.setVisibility(View.VISIBLE);
                                }

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        spinKitProgressThingsToDo.setVisibility(View.GONE);

                                        thingsToDoAdapter = new ThingsToDoAdapter(ThingsToDoActivity.this, thingsToDoList);
                                        linearLayoutManager = new LinearLayoutManager(ThingsToDoActivity.this);
                                        recyclerViewThingsToDo.setLayoutManager(linearLayoutManager);
                                        recyclerViewThingsToDo.setAdapter(thingsToDoAdapter);

                                        thingsToDoAdapter.notifyItemRangeInserted(0, thingsToDoList.size());
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        spinKitProgressThingsToDo.setVisibility(View.GONE);
                                        new Functions().ShowErrorDialog("Error Occurred", "Try Again", ThingsToDoActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    private void loadCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                            minLatitude = currentLatLng.latitude - searchRadius;
                            maxLatitude = currentLatLng.latitude + searchRadius;
                            minLongitude = currentLatLng.longitude - searchRadius;
                            maxLongitude = currentLatLng.longitude + searchRadius;

                            initThingsToDo();
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
}