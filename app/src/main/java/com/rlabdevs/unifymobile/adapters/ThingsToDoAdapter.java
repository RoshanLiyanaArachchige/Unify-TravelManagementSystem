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
import com.rlabdevs.unifymobile.activities.thingstodo.ThingsToDoViewActivity;
import com.rlabdevs.unifymobile.models.master.NewThingsToDoModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ThingsToDoAdapter extends RecyclerView.Adapter<ThingsToDoAdapter.ThingsToDoFilterViewHolder> {

    private Activity activity;
    private List<NewThingsToDoModel> thingsToDoList;

    public ThingsToDoAdapter(Activity activity, List<NewThingsToDoModel> thingsToDoList) {
        this.activity = activity;
        this.thingsToDoList = thingsToDoList;
    }

    @NonNull
    @Override
    public ThingsToDoAdapter.ThingsToDoFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.things_to_do_item_view, parent, false);
        return new ThingsToDoAdapter.ThingsToDoFilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThingsToDoAdapter.ThingsToDoFilterViewHolder holder, int position) {
        NewThingsToDoModel thingsToDo = thingsToDoList.get(position);

        holder.tvTaskID.setText(thingsToDo.getThingsToDoId().toString());
        holder.tvTaskTitle.setText(thingsToDo.getTaskTitle());
        holder.tvTaskDescription.setText(thingsToDo.getTaskDescription());

        Picasso.with(activity).load(thingsToDo.getTaskImage()).into(holder.imgViewThingsToDo, new Callback() {
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
        return thingsToDoList.size();
    }

    class ThingsToDoFilterViewHolder extends RecyclerView.ViewHolder{
        ImageView imgViewThingsToDo;
        TextView tvTaskID, tvTaskTitle, tvTaskDescription;
        SpinKitView spinKitProgress;

        public ThingsToDoFilterViewHolder(@NonNull View itemView) {
            super(itemView);

            imgViewThingsToDo = itemView.findViewById(R.id.imgViewThingsToDo);
            spinKitProgress = itemView.findViewById(R.id.spinKitProgress);

            tvTaskID = itemView.findViewById(R.id.tvTaskID);
            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvTaskDescription = itemView.findViewById(R.id.tvTaskDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent thingsToDoViewIntent = new Intent(activity, ThingsToDoViewActivity.class);
                    thingsToDoViewIntent.putExtra("ThingsToDo", new Gson().toJson(thingsToDoList.get(getAdapterPosition())));
                    //activity.finish();
                    activity.startActivity(thingsToDoViewIntent);
                }
            });
        }
    }
}
