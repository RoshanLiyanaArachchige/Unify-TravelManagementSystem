package com.rlabdevs.unifymobile.activities.user.manage.hotels;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.MainActivity;
import com.rlabdevs.unifymobile.adapters.MyHotelsAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.HotelModel;

import java.util.ArrayList;
import java.util.List;

public class MyHotelsActivity extends AppCompatActivity implements View.OnClickListener {

    private MyHotelsActivity myHotelsActivity;

    private RelativeLayout relativeLayoutMyHotels;
    private RecyclerView recyclerViewMyHotels;
    private TextView tvNoAddHotels;
    private ImageView imgViewAddNewHotel;
    private SpinKitView spinKitProgress;

    private CollectionReference hotelReference;
    private List<HotelModel> myHotelsList;
    private MyHotelsAdapter myHotelsAdapter;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_hotels);
        hotelReference = firestoreDB.collection("Hotels");
        myHotelsActivity = this;
        InitUI();
    }

    private void InitUI() {
        relativeLayoutMyHotels = findViewById(R.id.relativeLayoutMyHotels);
        tvNoAddHotels = findViewById(R.id.tvNoAddHotels);
        imgViewAddNewHotel = findViewById(R.id.imgViewAddNewHotel);
        spinKitProgress = findViewById(R.id.spinKitProgress);

        relativeLayoutMyHotels.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        tvNoAddHotels.setOnClickListener(this);
        imgViewAddNewHotel.setOnClickListener(this);
    }

    private void InitRecyclerViewMyHotelsList() {
        recyclerViewMyHotels = findViewById(R.id.recyclerViewMyHotels);
        myHotelsList = new ArrayList<>();
        myHotelsAdapter = new MyHotelsAdapter(this, myHotelsList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewMyHotels.setLayoutManager(linearLayoutManager);
        recyclerViewMyHotels.setAdapter(myHotelsAdapter);

        FetchMyHotels();
    }

    private void FetchMyHotels() {

        String UserDetailsCode = MainActivity.sharedPref.getString("UserDetailsCode", "");

        new Thread(new Runnable() {
            @Override
            public void run() {

                myHotelsActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        spinKitProgress.setVisibility(View.VISIBLE);
                        new Functions().ShowProgressBar(MyHotelsActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                hotelReference.whereEqualTo("userDetailsCode", UserDetailsCode).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(!queryDocumentSnapshots.isEmpty())
                                {
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot documentSnapshot: documentSnapshotList) {
                                        HotelModel hotel = documentSnapshot.toObject(HotelModel.class);
                                        hotel.setID(documentSnapshot.getId());
                                        myHotelsList.add(hotel);
                                        myHotelsAdapter.notifyDataSetChanged();
                                    }
                                    imgViewAddNewHotel.setVisibility(View.VISIBLE);
                                    spinKitProgress.setVisibility(View.GONE);
                                }
                                else
                                {
                                    myHotelsActivity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            spinKitProgress.setVisibility(View.GONE);
                                            tvNoAddHotels.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }

                                myHotelsActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                        spinKitProgress.setVisibility(View.GONE);
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                myHotelsActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                        new Functions().ShowErrorDialog("Error Occurred", "Try Again", MyHotelsActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imgViewAddNewHotel :
            case R.id.tvNoAddHotels: {
                new Functions().StartActivity(MyHotelsActivity.this, HotelActivity.class);
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        InitRecyclerViewMyHotelsList();
    }
}