package com.rlabdevs.unifymobile.activities.bookings.hotel;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.account.UserProfileActivity;
import com.rlabdevs.unifymobile.activities.explore.ExploreActivity;
import com.rlabdevs.unifymobile.adapters.HotelFilterAdapter;
import com.rlabdevs.unifymobile.adapters.RoomBookingAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.fragments.hotel.CreatedRoomBookingsFragment;
import com.rlabdevs.unifymobile.fragments.hotel.ReceivedRoomBookingsFragment;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.RoomBookingModel;
import com.rlabdevs.unifymobile.models.RoomModel;

import java.util.ArrayList;
import java.util.List;

public class ViewRoomBookingsActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btnCreatedBookings, btnReceivedBookings;
    private FragmentManager fragmentManager;
    private FrameLayout fragmentContainer;
    public static String selectedStatusCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room_bookings);

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
        loadFragment(new CreatedRoomBookingsFragment());
    }

    private void loadReceivedBookings() {
        btnReceivedBookings.setChecked(true);
        loadFragment(new ReceivedRoomBookingsFragment());
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