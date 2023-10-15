package com.rlabdevs.unifymobile.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.UserHomeActivity;
import com.rlabdevs.unifymobile.activities.bookings.hotel.RoomBookingActivity;
import com.rlabdevs.unifymobile.activities.hotels.rooms.RoomViewActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.rooms.RoomActivity;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.enums.StatusCode;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.RoomBookingModel;
import com.rlabdevs.unifymobile.models.RoomModel;
import com.rlabdevs.unifymobile.models.RoomTypesModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class RoomBookingAdapter extends RecyclerView.Adapter<RoomBookingAdapter.RoomBookingViewHolder> {

    private Activity activity;
    private List<HotelModel> hotelList;
    private List<RoomModel> roomsList;
    private List<RoomTypesModel> roomTypesList;
    private List<RoomBookingModel> roomBookingsList;

    public RoomBookingAdapter(Activity activity, List<HotelModel> hotelList, List<RoomModel> roomsList, List<RoomTypesModel> roomTypesList, List<RoomBookingModel> roomBookingsList) {
        this.activity = activity;
        this.hotelList = hotelList;
        this.roomsList = roomsList;
        this.roomTypesList = roomTypesList;
        this.roomBookingsList = roomBookingsList;
    }

    @NonNull
    @Override
    public RoomBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_booking_item, parent, false);
        return new RoomBookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomBookingViewHolder holder, int position) {
        RoomBookingModel roomBooking = roomBookingsList.get(position);

        holder.tvBookingCode.setText(roomBooking.getBookingCode());

        String hotelName = hotelList.stream().filter(h -> h.getHotelCode().equals(roomBooking.getHotelCode())).findFirst().get().getHotelName();
        holder.tvHotelName.setText(hotelName);

        String roomType = roomTypesList.stream().filter(h -> h.getRoomTypeCode().equals(roomBooking.getRoomTypeCode())).findFirst().get().getRoomType();
        holder.tvRoomType.setText(roomType);

        holder.tvFullName.setText(roomBooking.getFullName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        holder.tvDateRange.setText(sdf.format(roomBooking.getCheckinDate()) + " - " + sdf.format(roomBooking.getCheckoutDate()));

        String statusCode = roomBooking.getBookingStatusCode();
        if(statusCode.equals(StatusCode.Pending.getStatusCode())) {
            holder.tvBookingStatus.setText("Pending");
        } else if(statusCode.equals(StatusCode.Canceled.getStatusCode())) {
            holder.tvBookingStatus.setText("Cancelled");
        } else if(statusCode.equals(StatusCode.Confirmed.getStatusCode())) {
            holder.tvBookingStatus.setText("Confirmed");
        }
    }

    @Override
    public int getItemCount() {
        return roomBookingsList.size();
    }

    class RoomBookingViewHolder extends RecyclerView.ViewHolder{
        TextView tvBookingCode, tvHotelName, tvRoomType, tvFullName, tvDateRange, tvBookingStatus;

        public RoomBookingViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBookingCode = itemView.findViewById(R.id.tvBookingCode);
            tvHotelName = itemView.findViewById(R.id.tvHotelName);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvDateRange = itemView.findViewById(R.id.tvDateRange);
            tvBookingStatus = itemView.findViewById(R.id.tvBookingStatus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, RoomBookingActivity.class);
                    RoomBookingModel roomBookingData = roomBookingsList.get(getAdapterPosition());

                    HotelModel hotelModel = hotelList.stream().filter(h -> h.getHotelCode().equals(roomBookingData.getHotelCode())).findFirst().get();
                    RoomModel roomModel = roomsList.stream().filter(h -> h.getRoomCode().equals(roomBookingData.getRoomCode())).findFirst().get();

                    intent.putExtra("Room", new Gson().toJson(roomModel));
                    intent.putExtra("Hotel", new Gson().toJson(hotelModel));
                    intent.putExtra("RoomBooking", new Gson().toJson(roomBookingData));
                    //activity.finish();
                    activity.startActivity(intent);
                }
            });
        }
    }
}
