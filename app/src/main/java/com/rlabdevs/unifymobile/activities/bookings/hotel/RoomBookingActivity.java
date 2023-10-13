package com.rlabdevs.unifymobile.activities.bookings.hotel;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.MainActivity;
import com.rlabdevs.unifymobile.activities.account.UserProfileActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.HotelActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.rooms.RoomActivity;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.Regex;
import com.rlabdevs.unifymobile.common.enums.StatusCode;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.IndexModel;
import com.rlabdevs.unifymobile.models.LoginModel;
import com.rlabdevs.unifymobile.models.RoomBookingModel;
import com.rlabdevs.unifymobile.models.RoomModel;
import com.rlabdevs.unifymobile.models.SelectorItemModel;
import com.rlabdevs.unifymobile.models.UserDetailsModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoomBookingActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtBookingCode, txtFullName, txtEmailAddress, txtMobilePhone, txtNoOfTotalRooms, txtCheckinDate, txtCheckoutDate, txtBookingStatus;
    public static TextView tvNoOfAdults, tvNoOfChildren;
    private LinearLayout lnrLayoutActions;
    private Button btnViewRoom, btnReduceRoomCount, btnIncreaseRoomCount, btnCancel, btnConfirm, btnSubmit;

    private CollectionReference indexReference, userDetailsReference, roomBookingReference;
    private String userDetailsCode, roomBookingCode, currentBookingStatusCode;
    private Date dtmCheckinDate, dtmCheckoutDate;

    private IndexModel bookingDetailsIndex;
    private UserDetailsModel userDetailModel;
    private HotelModel hotelModel;
    private RoomModel roomModel;
    private RoomBookingModel roomBookingModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_booking);

        InitRoomBookingActivity();
    }

    private void InitRoomBookingActivity() {
        hotelModel = new Gson().fromJson(getIntent().getStringExtra("Hotel"), HotelModel.class);
        roomModel = new Gson().fromJson(getIntent().getStringExtra("Room"), RoomModel.class);

        if (getIntent().hasExtra("RoomBooking")) {
            roomBookingModel = new Gson().fromJson(getIntent().getStringExtra("RoomBooking"), RoomBookingModel.class);
        }

        lnrLayoutActions = findViewById(R.id.lnrLayoutActions);
        btnViewRoom = findViewById(R.id.btnViewRoom);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvNoOfAdults = findViewById(R.id.tvNoOfAdults);
        tvNoOfChildren = findViewById(R.id.tvNoOfChildren);
        txtNoOfTotalRooms = findViewById(R.id.txtNoOfTotalRooms);
        btnReduceRoomCount = findViewById(R.id.btnReduceRoomCount);
        btnIncreaseRoomCount = findViewById(R.id.btnIncreaseRoomCount);
        txtBookingCode = findViewById(R.id.txtBookingCode);
        txtFullName = findViewById(R.id.txtFullName);
        txtEmailAddress = findViewById(R.id.txtEmailAddress);
        txtMobilePhone = findViewById(R.id.txtMobilePhone);
        txtCheckinDate = findViewById(R.id.txtCheckinDate);
        txtCheckoutDate = findViewById(R.id.txtCheckoutDate);
        txtBookingStatus = findViewById(R.id.txtBookingStatus);


        tvNoOfAdults.setOnClickListener(this);
        tvNoOfChildren.setOnClickListener(this);
        btnReduceRoomCount.setOnClickListener(this);
        btnIncreaseRoomCount.setOnClickListener(this);

        txtCheckinDate.setOnClickListener(this);
        txtCheckoutDate.setOnClickListener(this);

        indexReference = firestoreDB.collection("Index");
        userDetailsReference = MainActivity.firestoreDB.collection("UserDetails");

        userDetailsCode = MainActivity.sharedPref.getString("UserDetailsCode", "").toString();
        
        if(roomBookingModel == null) {
            InitNewRoomBooking();
        } else {
            LoadExistingRoomBooking();
        }
    }

    private void InitNewRoomBooking() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(RoomBookingActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                userDetailsReference.whereEqualTo("userDetailsCode", userDetailsCode).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    userDetailModel = documentSnapshotList.get(0).toObject(UserDetailsModel.class);
                                    userDetailModel.setID(documentSnapshotList.get(0).getId());

                                    if (userDetailModel != null) {
                                        txtFullName.setText(userDetailModel.getFirstName() + " " + userDetailModel.getLastName());
                                        txtMobilePhone.setText(userDetailModel.getMobileNo());
                                        txtEmailAddress.setText(userDetailModel.getEmail());
                                    }
                                }
                            }})
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                    }
                                });
                            }});

                indexReference.whereEqualTo("indexName", "Bookings").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    bookingDetailsIndex = documentSnapshotList.get(0).toObject(IndexModel.class);
                                    bookingDetailsIndex.setID(documentSnapshotList.get(0).getId());
                                    if (bookingDetailsIndex != null) {
                                        roomBookingCode = bookingDetailsIndex.getPrefix() + (bookingDetailsIndex.getCurrentCount() + 1);
                                        txtBookingCode.setText(roomBookingCode + " (Booking Code)");
                                    }
                                }
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                    }
                                });
                            }
                        });

                btnSubmit.setVisibility(View.VISIBLE);
                btnSubmit.setText("Create Booking");

                currentBookingStatusCode = StatusCode.Pending.getStatusCode();
                txtBookingStatus.setText("Pending");

            }
        }).start();
    }

    private boolean ValidateRoomBooking() {
        String fullName = txtFullName.getText().toString().trim();
        if (fullName.equals("")) {
            new Functions().ShowErrorDialog("Full Name Required !", "Okay", this);
            return false;
        } else if (fullName.length() < 3) {
            new Functions().ShowErrorDialog("Full Name (5 Chars Min) !", "Try Again", this);
            return false;
        }

        String mobilePhone = txtMobilePhone.getText().toString().trim();
        if (mobilePhone.equals("")) {
            new Functions().ShowErrorDialog("Mobile No Required !", "Okay", this);
            return false;
        }

        String email = txtEmailAddress.getText().toString().trim();
        if (email.equals("")) {
            new Functions().ShowErrorDialog("Email Address Required !", "Okay", this);
            return false;
        } else if (!Regex.ValidateEmail(email)) {
            new Functions().ShowErrorDialog("Enter Valid Email Address !", "Try Again", this);
            return false;
        }

        String noOfAdults = tvNoOfAdults.getText().toString().trim();
        if (noOfAdults.equals("")) {
            return new Functions().ShowErrorDialog("Select No. Of Adults !", "Okay", this);
        }

        String noOfRooms = txtNoOfTotalRooms.getText().toString().trim();
        if (noOfRooms.equals("")) {
            return new Functions().ShowErrorDialog("Enter Total Room Qty !", "Okay", this);
        }

        return true;
    }

    private void LoadExistingRoomBooking() {
    }

    private View.OnClickListener onDateTimeSelectClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText dateTimeInputElement = Functions.dateTimeSelectionDialog.getDateTimeInputElement();
            dateTimeInputElement.setText(Functions.dateTimeSelectionDialog.getSelectedDateTimeString());
            if(dateTimeInputElement.getId() == R.id.txtCheckinDate) {
                dtmCheckinDate = Functions.dateTimeSelectionDialog.getSelectedDateTime();
            } else if(dateTimeInputElement.getId() == R.id.txtCheckoutDate) {
                dtmCheckoutDate = Functions.dateTimeSelectionDialog.getSelectedDateTime();
            }
            Functions.dateTimeSelectionDialog.dismiss();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvNoOfAdults: {
                SelectNoOfAdultsOrChildren(tvNoOfAdults, "Adult");
                break;
            }
            case R.id.tvNoOfChildren: {
                SelectNoOfAdultsOrChildren(tvNoOfChildren, "Child");
                break;
            }
            case R.id.btnReduceRoomCount: {
                UpdateNoOfTotalRooms(-1);
                break;
            }
            case R.id.btnIncreaseRoomCount: {
                UpdateNoOfTotalRooms(+1);
                break;
            }
            case R.id.txtCheckinDate: {
                Functions.showDateTimeSelectionDialog(RoomBookingActivity.this, txtCheckinDate, dtmCheckinDate,false, "Select Date Time", onDateTimeSelectClickListener);
                break;
            }
            case R.id.txtCheckoutDate: {
                Functions.showDateTimeSelectionDialog(RoomBookingActivity.this, txtCheckoutDate, dtmCheckoutDate,false, "Select Date Time", onDateTimeSelectClickListener);
                break;
            }
            case R.id.btnSubmit: {
                SaveRoomBooking();
                break;
            }
        }
    }

    private void SaveRoomBooking() {
        if(roomBookingModel == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            new Functions().ShowProgressBar(RoomBookingActivity.this, "Connecting to server...", "Please wait !");
                        }
                    });

                    roomBookingModel = new RoomBookingModel();
                    roomModel.setHotelCode(hotelModel.getHotelCode());
                    roomModel.setRoomCode(roomCode);
                    roomModel.setRoomTypeCode(txtRoomTypeCode.getText().toString());
                    roomModel.setRoomDescription(txtRoomDescription.getText().toString().trim());
                    roomModel.setRoomPrice(Double.parseDouble(txtRoomPrice.getText().toString().trim()));

                    String strNoAdults = tvNoOfAdults.getText().toString().replace("Adults", "").replace("Adult", "").trim();
                    try {
                        roomModel.setNoOfAdults(Integer.parseInt(strNoAdults));
                    } catch (Exception ex) {
                    }

                    String strNoChildren = tvNoOfChildren.getText().toString().replace("Children", "").replace("Child", "").trim();
                    try {
                        roomModel.setNoOfChildren(Integer.parseInt(strNoChildren));
                    } catch (Exception ex) {
                    }

                    String strNoRooms = txtNoOfTotalRooms.getText().toString().replace("Rooms", "").replace("Room", "").trim();
                    try {
                        roomModel.setNoOfTotalRooms(Integer.parseInt(strNoRooms));
                    } catch (Exception ex) {
                    }

                    roomModel.setCurrencyCode(currencyCode);
                    roomModel.setFreeWIFI(chkFreeWIFI.isChecked());
                    roomModel.setAirConditioned(chkAirConditioned.isChecked());
                    roomModel.setFreeBreakfast(chkFreeBreakfast.isChecked());
                    roomModel.setTeaCoffee(chkTeaCoffee.isChecked());
                    roomModel.setBar(chkBar.isChecked());
                    roomModel.setRoomService(chkRoomService.isChecked());
                    roomModel.setTelevision(chkTelevision.isChecked());
                    roomModel.setPool(chkPool.isChecked());
                    roomModel.setSpa(chkSpa.isChecked());
                    roomModel.setParking(chkParking.isChecked());
                    roomModel.setStatusCode(StatusCode.Active.getStatusCode());


                }
            }).start();
        } else {

        }
    }

    private void SelectNoOfAdultsOrChildren(TextView tvSet, String type) {
        List<SelectorItemModel> itemList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            if(i==1) itemList.add(new SelectorItemModel("ITM" + i, i + " " + type));
            else if (type.equals("Adult")) itemList.add(new SelectorItemModel("ITM" + i, i + " Adults"));
            else if (type.equals("Child")) itemList.add(new SelectorItemModel("ITM" + i, i + " Children"));
        }
        new Functions().ShowItemSelector("Select No. Of" + ((type.equals("Adult") ? " Adults" : " Children")), itemList, RoomBookingActivity.this, tvSet, null);
    }

    private void UpdateNoOfTotalRooms(int count) {
        String strNoOfRooms = txtNoOfTotalRooms.getText().toString().trim();
        if(strNoOfRooms.equals(""))
        {
            if(count > 0)
                txtNoOfTotalRooms.setText("1 Room");
        }
        else
        {
            int noOfRooms = Integer.parseInt(strNoOfRooms.replace(" Rooms", "").replace(" Room", ""));
            if(count > 0)
                txtNoOfTotalRooms.setText(++noOfRooms + " Rooms");
            else if(noOfRooms > 2)
                txtNoOfTotalRooms.setText(--noOfRooms + " Rooms");
            else if(noOfRooms == 2)
                txtNoOfTotalRooms.setText(--noOfRooms + " Room");
        }
    }
}