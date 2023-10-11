package com.rlabdevs.unifymobile.activities.thingstodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.UserHomeActivity;
import com.rlabdevs.unifymobile.activities.directions.GetDirectionsActivity;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.ThingsToDoModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Objects;

public class ThingsToDoViewActivity extends AppCompatActivity {

    private TextView tvTaskTitle, tvTaskDescription, txtViewLocationName, txtViewEntryFee, txtViewTimings;
    private ImageView imgViewTaskImage;
    private Button btnGetDirections;
    private ThingsToDoModel thingsToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_things_to_do_view);

        thingsToDo = new Gson().fromJson(getIntent().getStringExtra("ThingsToDo"), ThingsToDoModel.class);

        initThingsToDoViewActivity();
    }

    private void initThingsToDoViewActivity() {
        tvTaskTitle = findViewById(R.id.tvTaskTitle);
        tvTaskDescription = findViewById(R.id.tvTaskDescription);
        txtViewLocationName = findViewById(R.id.txtViewLocationName);
        txtViewEntryFee = findViewById(R.id.txtViewEntryFee);
        txtViewTimings = findViewById(R.id.txtViewTimings);

        imgViewTaskImage = findViewById(R.id.imgViewTaskImage);

        btnGetDirections = findViewById(R.id.btnGetDirections);
        btnGetDirections.setOnClickListener(getDirectionsOnClickListener);

        tvTaskTitle.setText(thingsToDo.getTaskTitle());
        tvTaskDescription.setText(Html.fromHtml(thingsToDo.getTaskDescription()));
        txtViewLocationName.setText(thingsToDo.getLocationName());

        Double entryFee = thingsToDo.getEntryFee();
        if(Objects.equals(entryFee, null) || entryFee == 0) {
            txtViewEntryFee.setText("Free");
        } else {
            String currencySymbol = UserHomeActivity.currencyList.stream().filter(c -> c.getCurrencyCode().equals(thingsToDo.getCurrencyCode())).findFirst().get().getSymbol();
            txtViewEntryFee.setText(entryFee.toString() + currencySymbol);
        }

        txtViewTimings.setText(thingsToDo.getTimings());

        Picasso.with(ThingsToDoViewActivity.this).load(thingsToDo.getTaskImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imgViewTaskImage.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    private View.OnClickListener getDirectionsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent thingsToDoViewIntent = new Intent(ThingsToDoViewActivity.this, GetDirectionsActivity.class);
            thingsToDoViewIntent.putExtra("OrgLatitude", ThingsToDoActivity.currentLatLng.latitude);
            thingsToDoViewIntent.putExtra("OrgLongitude", ThingsToDoActivity.currentLatLng.longitude);
            thingsToDoViewIntent.putExtra("DesLatitude", thingsToDo.getLatitude());
            thingsToDoViewIntent.putExtra("DesLongitude", thingsToDo.getLongitude());
            //activity.finish();
            startActivity(thingsToDoViewIntent);

        }
    };
}