package com.rlabdevs.unifymobile.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.bookings.hotel.RoomBookingActivity;
import com.rlabdevs.unifymobile.activities.bookings.hotel.ViewRoomBookingsActivity;
import com.rlabdevs.unifymobile.activities.location.ConfigureLocationActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.HotelActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.rooms.RoomActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.rooms.RoomTypesActivity;
import com.rlabdevs.unifymobile.activities.user.manage.restaurants.RestaurantActivity;
import com.rlabdevs.unifymobile.activities.user.manage.restaurants.meals.MealActivity;
import com.rlabdevs.unifymobile.activities.user.manage.restaurants.meals.MealTypesActivity;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.SelectorItemModel;

import java.util.List;

public class ItemSelectorAdapter extends RecyclerView.Adapter<ItemSelectorAdapter.ItemSelectorViewHolder> {

    private Activity activity;
    private TextView tvSet;
    private EditText txtSet;
    private List<SelectorItemModel> selectorItemList;

    public ItemSelectorAdapter(Activity activity, List<SelectorItemModel> selectorItemsList, TextView tvSet, EditText txtSet) {
        this.activity = activity;
        this.tvSet = tvSet;
        this.txtSet = txtSet;
        this.selectorItemList = selectorItemsList;
    }

    @NonNull
    @Override
    public ItemSelectorAdapter.ItemSelectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selector_view, parent, false);
        return new ItemSelectorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemSelectorAdapter.ItemSelectorViewHolder holder, int position) {
        holder.tvItemCode.setText(selectorItemList.get(position).getItemCode());
        holder.tvItemName.setText(selectorItemList.get(position).getItemName());
    }

    @Override
    public int getItemCount() {
        return selectorItemList.size();
    }

    class ItemSelectorViewHolder extends RecyclerView.ViewHolder{
        TextView tvItemCode, tvItemName;

        public ItemSelectorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemCode = itemView.findViewById(R.id.tvItemCode);
            tvItemName = itemView.findViewById(R.id.tvItemName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (activity.getClass().getSimpleName())
                    {
                        case "HotelActivity" :
                        {
                            Functions.HideItemSelector();
                            if(tvSet != null)
                            {
                                if(tvSet.getId() == R.id.tvHotelClass)
                                    HotelActivity.tvHotelClass.setText(tvItemName.getText());
                                else if(tvSet.getId() == R.id.tvCurrency)
                                {
                                    HotelActivity.currencyCode = tvItemCode.getText().toString();
                                    HotelActivity.tvCurrency.setText(tvItemName.getText());
                                }
                            }
                            break;
                        }
                        case "HotelFilterActivity" :
                        {
                            Functions.HideItemSelector();
                            if(tvSet != null)
                            {
                                if(tvSet.getId() == R.id.tvClassValue)
                                    tvSet.setText(tvItemName.getText());
                                else if(tvSet.getId() == R.id.tvLocationName)
                                    tvSet.setText(tvItemName.getText());
                                else if(tvSet.getId() == R.id.tvBudgetRange)
                                    tvSet.setText(tvItemName.getText());
                            }
                            break;
                        }
                        case "RoomActivity" :
                        {
                            Functions.HideItemSelector();
                            if(tvSet != null)
                            {
                                if(tvSet.getId() == R.id.tvCurrency)
                                {
                                    RoomActivity.currencyCode = tvItemCode.getText().toString();
                                    RoomActivity.tvCurrency.setText(tvItemName.getText());
                                }
                                else if(tvSet.getId() == R.id.tvRoomType)
                                {
                                    RoomActivity.txtRoomTypeCode.setText(tvItemCode.getText());
                                    RoomActivity.tvRoomType.setText(tvItemName.getText());
                                }
                                else if(tvSet.getId() == R.id.tvNoOfAdults)
                                {
                                    RoomActivity.tvNoOfAdults.setText(tvItemName.getText());
                                }
                                else if(tvSet.getId() == R.id.tvNoOfChildren)
                                {
                                    RoomActivity.tvNoOfChildren.setText(tvItemName.getText());
                                }
                            }
                            break;
                        }
                        case "RoomTypesActivity" :
                        {
                            Functions.HideItemSelector();
                            if(tvSet != null)
                            {
                                if(tvSet.getId() == R.id.tvStatus)
                                    RoomTypesActivity.tvStatus.setText(tvItemName.getText() + " (Status)");
                            }
                            break;
                        }
                        case "ConfigureLocationActivity" :
                        {
                            Functions.HideItemSelector();
                            if(tvSet != null)
                            {
                                if(tvSet.getId() == R.id.tvHotelCity)
                                {
                                    ConfigureLocationActivity.tvHotelCity.setText(tvItemName.getText());
                                    ConfigureLocationActivity.locationCode = tvItemCode.getText().toString();
                                }
                            }
                            break;
                        }
                        case "RoomBookingActivity" :
                        {
                            Functions.HideItemSelector();
                            if(tvSet != null)
                            {
                                if(tvSet.getId() == R.id.tvNoOfAdults)
                                {
                                    RoomBookingActivity.tvNoOfAdults.setText(tvItemName.getText());
                                }
                                else if(tvSet.getId() == R.id.tvNoOfChildren)
                                {
                                    RoomBookingActivity.tvNoOfChildren.setText(tvItemName.getText());
                                }
                            }
                            break;
                        }
                        case "ViewRoomBookingsActivity" :
                        {
                            Functions.HideItemSelector();
                            if(tvSet != null)
                            {
                                ViewRoomBookingsActivity.selectedStatusCode = tvItemCode.getText().toString();
                                tvSet.setText(tvItemName.getText());
                            }
                            break;
                        }

                        case "RestaurantActivity" :
                        {
                            Functions.HideItemSelector();
                            if(tvSet != null)
                            {
                                if(tvSet.getId() == R.id.tvRestaurantClass)
                                    RestaurantActivity.tvRestaurantClass.setText(tvItemName.getText());
                                else if(tvSet.getId() == R.id.tvCurrency)
                                {
                                    RestaurantActivity.currencyCode = tvItemCode.getText().toString();
                                    RestaurantActivity.tvCurrency.setText(tvItemName.getText());
                                }
                            }
                            break;
                        }
                        case "RestaurantFilterActivity" :
                        {
                            Functions.HideItemSelector();
                            if(tvSet != null)
                            {
                                if(tvSet.getId() == R.id.tvClassValue)
                                    tvSet.setText(tvItemName.getText());
                                else if(tvSet.getId() == R.id.tvLocationName)
                                    tvSet.setText(tvItemName.getText());
                                else if(tvSet.getId() == R.id.tvBudgetRange)
                                    tvSet.setText(tvItemName.getText());
                            }
                            break;
                        }
                        case "MealActivity" :
                        {
                            Functions.HideItemSelector();
                            if(tvSet != null)
                            {
                                if(tvSet.getId() == R.id.tvCurrency)
                                {
                                    MealActivity.currencyCode = tvItemCode.getText().toString();
                                    MealActivity.tvCurrency.setText(tvItemName.getText());
                                }
                                else if(tvSet.getId() == R.id.tvMealType)
                                {
                                    MealActivity.txtMealTypeCode.setText(tvItemCode.getText());
                                    MealActivity.tvMealType.setText(tvItemName.getText());
                                }
                            }
                            break;
                        }
                        case "MealTypesActivity" :
                        {
                            Functions.HideItemSelector();
                            if(tvSet != null)
                            {
                                if(tvSet.getId() == R.id.tvStatus)
                                    MealTypesActivity.tvStatus.setText(tvItemName.getText() + " (Status)");
                            }
                            break;
                        }
                    }
                }
            });
        }
    }
}
