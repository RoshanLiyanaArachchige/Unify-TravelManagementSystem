package com.rlabdevs.unifymobile.activities.user.manage.hotels.rooms;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.MyHotelsActivity;
import com.rlabdevs.unifymobile.adapters.HotelRoomsAdapter;
import com.rlabdevs.unifymobile.adapters.MyHotelsAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.RoomModel;
import com.rlabdevs.unifymobile.models.RoomTypesModel;

import java.util.ArrayList;
import java.util.List;

public class HotelRoomsActivity extends AppCompatActivity implements View.OnClickListener  {

    private HotelRoomsActivity hotelRoomsActivity;

    private RelativeLayout relativeLayoutHotelRooms;
    private RecyclerView recyclerViewHotelRooms;
    private TextView tvNoAddRooms;
    private ImageView imgViewAddNewRoom;
    private SpinKitView spinKitProgress;

    private CollectionReference roomsReference, roomTypesReference;
    private List<RoomModel> hotelRoomsList;
    private HotelRoomsAdapter hotelRoomsAdapter;

    private List<RoomTypesModel> roomTypesList;

    public HotelModel hotel;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_rooms);
        hotel = new Gson().fromJson(getIntent().getStringExtra("Hotel"), HotelModel.class);
        roomsReference = firestoreDB.collection("Hotels/" + hotel.getID() + "/Rooms");
        roomTypesReference = firestoreDB.collection("Hotels").document(hotel.getID()).collection("RoomTypes");
        hotelRoomsActivity = this;
        InitUI();
        InitRecyclerViewHotelsRoomsList();
        FetchHotelRoomsAndTypes();
    }

    private void InitUI() {
        relativeLayoutHotelRooms = findViewById(R.id.relativeLayoutHotelRooms);
        tvNoAddRooms = findViewById(R.id.tvNoAddRooms);
        imgViewAddNewRoom = findViewById(R.id.imgViewAddNewRoom);
        spinKitProgress = findViewById(R.id.spinKitProgress);

        relativeLayoutHotelRooms.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        tvNoAddRooms.setOnClickListener(this);
        imgViewAddNewRoom.setOnClickListener(this);
    }

    private void InitRecyclerViewHotelsRoomsList() {
        recyclerViewHotelRooms = findViewById(R.id.recyclerViewHotelRooms);
        hotelRoomsList = new ArrayList<>();
        roomTypesList = new ArrayList<>();
        hotelRoomsAdapter = new HotelRoomsAdapter(this, hotelRoomsList, roomTypesList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewHotelRooms.setLayoutManager(linearLayoutManager);
        recyclerViewHotelRooms.setAdapter(hotelRoomsAdapter);
    }

    private void FetchHotelRoomsAndTypes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                hotelRoomsActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        spinKitProgress.setVisibility(View.VISIBLE);
                        new Functions().ShowProgressBar(HotelRoomsActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                roomTypesReference.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(!queryDocumentSnapshots.isEmpty())
                                {
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot documentSnapshot: documentSnapshotList) {
                                        RoomTypesModel roomType = documentSnapshot.toObject(RoomTypesModel.class);
                                        roomType.setID(documentSnapshot.getId());
                                        roomTypesList.add(roomType);
                                    }

                                    roomsReference.get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    if(!queryDocumentSnapshots.isEmpty())
                                                    {
                                                        List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                                        for (DocumentSnapshot documentSnapshot: documentSnapshotList) {
                                                            RoomModel room = documentSnapshot.toObject(RoomModel.class);
                                                            room.setID(documentSnapshot.getId());
                                                            hotelRoomsList.add(room);
                                                            hotelRoomsAdapter.notifyDataSetChanged();
                                                        }
                                                        spinKitProgress.setVisibility(View.GONE);
                                                        Functions.HideProgressBar();
                                                    }
                                                    else
                                                    {
                                                        hotelRoomsActivity.runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                spinKitProgress.setVisibility(View.GONE);
                                                                Functions.HideProgressBar();
                                                                tvNoAddRooms.setVisibility(View.VISIBLE);
                                                            }
                                                        });
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    spinKitProgress.setVisibility(View.GONE);
                                                    Functions.HideProgressBar();
                                                    new Functions().ShowErrorDialog("Rooms Load Failure !", "Try Again", HotelRoomsActivity.this);
                                                }
                                            });
                                }
                                else
                                {
                                    hotelRoomsActivity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            spinKitProgress.setVisibility(View.GONE);
                                            Functions.HideProgressBar();
                                        }
                                    });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                hotelRoomsActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                        new Functions().ShowErrorDialog("Error Occurred", "Try Again", HotelRoomsActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imgViewAddNewRoom: {
                NewHotelRoom();
                break;
            }
            case R.id.tvNoAddRooms: {
                NewHotelRoom();
                break;
            }
        }
    }

    private void NewHotelRoom() {
        Intent hotelRoomIntent = new Intent(hotelRoomsActivity, RoomActivity.class);
        hotelRoomIntent.putExtra("Hotel", new Gson().toJson(hotel));
        //finish();
        startActivity(hotelRoomIntent);
    }
}