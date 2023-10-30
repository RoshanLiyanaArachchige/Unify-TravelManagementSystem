package com.rlabdevs.unifymobile.fragments.restaurant;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.MainActivity;
import com.rlabdevs.unifymobile.activities.bookings.hotel.ViewRoomBookingsActivity;
import com.rlabdevs.unifymobile.activities.bookings.restaurant.ViewRestaurantBookingsActivity;
import com.rlabdevs.unifymobile.adapters.RestaurantBookingAdapter;
import com.rlabdevs.unifymobile.adapters.RoomBookingAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.RestaurantBookingModel;
import com.rlabdevs.unifymobile.models.RestaurantModel;
import com.rlabdevs.unifymobile.models.RoomBookingModel;
import com.rlabdevs.unifymobile.models.RoomModel;
import com.rlabdevs.unifymobile.models.RoomTypesModel;
import com.rlabdevs.unifymobile.models.SelectorItemModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreatedRestaurantBookingsFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private Activity activity;
    private SpinKitView spinKitProgressBookings;
    private LinearLayout linearLayoutBookingsNoResults;
    private RecyclerView recyclerViewRestaurantBookings;
    private ImageView imgViewShowHideFilters, imgViewSelectDateFrom, imgViewSelectDateTo, imgViewSelectStatus;
    private TextView tvDateFromValue, tvDateToValue, tvStatusValue;

    private GridLayout gridLayoutRestaurantFilters;
    private Date dateFrom, dateTo;
    private String userDetailsCode;

    private CollectionReference restaurantReference;
    private List<RestaurantModel> restaurantList;
    private List<RestaurantBookingModel> restaurantBookingList;

    public CreatedRestaurantBookingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        userDetailsCode = MainActivity.sharedPref.getString("UserDetailsCode", "").toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_created_restaurant_bookings, container, false);

        gridLayoutRestaurantFilters = rootView.findViewById(R.id.gridLayoutRestaurantFilters);

        imgViewShowHideFilters = rootView.findViewById(R.id.imgViewShowHideFilters);
        imgViewSelectDateFrom = rootView.findViewById(R.id.imgViewSelectDateFrom);
        imgViewSelectDateTo = rootView.findViewById(R.id.imgViewSelectDateTo);
        imgViewSelectStatus = rootView.findViewById(R.id.imgViewSelectStatus);

        tvDateFromValue = rootView.findViewById(R.id.tvDateFromValue);
        tvDateToValue = rootView.findViewById(R.id.tvDateToValue);
        tvStatusValue = rootView.findViewById(R.id.tvStatusValue);

        imgViewShowHideFilters.setOnClickListener(this);
        imgViewSelectDateFrom.setOnClickListener(this);
        tvDateFromValue.setOnClickListener(this);
        imgViewSelectDateTo.setOnClickListener(this);
        tvDateToValue.setOnClickListener(this);
        imgViewSelectStatus.setOnClickListener(this);
        tvStatusValue.setOnClickListener(this);

        spinKitProgressBookings = rootView.findViewById(R.id.spinKitProgressBookings);
        linearLayoutBookingsNoResults = rootView.findViewById(R.id.linearLayoutBookingsNoResults);
        recyclerViewRestaurantBookings = rootView.findViewById(R.id.recyclerViewRestaurantBookings);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

        dateFrom = new Date(currentDate.getYear(), 0, 1);
        tvDateFromValue.setText(sdf.format(dateFrom));

        dateTo = new Date(currentDate.getYear() + 1, 0, 1);
        tvDateToValue.setText(sdf.format(dateTo));

        tvStatusValue.addTextChangedListener(this);

        initRestaurantBookings();
    }

    private void initRestaurantBookings() {
        spinKitProgressBookings.setVisibility(View.VISIBLE);

        firestoreDB.collection("RestaurantBookings")
                .whereEqualTo("bookedByUserDetailsCode", userDetailsCode)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<String> restaurantCodeList = new ArrayList<>();

                            restaurantBookingList = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                restaurantCodeList.add(documentSnapshot.get("restaurantCode", String.class));
                                restaurantBookingList.add(documentSnapshot.toObject(RestaurantBookingModel.class));
                            }

                            firestoreDB.collection("Restaurants")
                                    .whereIn("restaurantCode", restaurantCodeList)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if (!queryDocumentSnapshots.isEmpty()) {
                                                restaurantList = new ArrayList<>();
                                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                                    restaurantList.add(documentSnapshot.toObject(RestaurantModel.class));
                                                }

                                                filterRestaurantBookings();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            e.printStackTrace();
                                            activity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    spinKitProgressBookings.setVisibility(View.GONE);
                                                    Toast.makeText(activity, "Failed to get restaurant details.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                        } else {
                            spinKitProgressBookings.setVisibility(View.GONE);
                            linearLayoutBookingsNoResults.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                spinKitProgressBookings.setVisibility(View.GONE);
                                Toast.makeText(activity, "Failed to get restaurant booking details.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void filterRestaurantBookings() {
        spinKitProgressBookings.setVisibility(View.VISIBLE);
        List<RestaurantBookingModel> displayRestaurantBookings = new ArrayList<>();

        for (RestaurantBookingModel restaurantBooking : restaurantBookingList) {
            if (restaurantBooking.getReservationDate().before(dateFrom)) {
                continue;
            }

            if (restaurantBooking.getReservationDate().after(dateTo)) {
                continue;
            }

            String statusText = tvStatusValue.getText().toString();
            if (!(statusText.equals("") || statusText.equals("Any"))) {
                if (!restaurantBooking.getBookingStatusCode().equals(ViewRestaurantBookingsActivity.selectedStatusCode)) {
                    continue;
                }
            }

            displayRestaurantBookings.add(restaurantBooking);
        }

        if (displayRestaurantBookings.size() == 0) {
            linearLayoutBookingsNoResults.setVisibility(View.VISIBLE);
        } else {
            linearLayoutBookingsNoResults.setVisibility(View.GONE);
        }

        RestaurantBookingAdapter restaurantBookingAdapter = new RestaurantBookingAdapter(activity, restaurantList, displayRestaurantBookings);
        LinearLayoutManager linearLayoutManagerBookings = new LinearLayoutManager(activity);
        recyclerViewRestaurantBookings.setLayoutManager(linearLayoutManagerBookings);
        recyclerViewRestaurantBookings.setAdapter(restaurantBookingAdapter);

        restaurantBookingAdapter.notifyDataSetChanged();
        spinKitProgressBookings.setVisibility(View.GONE);
    }

    private void ShowHideRestaurantFilters() {
        if (gridLayoutRestaurantFilters.getVisibility() == View.VISIBLE)
            gridLayoutRestaurantFilters.setVisibility(View.GONE);
        else
            gridLayoutRestaurantFilters.setVisibility(View.VISIBLE);
    }

    private void ChangeBookingStatus() {
        List<SelectorItemModel> itemList = new ArrayList<>();
        itemList.add(new SelectorItemModel("ITM1", "Any"));
        itemList.add(new SelectorItemModel("STS4", "Pending"));
        itemList.add(new SelectorItemModel("STS9", "Confirmed"));
        itemList.add(new SelectorItemModel("STS6", "Cancelled"));
        new Functions().ShowItemSelector("Select Status", itemList, activity, tvStatusValue, null);
    }

    private View.OnClickListener onDateSelectClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView dateInputElement = Functions.dateSelectionDialog.getDateInputElement();
            dateInputElement.setText(Functions.dateSelectionDialog.getSelectedDateString());
            if (dateInputElement.getId() == R.id.tvDateFromValue) {
                dateFrom = Functions.dateSelectionDialog.getSelectedDate();
            } else if (dateInputElement.getId() == R.id.tvDateToValue) {
                dateTo = Functions.dateSelectionDialog.getSelectedDate();
            }
            Functions.dateSelectionDialog.dismiss();
            filterRestaurantBookings();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgViewShowHideFilters: {
                ShowHideRestaurantFilters();
                break;
            }
            case R.id.imgViewSelectDateFrom: {
                Functions.showDateSelectionDialog(activity, tvDateFromValue, dateFrom, false, "Select Date From", onDateSelectClickListener);
                break;
            }
            case R.id.tvDateFromValue: {
                Functions.showDateSelectionDialog(activity, tvDateFromValue, dateFrom, false, "Select Date From", onDateSelectClickListener);
                break;
            }
            case R.id.tvDateToValue: {
                Functions.showDateSelectionDialog(activity, tvDateToValue, dateTo, false, "Select Date To", onDateSelectClickListener);
                break;
            }
            case R.id.imgViewSelectDateTo: {
                Functions.showDateSelectionDialog(activity, tvDateToValue, dateTo, false, "Select Date To", onDateSelectClickListener);
                break;
            }
            case R.id.tvStatusValue: {
                ChangeBookingStatus();
                break;
            }
            case R.id.imgViewSelectStatus: {
                ChangeBookingStatus();
                break;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        filterRestaurantBookings();
    }

    @Override
    public void onResume() {
        super.onResume();
        initRestaurantBookings();
    }
}