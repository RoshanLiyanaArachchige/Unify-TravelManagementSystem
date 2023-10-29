package com.rlabdevs.unifymobile.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.models.MealReviewModel;
import com.rlabdevs.unifymobile.models.RoomReviewModel;

import java.util.List;

public class MealReviewAdapter extends RecyclerView.Adapter<MealReviewAdapter.MealReviewViewHolder> {

    private Activity activity;
    private List<MealReviewModel> mealReviewList;

    public MealReviewAdapter(Activity activity, List<MealReviewModel> mealReviewList) {
        this.activity = activity;
        this.mealReviewList = mealReviewList;
    }

    @NonNull
    @Override
    public MealReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_review_item, parent, false);
        return new MealReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealReviewViewHolder holder, int position) {
        MealReviewModel mealReview = mealReviewList.get(position);

        holder.tvReviewContent.setText(mealReview.getReviewContent());
        holder.tvReviewDate.setText(mealReview.getPostedDateTime().toLocaleString());
    }

    @Override
    public int getItemCount() {
        return mealReviewList.size();
    }

    class MealReviewViewHolder extends RecyclerView.ViewHolder{
        TextView tvReviewContent, tvReviewDate;

        public MealReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            tvReviewContent = itemView.findViewById(R.id.tvReviewContent);
            tvReviewDate = itemView.findViewById(R.id.tvReviewDate);
        }
    }
}
