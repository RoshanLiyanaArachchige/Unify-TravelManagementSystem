package com.rlabdevs.unifymobile.activities.user.manage.hotels;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.rlabdevs.unifymobile.common.enums.ApiResponse;
import com.rlabdevs.unifymobile.common.enums.Status;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.service.NewServiceAmenityModel;
import com.rlabdevs.unifymobile.models.service.NewServiceClassModel;
import com.rlabdevs.unifymobile.models.service.NewServiceHourModel;
import com.rlabdevs.unifymobile.models.service.NewServiceLocationModel;
import com.rlabdevs.unifymobile.models.service.NewServiceModel;
import com.rlabdevs.unifymobile.services.RetrofitClient;
import com.rlabdevs.unifymobile.services.interfaces.other.ISystemService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyHotelsActivity extends AppCompatActivity implements View.OnClickListener {

    private MyHotelsActivity myHotelsActivity;

    private RelativeLayout relativeLayoutMyHotels;
    private RecyclerView recyclerViewMyHotels;
    private TextView tvNoAddHotels;
    private ImageView imgViewAddNewHotel;
    private SpinKitView spinKitProgress;

    private List<NewServiceModel> myHotelsList;
    private MyHotelsAdapter myHotelsAdapter;

    private LinearLayoutManager linearLayoutManager;
    private ISystemService systemService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_hotels);
        systemService = RetrofitClient.getClient().create(ISystemService.class);
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
        Integer currentUserId = MainActivity.sharedPref.getInt("UserDetailsID", 0);

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