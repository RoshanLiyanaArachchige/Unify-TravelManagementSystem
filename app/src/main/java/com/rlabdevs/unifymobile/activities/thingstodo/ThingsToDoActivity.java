package com.rlabdevs.unifymobile.activities.thingstodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.maps.model.LatLng;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.adapters.ThingsToDoAdapter;
import com.rlabdevs.unifymobile.common.Constants;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.enums.ApiResponse;
import com.rlabdevs.unifymobile.models.master.NewThingsToDoFilterModel;
import com.rlabdevs.unifymobile.services.RetrofitClient;
import com.rlabdevs.unifymobile.services.interfaces.other.IMasterService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThingsToDoActivity extends AppCompatActivity {

    private SpinKitView spinKitProgressThingsToDo;
    private RecyclerView recyclerViewThingsToDo;

    public static LatLng currentLatLng;
    private ThingsToDoAdapter thingsToDoAdapter;
    private LinearLayoutManager linearLayoutManager;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private IMasterService masterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_things_to_do);

        masterService = RetrofitClient.getClient().create(IMasterService.class);

        initThingsToDo();
    }

    private void initThingsToDo() {
        spinKitProgressThingsToDo = findViewById(R.id.spinKitProgressThingsToDo);
        LinearLayout linearLayoutThingsToDoNoResults = findViewById(R.id.linearLayoutThingsToDoNoResults);
        recyclerViewThingsToDo = findViewById(R.id.recyclerViewThingsToDo);

        spinKitProgressThingsToDo.setVisibility(View.GONE);
        linearLayoutThingsToDoNoResults.setVisibility(View.GONE);

        loadCurrentLocation();
    }

    private void getThingsToDo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(ThingsToDoActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                Call<NewThingsToDoFilterModel> thingToDoRequest = masterService.getThingsToDoList(currentLatLng.latitude, currentLatLng.longitude);

                thingToDoRequest.enqueue(new Callback<NewThingsToDoFilterModel>() {
                    @Override
                    public void onResponse(Call<NewThingsToDoFilterModel> call, Response<NewThingsToDoFilterModel> response) {
                        NewThingsToDoFilterModel apiResponse = response.body();
                        if(apiResponse.getApiResponseStatus().equals(ApiResponse.Success.getValue())) {
                            Functions.HideProgressBar();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    spinKitProgressThingsToDo.setVisibility(View.GONE);
                                    thingsToDoAdapter = new ThingsToDoAdapter(ThingsToDoActivity.this, apiResponse.getThingsToDoList());
                                    linearLayoutManager = new LinearLayoutManager(ThingsToDoActivity.this);
                                    recyclerViewThingsToDo.setLayoutManager(linearLayoutManager);
                                    recyclerViewThingsToDo.setAdapter(thingsToDoAdapter);

                                    thingsToDoAdapter.notifyItemRangeInserted(0, apiResponse.getThingsToDoList().size());
                                }
                            });
                        } else {
                            Functions.HideProgressBar();
                            Toast.makeText(ThingsToDoActivity.this, apiResponse.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<NewThingsToDoFilterModel> call, Throwable t) {
                        Functions.HideProgressBar();
                        Toast.makeText(ThingsToDoActivity.this, "An error occurred while getting things to do's.", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
            }
        }).start();
    }

    private void loadCurrentLocation() {
        new Functions().ShowProgressBar(ThingsToDoActivity.this, "Getting current location...", "Please wait !");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            initializeLocationListener();
            getCurrentLocation();
        }
    }

    private void initializeLocationListener() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Functions.HideProgressBar();
                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                locationManager.removeUpdates(this);
                getThingsToDo();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
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
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}