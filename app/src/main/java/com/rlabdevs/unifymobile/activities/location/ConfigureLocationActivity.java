package com.rlabdevs.unifymobile.activities.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.UserHomeActivity;
import com.rlabdevs.unifymobile.activities.hotels.HotelFilterActivity;
import com.rlabdevs.unifymobile.common.Constants;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.LocationModel;
import com.rlabdevs.unifymobile.models.SelectorItemModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigureLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap googleMap;
    private Button btnSelectLocation;
    public static TextView tvHotelCity;
    public static String locationCode;
    private String locationName;
    private Double selectedLatitude, selectedLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_location);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationCode = getIntent().getStringExtra("LocationCode");
        locationName = getIntent().getStringExtra("LocationName");
        selectedLatitude = getIntent().getDoubleExtra("Latitude", 0);
        selectedLongitude = getIntent().getDoubleExtra("Longitude", 0);

        btnSelectLocation = findViewById(R.id.btnSelectLocation);
        btnSelectLocation.setOnClickListener(onSelectLocation);

        tvHotelCity = findViewById(R.id.tvHotelCity);
        if(locationName != null && !locationName.equals("")) {
            tvHotelCity.setText(locationName);
        }
        tvHotelCity.setOnClickListener(cityOnClickListener);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        mapFragment.getMapAsync(this);
    }

    private View.OnClickListener cityOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            List<SelectorItemModel> itemList = new ArrayList<>();
            for(LocationModel locationData : UserHomeActivity.locationList) {
                itemList.add(new SelectorItemModel(locationData.getLocationCode(), locationData.getLocationName()));
            }
            new Functions().ShowItemSelector("Select City", itemList, ConfigureLocationActivity.this, tvHotelCity, null);
        }
    };

    private View.OnClickListener onSelectLocation = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            locationName = tvHotelCity.getText().toString();

            if(Objects.equals(locationCode, null) || Objects.equals(locationName, null) || locationCode.equals("") || locationName.equals("")) {
                new Functions().ShowErrorDialog("City Selection Required !", "Okay", ConfigureLocationActivity.this);
            } else if(selectedLatitude == 0 || selectedLongitude == 0) {
                new Functions().ShowErrorDialog("Location Pinning Required !", "Okay", ConfigureLocationActivity.this);
            } else {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("LocationCode", locationCode);
                resultIntent.putExtra("LocationName", locationName);
                resultIntent.putExtra("Latitude", selectedLatitude);
                resultIntent.putExtra("Longitude", selectedLongitude);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        }
    };

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (selectedLatitude != 0 && selectedLongitude != 0) {
            LatLng defaultLocation = new LatLng(selectedLatitude, selectedLongitude);
            googleMap.addMarker(new MarkerOptions().position(defaultLocation).title("Selected Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(defaultLocation).zoom(16).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else {
            loadCurrentLocation();
        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
                selectedLatitude = latLng.latitude;
                selectedLongitude = latLng.longitude;
            }
        });
    }

    private void loadCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            googleMap.setMyLocationEnabled(true);
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLatLng).zoom(16).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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