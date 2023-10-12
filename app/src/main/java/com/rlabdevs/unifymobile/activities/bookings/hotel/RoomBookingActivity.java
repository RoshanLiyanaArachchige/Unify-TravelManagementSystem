package com.rlabdevs.unifymobile.activities.bookings.hotel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.gson.Gson;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.RoomModel;

public class RoomBookingActivity extends AppCompatActivity {

    private HotelModel hotelModel;
    private RoomModel roomModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_booking);

        InitRoomBookingActivity();
    }

    private void InitRoomBookingActivity() {
        hotelModel = new Gson().fromJson(getIntent().getStringExtra("Hotel"), HotelModel.class);
        roomModel = new Gson().fromJson(getIntent().getStringExtra("Room"), RoomModel.class);
    }
}