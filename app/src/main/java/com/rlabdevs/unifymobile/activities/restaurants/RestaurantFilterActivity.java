package com.rlabdevs.unifymobile.activities.restaurants;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
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
import com.rlabdevs.unifymobile.activities.UserHomeActivity;
import com.rlabdevs.unifymobile.adapters.ItemSelectorAdapter;
import com.rlabdevs.unifymobile.adapters.RestaurantFilterAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.RestaurantModel;
import com.rlabdevs.unifymobile.models.SelectorItemModel;
import com.rlabdevs.unifymobile.models.master.NewLocationModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RestaurantFilterActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private RestaurantFilterActivity restaurantFilterActivity;

    private RelativeLayout relativeLayoutRestaurantFilter, relativeLytNoRestaurants;
    private GridLayout gridLayoutRestaurantFilters;
    private TextView tvLocationName, tvClassValue;
    private RecyclerView recyclerViewRestaurants;
    private ImageView imgViewShowHideFilters, imgViewSelectLocation, imgViewSelectClass;
    private SpinKitView spinKitProgress;

    private CollectionReference restaurantReference;
    private ArrayList<RestaurantModel> restaurantList;
    private List<RestaurantModel> restaurantFilterList;
    private RestaurantFilterAdapter restaurantFilterAdapter;

    private List<SelectorItemModel> selectorItemsList;
    public ItemSelectorAdapter itemSelectorAdapter;
    private LinearLayoutManager linearLayoutManager;

    private boolean isItemSelectorVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_filter);
        restaurantReference = firestoreDB.collection("Restaurants");
        restaurantFilterActivity = this;
        InitUI();
        InitRecyclerViewRestaurantList();
        FetchRestaurants();
    }

    private void InitUI() {
        relativeLayoutRestaurantFilter = findViewById(R.id.relativeLayoutRestaurantFilter);
        relativeLytNoRestaurants = findViewById(R.id.relativeLytNoRestaurants);
        gridLayoutRestaurantFilters = findViewById(R.id.gridLayoutRestaurantFilters);
        tvLocationName = findViewById(R.id.tvLocationName);
        tvClassValue = findViewById(R.id.tvClassValue);
        imgViewShowHideFilters = findViewById(R.id.imgViewShowHideFilters);
        imgViewSelectLocation = findViewById(R.id.imgViewSelectLocation);
        imgViewSelectClass = findViewById(R.id.imgViewSelectClass);
        spinKitProgress = findViewById(R.id.spinKitProgress);

        imgViewShowHideFilters.setOnClickListener(this);
        tvLocationName.setOnClickListener(this);
        tvClassValue.setOnClickListener(this);
        imgViewSelectLocation.setOnClickListener(this);
        imgViewSelectClass.setOnClickListener(this);

        tvLocationName.addTextChangedListener(this);
        tvClassValue.addTextChangedListener(this);
    }

    private void InitRecyclerViewRestaurantList() {
        recyclerViewRestaurants = findViewById(R.id.recyclerViewRestaurants);
        restaurantFilterList = new ArrayList<>();
        restaurantFilterAdapter = new RestaurantFilterAdapter(this, restaurantFilterList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewRestaurants.setLayoutManager(linearLayoutManager);
        recyclerViewRestaurants.setAdapter(restaurantFilterAdapter);
    }

    private void FilterRestaurants() {
        String location = tvLocationName.getText().toString();
        String restaurantClass = tvClassValue.getText().toString();

        ArrayList<RestaurantModel> tmpRestaurantList = (ArrayList<RestaurantModel>) restaurantList.clone();

        if (!(location.equals("") || location.equals("Any"))) {
            Iterator<RestaurantModel> restaurantModelIterator = tmpRestaurantList.iterator();
            while (restaurantModelIterator.hasNext()) {
                RestaurantModel restaurantFilter = restaurantModelIterator.next();
                if (!restaurantFilter.getRestaurantLocation().equals(location))
                    restaurantModelIterator.remove();
            }
        }

        if (!(restaurantClass.equals("") || restaurantClass.equals("Any"))) {
            double classVal = Double.parseDouble(restaurantClass.replace(" Star", ""));
            Iterator<RestaurantModel> restaurantModelIterator = tmpRestaurantList.iterator();
            while (restaurantModelIterator.hasNext()) {
                RestaurantModel restaurantFilter = restaurantModelIterator.next();
                if (restaurantFilter.getRestaurantClass() != classVal)
                    restaurantModelIterator.remove();
            }
        }

        restaurantFilterList.clear();

        for (RestaurantModel restaurantFilter : tmpRestaurantList) {
            restaurantFilterList.add(restaurantFilter);
            restaurantFilterAdapter.notifyDataSetChanged();
        }

        if(restaurantFilterList.size() == 0) {
            relativeLytNoRestaurants.setVisibility(View.VISIBLE);
        } else {
            relativeLytNoRestaurants.setVisibility(View.GONE);
        }
    }

    private void FetchRestaurants() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                restaurantFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        spinKitProgress.setVisibility(View.VISIBLE);
                        new Functions().ShowProgressBar(RestaurantFilterActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                restaurantReference.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    restaurantList = new ArrayList<>();
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                                        restaurantList.add(documentSnapshot.toObject(RestaurantModel.class));
                                    }

                                    FilterRestaurants();
                                } else {
                                    //empty
                                }

                                restaurantFilterActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        spinKitProgress.setVisibility(View.GONE);
                                        Functions.HideProgressBar();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                restaurantFilterActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        spinKitProgress.setVisibility(View.GONE);
                                        Functions.HideProgressBar();
                                        new Functions().ShowErrorDialog("Error Occurred", "Try Again", RestaurantFilterActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgViewShowHideFilters: {
                ShowHideRestaurantFilters();
                break;
            }
            case R.id.tvLocationName:
            case R.id.imgViewSelectLocation: {
                ChangeRestaurantLocation();
                break;
            }
            case R.id.tvClassValue:
            case R.id.imgViewSelectClass: {
                ChangeRestaurantClass();
                break;
            }
        }
    }

    private void ChangeRestaurantClass() {
        List<SelectorItemModel> itemList = new ArrayList<>();
        itemList.add(new SelectorItemModel("ITM1", "Any"));
        itemList.add(new SelectorItemModel("ITM2", "2 Star"));
        itemList.add(new SelectorItemModel("ITM3", "3 Star"));
        itemList.add(new SelectorItemModel("ITM4", "4 Star"));
        itemList.add(new SelectorItemModel("ITM5", "5 Star"));
        new Functions().ShowItemSelector("Select Restaurant Class", itemList, RestaurantFilterActivity.this, tvClassValue, null);
    }

    private void ChangeRestaurantLocation() {
        List<SelectorItemModel> itemList = new ArrayList<>();
        itemList.add(new SelectorItemModel("", "Any"));
        for(NewLocationModel locationData : UserHomeActivity.locationList) {
            itemList.add(new SelectorItemModel(locationData.getLocationId().toString(), locationData.getName()));
        }
        new Functions().ShowItemSelector("Select Restaurant Location", itemList, RestaurantFilterActivity.this, tvLocationName, null);
    }

    private void ShowHideRestaurantFilters() {
        if (gridLayoutRestaurantFilters.getVisibility() == View.VISIBLE)
            gridLayoutRestaurantFilters.setVisibility(View.GONE);
        else
            gridLayoutRestaurantFilters.setVisibility(View.VISIBLE);
    }

    private void InitRecyclerViewItemsList(Dialog itemSelectorDialog, TextView tvSet, EditText txtSet) {
        RecyclerView recyclerViewItemsList = itemSelectorDialog.findViewById(R.id.recyclerViewItemsList);
        selectorItemsList = new ArrayList<>();
        itemSelectorAdapter = new ItemSelectorAdapter(this, selectorItemsList, tvSet, txtSet);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewItemsList.setLayoutManager(linearLayoutManager);
        recyclerViewItemsList.setAdapter(itemSelectorAdapter);
    }

    private void LoadSelectorItems(List<SelectorItemModel> itemList) {
        for (SelectorItemModel selectorItem : itemList) {
            selectorItemsList.add(selectorItem);
            itemSelectorAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        FilterRestaurants();
    }
}