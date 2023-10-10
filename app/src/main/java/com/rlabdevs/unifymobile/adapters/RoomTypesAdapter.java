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
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.enums.StatusCode;
import com.rlabdevs.unifymobile.models.RoomTypesModel;

import java.util.List;

public class RoomTypesAdapter  extends RecyclerView.Adapter<RoomTypesAdapter.RoomTypeViewHolder> {

    private Activity activity;
    private List<RoomTypesModel> roomTypesList;

    public RoomTypesAdapter(Activity activity, List<RoomTypesModel> roomTypesList) {
        this.activity = activity;
        this.roomTypesList = roomTypesList;
    }

    @NonNull
    @Override
    public RoomTypesAdapter.RoomTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_type_item_view, parent, false);
        return new RoomTypesAdapter.RoomTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomTypesAdapter.RoomTypeViewHolder holder, int position) {
        RoomTypesModel roomType = roomTypesList.get(position);
        holder.tvRoomTypeID.setText(roomType.getID());
        holder.tvRoomTypeCode.setText(roomType.getRoomTypeCode());
        holder.tvRoomType.setText(roomType.getRoomType());
        holder.tvStatusCode.setText(roomType.getStatusCode());
        holder.tvStatus.setText(Functions.GetStatusNameFromCode(roomType.getStatusCode()));
        if(holder.tvStatus.getText().toString().equals("Active"))
            holder.tvStatus.setBackgroundResource(R.drawable.bg_green);
        else
            holder.tvStatus.setBackgroundResource(R.drawable.bg_red);
    }

    @Override
    public int getItemCount() {
        return roomTypesList.size();
    }

    class RoomTypeViewHolder extends RecyclerView.ViewHolder{
        TextView tvRoomTypeID, tvRoomTypeCode, tvStatusCode, tvRoomType, tvStatus;

        public RoomTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomTypeID = itemView.findViewById(R.id.tvRoomTypeID);
            tvRoomTypeCode = itemView.findViewById(R.id.tvRoomTypeCode);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            tvStatusCode = itemView.findViewById(R.id.tvStatusCode);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((RoomTypesActivity)activity).roomTypeIndex = null;
                    ((RoomTypesActivity)activity).relativeLytNewRoomType.setVisibility(View.VISIBLE);
                    ((RoomTypesActivity)activity).txtRoomTypeCode.setText(tvRoomTypeCode.getText().toString());
                    ((RoomTypesActivity)activity).txtRoomType.setText(tvRoomType.getText().toString());
                    RoomTypesActivity.tvRoomTypeID.setText(tvRoomTypeID.getText().toString());
                    RoomTypesActivity.tvStatus.setText(tvStatus.getText().toString() + " (Status)");
                    RoomTypesActivity.txtStatusCode.setText(tvStatusCode.getText().toString());
                }
            });
        }
    }
}