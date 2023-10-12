package com.rlabdevs.unifymobile.activities.hotels.rooms;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.bookings.hotel.RoomBookingActivity;
import com.rlabdevs.unifymobile.adapters.HotelFilterAdapter;
import com.rlabdevs.unifymobile.adapters.RoomReviewAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.enums.StatusCode;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.RoomModel;
import com.rlabdevs.unifymobile.models.RoomReviewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class RoomViewActivity extends AppCompatActivity {

    private RoomViewActivity roomViewActivity;

    private ImageView imgViewRoomImage;
    private LinearLayout lnrLytFreeWIFI, lnrLytAirConditioned, lnrLytFreeBreakfast, lnrLytTeaCoffee, lnrLytBar, lnrLytRoomService,
            lnrLytTelevision, lnrLytPool, lnrLytSpa, lnrLytParking;
    private TextView tvRoomType, tvRoomPrice, tvNoOfAdultsChildren, tvNoRoomReviews;
    private EditText txtRoomDescription;
    private RecyclerView recyclerViewRoomReviews;
    private SpinKitView spinKitProgress;
    private Button btnBookNow;

    private CollectionReference hotelReference;
    private List<RoomReviewModel> roomReviewList;
    private RoomReviewAdapter roomReviewAdapter;

    private LinearLayoutManager linearLayoutManager;

    private HotelModel hotelModel;
    private RoomModel roomModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_view);
        roomViewActivity= this;
        InitUI();
        InitRecyclerViewRoomReviews();
        InitRoomView();
        FetchRoomReviews();
    }

    private void InitUI() {
        imgViewRoomImage = findViewById(R.id.imgViewRoomImage);

        lnrLytFreeWIFI = findViewById(R.id.lnrLytFreeWIFI);
        lnrLytAirConditioned = findViewById(R.id.lnrLytAirConditioned);
        lnrLytFreeBreakfast = findViewById(R.id.lnrLytFreeBreakfast);
        lnrLytTeaCoffee = findViewById(R.id.lnrLytTeaCoffee);
        lnrLytBar = findViewById(R.id.lnrLytBar);
        lnrLytRoomService = findViewById(R.id.lnrLytRoomService);
        lnrLytTelevision = findViewById(R.id.lnrLytTelevision);
        lnrLytPool = findViewById(R.id.lnrLytPool);
        lnrLytSpa = findViewById(R.id.lnrLytSpa);
        lnrLytParking = findViewById(R.id.lnrLytParking);

        tvRoomType = findViewById(R.id.tvRoomType);
        tvRoomPrice = findViewById(R.id.tvRoomPrice);
        tvNoOfAdultsChildren = findViewById(R.id.tvNoOfAdultsChildren);
        tvNoRoomReviews = findViewById(R.id.tvNoRoomReviews);

        txtRoomDescription = findViewById(R.id.txtRoomDescription);
        spinKitProgress = findViewById(R.id.spinKitProgress);

        btnBookNow = findViewById(R.id.btnBookNow);
        btnBookNow.setOnClickListener(bookNowOnClickListener);
    }

    private void InitRecyclerViewRoomReviews() {
        recyclerViewRoomReviews = findViewById(R.id.recyclerViewRoomReviews);
        roomReviewList = new ArrayList<>();
        roomReviewAdapter = new RoomReviewAdapter(this, roomReviewList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewRoomReviews.setLayoutManager(linearLayoutManager);
        recyclerViewRoomReviews.setAdapter(roomReviewAdapter);
    }

    private void InitRoomView() {
        hotelModel = new Gson().fromJson(getIntent().getStringExtra("Hotel"), HotelModel.class);
        roomModel = new Gson().fromJson(getIntent().getStringExtra("Room"), RoomModel.class);

        Picasso.with(RoomViewActivity.this).load(roomModel.getRoomImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imgViewRoomImage.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });

        txtRoomDescription.setText(Html.fromHtml(roomModel.getRoomDescription()));
        tvRoomPrice.setText(roomModel.getRoomPrice() + Functions.GetCurrencyNameFromCode(roomModel.getCurrencyCode()));
        tvNoOfAdultsChildren.setText("Max: " + Functions.RoomCapacityText(roomModel.getNoOfAdults(), roomModel.getNoOfChildren()));

        if(roomModel.isFreeWIFI()){lnrLytFreeWIFI.setVisibility(View.VISIBLE);}
        if(roomModel.isAirConditioned()){lnrLytAirConditioned.setVisibility(View.VISIBLE);}
        if(roomModel.isFreeBreakfast()){lnrLytFreeBreakfast.setVisibility(View.VISIBLE);}
        if(roomModel.isTeaCoffee()){lnrLytTeaCoffee.setVisibility(View.VISIBLE);}
        if(roomModel.isBar()){lnrLytBar.setVisibility(View.VISIBLE);}
        if(roomModel.isRoomService()){lnrLytRoomService.setVisibility(View.VISIBLE);}
        if(roomModel.isTelevision()){lnrLytTelevision.setVisibility(View.VISIBLE);}
        if(roomModel.isPool()){lnrLytPool.setVisibility(View.VISIBLE);}
        if(roomModel.isParking()){lnrLytParking.setVisibility(View.VISIBLE);}
        if(roomModel.isSpa()){lnrLytSpa.setVisibility(View.VISIBLE);}
    }

    private void FetchRoomReviews() {
        CollectionReference roomReviewReference = firestoreDB.collection("Hotels").document(hotelModel.getID()).collection( "Rooms").document(roomModel.getID()).collection( "RoomReviews");
        roomReviewReference.whereEqualTo("statusCode", StatusCode.Active.getStatusCode()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                                roomReviewList.add(documentSnapshot.toObject(RoomReviewModel.class));
                            }
                            roomReviewAdapter.notifyDataSetChanged();
                        } else {
                            tvNoRoomReviews.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private View.OnClickListener bookNowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(RoomViewActivity.this, RoomBookingActivity.class);
            intent.putExtra("Hotel", new Gson().toJson(hotelModel));
            intent.putExtra("Room", new Gson().toJson(roomModel));
            startActivity(intent);
        }
    };
}