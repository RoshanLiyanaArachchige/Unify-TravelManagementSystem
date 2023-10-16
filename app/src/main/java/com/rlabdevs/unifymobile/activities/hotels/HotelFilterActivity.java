package com.rlabdevs.unifymobile.activities.hotels;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.UserHomeActivity;
import com.rlabdevs.unifymobile.adapters.HotelFilterAdapter;
import com.rlabdevs.unifymobile.adapters.ItemSelectorAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.LocationModel;
import com.rlabdevs.unifymobile.models.SelectorItemModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HotelFilterActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private HotelFilterActivity hotelFilterActivity;

    private RelativeLayout relativeLayoutHotelFilter, relativeLytNoHotels;
    private GridLayout gridLayoutHotelFilters;
    private TextView tvLocationName, tvBudgetRange, tvClassValue;
    private RecyclerView recyclerViewHotels;
    private ImageView imgViewShowHideFilters, imgViewSelectLocation, imgViewSelectBudget, imgViewSelectClass;
    private SpinKitView spinKitProgress;

    private CollectionReference hotelReference;
    private ArrayList<HotelModel> hotelList;
    private List<HotelModel> hotelFilterList;
    private HotelFilterAdapter hotelFilterAdapter;

    private List<SelectorItemModel> selectorItemsList;
    public ItemSelectorAdapter itemSelectorAdapter;
    private LinearLayoutManager linearLayoutManager;

    private boolean isItemSelectorVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_filter);
        hotelReference = firestoreDB.collection("Hotels");
        hotelFilterActivity = this;
        InitUI();
        InitRecyclerViewHotelList();
        FetchHotels();
    }

    private void InitUI() {
        relativeLayoutHotelFilter = findViewById(R.id.relativeLayoutHotelFilter);
        relativeLytNoHotels = findViewById(R.id.relativeLytNoHotels);
        gridLayoutHotelFilters = findViewById(R.id.gridLayoutHotelFilters);
        tvLocationName = findViewById(R.id.tvLocationName);
        tvBudgetRange = findViewById(R.id.tvBudgetRange);
        tvClassValue = findViewById(R.id.tvClassValue);
        imgViewShowHideFilters = findViewById(R.id.imgViewShowHideFilters);
        imgViewSelectLocation = findViewById(R.id.imgViewSelectLocation);
        imgViewSelectBudget = findViewById(R.id.imgViewSelectBudget);
        imgViewSelectClass = findViewById(R.id.imgViewSelectClass);
        spinKitProgress = findViewById(R.id.spinKitProgress);

        imgViewShowHideFilters.setOnClickListener(this);
        tvLocationName.setOnClickListener(this);
        tvBudgetRange.setOnClickListener(this);
        tvClassValue.setOnClickListener(this);
        imgViewSelectLocation.setOnClickListener(this);
        imgViewSelectBudget.setOnClickListener(this);
        imgViewSelectClass.setOnClickListener(this);

        tvLocationName.addTextChangedListener(this);
        tvBudgetRange.addTextChangedListener(this);
        tvClassValue.addTextChangedListener(this);
    }

    private void InitRecyclerViewHotelList() {
        recyclerViewHotels = findViewById(R.id.recyclerViewHotels);
        hotelFilterList = new ArrayList<>();
        hotelFilterAdapter = new HotelFilterAdapter(this, hotelFilterList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewHotels.setLayoutManager(linearLayoutManager);
        recyclerViewHotels.setAdapter(hotelFilterAdapter);
    }

    private void FilterHotels()
    {
        String location = tvLocationName.getText().toString();
        String budgetRange = tvBudgetRange.getText().toString();
        String hotelClass = tvClassValue.getText().toString();

        ArrayList<HotelModel> tmpHotelList = (ArrayList<HotelModel>) hotelList.clone();

        if(!(location.equals("") || location.equals("Any")))
        {
            Iterator<HotelModel> hotelModelIterator = tmpHotelList.iterator();
            while(hotelModelIterator.hasNext())
            {
                HotelModel hotelFilter = hotelModelIterator.next();
                if(!hotelFilter.getHotelLocation().equals(location))
                    hotelModelIterator.remove();
            }
        }

        if(!(budgetRange.equals("") || budgetRange.equals("Any")))
        {
            if(budgetRange.contains("Below"))
            {
                double budget = Double.parseDouble(budgetRange.replace("Below", "").replace("LKR", "").trim());
                Iterator<HotelModel> hotelModelIterator = tmpHotelList.iterator();
                while(hotelModelIterator.hasNext())
                {
                    HotelModel hotelFilter = hotelModelIterator.next();
                    if(hotelFilter.getBudget() > budget)
                        hotelModelIterator.remove();
                }

            }
            else if (budgetRange.contains("Above"))
            {
                double budget = Double.parseDouble(budgetRange.replace("Above", "").replace("LKR", "").trim());
                Iterator<HotelModel> hotelModelIterator = tmpHotelList.iterator();
                while(hotelModelIterator.hasNext())
                {
                    HotelModel hotelFilter = hotelModelIterator.next();
                    if(hotelFilter.getBudget() < budget)
                        hotelModelIterator.remove();
                }
            }
            else
            {
                String[] ranges = budgetRange.replace("LKR", "").trim().split("-");
                double lowerBudget = Double.parseDouble(ranges[0]);
                double higherBudget = Double.parseDouble(ranges[1]);
                Iterator<HotelModel> hotelModelIterator = tmpHotelList.iterator();
                while(hotelModelIterator.hasNext())
                {
                    HotelModel hotelFilter = hotelModelIterator.next();
                    if(hotelFilter.getBudget() < lowerBudget || hotelFilter.getBudget() > higherBudget)
                        hotelModelIterator.remove();
                }
            }
        }

        if(!(hotelClass.equals("") || hotelClass.equals("Any")))
        {
            double classVal = Double.parseDouble(hotelClass.replace(" Star", ""));
            Iterator<HotelModel> hotelModelIterator = tmpHotelList.iterator();
            while(hotelModelIterator.hasNext())
            {
                HotelModel hotelFilter = hotelModelIterator.next();
                if(hotelFilter.getHotelClass() != classVal)
                    hotelModelIterator.remove();
            }
        }

        hotelFilterList.clear();

        for (HotelModel hotelFilter:tmpHotelList) {
            hotelFilterList.add(hotelFilter);
            hotelFilterAdapter.notifyDataSetChanged();
        }

        if(hotelFilterList.size() == 0) {
            relativeLytNoHotels.setVisibility(View.VISIBLE);
        } else {
            relativeLytNoHotels.setVisibility(View.GONE);
        }

    }

    private void FetchHotels() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                hotelFilterActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        spinKitProgress.setVisibility(View.VISIBLE);
                        new Functions().ShowProgressBar(HotelFilterActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                hotelReference.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(!queryDocumentSnapshots.isEmpty())
                                {
                                    hotelList = new ArrayList<>();
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot documentSnapshot: documentSnapshotList) {
                                        hotelList.add(documentSnapshot.toObject(HotelModel.class));
                                    }

                                    FilterHotels();
                                }
                                else
                                {
                                    //empty
                                }

                                hotelFilterActivity.runOnUiThread(new Runnable() {
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
                                hotelFilterActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        spinKitProgress.setVisibility(View.GONE);
                                        Functions.HideProgressBar();
                                        new Functions().ShowErrorDialog("Error Occurred", "Try Again", HotelFilterActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imgViewShowHideFilters:
            {
                ShowHideHotelFilters();
                break;
            }
            case R.id.tvLocationName:
            case R.id.imgViewSelectLocation: {
                ChangeHotelLocation();
                break;
            }
            case R.id.tvBudgetRange:
            case R.id.imgViewSelectBudget: {
                ChangeBudgetRange();
                break;
            }
            case R.id.tvClassValue:
            case R.id.imgViewSelectClass: {
                ChangeHotelClass();
                break;
            }
        }
    }

    private void ChangeHotelClass() {
        List<SelectorItemModel> itemList = new ArrayList<>();
        itemList.add(new SelectorItemModel("ITM1", "Any"));
        itemList.add(new SelectorItemModel("ITM2", "2 Star"));
        itemList.add(new SelectorItemModel("ITM3", "3 Star"));
        itemList.add(new SelectorItemModel("ITM4", "4 Star"));
        itemList.add(new SelectorItemModel("ITM5", "5 Star"));
        new Functions().ShowItemSelector("Select Hotel Class", itemList, HotelFilterActivity.this, tvClassValue, null);
    }

    private void ChangeBudgetRange() {
        List<SelectorItemModel> itemList = new ArrayList<>();
        itemList.add(new SelectorItemModel("ITM1", "Any"));
        itemList.add(new SelectorItemModel("ITM2", "Below 10000LKR"));
        itemList.add(new SelectorItemModel("ITM3", "10000LKR - 20000LKR"));
        itemList.add(new SelectorItemModel("ITM4", "20000LKR - 30000LKR"));
        itemList.add(new SelectorItemModel("ITM5", "30000LKR - 40000LKR"));
        itemList.add(new SelectorItemModel("ITM6", "Above 40000LKR"));
        new Functions().ShowItemSelector("Select Budget Range", itemList, HotelFilterActivity.this, tvBudgetRange, null);
    }

    private void ChangeHotelLocation() {
        List<SelectorItemModel> itemList = new ArrayList<>();
        itemList.add(new SelectorItemModel("", "Any"));
        for(LocationModel locationData : UserHomeActivity.locationList) {
            itemList.add(new SelectorItemModel(locationData.getLocationCode(), locationData.getLocationName()));
        }
        new Functions().ShowItemSelector("Select Hotel Location", itemList, HotelFilterActivity.this, tvLocationName, null);
    }

    private void ShowHideHotelFilters() {
        if(gridLayoutHotelFilters.getVisibility() == View.VISIBLE)
            gridLayoutHotelFilters.setVisibility(View.GONE);
        else
            gridLayoutHotelFilters.setVisibility(View.VISIBLE);
    }

    private void InitRecyclerViewItemsList(Dialog itemSelectorDialog, TextView tvSet, EditText txtSet)
    {
        RecyclerView recyclerViewItemsList = itemSelectorDialog.findViewById(R.id.recyclerViewItemsList);
        selectorItemsList = new ArrayList<>();
        itemSelectorAdapter = new ItemSelectorAdapter(this, selectorItemsList, tvSet, txtSet);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewItemsList.setLayoutManager(linearLayoutManager);
        recyclerViewItemsList.setAdapter(itemSelectorAdapter);
    }

    private void LoadSelectorItems(List<SelectorItemModel> itemList)
    {
        for(SelectorItemModel selectorItem : itemList)
        {
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
        FilterHotels();
    }
}