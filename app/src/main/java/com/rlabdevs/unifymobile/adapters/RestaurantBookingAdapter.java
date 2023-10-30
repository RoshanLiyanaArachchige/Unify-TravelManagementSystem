package com.rlabdevs.unifymobile.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.bookings.hotel.RoomBookingActivity;
import com.rlabdevs.unifymobile.activities.bookings.restaurant.ResturantBookingActivity;
import com.rlabdevs.unifymobile.common.enums.StatusCode;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.RestaurantBookingModel;
import com.rlabdevs.unifymobile.models.RestaurantModel;
import com.rlabdevs.unifymobile.models.RoomBookingModel;
import com.rlabdevs.unifymobile.models.RoomModel;
import com.rlabdevs.unifymobile.models.RoomTypesModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class RestaurantBookingAdapter extends RecyclerView.Adapter<RestaurantBookingAdapter.RestaurantBookingViewHolder> {

    private Activity activity;
    private List<RestaurantModel> restaurantList;
    private List<RestaurantBookingModel> restaurantBookingsList;

    public RestaurantBookingAdapter(Activity activity, List<RestaurantModel> restaurantList, List<RestaurantBookingModel> restaurantBookingsList) {
        this.activity = activity;
        this.restaurantList = restaurantList;
        this.restaurantBookingsList = restaurantBookingsList;
    }

    @NonNull
    @Override
    public RestaurantBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_booking_item, parent, false);
        return new RestaurantBookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantBookingViewHolder holder, int position) {
        RestaurantBookingModel restaurantBooking = restaurantBookingsList.get(position);

        holder.tvBookingCode.setText(restaurantBooking.getBookingCode());

        String restaurantName = restaurantList.stream().filter(h -> h.getRestaurantCode().equals(restaurantBooking.getRestaurantCode())).findFirst().get().getRestaurantName();
        holder.tvRestaurantName.setText(restaurantName);

        holder.tvFullName.setText(restaurantBooking.getFullName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        holder.tvDateRange.setText(sdf.format(restaurantBooking.getReservationDate()));

        String statusCode = restaurantBooking.getBookingStatusCode();
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
        return restaurantBookingsList.size();
    }

    class RestaurantBookingViewHolder extends RecyclerView.ViewHolder{
        TextView tvBookingCode, tvRestaurantName, tvRestaurantType, tvFullName, tvDateRange, tvBookingStatus;

        public RestaurantBookingViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBookingCode = itemView.findViewById(R.id.tvBookingCode);
            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvDateRange = itemView.findViewById(R.id.tvDateRange);
            tvBookingStatus = itemView.findViewById(R.id.tvBookingStatus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ResturantBookingActivity.class);
                    RestaurantBookingModel restaurantBookingData = restaurantBookingsList.get(getAdapterPosition());

                    RestaurantModel restaurantModel = restaurantList.stream().filter(h -> h.getRestaurantCode().equals(restaurantBookingData.getRestaurantCode())).findFirst().get();

                    intent.putExtra("Restaurant", new Gson().toJson(restaurantModel));
                    intent.putExtra("RestaurantBooking", new Gson().toJson(restaurantBookingData));
                    //activity.finish();
                    activity.startActivity(intent);
                }
            });
        }
    }
}
