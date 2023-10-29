package com.rlabdevs.unifymobile.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.models.RoomReviewModel;

import java.util.List;

public class RoomReviewAdapter extends RecyclerView.Adapter<RoomReviewAdapter.RoomReviewViewHolder> {

    private Activity activity;
    private List<RoomReviewModel> roomReviewList;

    public RoomReviewAdapter(Activity activity, List<RoomReviewModel> roomReviewList) {
        this.activity = activity;
        this.roomReviewList = roomReviewList;
    }

    @NonNull
    @Override
    public RoomReviewAdapter.RoomReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_review_item, parent, false);
        return new RoomReviewAdapter.RoomReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomReviewAdapter.RoomReviewViewHolder holder, int position) {
        RoomReviewModel roomReview = roomReviewList.get(position);

        holder.tvReviewContent.setText(roomReview.getReviewContent());
        holder.tvReviewDate.setText(roomReview.getPostedDateTime().toLocaleString());
    }

    @Override
    public int getItemCount() {
        return roomReviewList.size();
    }

    class RoomReviewViewHolder extends RecyclerView.ViewHolder{
        TextView tvReviewContent, tvReviewDate;

        public RoomReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            tvReviewContent = itemView.findViewById(R.id.tvReviewContent);
            tvReviewDate = itemView.findViewById(R.id.tvReviewDate);
        }
    }
}
