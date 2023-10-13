package com.rlabdevs.unifymobile.activities.bookings.hotel;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.rlabdevs.unifymobile.activities.hotels.HotelFilterActivity;
import com.rlabdevs.unifymobile.activities.hotels.rooms.RoomViewActivity;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RoomBookingActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtBookingCode, txtFullName, txtEmailAddress, txtMobilePhone, txtNoOfTotalRooms, txtCheckinDate, txtCheckoutDate, txtBookingStatus;
    public static TextView tvNoOfAdults, tvNoOfChildren;
    private LinearLayout lnrLayoutActions;
    private Button btnViewRoom, btnReduceRoomCount, btnIncreaseRoomCount, btnCancel, btnConfirm, btnSubmit;

    private CollectionReference indexReference, userDetailsReference, roomBookingReference, hotelReference, roomReference;
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
        if (getIntent().hasExtra("RoomBooking")) {
            roomBookingModel = new Gson().fromJson(getIntent().getStringExtra("RoomBooking"), RoomBookingModel.class);
        }

        if (getIntent().hasExtra("Hotel")) {
            hotelModel = new Gson().fromJson(getIntent().getStringExtra("Hotel"), HotelModel.class);
        } else {
        }

        if (getIntent().hasExtra("Room")) {
            roomModel = new Gson().fromJson(getIntent().getStringExtra("Room"), RoomModel.class);
        } else {
            if(roomBookingModel != null) {
                hotelReference = firestoreDB.collection("Hotels");
                hotelReference.whereEqualTo("hotelCode", roomBookingModel.getHotelCode()).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(!queryDocumentSnapshots.isEmpty())
                                {
                                    hotelModel = queryDocumentSnapshots.getDocuments().get(0).toObject(HotelModel.class);

                                    roomReference = firestoreDB.collection("Hotels").document(hotelModel.getID()).collection("Rooms");
                                    roomReference.whereEqualTo("roomCode", roomBookingModel.getRoomCode()).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    if(!queryDocumentSnapshots.isEmpty())
                                                    {
                                                        roomModel = queryDocumentSnapshots.getDocuments().get(0).toObject(RoomModel.class);
                                                    }
                                                    else
                                                    {
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    runOnUiThread(new Runnable() {
                                                        public void run() {
                                                        }
                                                    });
                                                }
                                            });
                                }
                                else
                                {
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                    }
                                });
                            }
                        });
            }
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
        btnSubmit.setOnClickListener(this);
        btnViewRoom.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        txtCheckinDate.setOnClickListener(this);
        txtCheckoutDate.setOnClickListener(this);

        indexReference = firestoreDB.collection("Index");
        userDetailsReference = MainActivity.firestoreDB.collection("UserDetails");
        roomBookingReference = MainActivity.firestoreDB.collection("Hotels").document(hotelModel.getID()).collection("Rooms").document(roomModel.getID()).collection("RoomBookings");

        userDetailsCode = MainActivity.sharedPref.getString("UserDetailsCode", "").toString();

        if (roomBookingModel == null) {
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
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                    }
                                });
                            }
                        });

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

        if (Objects.equals(dtmCheckinDate, null)) {
            return new Functions().ShowErrorDialog("Select Checkin Date !", "Okay", this);
        } else if (Objects.equals(dtmCheckoutDate, null)) {
            return new Functions().ShowErrorDialog("Select Checkout Date !", "Okay", this);
        } else if (!dtmCheckoutDate.after(dtmCheckinDate)) {
            return new Functions().ShowErrorDialog("Checkout Date Must Be After Checkin Date !", "Okay", this);
        }

        return true;
    }

    private void LoadExistingRoomBooking() {
        txtBookingCode.setText(roomBookingModel.getBookingCode() + " (Booking Code)");
        txtFullName.setText(roomBookingModel.getFullName());
        txtEmailAddress.setText(roomBookingModel.getEmailAddress());
        txtMobilePhone.setText(roomBookingModel.getMobileNo());

        tvNoOfAdults.setText(roomBookingModel.getNoOfAdults() > 1 ? roomBookingModel.getNoOfAdults() + " Adults" : roomBookingModel.getNoOfAdults() + " Adult");

        if(!Objects.equals(roomBookingModel.getNoOfChildren(), null)) {
            tvNoOfChildren.setText(roomBookingModel.getNoOfChildren() > 1 ? roomBookingModel.getNoOfChildren() + " Children" : roomBookingModel.getNoOfChildren() + " Child");
        }

        txtNoOfTotalRooms.setText(roomBookingModel.getNoOfRooms() > 1 ? roomBookingModel.getNoOfRooms() + " Rooms" : roomBookingModel.getNoOfRooms() + " Room");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());

        dtmCheckinDate = roomBookingModel.getCheckinDate();
        txtCheckinDate.setText(sdf.format(dtmCheckinDate));

        dtmCheckoutDate = roomBookingModel.getCheckoutDate();
        txtCheckoutDate.setText(sdf.format(dtmCheckoutDate));

        currentBookingStatusCode = roomBookingModel.getBookingStatusCode();
        if(currentBookingStatusCode.equals(StatusCode.Pending.getStatusCode())) {
            txtBookingStatus.setText("Pending");
        } else if(currentBookingStatusCode.equals(StatusCode.Canceled.getStatusCode())) {
            txtBookingStatus.setText("Cancelled");
        } else if(currentBookingStatusCode.equals(StatusCode.Confirmed.getStatusCode())) {
            txtBookingStatus.setText("Confirmed");
        }

        if (userDetailsCode.equals(roomBookingModel.getBookedByUserDetailsCode())) {
            if (currentBookingStatusCode.equals(StatusCode.Pending.getStatusCode())) {
                btnCancel.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);
                btnSubmit.setText("Update Booking");
            }
        } else if (hotelModel.getUserDetailsCode().equals(userDetailsCode)) {
            txtFullName.setEnabled(false);
            txtEmailAddress.setEnabled(false);
            txtMobilePhone.setEnabled(false);
            tvNoOfAdults.setEnabled(false);
            tvNoOfChildren.setEnabled(false);
            txtNoOfTotalRooms.setEnabled(false);
            txtCheckinDate.setEnabled(false);
            txtCheckoutDate.setEnabled(false);

            btnCancel.setVisibility(View.VISIBLE);
            btnConfirm.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener onDateTimeSelectClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText dateTimeInputElement = Functions.dateTimeSelectionDialog.getDateTimeInputElement();
            dateTimeInputElement.setText(Functions.dateTimeSelectionDialog.getSelectedDateTimeString());
            if (dateTimeInputElement.getId() == R.id.txtCheckinDate) {
                dtmCheckinDate = Functions.dateTimeSelectionDialog.getSelectedDateTime();
            } else if (dateTimeInputElement.getId() == R.id.txtCheckoutDate) {
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
                Functions.showDateTimeSelectionDialog(RoomBookingActivity.this, txtCheckinDate, dtmCheckinDate, false, "Select Date Time", onDateTimeSelectClickListener);
                break;
            }
            case R.id.txtCheckoutDate: {
                Functions.showDateTimeSelectionDialog(RoomBookingActivity.this, txtCheckoutDate, dtmCheckoutDate, false, "Select Date Time", onDateTimeSelectClickListener);
                break;
            }
            case R.id.btnSubmit: {
                SaveRoomBooking();
                break;
            }
            case R.id.btnConfirm: {
                ConfirmRoomBooking();
                break;
            }
            case R.id.btnCancel: {
                CancelRoomBooking();
                break;
            }
            case R.id.btnViewRoom: {
                ViewRoomDetails();
                break;
            }
        }
    }

    private void CancelRoomBooking() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(RoomBookingActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                roomBookingModel.setBookingStatusCode(StatusCode.Confirmed.getStatusCode());

                roomBookingReference.document(roomBookingModel.getID()).set(roomBookingModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                        Toast.makeText(RoomBookingActivity.this, "Booking Cancelled !", Toast.LENGTH_SHORT).show();
                                        finish();
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
                                        new Functions().ShowErrorDialog("Booking Cancel Failure !", "Try Again", RoomBookingActivity.this);
                                    }
                                });
                            }
                        });

            }
        }).start();
    }

    private void ConfirmRoomBooking() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(RoomBookingActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                roomBookingModel.setBookingStatusCode(StatusCode.Confirmed.getStatusCode());

                roomBookingReference.document(roomBookingModel.getID()).set(roomBookingModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                        Toast.makeText(RoomBookingActivity.this, "Booking Confirmed !", Toast.LENGTH_SHORT).show();
                                        finish();
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
                                        new Functions().ShowErrorDialog("Booking Confirm Failure !", "Try Again", RoomBookingActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    private void ViewRoomDetails() {
        Intent intent = new Intent(RoomBookingActivity.this, RoomViewActivity.class);
        intent.putExtra("Hotel", new Gson().toJson(hotelModel));
        intent.putExtra("Room", new Gson().toJson(roomModel));
        startActivity(intent);
    }

    private void SaveRoomBooking() {
        if (ValidateRoomBooking()) {
            if (roomBookingModel == null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(RoomBookingActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });

                        roomBookingModel = new RoomBookingModel();
                        roomBookingModel.setBookedByUserDetailsCode(userDetailsCode);
                        roomBookingModel.setBookingCode(roomBookingCode);
                        roomBookingModel.setBookingStatusCode(currentBookingStatusCode);
                        roomBookingModel.setCheckinDate(dtmCheckinDate);
                        roomBookingModel.setCheckoutDate(dtmCheckoutDate);
                        roomBookingModel.setEmailAddress(txtEmailAddress.getText().toString());
                        roomBookingModel.setFullName(txtFullName.getText().toString());
                        roomBookingModel.setHotelCode(hotelModel.getHotelCode());
                        roomBookingModel.setMobileNo(txtMobilePhone.getText().toString());
                        roomBookingModel.setRoomCode(roomModel.getRoomCode());
                        roomBookingModel.setRoomTypeCode(roomModel.getRoomTypeCode());

                        String strNoAdults = tvNoOfAdults.getText().toString().replace("Adults", "").replace("Adult", "").trim();
                        try {
                            roomBookingModel.setNoOfAdults(Integer.parseInt(strNoAdults));
                        } catch (Exception ex) {
                        }

                        String strNoChildren = tvNoOfChildren.getText().toString().replace("Children", "").replace("Child", "").trim();
                        try {
                            roomBookingModel.setNoOfChildren(Integer.parseInt(strNoChildren));
                        } catch (Exception ex) {
                        }

                        String strNoRooms = txtNoOfTotalRooms.getText().toString().replace("Rooms", "").replace("Room", "").trim();
                        try {
                            roomBookingModel.setNoOfRooms(Integer.parseInt(strNoRooms));
                        } catch (Exception ex) {
                        }

                        DocumentReference roomBookingDoc = roomBookingReference.document();
                        roomBookingModel.setID(roomBookingDoc.getId());
                        roomBookingDoc.set(roomBookingModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        bookingDetailsIndex.setCurrentCount(bookingDetailsIndex.getCurrentCount() + 1);
                                        indexReference.document(bookingDetailsIndex.getID()).set(bookingDetailsIndex)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                Functions.HideProgressBar();
                                                                Toast.makeText(RoomBookingActivity.this, "Booking Placed !", Toast.LENGTH_SHORT).show();
                                                                finish();
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
                                                                new Functions().ShowErrorDialog("Error Occurred !", "Try Again", RoomBookingActivity.this);
                                                            }
                                                        });
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
                                                new Functions().ShowErrorDialog("Booking Place Failure !", "Try Again", RoomBookingActivity.this);
                                            }
                                        });
                                    }
                                });

                    }
                }).start();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(RoomBookingActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });

                        roomBookingModel.setCheckinDate(dtmCheckinDate);
                        roomBookingModel.setCheckoutDate(dtmCheckoutDate);
                        roomBookingModel.setEmailAddress(txtEmailAddress.getText().toString());
                        roomBookingModel.setFullName(txtFullName.getText().toString());
                        roomBookingModel.setMobileNo(txtMobilePhone.getText().toString());

                        String strNoAdults = tvNoOfAdults.getText().toString().replace("Adults", "").replace("Adult", "").trim();
                        try {
                            roomBookingModel.setNoOfAdults(Integer.parseInt(strNoAdults));
                        } catch (Exception ex) {
                        }

                        String strNoChildren = tvNoOfChildren.getText().toString().replace("Children", "").replace("Child", "").trim();
                        try {
                            roomBookingModel.setNoOfChildren(Integer.parseInt(strNoChildren));
                        } catch (Exception ex) {
                        }

                        String strNoRooms = txtNoOfTotalRooms.getText().toString().replace("Rooms", "").replace("Room", "").trim();
                        try {
                            roomBookingModel.setNoOfRooms(Integer.parseInt(strNoRooms));
                        } catch (Exception ex) {
                        }

                        roomBookingReference.document(roomBookingModel.getID()).set(roomBookingModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Functions.HideProgressBar();
                                                Toast.makeText(RoomBookingActivity.this, "Booking Updated !", Toast.LENGTH_SHORT).show();
                                                finish();
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
                                                new Functions().ShowErrorDialog("Booking Update Failure !", "Try Again", RoomBookingActivity.this);
                                            }
                                        });
                                    }
                                });

                    }
                }).start();
            }
        }
    }

    private void SelectNoOfAdultsOrChildren(TextView tvSet, String type) {
        List<SelectorItemModel> itemList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            if (i == 1) itemList.add(new SelectorItemModel("ITM" + i, i + " " + type));
            else if (type.equals("Adult"))
                itemList.add(new SelectorItemModel("ITM" + i, i + " Adults"));
            else if (type.equals("Child"))
                itemList.add(new SelectorItemModel("ITM" + i, i + " Children"));
        }
        new Functions().ShowItemSelector("Select No. Of" + ((type.equals("Adult") ? " Adults" : " Children")), itemList, RoomBookingActivity.this, tvSet, null);
    }

    private void UpdateNoOfTotalRooms(int count) {
        String strNoOfRooms = txtNoOfTotalRooms.getText().toString().trim();
        if (strNoOfRooms.equals("")) {
            if (count > 0)
                txtNoOfTotalRooms.setText("1 Room");
        } else {
            int noOfRooms = Integer.parseInt(strNoOfRooms.replace(" Rooms", "").replace(" Room", ""));
            if (count > 0)
                txtNoOfTotalRooms.setText(++noOfRooms + " Rooms");
            else if (noOfRooms > 2)
                txtNoOfTotalRooms.setText(--noOfRooms + " Rooms");
            else if (noOfRooms == 2)
                txtNoOfTotalRooms.setText(--noOfRooms + " Room");
        }
    }
}