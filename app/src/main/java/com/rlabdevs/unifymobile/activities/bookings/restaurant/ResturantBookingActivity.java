package com.rlabdevs.unifymobile.activities.bookings.restaurant;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.gson.Gson;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.MainActivity;
import com.rlabdevs.unifymobile.activities.bookings.hotel.RoomBookingActivity;
import com.rlabdevs.unifymobile.activities.hotels.rooms.RoomViewActivity;
import com.rlabdevs.unifymobile.activities.restaurants.RestaurantViewActivity;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.Regex;
import com.rlabdevs.unifymobile.common.enums.StatusCode;
import com.rlabdevs.unifymobile.models.IndexModel;
import com.rlabdevs.unifymobile.models.RestaurantBookingModel;
import com.rlabdevs.unifymobile.models.RestaurantModel;
import com.rlabdevs.unifymobile.models.UserDetailsModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ResturantBookingActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtBookingCode, txtFullName, txtEmailAddress, txtMobilePhone, txtNoOfPersons, txtReservationDate, txtBookingStatus;
    private TextView tvPlaceBookingHeading;
    private LinearLayout lnrLayoutActions;
    private Button btnReducePersonCount, btnIncreasePersonCount, btnCancel, btnConfirm, btnSubmit, btnViewRestaurant;

    private CollectionReference indexReference, userDetailsReference, restaurantBookingReference, restaurantReference, mealReference;
    private String userDetailsCode, roomBookingCode, currentBookingStatusCode;
    private Date dtmReservationDate;

    private IndexModel bookingDetailsIndex;
    private UserDetailsModel userDetailModel;
    private RestaurantModel restaurantModel;
    private RestaurantBookingModel restaurantBookingModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_booking);

        InitRoomBookingActivity();
    }

    private void InitRoomBookingActivity() {
        if (getIntent().hasExtra("RestaurantBooking")) {
            restaurantBookingModel = new Gson().fromJson(getIntent().getStringExtra("RestaurantBooking"), RestaurantBookingModel.class);
        }

        if (getIntent().hasExtra("Restaurant")) {
            restaurantModel = new Gson().fromJson(getIntent().getStringExtra("Restaurant"), RestaurantModel.class);
        } else {
        }

        lnrLayoutActions = findViewById(R.id.lnrLayoutActions);
        btnViewRestaurant = findViewById(R.id.btnViewRestaurant);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvPlaceBookingHeading = findViewById(R.id.tvPlaceBookingHeading);
        txtNoOfPersons = findViewById(R.id.txtNoOfPersons);
        btnReducePersonCount = findViewById(R.id.btnReducePersonCount);
        btnIncreasePersonCount = findViewById(R.id.btnIncreasePersonCount);
        txtBookingCode = findViewById(R.id.txtBookingCode);
        txtFullName = findViewById(R.id.txtFullName);
        txtEmailAddress = findViewById(R.id.txtEmailAddress);
        txtMobilePhone = findViewById(R.id.txtMobilePhone);
        txtReservationDate = findViewById(R.id.txtReservationDate);
        txtBookingStatus = findViewById(R.id.txtBookingStatus);

        btnReducePersonCount.setOnClickListener(this);
        btnIncreasePersonCount.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnViewRestaurant.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        txtReservationDate.setOnClickListener(this);

        indexReference = firestoreDB.collection("Index");
        userDetailsReference = firestoreDB.collection("UserDetails");
        restaurantBookingReference = firestoreDB.collection("RestaurantBookings");

        userDetailsCode = MainActivity.sharedPref.getString("UserDetailsCode", "").toString();

        if (restaurantBookingModel == null) {
            InitNewRestaurantBooking();
        } else {
            LoadExistingRestaurantBooking();
        }
    }

    private void InitNewRestaurantBooking() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(ResturantBookingActivity.this, "Connecting to server...", "Please wait !");
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

    private boolean ValidateRestaurantBooking() {
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

        String noOfPersons = txtNoOfPersons.getText().toString().trim();
        if (noOfPersons.equals("")) {
            return new Functions().ShowErrorDialog("Enter No. Persons !", "Okay", this);
        }

        if (Objects.equals(dtmReservationDate, null)) {
            return new Functions().ShowErrorDialog("Select Reservation Date !", "Okay", this);
        }

        return true;
    }

    private void LoadExistingRestaurantBooking() {
        tvPlaceBookingHeading.setText("View Booking");
        txtBookingCode.setText(restaurantBookingModel.getBookingCode() + " (Booking Code)");
        txtFullName.setText(restaurantBookingModel.getFullName());
        txtEmailAddress.setText(restaurantBookingModel.getEmailAddress());
        txtMobilePhone.setText(restaurantBookingModel.getMobileNo());

        txtNoOfPersons.setText(restaurantBookingModel.getNoOfPersons() > 1 ? restaurantBookingModel.getNoOfPersons() + " Persons" : restaurantBookingModel.getNoOfPersons() + " Person");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());

        dtmReservationDate = restaurantBookingModel.getReservationDate();
        txtReservationDate.setText(sdf.format(dtmReservationDate));

        currentBookingStatusCode = restaurantBookingModel.getBookingStatusCode();
        if(currentBookingStatusCode.equals(StatusCode.Pending.getStatusCode())) {
            txtBookingStatus.setText("Pending");
        } else if(currentBookingStatusCode.equals(StatusCode.Canceled.getStatusCode())) {
            txtBookingStatus.setText("Cancelled");
        } else if(currentBookingStatusCode.equals(StatusCode.Confirmed.getStatusCode())) {
            txtBookingStatus.setText("Confirmed");
        }

        if (userDetailsCode.equals(restaurantBookingModel.getBookedByUserDetailsCode())) {
            if (currentBookingStatusCode.equals(StatusCode.Pending.getStatusCode())) {
                lnrLayoutActions.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);
                btnSubmit.setText("Update Booking");
            }
        } else if (restaurantModel.getUserDetailsCode().equals(userDetailsCode)) {
            txtFullName.setEnabled(false);
            txtEmailAddress.setEnabled(false);
            txtMobilePhone.setEnabled(false);
            txtNoOfPersons.setEnabled(false);
            txtReservationDate.setEnabled(false);

            btnReducePersonCount.setEnabled(false);
            btnIncreasePersonCount.setEnabled(false);

            if (currentBookingStatusCode.equals(StatusCode.Pending.getStatusCode()) || currentBookingStatusCode.equals(StatusCode.Confirmed.getStatusCode())) {
                lnrLayoutActions.setVisibility(View.VISIBLE);
                if(currentBookingStatusCode.equals(StatusCode.Pending.getStatusCode())) {
                    btnConfirm.setVisibility(View.VISIBLE);
                }
                btnCancel.setVisibility(View.VISIBLE);
            }
        }
    }

    private View.OnClickListener onDateTimeSelectClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText dateTimeInputElement = Functions.dateTimeSelectionDialog.getDateTimeInputElement();
            dateTimeInputElement.setText(Functions.dateTimeSelectionDialog.getSelectedDateTimeString());
            if (dateTimeInputElement.getId() == R.id.txtReservationDate) {
                dtmReservationDate = Functions.dateTimeSelectionDialog.getSelectedDateTime();
            }
            Functions.dateTimeSelectionDialog.dismiss();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReducePersonCount: {
                UpdateNoOfPersons(-1);
                break;
            }
            case R.id.btnIncreasePersonCount: {
                UpdateNoOfPersons(+1);
                break;
            }
            case R.id.txtReservationDate: {
                Functions.showDateTimeSelectionDialog(ResturantBookingActivity.this, txtReservationDate, dtmReservationDate, false, "Select Date Time", onDateTimeSelectClickListener);
                break;
            }
            case R.id.btnSubmit: {
                SaveRestaurantBooking();
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
            case R.id.btnViewRestaurant: {
                ViewRestaurantDetails();
                break;
            }
        }
    }

    private void ViewRestaurantDetails() {
        Intent intent = new Intent(ResturantBookingActivity.this, RestaurantViewActivity.class);
        intent.putExtra("Restaurant", new Gson().toJson(restaurantModel));
        startActivity(intent);
    }

    private void CancelRoomBooking() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(ResturantBookingActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                restaurantBookingModel.setBookingStatusCode(StatusCode.Canceled.getStatusCode());

                restaurantBookingReference.document(restaurantBookingModel.getID()).set(restaurantBookingModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                        Toast.makeText(ResturantBookingActivity.this, "Booking Cancelled !", Toast.LENGTH_SHORT).show();
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
                                        new Functions().ShowErrorDialog("Booking Cancel Failure !", "Try Again", ResturantBookingActivity.this);
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
                        new Functions().ShowProgressBar(ResturantBookingActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                restaurantBookingModel.setBookingStatusCode(StatusCode.Confirmed.getStatusCode());

                restaurantBookingReference.document(restaurantBookingModel.getID()).set(restaurantBookingModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                        Toast.makeText(ResturantBookingActivity.this, "Booking Confirmed !", Toast.LENGTH_SHORT).show();
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
                                        new Functions().ShowErrorDialog("Booking Confirm Failure !", "Try Again", ResturantBookingActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    private void SaveRestaurantBooking() {
        if (ValidateRestaurantBooking()) {
            if (restaurantBookingModel == null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(ResturantBookingActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });

                        restaurantBookingModel = new RestaurantBookingModel();
                        restaurantBookingModel.setBookedByUserDetailsCode(userDetailsCode);
                        restaurantBookingModel.setBookingCode(roomBookingCode);
                        restaurantBookingModel.setBookingStatusCode(currentBookingStatusCode);
                        restaurantBookingModel.setReservationDate(dtmReservationDate);
                        restaurantBookingModel.setEmailAddress(txtEmailAddress.getText().toString());
                        restaurantBookingModel.setFullName(txtFullName.getText().toString());
                        restaurantBookingModel.setRestaurantCode(restaurantModel.getRestaurantCode());
                        restaurantBookingModel.setMobileNo(txtMobilePhone.getText().toString());

                        String strNoPersons = txtNoOfPersons.getText().toString().replace("Persons", "").replace("Person", "").trim();
                        try {
                            restaurantBookingModel.setNoOfPersons(Integer.parseInt(strNoPersons));
                        } catch (Exception ex) {
                        }

                        DocumentReference restaurantBookingDoc = restaurantBookingReference.document();
                        restaurantBookingModel.setID(restaurantBookingDoc.getId());
                        restaurantBookingDoc.set(restaurantBookingModel)
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
                                                                Toast.makeText(ResturantBookingActivity.this, "Booking Placed !", Toast.LENGTH_SHORT).show();
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
                                                                new Functions().ShowErrorDialog("Error Occurred !", "Try Again", ResturantBookingActivity.this);
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
                                                new Functions().ShowErrorDialog("Booking Place Failure !", "Try Again", ResturantBookingActivity.this);
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
                                new Functions().ShowProgressBar(ResturantBookingActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });

                        restaurantBookingModel.setReservationDate(dtmReservationDate);
                        restaurantBookingModel.setEmailAddress(txtEmailAddress.getText().toString());
                        restaurantBookingModel.setFullName(txtFullName.getText().toString());
                        restaurantBookingModel.setMobileNo(txtMobilePhone.getText().toString());

                        String strNoRooms = txtNoOfPersons.getText().toString().replace("Persons", "").replace("Person", "").trim();
                        try {
                            restaurantBookingModel.setNoOfPersons(Integer.parseInt(strNoRooms));
                        } catch (Exception ex) {
                        }

                        restaurantBookingReference.document(restaurantBookingModel.getID()).set(restaurantBookingModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Functions.HideProgressBar();
                                                Toast.makeText(ResturantBookingActivity.this, "Booking Updated !", Toast.LENGTH_SHORT).show();
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
                                                new Functions().ShowErrorDialog("Booking Update Failure !", "Try Again", ResturantBookingActivity.this);
                                            }
                                        });
                                    }
                                });

                    }
                }).start();
            }
        }
    }

    private void UpdateNoOfPersons(int count) {
        String strNoOfPersons = txtNoOfPersons.getText().toString().trim();
        if (strNoOfPersons.equals("")) {
            if (count > 0)
                txtNoOfPersons.setText("1 Person");
        } else {
            int noOfPersons = Integer.parseInt(strNoOfPersons.replace(" Persons", "").replace(" Person", ""));
            if (count > 0)
                txtNoOfPersons.setText(++noOfPersons + " Persons");
            else if (noOfPersons > 2)
                txtNoOfPersons.setText(--noOfPersons + " Persons");
            else if (noOfPersons == 2)
                txtNoOfPersons.setText(--noOfPersons + " Person");
        }
    }
}