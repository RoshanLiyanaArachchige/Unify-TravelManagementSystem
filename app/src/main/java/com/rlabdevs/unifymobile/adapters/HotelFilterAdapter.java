package com.rlabdevs.unifymobile.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.UserHomeActivity;
import com.rlabdevs.unifymobile.activities.hotels.HotelViewActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.HotelActivity;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HotelFilterAdapter extends RecyclerView.Adapter<HotelFilterAdapter.HotelFilterViewHolder> {

    private Activity activity;
    private List<HotelModel> hotelList;

    public HotelFilterAdapter(Activity activity, List<HotelModel> hotelList) {
        this.activity = activity;
        this.hotelList = hotelList;
    }

    @NonNull
    @Override
    public HotelFilterAdapter.HotelFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_item_view, parent, false);
        return new HotelFilterAdapter.HotelFilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelFilterAdapter.HotelFilterViewHolder holder, int position) {
        HotelModel hotel = hotelList.get(position);
        holder.tvHotelName.setText(hotel.getHotelName());
        holder.tvHotelRating.setText(String.valueOf(hotel.getHotelRating()));
        String currencySymbol = UserHomeActivity.currencyList.stream().filter(c -> c.getCurrencyCode().equals(hotel.getCurrencyCode())).findFirst().get().getSymbol();
        holder.tvBudgetHotelClass.setText(hotel.getBudget() + currencySymbol + "+ ("+ hotel.getHotelClass() + " Star)");
        holder.lnrLytFreeWIFI.setVisibility(hotel.isFreeWIFI() ? View.VISIBLE : View.GONE);
        holder.lnrLytAirConditioner.setVisibility(hotel.isAirConditioned() ? View.VISIBLE : View.GONE);
        holder.lnrLytBreakfast.setVisibility(hotel.isFreeBreakfast() ? View.VISIBLE : View.GONE);
        holder.lnrLytTeaCoffee.setVisibility(hotel.isTeaCoffee() ? View.VISIBLE : View.GONE);
        holder.lnrLytBar.setVisibility(hotel.isBar() ? View.VISIBLE : View.GONE);
        holder.lnrLytRoomService.setVisibility(hotel.isRoomService() ? View.VISIBLE : View.GONE);
        holder.lnrLytTelevision.setVisibility(hotel.isTelevision() ? View.VISIBLE : View.GONE);
        holder.lnrLytPool.setVisibility(hotel.isPool() ? View.VISIBLE : View.GONE);
        holder.lnrLytParking.setVisibility(hotel.isParking() ? View.VISIBLE : View.GONE);
        holder.lnrLytSPA.setVisibility(hotel.isSpa() ? View.VISIBLE : View.GONE);

        Picasso.with(activity).load(hotel.getHotelImage()).into(holder.imgViewHotel, new Callback() {
            @Override
            public void onSuccess() {
                holder.spinKitProgress.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    class HotelFilterViewHolder extends RecyclerView.ViewHolder{
        LinearLayout lnrLytFreeWIFI, lnrLytAirConditioner, lnrLytBreakfast, lnrLytTeaCoffee, lnrLytBar, lnrLytRoomService, lnrLytTelevision, lnrLytPool, lnrLytParking, lnrLytSPA;
        ImageView imgViewHotel;
        TextView tvHotelName, tvHotelRating, tvBudgetHotelClass, tvFreeWIFI, tvAirConditioner, tvFreeBreakfast, tvFreeTeaCoffee, tvBar, tvRoomService, tvTelevision, tvPool, tvParking, tvSPA;
        SpinKitView spinKitProgress;

        public HotelFilterViewHolder(@NonNull View itemView) {
            super(itemView);
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

            imgViewHotel = itemView.findViewById(R.id.imgViewHotel);
            spinKitProgress = itemView.findViewById(R.id.spinKitProgress);

            tvHotelName = itemView.findViewById(R.id.tvHotelName);
            tvHotelRating = itemView.findViewById(R.id.tvHotelRating);
            tvBudgetHotelClass = itemView.findViewById(R.id.tvBudgetHotelClass);
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
                    Intent hotelViewIntent = new Intent(activity, HotelViewActivity.class);
                    hotelViewIntent.putExtra("Hotel", new Gson().toJson(hotelList.get(getAdapterPosition())));
                    //activity.finish();
                    activity.startActivity(hotelViewIntent);
                }
            });
        }
    }
}
