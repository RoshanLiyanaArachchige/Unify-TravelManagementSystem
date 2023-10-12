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
import com.rlabdevs.unifymobile.activities.hotels.HotelViewActivity;
import com.rlabdevs.unifymobile.activities.hotels.rooms.RoomViewActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.rooms.HotelRoomsActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.rooms.RoomActivity;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.RoomModel;
import com.rlabdevs.unifymobile.models.RoomTypesModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class HotelRoomsAdapter extends RecyclerView.Adapter<HotelRoomsAdapter.HotelRoomsViewHolder> {

    private Activity activity;
    private List<RoomModel> hotelRoomsList;
    private List<RoomTypesModel> roomTypesList;
    private HotelModel hotel;

    public HotelRoomsAdapter(Activity activity, List<RoomModel> hotelRoomsList, List<RoomTypesModel> roomTypesList) {
        this.activity = activity;
        this.hotelRoomsList = hotelRoomsList;
        this.roomTypesList = roomTypesList;
        if(activity.getClass().getSimpleName().equals("HotelRoomsActivity")) { this.hotel = ((HotelRoomsActivity)activity).hotel; }
        else if(activity.getClass().getSimpleName().equals("HotelViewActivity")) { this.hotel = ((HotelViewActivity)activity).hotel; }
    }

    @NonNull
    @Override
    public HotelRoomsAdapter.HotelRoomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item_view, parent, false);
        return new HotelRoomsAdapter.HotelRoomsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelRoomsAdapter.HotelRoomsViewHolder holder, int position) {
        RoomModel room = hotelRoomsList.get(position);
        Picasso.with(activity).load(room.getRoomImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.linearLytImageProgress.setVisibility(View.GONE);
                holder.imgViewRoom.setImageBitmap(bitmap);
            }
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                holder.linearLytImageProgress.setVisibility(View.GONE);
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });

        holder.tvRoomCode.setText(room.getRoomCode());
        for (RoomTypesModel roomType: roomTypesList) {
            if(roomType.getRoomTypeCode().equals(room.getRoomTypeCode())) {
                holder.tvRoomType.setText(roomType.getRoomType());
            }
        }

        holder.tvRoomCapacity.setText("Max: " + Functions.RoomCapacityText(room.getNoOfAdults(), room.getNoOfChildren()));

        String currencySymbol = UserHomeActivity.currencyList.stream().filter(c -> c.getCurrencyCode().equals(hotel.getCurrencyCode())).findFirst().get().getSymbol();
        holder.tvRoomPrice.setText(String.valueOf(room.getRoomPrice()) + " " + currencySymbol);

        //Score and no of reviews
        //holder.tvScoreNoReviews.setText("No reviews yet");

        holder.lnrLytFreeWIFI.setVisibility(room.isFreeWIFI() ? View.VISIBLE : View.GONE);
        holder.lnrLytAirConditioner.setVisibility(room.isAirConditioned() ? View.VISIBLE : View.GONE);
        holder.lnrLytBreakfast.setVisibility(room.isFreeBreakfast() ? View.VISIBLE : View.GONE);
        holder.lnrLytTeaCoffee.setVisibility(room.isTeaCoffee() ? View.VISIBLE : View.GONE);
        holder.lnrLytBar.setVisibility(room.isBar() ? View.VISIBLE : View.GONE);
        holder.lnrLytRoomService.setVisibility(room.isRoomService() ? View.VISIBLE : View.GONE);
        holder.lnrLytTelevision.setVisibility(room.isTelevision() ? View.VISIBLE : View.GONE);
        holder.lnrLytPool.setVisibility(room.isPool() ? View.VISIBLE : View.GONE);
        holder.lnrLytParking.setVisibility(room.isParking() ? View.VISIBLE : View.GONE);
        holder.lnrLytSPA.setVisibility(room.isSpa() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return hotelRoomsList.size();
    }

    class HotelRoomsViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLytImageProgress, lnrLytFreeWIFI, lnrLytAirConditioner, lnrLytBreakfast, lnrLytTeaCoffee, lnrLytBar, lnrLytRoomService, lnrLytTelevision, lnrLytPool, lnrLytParking, lnrLytSPA;
        ImageView imgViewRoom;
        TextView tvRoomCode, tvRoomType, tvRoomCapacity, tvRoomPrice, tvScoreNoReviews, tvFreeWIFI, tvAirConditioner, tvFreeBreakfast, tvFreeTeaCoffee, tvBar, tvRoomService, tvTelevision, tvPool, tvParking, tvSPA;

        public HotelRoomsViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLytImageProgress = itemView.findViewById(R.id.linearLytImageProgress);
            lnrLytFreeWIFI = itemView.findViewById(R.id.lnrLytFreeWIFI);
            lnrLytAirConditioner = itemView.findViewById(R.id.lnrLytAirConditioner);
            lnrLytBreakfast = itemView.findViewById(R.id.lnrLytBreakfast);
            lnrLytTeaCoffee = itemView.findViewById(R.id.lnrLytTeaCoffee);
            lnrLytBar = itemView.findViewById(R.id.lnrLytBar);
            lnrLytRoomService = itemView.findViewById(R.id.lnrLytRoomService);
            lnrLytTelevision = itemView.findViewById(R.id.lnrLytTelevision);
            lnrLytPool = itemView.findViewById(R.id.lnrLytPool);
            lnrLytParking = itemView.findViewById(R.id.lnrLytParking);
            lnrLytSPA = itemView.findViewById(R.id.lnrLytSPA);

            imgViewRoom = itemView.findViewById(R.id.imgViewRoom);

            tvRoomCode = itemView.findViewById(R.id.tvRoomCode);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            tvRoomCapacity = itemView.findViewById(R.id.tvRoomCapacity);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            //tvScoreNoReviews = itemView.findViewById(R.id.tvScoreNoReviews);
            tvFreeWIFI = itemView.findViewById(R.id.tvFreeWIFI);
            tvAirConditioner = itemView.findViewById(R.id.tvAirConditioner);
            tvFreeBreakfast = itemView.findViewById(R.id.tvFreeBreakfast);
            tvFreeTeaCoffee = itemView.findViewById(R.id.tvFreeTeaCoffee);
            tvBar = itemView.findViewById(R.id.tvBar);
            tvRoomService = itemView.findViewById(R.id.tvRoomService);
            tvTelevision = itemView.findViewById(R.id.tvTelevision);
            tvPool = itemView.findViewById(R.id.tvPool);
            tvParking = itemView.findViewById(R.id.tvParking);
            tvSPA = itemView.findViewById(R.id.tvSPA);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if(activity.getClass().getSimpleName().equals("HotelRoomsActivity")) {intent = new Intent(activity, RoomActivity.class); }
                    else if(activity.getClass().getSimpleName().equals("HotelViewActivity")) { intent = new Intent(activity, RoomViewActivity.class); }

                    intent.putExtra("Room", new Gson().toJson(hotelRoomsList.get(getAdapterPosition())));
                    intent.putExtra("Hotel", new Gson().toJson(hotel));
                    //activity.finish();
                    activity.startActivity(intent);
                }
            });
        }
    }
}
