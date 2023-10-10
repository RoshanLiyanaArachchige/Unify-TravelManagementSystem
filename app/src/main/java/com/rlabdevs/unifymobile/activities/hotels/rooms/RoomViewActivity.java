package com.rlabdevs.unifymobile.activities.hotels.rooms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.firestore.CollectionReference;
import com.google.gson.Gson;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.adapters.HotelFilterAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.RoomModel;

import java.util.ArrayList;
import java.util.List;

public class RoomViewActivity extends AppCompatActivity {

    private RoomViewActivity roomViewActivity;

    private LinearLayout lnrLytFreeWIFI, lnrLytAirConditioned, lnrLytFreeBreakfast, lnrLytTeaCoffee, lnrLytBar, lnrLytRoomService,
            lnrLytTelevision, lnrLytPool, lnrLytSpa, lnrLytParking;
    private TextView tvRoomType, tvRoomPrice, tvNoOfAdultsChildren, tvNoRoomReviews;
    private EditText txtRoomDescription;
    private RecyclerView recyclerViewRoomReviews;
    private SpinKitView spinKitProgress;

    private CollectionReference hotelReference;
    private List<HotelModel> hotelFilterList;
    private HotelFilterAdapter hotelFilterAdapter;

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
    }

    private void InitRecyclerViewRoomReviews() {
        recyclerViewRoomReviews = findViewById(R.id.recyclerViewRoomReviews);
        hotelFilterList = new ArrayList<>();
        hotelFilterAdapter = new HotelFilterAdapter(this, hotelFilterList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewRoomReviews.setLayoutManager(linearLayoutManager);
        recyclerViewRoomReviews.setAdapter(hotelFilterAdapter);
    }

    private void InitRoomView() {
        hotelModel = new Gson().fromJson(getIntent().getStringExtra("Hotel"), HotelModel.class);
        roomModel = new Gson().fromJson(getIntent().getStringExtra("Room"), RoomModel.class);

        txtRoomDescription.setText(roomModel.getRoomDescription());
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

    }
}