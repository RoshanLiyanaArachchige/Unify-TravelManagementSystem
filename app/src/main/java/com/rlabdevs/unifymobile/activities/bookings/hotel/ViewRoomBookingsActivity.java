package com.rlabdevs.unifymobile.activities.bookings.hotel;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.account.UserProfileActivity;
import com.rlabdevs.unifymobile.activities.explore.ExploreActivity;
import com.rlabdevs.unifymobile.adapters.HotelFilterAdapter;
import com.rlabdevs.unifymobile.adapters.RoomBookingAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.RoomBookingModel;
import com.rlabdevs.unifymobile.models.RoomModel;

import java.util.ArrayList;
import java.util.List;

public class ViewRoomBookingsActivity extends AppCompatActivity {

    private SpinKitView spinKitProgressBookings;
    private LinearLayout linearLayoutBookingsNoResults;
    private RecyclerView recyclerViewRoomBookings;

    private CollectionReference hotelReference;
    private List<HotelModel> hotelList;
    private List<RoomModel> roomList;
    private List<RoomBookingModel> roomBookingList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room_bookings);

        initRoomBookings();
    }

    private void initRoomBookings() {
        spinKitProgressBookings = findViewById(R.id.spinKitProgressBookings);
        linearLayoutBookingsNoResults = findViewById(R.id.linearLayoutBookingsNoResults);
        recyclerViewRoomBookings = findViewById(R.id.recyclerViewRoomBookings);

        runOnUiThread(new Runnable() {
            public void run() {
                new Functions().ShowProgressBar(ViewRoomBookingsActivity.this, "Connecting to server...", "Please wait !");
            }
        });

        hotelReference = firestoreDB.collection("Hotels");
        hotelReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    hotelList = new ArrayList<>();
                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        hotelList.add(documentSnapshot.toObject(HotelModel.class));
                    }
                }

                firestoreDB.collectionGroup("Rooms").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(!queryDocumentSnapshots.isEmpty()) {
                                    roomList = new ArrayList<>();
                                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                        roomList.add(documentSnapshot.toObject(RoomModel.class));
                                    }
                                }

                                firestoreDB.collectionGroup("RoomBookings").get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if(!queryDocumentSnapshots.isEmpty()) {
                                                    roomBookingList = new ArrayList<>();
                                                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                                        roomBookingList.add(documentSnapshot.toObject(RoomBookingModel.class));
                                                    }

                                                    RoomBookingAdapter roomBookingAdapter = new RoomBookingAdapter(ViewRoomBookingsActivity.this, hotelList, roomList, roomBookingList);
                                                    LinearLayoutManager linearLayoutManagerBookings = new LinearLayoutManager(ViewRoomBookingsActivity.this);
                                                    recyclerViewRoomBookings.setLayoutManager(linearLayoutManagerBookings);
                                                    recyclerViewRoomBookings.setAdapter(roomBookingAdapter);

                                                    roomBookingAdapter.notifyItemRangeInserted(0, roomBookingList.size());
                                                    spinKitProgressBookings.setVisibility(View.GONE);

                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Functions.HideProgressBar();
                                                        }
                                                    });
                                                }
                                            }
                                        });
                            }
                        });
            }
        });
    }
}