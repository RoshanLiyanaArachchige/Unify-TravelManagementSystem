package com.rlabdevs.unifymobile.activities.directions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.common.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetDirectionsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private double desLatitude, desLongitude, orgLatitude, orgLongitude;

    private GoogleMap googleMap;
    private Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_directions);

        initGetDirections();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(GetDirectionsActivity.this);
        }
    }

    private void initGetDirections() {
        desLatitude = getIntent().getDoubleExtra("DesLatitude", 0);
        desLongitude = getIntent().getDoubleExtra("DesLongitude", 0);
        orgLatitude = getIntent().getDoubleExtra("OrgLatitude", 0);
        orgLongitude = getIntent().getDoubleExtra("OrgLongitude", 0);

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + desLatitude + "," + desLongitude + "&mode=d");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
            finish();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng origin = new LatLng(orgLatitude, orgLongitude);
        LatLng destination = new LatLng(desLatitude, desLongitude);

        googleMap.addMarker(new MarkerOptions().position(destination).title("Destination"));
        googleMap.addMarker(new MarkerOptions().position(origin).title("My Location"));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(origin).zoom(16).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        new GetDirections().execute(origin, destination);
    }

    private class GetDirections extends AsyncTask<LatLng, Void, List<LatLng>> {

        @Override
        protected List<LatLng> doInBackground(LatLng... params) {
            LatLng origin = params[0];
            LatLng destination = params[1];
            List<LatLng> polylinePoints = new ArrayList<>();

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                StringBuilder urlStringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
                urlStringBuilder.append("origin=")
                        .append(origin.latitude)
                        .append(",")
                        .append(origin.longitude);
                urlStringBuilder.append("&destination=")
                        .append(destination.latitude)
                        .append(",")
                        .append(destination.longitude);
                urlStringBuilder.append("&key=" + getResources().getString(R.string.GeoApiKey));
                URL url = new URL(urlStringBuilder.toString());

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder responseStringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseStringBuilder.append(line);
                }

                JSONObject jsonResponse = new JSONObject(responseStringBuilder.toString());

                String status = jsonResponse.getString("status");
                if (status.equals("OK")) {

                    JSONArray routesArray = jsonResponse.getJSONArray("routes");
                    if (routesArray.length() > 0) {
                        JSONObject route = routesArray.getJSONObject(0);
                        JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                        String encodedPolyline = overviewPolyline.getString("points");
                        polylinePoints.addAll(Functions.decodePolyline(encodedPolyline));
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return polylinePoints;
        }

        @Override
        protected void onPostExecute(List<LatLng> polylinePoints) {
            if (polylinePoints != null && polylinePoints.size() > 0) {
                PolylineOptions polylineOptions = new PolylineOptions()
                        .addAll(polylinePoints)
                        .width(5)
                        .color(Color.BLUE);

                if (polyline != null) {
                    polyline.remove();
                }

                polyline = googleMap.addPolyline(polylineOptions);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(getLatLngBounds(polylinePoints), 50));
            }
        }
    }

    private LatLngBounds getLatLngBounds(List<LatLng> points) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : points) {
            builder.include(point);
        }
        return builder.build();
    }
}