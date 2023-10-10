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
import com.rlabdevs.unifymobile.activities.user.manage.hotels.HotelActivity;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class MyHotelsAdapter extends RecyclerView.Adapter<MyHotelsAdapter.MyHotelsViewHolder> {

    private Activity activity;
    private List<HotelModel> myHotelsList;

    public MyHotelsAdapter(Activity activity, List<HotelModel> myHotelsList) {
        this.activity = activity;
        this.myHotelsList = myHotelsList;
    }

    @NonNull
    @Override
    public MyHotelsAdapter.MyHotelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_item_view, parent, false);
        return new MyHotelsAdapter.MyHotelsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHotelsAdapter.MyHotelsViewHolder holder, int position) {
        HotelModel hotel = myHotelsList.get(position);
        Picasso.with(activity).load(hotel.getHotelImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.linearLytImageProgress.setVisibility(View.GONE);
                holder.imgViewHotel.setImageBitmap(bitmap);
            }
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                holder.linearLytImageProgress.setVisibility(View.GONE);
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
        holder.tvHotelCode.setText(hotel.getHotelCode());
        holder.tvHotelName.setText(hotel.getHotelName());
        holder.tvHotelRating.setText(String.valueOf(hotel.getHotelRating()));
        holder.tvBudgetHotelClass.setText(myHotelsList.get(position).getBudget() + "LKR ("+ hotel.getHotelClass() + " Star)");
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
    }

    @Override
    public int getItemCount() {
        return myHotelsList.size();
    }

    class MyHotelsViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLytImageProgress, lnrLytFreeWIFI, lnrLytAirConditioner, lnrLytBreakfast, lnrLytTeaCoffee, lnrLytBar, lnrLytRoomService, lnrLytTelevision, lnrLytPool, lnrLytParking, lnrLytSPA;
        ImageView imgViewHotel;
        TextView tvHotelCode, tvHotelName, tvHotelRating, tvBudgetHotelClass, tvFreeWIFI, tvAirConditioner, tvFreeBreakfast, tvFreeTeaCoffee, tvBar, tvRoomService, tvTelevision, tvPool, tvParking, tvSPA;

        public MyHotelsViewHolder(@NonNull View itemView) {
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

            imgViewHotel = itemView.findViewById(R.id.imgViewHotel);

            tvHotelCode = itemView.findViewById(R.id.tvHotelCode);
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
                    Intent hotelConfigure = new Intent(activity, HotelActivity.class);
                    hotelConfigure.putExtra("HotelCode", tvHotelCode.getText());
                    hotelConfigure.putExtra("MyHotel", new Gson().toJson(myHotelsList.get(getAdapterPosition())));
                    //activity.finish();
                    activity.startActivity(hotelConfigure);
                }
            });
        }
    }
}
