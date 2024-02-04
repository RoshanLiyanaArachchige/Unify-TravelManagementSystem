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
import com.rlabdevs.unifymobile.activities.restaurants.RestaurantViewActivity;
import com.rlabdevs.unifymobile.models.RestaurantModel;
import com.rlabdevs.unifymobile.models.RestaurantModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantFilterAdapter extends RecyclerView.Adapter<RestaurantFilterAdapter.RestaurantFilterViewHolder> {

    private Activity activity;
    private List<RestaurantModel> restaurantList;

    public RestaurantFilterAdapter(Activity activity, List<RestaurantModel> restaurantList) {
        this.activity = activity;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public RestaurantFilterAdapter.RestaurantFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item_view, parent, false);
        return new RestaurantFilterAdapter.RestaurantFilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantFilterAdapter.RestaurantFilterViewHolder holder, int position) {
        RestaurantModel restaurant = restaurantList.get(position);
        holder.tvRestaurantName.setText(restaurant.getRestaurantName());
        holder.tvRestaurantRating.setText(String.valueOf(restaurant.getRestaurantRating()));
        String currencySymbol = UserHomeActivity.currencyList.stream().filter(c -> c.getCurrencyId().equals(restaurant.getCurrencyCode())).findFirst().get().getSymbol();
        holder.tvBudgetRestaurantClass.setText("~" + restaurant.getAveragePrice() + currencySymbol + " ("+ restaurant.getRestaurantClass() + " Star)");
        holder.lnrLytFreeWIFI.setVisibility(restaurant.isFreeWIFI() ? View.VISIBLE : View.GONE);
        holder.lnrLytBeverages.setVisibility(restaurant.isBeverages() ? View.VISIBLE : View.GONE);
        holder.lnrLytTakeaway.setVisibility(restaurant.isTakeaway() ? View.VISIBLE : View.GONE);
        holder.lnrLytParking.setVisibility(restaurant.isParking() ? View.VISIBLE : View.GONE);

        Picasso.with(activity).load(restaurant.getRestaurantImage()).into(holder.imgViewRestaurant, new Callback() {
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
        return restaurantList.size();
    }

    class RestaurantFilterViewHolder extends RecyclerView.ViewHolder{
        LinearLayout lnrLytFreeWIFI, lnrLytBeverages, lnrLytTakeaway, lnrLytParking;
        ImageView imgViewRestaurant;
        TextView tvRestaurantName, tvRestaurantRating, tvBudgetRestaurantClass;
        SpinKitView spinKitProgress;

        public RestaurantFilterViewHolder(@NonNull View itemView) {
            super(itemView);
            lnrLytFreeWIFI = itemView.findViewById(R.id.lnrLytFreeWIFI);
            lnrLytBeverages = itemView.findViewById(R.id.lnrLytBeverages);
            lnrLytTakeaway = itemView.findViewById(R.id.lnrLytTakeaway);
            lnrLytParking = itemView.findViewById(R.id.lnrLytParking);

            imgViewRestaurant = itemView.findViewById(R.id.imgViewRestaurant);
            spinKitProgress = itemView.findViewById(R.id.spinKitProgress);

            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            tvRestaurantRating = itemView.findViewById(R.id.tvRestaurantRating);
            tvBudgetRestaurantClass = itemView.findViewById(R.id.tvBudgetRestaurantClass);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent restaurantViewIntent = new Intent(activity, RestaurantViewActivity.class);
                    restaurantViewIntent.putExtra("Restaurant", new Gson().toJson(restaurantList.get(getAdapterPosition())));
                    //activity.finish();
                    activity.startActivity(restaurantViewIntent);
                }
            });
        }
    }
}
