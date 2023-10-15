package com.rlabdevs.unifymobile.fragments.hotel;

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
import com.rlabdevs.unifymobile.adapters.RoomBookingAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.models.HotelModel;
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

public class ReceivedRoomBookingsFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private Activity activity;
    private SpinKitView spinKitProgressBookings;
    private LinearLayout linearLayoutBookingsNoResults;
    private RecyclerView recyclerViewRoomBookings;
    private ImageView imgViewShowHideFilters, imgViewSelectDateFrom, imgViewSelectDateTo, imgViewSelectStatus;
    private TextView tvDateFromValue, tvDateToValue, tvStatusValue;

    private GridLayout gridLayoutHotelFilters;
    private Date dateFrom, dateTo;
    private String userDetailsCode;

    private CollectionReference hotelReference;
    private List<HotelModel> hotelList;
    private List<RoomModel> roomList;
    private List<RoomTypesModel> roomTypeList;
    private List<RoomBookingModel> roomBookingList;

    public ReceivedRoomBookingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        userDetailsCode = MainActivity.sharedPref.getString("UserDetailsCode", "").toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_received_room_bookings, container, false);

        gridLayoutHotelFilters = rootView.findViewById(R.id.gridLayoutHotelFilters);

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
        recyclerViewRoomBookings = rootView.findViewById(R.id.recyclerViewRoomBookings);

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

        initRoomBookings();
    }

    private void initRoomBookings() {
        spinKitProgressBookings.setVisibility(View.VISIBLE);

        List<String> hotelCodeList = new ArrayList<>();
        List<String> roomCodeList = new ArrayList<>();

        firestoreDB.collection("Hotels")
                .whereEqualTo("userDetailsCode", userDetailsCode)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        hotelList = new ArrayList<>();
                        if(!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                hotelCodeList.add(documentSnapshot.get("hotelCode", String.class));
                                hotelList.add(documentSnapshot.toObject(HotelModel.class));
                            }

                            firestoreDB.collection("Rooms")
                                    .whereIn("hotelCode", hotelCodeList)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            roomList = new ArrayList<>();
                                            if(!queryDocumentSnapshots.isEmpty()) {
                                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                                    roomCodeList.add(documentSnapshot.get("roomCode", String.class));
                                                    roomList.add(documentSnapshot.toObject(RoomModel.class));
                                                }

                                                firestoreDB.collection("RoomTypes")
                                                        .whereIn("hotelCode", hotelCodeList)
                                                        .get()
                                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                roomTypeList = new ArrayList<>();
                                                                if(!queryDocumentSnapshots.isEmpty()) {
                                                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                                                        roomTypeList.add(documentSnapshot.toObject(RoomTypesModel.class));
                                                                    }

                                                                    firestoreDB.collection("RoomBookings")
                                                                            .whereIn("roomCode", roomCodeList)
                                                                            .get()
                                                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                                @Override
                                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                                    roomBookingList = new ArrayList<>();
                                                                                    if(!queryDocumentSnapshots.isEmpty()) {
                                                                                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                                                                            roomBookingList.add(documentSnapshot.toObject(RoomBookingModel.class));
                                                                                        }
                                                                                        filterRoomBookings();
                                                                                    } else {
                                                                                        spinKitProgressBookings.setVisibility(View.GONE);
                                                                                        linearLayoutBookingsNoResults.setVisibility(View.VISIBLE);
                                                                                    }
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    spinKitProgressBookings.setVisibility(View.GONE);
                                                                                    Toast.makeText(activity, "Failed to get room booking details.", Toast.LENGTH_SHORT).show();
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
                                                                spinKitProgressBookings.setVisibility(View.GONE);
                                                                Toast.makeText(activity, "Failed to get room type details.", Toast.LENGTH_SHORT).show();
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
                                            spinKitProgressBookings.setVisibility(View.GONE);
                                            Toast.makeText(activity, "Failed to get room details.", Toast.LENGTH_SHORT).show();
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
                        spinKitProgressBookings.setVisibility(View.GONE);
                        Toast.makeText(activity, "Failed to get hotel details.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void filterRoomBookings() {
        spinKitProgressBookings.setVisibility(View.VISIBLE);
        List<RoomBookingModel> displayRoomBookings = new ArrayList<>();

        for (RoomBookingModel roomBooking : roomBookingList) {
            if (roomBooking.getCheckinDate().before(dateFrom)) {
                continue;
            }

            if (roomBooking.getCheckoutDate().after(dateTo)) {
                continue;
            }

            String statusText = tvStatusValue.getText().toString();
            if (!(statusText.equals("") || statusText.equals("Any"))) {
                if (!roomBooking.getBookingStatusCode().equals(ViewRoomBookingsActivity.selectedStatusCode)) {
                    continue;
                }
            }

            displayRoomBookings.add(roomBooking);
        }

        if (displayRoomBookings.size() == 0) {
            linearLayoutBookingsNoResults.setVisibility(View.VISIBLE);
        } else {
            linearLayoutBookingsNoResults.setVisibility(View.GONE);
        }

        RoomBookingAdapter roomBookingAdapter = new RoomBookingAdapter(activity, hotelList, roomList, roomTypeList, displayRoomBookings);
        LinearLayoutManager linearLayoutManagerBookings = new LinearLayoutManager(activity);
        recyclerViewRoomBookings.setLayoutManager(linearLayoutManagerBookings);
        recyclerViewRoomBookings.setAdapter(roomBookingAdapter);

        roomBookingAdapter.notifyDataSetChanged();
        spinKitProgressBookings.setVisibility(View.GONE);
    }

    private void ShowHideHotelFilters() {
        if (gridLayoutHotelFilters.getVisibility() == View.VISIBLE)
            gridLayoutHotelFilters.setVisibility(View.GONE);
        else
            gridLayoutHotelFilters.setVisibility(View.VISIBLE);
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
            filterRoomBookings();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgViewShowHideFilters: {
                ShowHideHotelFilters();
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
        filterRoomBookings();
    }

    @Override
    public void onResume() {
        super.onResume();
        initRoomBookings();
    }
}