package com.rlabdevs.unifymobile.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.rooms.RoomTypesActivity;
import com.rlabdevs.unifymobile.activities.user.manage.restaurants.meals.MealTypesActivity;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.MealTypesModel;
import com.rlabdevs.unifymobile.models.RoomTypesModel;

import java.util.List;

public class MealTypesAdapter extends RecyclerView.Adapter<MealTypesAdapter.MealTypeViewHolder> {

    private Activity activity;
    private List<MealTypesModel> mealTypesList;

    public MealTypesAdapter(Activity activity, List<MealTypesModel> mealTypesList) {
        this.activity = activity;
        this.mealTypesList = mealTypesList;
    }

    @NonNull
    @Override
    public MealTypesAdapter.MealTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_type_item_view, parent, false);
        return new MealTypesAdapter.MealTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealTypesAdapter.MealTypeViewHolder holder, int position) {
        MealTypesModel mealType = mealTypesList.get(position);
        holder.tvMealTypeID.setText(mealType.getID());
        holder.tvMealTypeCode.setText(mealType.getMealTypeCode());
        holder.tvMealType.setText(mealType.getMealTypeName());
        holder.tvStatusCode.setText(mealType.getStatusCode());
        holder.tvStatus.setText(Functions.GetStatusNameFromCode(mealType.getStatusCode()));
        if(holder.tvStatus.getText().toString().equals("Active"))
            holder.tvStatus.setBackgroundResource(R.drawable.bg_green);
        else
            holder.tvStatus.setBackgroundResource(R.drawable.bg_red);
    }

    @Override
    public int getItemCount() {
        return mealTypesList.size();
    }

    class MealTypeViewHolder extends RecyclerView.ViewHolder{
        TextView tvMealTypeID, tvMealTypeCode, tvStatusCode, tvMealType, tvStatus;

        public MealTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMealTypeID = itemView.findViewById(R.id.tvMealTypeID);
            tvMealTypeCode = itemView.findViewById(R.id.tvMealTypeCode);
            tvMealType = itemView.findViewById(R.id.tvMealType);
            tvStatusCode = itemView.findViewById(R.id.tvStatusCode);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MealTypesActivity)activity).mealTypeIndex = null;
                    ((MealTypesActivity)activity).relativeLytNewMealType.setVisibility(View.VISIBLE);
                    ((MealTypesActivity)activity).txtMealTypeCode.setText(tvMealTypeCode.getText().toString());
                    ((MealTypesActivity)activity).txtMealType.setText(tvMealType.getText().toString());
                    MealTypesActivity.tvMealTypeID.setText(tvMealTypeID.getText().toString());
                    MealTypesActivity.tvStatus.setText(tvStatus.getText().toString() + " (Status)");
                    MealTypesActivity.txtStatusCode.setText(tvStatusCode.getText().toString());
                }
            });
        }
    }
}