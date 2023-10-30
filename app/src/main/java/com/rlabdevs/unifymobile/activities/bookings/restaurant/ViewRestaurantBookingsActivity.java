package com.rlabdevs.unifymobile.activities.bookings.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.button.MaterialButton;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.fragments.hotel.CreatedRoomBookingsFragment;
import com.rlabdevs.unifymobile.fragments.hotel.ReceivedRoomBookingsFragment;
import com.rlabdevs.unifymobile.fragments.restaurant.CreatedRestaurantBookingsFragment;
import com.rlabdevs.unifymobile.fragments.restaurant.ReceivedRestaurantBookingsFragment;

public class ViewRestaurantBookingsActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btnCreatedBookings, btnReceivedBookings;
    private FragmentManager fragmentManager;
    private FrameLayout fragmentContainer;
    public static String selectedStatusCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurant_bookings);

        initRoomBookings();
    }

    private void initRoomBookings() {
        btnCreatedBookings = findViewById(R.id.btnCreatedBookings);
        btnReceivedBookings = findViewById(R.id.btnReceivedBookings);
        fragmentContainer = findViewById(R.id.fragmentContainer);

        fragmentManager = getSupportFragmentManager();

        btnCreatedBookings.setOnClickListener(this);
        btnReceivedBookings.setOnClickListener(this);

        loadCreatedBookings();
    }

    private void loadCreatedBookings() {
        btnCreatedBookings.setChecked(true);
        loadFragment(new CreatedRestaurantBookingsFragment());
    }

    private void loadReceivedBookings() {
        btnReceivedBookings.setChecked(true);
        loadFragment(new ReceivedRestaurantBookingsFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreatedBookings: {
                loadCreatedBookings();
                break;
            }
            case R.id.btnReceivedBookings: {
                loadReceivedBookings();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}