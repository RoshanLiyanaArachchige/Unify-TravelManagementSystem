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
import com.rlabdevs.unifymobile.activities.user.manage.restaurants.RestaurantActivity;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.RestaurantModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class MyRestaurantsAdapter extends RecyclerView.Adapter<MyRestaurantsAdapter.MyRestaurantsViewHolder> {

    private Activity activity;
    private List<RestaurantModel> myRestaurantsList;

    public MyRestaurantsAdapter(Activity activity, List<RestaurantModel> myRestaurantsList) {
        this.activity = activity;
        this.myRestaurantsList = myRestaurantsList;
    }

    @NonNull
    @Override
    public MyRestaurantsAdapter.MyRestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item_view, parent, false);
        return new MyRestaurantsAdapter.MyRestaurantsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRestaurantsAdapter.MyRestaurantsViewHolder holder, int position) {
        RestaurantModel restaurant = myRestaurantsList.get(position);
        Picasso.with(activity).load(restaurant.getRestaurantImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.linearLytImageProgress.setVisibility(View.GONE);
                holder.imgViewRestaurant.setImageBitmap(bitmap);
            }
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                holder.linearLytImageProgress.setVisibility(View.GONE);
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
        holder.tvRestaurantCode.setText(restaurant.getRestaurantCode());
        holder.tvRestaurantName.setText(restaurant.getRestaurantName());
        holder.tvRestaurantRating.setText(String.valueOf(restaurant.getRestaurantRating()));
        holder.tvBudgetRestaurantClass.setText(myRestaurantsList.get(position).getAveragePrice() + "LKR ("+ restaurant.getRestaurantClass() + " Star)");
        holder.lnrLytFreeWIFI.setVisibility(restaurant.isFreeWIFI() ? View.VISIBLE : View.GONE);
        holder.lnrLytBeverages.setVisibility(restaurant.isBeverages() ? View.VISIBLE : View.GONE);
        holder.lnrLytTakeaway.setVisibility(restaurant.isTakeaway() ? View.VISIBLE : View.GONE);
        holder.lnrLytParking.setVisibility(restaurant.isParking() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return myRestaurantsList.size();
    }

    class MyRestaurantsViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLytImageProgress, lnrLytFreeWIFI, lnrLytBeverages, lnrLytTakeaway, lnrLytParking;
        ImageView imgViewRestaurant;
        TextView tvRestaurantCode, tvRestaurantName, tvRestaurantRating, tvBudgetRestaurantClass;

        public MyRestaurantsViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLytImageProgress = itemView.findViewById(R.id.linearLytImageProgress);
            lnrLytFreeWIFI = itemView.findViewById(R.id.lnrLytFreeWIFI);
            lnrLytBeverages = itemView.findViewById(R.id.lnrLytBeverages);
            lnrLytTakeaway = itemView.findViewById(R.id.lnrLytTakeaway);
            lnrLytParking = itemView.findViewById(R.id.lnrLytParking);

            imgViewRestaurant = itemView.findViewById(R.id.imgViewRestaurant);

            tvRestaurantCode = itemView.findViewById(R.id.tvRestaurantCode);
            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            tvRestaurantRating = itemView.findViewById(R.id.tvRestaurantRating);
            tvBudgetRestaurantClass = itemView.findViewById(R.id.tvBudgetRestaurantClass);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent restaurantConfigure = new Intent(activity, RestaurantActivity.class);
                    restaurantConfigure.putExtra("RestaurantCode", tvRestaurantCode.getText());
                    restaurantConfigure.putExtra("MyRestaurant", new Gson().toJson(myRestaurantsList.get(getAdapterPosition())));
                    //activity.finish();
                    activity.startActivity(restaurantConfigure);
                }
            });
        }
    }
}
