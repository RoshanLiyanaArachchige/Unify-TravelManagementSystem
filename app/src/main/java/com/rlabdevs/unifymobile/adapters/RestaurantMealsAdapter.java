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
import com.rlabdevs.unifymobile.activities.hotels.rooms.RoomViewActivity;
import com.rlabdevs.unifymobile.activities.restaurants.RestaurantViewActivity;
import com.rlabdevs.unifymobile.activities.restaurants.meals.MealViewActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.rooms.RoomActivity;
import com.rlabdevs.unifymobile.activities.user.manage.restaurants.meals.MealActivity;
import com.rlabdevs.unifymobile.activities.user.manage.restaurants.meals.RestaurantMealsActivity;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.MealModel;
import com.rlabdevs.unifymobile.models.MealTypesModel;
import com.rlabdevs.unifymobile.models.RestaurantModel;
import com.rlabdevs.unifymobile.models.RoomTypesModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.MessageFormat;
import java.util.List;

public class RestaurantMealsAdapter extends RecyclerView.Adapter<RestaurantMealsAdapter.RestaurantMealViewHolder> {

    private Activity activity;
    private List<MealModel> restaurantMealList;
    private List<MealTypesModel> mealTypesList;
    private RestaurantModel restaurant;

    public RestaurantMealsAdapter(Activity activity, List<MealModel> restaurantMealList, List<MealTypesModel> mealTypesList) {
        this.activity = activity;
        this.restaurantMealList = restaurantMealList;
        this.mealTypesList = mealTypesList;
        if(activity.getClass().getSimpleName().equals("RestaurantMealsActivity")) { this.restaurant = ((RestaurantMealsActivity)activity).restaurant; }
        else if(activity.getClass().getSimpleName().equals("RestaurantViewActivity")) { this.restaurant = ((RestaurantViewActivity)activity).restaurant; }
    }

    @NonNull
    @Override
    public RestaurantMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item_view, parent, false);
        return new RestaurantMealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantMealViewHolder holder, int position) {
        MealModel meal = restaurantMealList.get(position);
        Picasso.with(activity).load(meal.getMealImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.linearLytImageProgress.setVisibility(View.GONE);
                holder.imgViewMeal.setImageBitmap(bitmap);
            }
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                holder.linearLytImageProgress.setVisibility(View.GONE);
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });

        holder.tvMealCode.setText(meal.getMealCode());
        for (MealTypesModel mealType: mealTypesList) {
            if(mealType.getMealTypeCode().equals(meal.getMealTypeCode())) {
                holder.tvMealType.setText(mealType.getMealTypeName());
            }
        }

        holder.tvMealName.setText(meal.getMealName());

        Integer mealSize = meal.getPortionSize();
        if(mealSize == 1) {
            holder.tvMealSize.setText("For 1 Person");
        } else {
            holder.tvMealSize.setText(MessageFormat.format("For {0} Persons", mealSize));
        }

        String currencySymbol = UserHomeActivity.currencyList.stream().filter(c -> c.getCurrencyId().equals(restaurant.getCurrencyCode())).findFirst().get().getSymbol();
        holder.tvMealPrice.setText(String.valueOf(meal.getMealPrice()) + " " + currencySymbol);
    }

    @Override
    public int getItemCount() {
        return restaurantMealList.size();
    }

    class RestaurantMealViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLytImageProgress;
        ImageView imgViewMeal;
        TextView tvMealCode, tvMealName, tvMealType, tvMealSize, tvMealPrice;

        public RestaurantMealViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLytImageProgress = itemView.findViewById(R.id.linearLytImageProgress);

            imgViewMeal = itemView.findViewById(R.id.imgViewMeal);

            tvMealCode = itemView.findViewById(R.id.tvMealCode);
            tvMealName = itemView.findViewById(R.id.tvMealName);
            tvMealType = itemView.findViewById(R.id.tvMealType);
            tvMealSize = itemView.findViewById(R.id.tvMealSize);
            tvMealPrice = itemView.findViewById(R.id.tvMealPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if(activity.getClass().getSimpleName().equals("RestaurantMealsActivity")) {intent = new Intent(activity, MealActivity.class); }
                    else if(activity.getClass().getSimpleName().equals("RestaurantViewActivity")) { intent = new Intent(activity, MealViewActivity.class); }

                    intent.putExtra("Meal", new Gson().toJson(restaurantMealList.get(getAdapterPosition())));
                    intent.putExtra("Restaurant", new Gson().toJson(restaurant));
                    //activity.finish();
                    activity.startActivity(intent);
                }
            });
        }
    }
}
