package com.rlabdevs.unifymobile.fragments.hotel;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.bookings.hotel.ViewRoomBookingsActivity;
import com.rlabdevs.unifymobile.adapters.RoomBookingAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.RoomBookingModel;
import com.rlabdevs.unifymobile.models.RoomModel;

import java.util.ArrayList;
import java.util.List;

public class CreatedRoomBookingsFragment extends Fragment {

    private Activity activity;
    private SpinKitView spinKitProgressBookings;
    private LinearLayout linearLayoutBookingsNoResults;
    private RecyclerView recyclerViewRoomBookings;

    private CollectionReference hotelReference;
    private List<HotelModel> hotelList;
    private List<RoomModel> roomList;
    private List<RoomBookingModel> roomBookingList;

    public CreatedRoomBookingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        initRoomBookings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_created_room_bookings, container, false);

        spinKitProgressBookings = rootView.findViewById(R.id.spinKitProgressBookings);
        linearLayoutBookingsNoResults = rootView.findViewById(R.id.linearLayoutBookingsNoResults);
        recyclerViewRoomBookings = rootView.findViewById(R.id.recyclerViewRoomBookings);

        return rootView;
    }

    private void initRoomBookings() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                new Functions().ShowProgressBar(activity, "Connecting to server...", "Please wait !");
            }
        });

        hotelReference = firestoreDB.collection("Hotels");
        hotelReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            hotelList = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                hotelList.add(documentSnapshot.toObject(HotelModel.class));
                            }
                        }

                        firestoreDB.collectionGroup("Rooms").get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        List<String> roomCodeList = new ArrayList<>();

                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            roomList = new ArrayList<>();
                                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                                roomCodeList.add(documentSnapshot.get("roomCode", String.class));
                                                roomList.add(documentSnapshot.toObject(RoomModel.class));
                                            }
                                        }

                                        firestoreDB.collectionGroup("RoomBookings")
                                                .whereArrayContainsAny("RoomCode", roomCodeList)
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        if (!queryDocumentSnapshots.isEmpty()) {
                                                            roomBookingList = new ArrayList<>();
                                                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                                                roomBookingList.add(documentSnapshot.toObject(RoomBookingModel.class));
                                                            }

                                                            RoomBookingAdapter roomBookingAdapter = new RoomBookingAdapter(activity, hotelList, roomList, roomBookingList);
                                                            LinearLayoutManager linearLayoutManagerBookings = new LinearLayoutManager(activity);
                                                            recyclerViewRoomBookings.setLayoutManager(linearLayoutManagerBookings);
                                                            recyclerViewRoomBookings.setAdapter(roomBookingAdapter);

                                                            roomBookingAdapter.notifyItemRangeInserted(0, roomBookingList.size());
                                                            spinKitProgressBookings.setVisibility(View.GONE);

                                                            activity.runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Functions.HideProgressBar();
                                                                }
                                                            });
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        e.printStackTrace();
                                                        activity.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Functions.HideProgressBar();
                                                                Toast.makeText(activity, "Failed to get room booking details.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Functions.HideProgressBar();
                                                Toast.makeText(activity, "Failed to get room details.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Functions.HideProgressBar();
                                Toast.makeText(activity, "Failed to get hotel details.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
}