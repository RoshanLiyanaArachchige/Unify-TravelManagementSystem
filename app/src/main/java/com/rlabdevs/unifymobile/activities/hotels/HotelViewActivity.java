package com.rlabdevs.unifymobile.activities.hotels;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.directions.GetDirectionsActivity;
import com.rlabdevs.unifymobile.activities.thingstodo.ThingsToDoActivity;
import com.rlabdevs.unifymobile.activities.thingstodo.ThingsToDoViewActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.rooms.HotelRoomsActivity;
import com.rlabdevs.unifymobile.adapters.HotelRoomsAdapter;
import com.rlabdevs.unifymobile.adapters.MyHotelsAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.RoomModel;
import com.rlabdevs.unifymobile.models.RoomTypesModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class HotelViewActivity extends AppCompatActivity {

    private HotelViewActivity hotelViewActivity;

    private RelativeLayout relativeLytNoRooms, relativeLayoutHotel;
    private TextView tvHotelName, tvHotelClass;
    private EditText txtHotelDescription, txtHotelLocation, txtTelephoneNo, txtCheckInOut;
    private ImageView imgViewHotelImage;
    private SpinKitView spinKitProgress;
    private RecyclerView recyclerViewHotelRooms;
    private Button btnGetDirections;

    private CollectionReference roomsReference, roomTypesReference;
    private List<RoomModel> hotelRoomsList;
    private HotelRoomsAdapter hotelRoomsAdapter;

    private List<RoomTypesModel> roomTypesList;

    public HotelModel hotel;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_view);
        hotel = new Gson().fromJson(getIntent().getStringExtra("Hotel"), HotelModel.class);
        roomsReference = firestoreDB.collection("Rooms");
        roomTypesReference = firestoreDB.collection("RoomTypes");
        hotelViewActivity = this;
        InitUI();
        InitRecyclerViewHotelRooms();
        FetchHotelRoomsAndTypes();
    }

    private void InitUI() {
        relativeLayoutHotel = findViewById(R.id.relativeLayoutHotel);
        relativeLytNoRooms = findViewById(R.id.relativeLytNoRooms);
        tvHotelName = findViewById(R.id.tvHotelName);
        tvHotelClass = findViewById(R.id.tvHotelClass);
        txtHotelDescription = findViewById(R.id.txtHotelDescription);
        txtHotelLocation = findViewById(R.id.txtHotelLocation);
        txtTelephoneNo = findViewById(R.id.txtTelephoneNo);
        txtCheckInOut = findViewById(R.id.txtCheckInOut);
        imgViewHotelImage = findViewById(R.id.imgViewHotelImage);
        spinKitProgress = findViewById(R.id.spinKitProgress);

        btnGetDirections = findViewById(R.id.btnGetDirections);
        btnGetDirections.setOnClickListener(getDirectionsOnClickListener);

        Picasso.with(hotelViewActivity).load(hotel.getHotelImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                //linearLytImageProgress.setVisibility(View.GONE);
                imgViewHotelImage.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                //linearLytImageProgress.setVisibility(View.GONE);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });

        tvHotelName.setText(hotel.getHotelName());
        txtHotelDescription.setText(Html.fromHtml(hotel.getHotelDescription()));
        txtHotelLocation.setText("Location: " + hotel.getHotelLocation());
        tvHotelClass.setText("Class: " + hotel.getHotelClass() + " Star");
        txtTelephoneNo.setText("Tel No: " + hotel.getHotelTelNo());
        txtCheckInOut.setText("In/Out: " + hotel.getCheckIn() + "/" + hotel.getCheckOut());
    }

    private View.OnClickListener getDirectionsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent thingsToDoViewIntent = new Intent(HotelViewActivity.this, GetDirectionsActivity.class);
            thingsToDoViewIntent.putExtra("DesLatitude", hotel.getLatitude());
            thingsToDoViewIntent.putExtra("DesLongitude", hotel.getLongitude());
            startActivity(thingsToDoViewIntent);
        }
    };

    private void InitRecyclerViewHotelRooms() {
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
                hotelViewActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        spinKitProgress.setVisibility(View.VISIBLE);
                        new Functions().ShowProgressBar(HotelViewActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                roomTypesReference.whereEqualTo("hotelCode", hotel.getHotelCode()).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                                        RoomTypesModel roomType = documentSnapshot.toObject(RoomTypesModel.class);
                                        roomType.setID(documentSnapshot.getId());
                                        roomTypesList.add(roomType);
                                    }

                                    roomsReference.whereEqualTo("hotelCode", hotel.getHotelCode()).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    if (!queryDocumentSnapshots.isEmpty()) {
                                                        List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                                        for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                                                            RoomModel room = documentSnapshot.toObject(RoomModel.class);
                                                            room.setID(documentSnapshot.getId());
                                                            hotelRoomsList.add(room);
                                                            hotelRoomsAdapter.notifyDataSetChanged();
                                                        }
                                                        spinKitProgress.setVisibility(View.GONE);
                                                        Functions.HideProgressBar();
                                                    } else {
                                                        hotelViewActivity.runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                spinKitProgress.setVisibility(View.GONE);
                                                                Functions.HideProgressBar();
                                                                relativeLytNoRooms.setVisibility(View.VISIBLE);
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
                                                    new Functions().ShowErrorDialog("Rooms Load Failure !", "Try Again", HotelViewActivity.this);
                                                }
                                            });
                                } else {
                                    hotelViewActivity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            spinKitProgress.setVisibility(View.GONE);
                                            Functions.HideProgressBar();
                                            relativeLytNoRooms.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                hotelViewActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                        new Functions().ShowErrorDialog("Error Occurred", "Try Again", HotelViewActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }
}