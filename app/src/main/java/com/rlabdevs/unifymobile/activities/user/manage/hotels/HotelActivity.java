package com.rlabdevs.unifymobile.activities.user.manage.hotels;

import static com.rlabdevs.unifymobile.activities.MainActivity.firebaseStorage;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.MainActivity;
import com.rlabdevs.unifymobile.activities.UserHomeActivity;
import com.rlabdevs.unifymobile.activities.location.ConfigureLocationActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.rooms.HotelRoomsActivity;
import com.rlabdevs.unifymobile.common.Constants;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.Regex;
import com.rlabdevs.unifymobile.common.enums.ApiResponse;
import com.rlabdevs.unifymobile.common.enums.IndexReference;
import com.rlabdevs.unifymobile.common.enums.Status;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.SelectorItemModel;
import com.rlabdevs.unifymobile.models.master.NewCurrencyModel;
import com.rlabdevs.unifymobile.models.service.NewServiceAmenityModel;
import com.rlabdevs.unifymobile.models.service.NewServiceClassModel;
import com.rlabdevs.unifymobile.models.service.NewServiceHourModel;
import com.rlabdevs.unifymobile.models.service.NewServiceLocationModel;
import com.rlabdevs.unifymobile.models.service.NewServiceModel;
import com.rlabdevs.unifymobile.services.RetrofitClient;
import com.rlabdevs.unifymobile.services.interfaces.other.IMasterService;
import com.rlabdevs.unifymobile.services.interfaces.other.ISystemService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelActivity extends AppCompatActivity implements View.OnClickListener {
    private HotelActivity hotelActivity;

    public static TextView tvHotelClass, tvCurrency, tvHotelLocation, txtCheckIn, txtCheckOut;
    private ImageView imgViewHotelImage, imgViewSelectCoverImage;
    private EditText txtHotelCode, txtHotelName, txtHotelDescription, txtTelephoneNo, txtBudget;
    private CheckBox chkFreeWIFI, chkAirConditioned, chkFreeBreakfast, chkTeaCoffee, chkBar, chkRoomService, chkTelevision, chkPool, chkSpa, chkParking;
    private Button btnSaveHotel, btnManageRooms;

    private Dialog setupRoomConfirmationDialog;

    private StorageReference storageReference;

    public NewServiceModel serviceModel;
    public HotelModel myHotelModel;

    private Uri hotelCoverImageURI, serviceCoverImageURI;
    private Bitmap hotelCoverBitmap;

    public static Integer currencyId;
    private Integer serviceId, locationId;
    private String serviceCode, locationCode, locationName;
    private Double latitude, longitude;
    private boolean isHotelCoverSelected, isHotelCoverUpdated, isSetupRoomsConfirmationDialogVisible;

    private ISystemService systemService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        InitActivity();
    }

    private void InitActivity() {
        imgViewHotelImage = findViewById(R.id.imgViewHotelImage);
        imgViewSelectCoverImage = findViewById(R.id.imgViewSelectCoverImage);
        txtHotelCode = findViewById(R.id.txtHotelCode);
        txtHotelName = findViewById(R.id.txtHotelName);
        txtHotelDescription = findViewById(R.id.txtHotelDescription);
        txtTelephoneNo = findViewById(R.id.txtTelephoneNo);
        txtBudget = findViewById(R.id.txtBudget);
        tvCurrency = findViewById(R.id.tvCurrency);
        tvHotelClass = findViewById(R.id.tvHotelClass);
        tvHotelLocation = findViewById(R.id.tvHotelLocation);
        txtCheckIn = findViewById(R.id.txtCheckIn);
        txtCheckOut = findViewById(R.id.txtCheckOut);
        chkFreeWIFI = findViewById(R.id.chkFreeWIFI);
        chkAirConditioned = findViewById(R.id.chkAirConditioned);
        chkTeaCoffee = findViewById(R.id.chkTeaCoffee);
        chkFreeBreakfast = findViewById(R.id.chkFreeBreakfast);
        chkBar = findViewById(R.id.chkBar);
        chkRoomService = findViewById(R.id.chkRoomService);
        chkTelevision = findViewById(R.id.chkTelevision);
        chkPool = findViewById(R.id.chkPool);
        chkSpa = findViewById(R.id.chkSpa);
        chkParking = findViewById(R.id.chkParking);
        btnManageRooms = findViewById(R.id.btnManageRooms);
        btnSaveHotel = findViewById(R.id.btnSaveHotel);

        imgViewSelectCoverImage.setOnClickListener(this);
        btnManageRooms.setOnClickListener(this);
        btnSaveHotel.setOnClickListener(this);
        tvHotelClass.setOnClickListener(this);
        tvCurrency.setOnClickListener(this);
        tvHotelLocation.setOnClickListener(this);

        systemService = RetrofitClient.getClient().create(ISystemService.class);

        serviceId = getIntent().getIntExtra("ServiceId", 0);

        if (getIntent().hasExtra("MyService")) {
            serviceModel = new Gson().fromJson(getIntent().getStringExtra("MyService"), NewServiceModel.class);
        }

        storageReference = firebaseStorage.getReference();
        hotelActivity = this;

        if (myHotelModel != null) InitHotel();
        else InitHotelRegistration();
    }

    private void InitHotelRegistration() {
        new Functions().ShowProgressBar(HotelActivity.this, "Connecting to server...", "Please wait !");

        IMasterService masterService = RetrofitClient.getClient().create(IMasterService.class);
        Call<String> indexReferenceRequest = masterService.getIndexReferenceCode(IndexReference.Hotels.getValue());
        indexReferenceRequest.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Functions.HideProgressBar();
                if(response.isSuccessful()) {
                    serviceCode = response.body();
                    txtHotelCode.setText(serviceCode + " (Registration Code)");
                } else {
                    Toast.makeText(HotelActivity.this, "An error occurred while getting registration code.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Functions.HideProgressBar();
                Toast.makeText(HotelActivity.this, "An error occurred while getting registration code.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void InitHotel() {
        btnSaveHotel.setText("Update Hotel");

        new Thread(new Runnable() {
            @Override
            public void run() {
                hotelActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(HotelActivity.this, "Connecting to server...", "Please wait !");
                        Picasso.with(hotelActivity).load(myHotelModel.getHotelImage()).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                imgViewHotelImage.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                //linearLytImageProgress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }
                        });
                    }
                });

                isHotelCoverSelected = true;
                btnManageRooms.setVisibility(View.VISIBLE);
                txtHotelCode.setText(myHotelModel.getHotelCode() + " (Registration Code)");
                txtHotelName.setText(myHotelModel.getHotelName());
                txtHotelDescription.setText(Html.fromHtml(myHotelModel.getHotelDescription()));
                tvHotelLocation.setText(myHotelModel.getHotelLocation());
                tvHotelClass.setText(myHotelModel.getHotelClass() + " Star");
                txtTelephoneNo.setText(myHotelModel.getHotelTelNo());
                txtBudget.setText(String.valueOf(myHotelModel.getBudget()));
                chkFreeWIFI.setChecked(myHotelModel.isFreeWIFI());
                chkAirConditioned.setChecked(myHotelModel.isAirConditioned());
                chkFreeBreakfast.setChecked(myHotelModel.isFreeBreakfast());
                chkTeaCoffee.setChecked(myHotelModel.isTeaCoffee());
                chkBar.setChecked(myHotelModel.isBar());
                chkRoomService.setChecked(myHotelModel.isRoomService());
                chkTelevision.setChecked(myHotelModel.isTelevision());
                chkPool.setChecked(myHotelModel.isPool());
                chkSpa.setChecked(myHotelModel.isSpa());
                chkParking.setChecked(myHotelModel.isParking());

                locationCode = myHotelModel.getLocationCode();
                locationName = UserHomeActivity.locationList.stream().filter(c -> c.getLocationId().equals(locationCode)).findFirst().get().getName();

                latitude = myHotelModel.getLatitude();
                longitude = myHotelModel.getLongitude();

                txtCheckIn.setText(myHotelModel.getCheckIn());
                txtCheckOut.setText(myHotelModel.getCheckOut());

                //currencyCode = myHotelModel.getC;
                String currencySymbol = UserHomeActivity.currencyList.stream().filter(c -> c.getCurrencyId().equals("Replace 1232")).findFirst().get().getSymbol();
                tvCurrency.setText(currencySymbol);

                hotelActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        Functions.HideProgressBar();
                    }
                });
            }
        }).start();
    }

    private void SaveHotelCoverImage() {
        Functions.UpdateProgress("Uploading Cover 0%");
        StorageReference hotelStorageReference = storageReference.child("Images/Hotels/" + serviceCode + "/CoverImages/Image-1");

        hotelStorageReference.putFile(hotelCoverImageURI)
                .addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                hotelStorageReference.getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                serviceCoverImageURI = uri;
                                                SaveHotelMasterInformation();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                hotelActivity.runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        Functions.HideProgressBar();
                                                        new Functions().ShowErrorDialog("An error occurred while obtaining cover image download url.", "Try Again", HotelActivity.this);
                                                    }
                                                });
                                            }
                                        });
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hotelActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                Functions.HideProgressBar();
                                new Functions().ShowErrorDialog("An error occurred while uploading cover image.", "Try Again", HotelActivity.this);
                            }
                        });
                    }
                })
                .addOnProgressListener(
                        new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                Functions.UpdateProgress("Uploading Cover " + (int) progress + "%");
                            }
                        });
    }

    private void SaveHotelMasterInformation() {
        NewServiceModel serviceData = new NewServiceModel();
        serviceData.setServiceId(serviceId);
        serviceData.setCode(serviceCode);
        serviceData.setName(txtHotelName.getText().toString());
        serviceData.setDescription(txtHotelDescription.getText().toString());
        serviceData.setImagePath(serviceCoverImageURI.toString());
        serviceData.setTelephoneNo(txtTelephoneNo.getText().toString());
        serviceData.setStartingPrice(Double.parseDouble(txtBudget.getText().toString()));
        serviceData.setCurrencyId(currencyId);
        serviceData.setStatusId(Status.Active.getValue());
        serviceData.setUserId(MainActivity.sharedPref.getInt("UserDetailsID", 0));

        NewServiceClassModel serviceClass = new NewServiceClassModel();
        serviceClass.setServiceClass(Integer.parseInt(tvHotelClass.getText().toString().replace(" Star", "")));
        serviceData.setServiceClass(serviceClass);

        NewServiceHourModel serviceHour = new NewServiceHourModel();
        serviceHour.setStartingHour(txtCheckIn.getText().toString());
        serviceHour.setEndingHour(txtCheckOut.getText().toString());
        serviceData.setServiceHour(serviceHour);

        NewServiceLocationModel serviceLocation = new NewServiceLocationModel();
        serviceLocation.setLocationId(locationId);
        serviceLocation.setLatitude(latitude);
        serviceLocation.setLongitude(longitude);
        serviceData.setServiceLocation(serviceLocation);

        List<NewServiceAmenityModel> serviceAmenities = new ArrayList<>();
        List<CheckBox> chkAmenities = Arrays.asList(chkFreeWIFI, chkAirConditioned, chkFreeBreakfast, chkTeaCoffee, chkBar,chkRoomService, chkTelevision, chkPool, chkSpa, chkParking);
        for (CheckBox chkAmenity: chkAmenities) {
            if(chkAmenity.isChecked()) {
                NewServiceAmenityModel serviceAmenity = new NewServiceAmenityModel();
                serviceAmenity.setAmenityId(Integer.parseInt(chkAmenity.getTag().toString()));
                serviceAmenities.add(serviceAmenity);
            }
        }
        serviceData.setServiceAmenities(serviceAmenities);

        Functions.UpdateProgress("Saving Hotel Information");
        Call<NewServiceModel> saveHotelRequest = systemService.saveHotel(serviceData);

        saveHotelRequest.enqueue(new Callback<NewServiceModel>() {
            @Override
            public void onResponse(Call<NewServiceModel> call, Response<NewServiceModel> response) {
                if(response.isSuccessful()) {
                    NewServiceModel apiResponse = response.body();
                    if(apiResponse.getApiResponseStatus().equals(ApiResponse.Success.getValue())) {
                        Functions.HideProgressBar();
                        serviceId = apiResponse.getServiceId();
                        Toast.makeText(HotelActivity.this, "Hotel information saved successfully.", Toast.LENGTH_SHORT).show();
                        ShowSetupRoomsConfirmationDialog();
                    } else {
                        Functions.HideProgressBar();
                        Toast.makeText(hotelActivity, apiResponse.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Functions.HideProgressBar();
                    Toast.makeText(hotelActivity, "An error occurred while saving the hotel information.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewServiceModel> call, Throwable t) {
                Functions.HideProgressBar();
                Toast.makeText(hotelActivity, "An error occurred while saving the hotel information.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void SaveHotel() {
        if (ValidateHotel()) {
            new Thread(() -> {
                hotelActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(HotelActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                if(Objects.equals(hotelCoverImageURI, null) || isHotelCoverUpdated) {
                    SaveHotelCoverImage();
                } else {
                    SaveHotelMasterInformation();
                }
            }).start();




            if (myHotelModel == null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        /*myHotelModel = new HotelModel();
                        myHotelModel.setHotelCode(hotelCode);
                        myHotelModel.setHotelName(txtHotelName.getText().toString().trim());
                        myHotelModel.setHotelDescription(txtHotelDescription.getText().toString().trim());
                        myHotelModel.setLocationCode(locationCode);
                        myHotelModel.setHotelLocation(tvHotelLocation.getText().toString().trim());
                        myHotelModel.setLatitude(latitude);
                        myHotelModel.setLongitude(longitude);
                        myHotelModel.setHotelClass(Integer.parseInt(tvHotelClass.getText().toString().replace(" Star", "")));
                        myHotelModel.setHotelTelNo(txtTelephoneNo.getText().toString().trim());
                        myHotelModel.setCheckIn(txtCheckIn.getText().toString().trim());
                        myHotelModel.setCheckOut(txtCheckOut.getText().toString().trim());
                        myHotelModel.setBudget(Double.parseDouble(txtBudget.getText().toString().trim()));
                        myHotelModel.setCurrencyCode(currencyCode);
                        myHotelModel.setFreeWIFI(chkFreeWIFI.isChecked());
                        myHotelModel.setAirConditioned(chkAirConditioned.isChecked());
                        myHotelModel.setFreeBreakfast(chkFreeBreakfast.isChecked());
                        myHotelModel.setTeaCoffee(chkTeaCoffee.isChecked());
                        myHotelModel.setBar(chkBar.isChecked());
                        myHotelModel.setRoomService(chkRoomService.isChecked());
                        myHotelModel.setTelevision(chkTelevision.isChecked());
                        myHotelModel.setPool(chkPool.isChecked());
                        myHotelModel.setSpa(chkSpa.isChecked());
                        myHotelModel.setParking(chkParking.isChecked());
                        myHotelModel.setUserDetailsCode(MainActivity.sharedPref.getString("UserDetailsCode", ""));
                        myHotelModel.setStatusCode(StatusCode.Active.getStatusCode());

                        if (hotelCoverImageURI != null) {
                            Functions.UpdateProgress("Uploading Cover 0%");
                            StorageReference hotelStorageReference = storageReference.child("Images/Hotels/" + hotelCode + "/CoverImages/Image-1");

                            hotelStorageReference.putFile(hotelCoverImageURI)
                                    .addOnSuccessListener(
                                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    hotelStorageReference.getDownloadUrl()
                                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    myHotelModel.setHotelImage(uri.toString());
                                                                    DocumentReference hotelDoc = hotelsReference.document();
                                                                    myHotelModel.setID(hotelDoc.getId());
                                                                    hotelDoc.set(myHotelModel)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    hotelDetailsIndex.setCurrentCount(hotelDetailsIndex.getCurrentCount() + 1);
                                                                                    indexReference.document(hotelDetailsIndex.getID()).set(hotelDetailsIndex)
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    hotelActivity.runOnUiThread(new Runnable() {
                                                                                                        public void run() {
                                                                                                            Functions.HideProgressBar();
                                                                                                            Toast.makeText(HotelActivity.this, "Hotel Registered !", Toast.LENGTH_SHORT).show();
                                                                                                            ShowSetupRoomsConfirmationDialog();
                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            })
                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                @Override
                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                    hotelActivity.runOnUiThread(new Runnable() {
                                                                                                        public void run() {
                                                                                                            Functions.HideProgressBar();
                                                                                                            new Functions().ShowErrorDialog("Error Occurred !", "Try Again", HotelActivity.this);
                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            });
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    hotelActivity.runOnUiThread(new Runnable() {
                                                                                        public void run() {
                                                                                            Functions.HideProgressBar();
                                                                                            new Functions().ShowErrorDialog("Hotel Registration Failure !", "Try Again", HotelActivity.this);
                                                                                        }
                                                                                    });
                                                                                }
                                                                            });
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    hotelActivity.runOnUiThread(new Runnable() {
                                                                        public void run() {
                                                                            Functions.HideProgressBar();
                                                                            new Functions().ShowErrorDialog("Cover Upload Failure !", "Try Again", HotelActivity.this);
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                }
                                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            hotelActivity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Functions.HideProgressBar();
                                                    new Functions().ShowErrorDialog("Hotel Cover Image Upload Failed !", "Try Again", HotelActivity.this);
                                                }
                                            });
                                        }
                                    })
                                    .addOnProgressListener(
                                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                                    Functions.UpdateProgress("Uploading Cover " + (int) progress + "%");
                                                }
                                            });
                        }*/
                    }
                }).start();
            } else {
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        hotelActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(HotelActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });

                        myHotelModel.setHotelName(txtHotelName.getText().toString().trim());
                        myHotelModel.setHotelDescription(txtHotelDescription.getText().toString().trim());
                        myHotelModel.setLocationCode(locationCode);
                        myHotelModel.setHotelLocation(tvHotelLocation.getText().toString().trim());
                        myHotelModel.setLatitude(latitude);
                        myHotelModel.setLongitude(longitude);
                        myHotelModel.setHotelClass(Integer.parseInt(tvHotelClass.getText().toString().replace(" Star", "")));
                        myHotelModel.setHotelTelNo(txtTelephoneNo.getText().toString().trim());
                        myHotelModel.setCheckIn(txtCheckIn.getText().toString().trim());
                        myHotelModel.setCheckOut(txtCheckOut.getText().toString().trim());
                        myHotelModel.setBudget(Double.parseDouble(txtBudget.getText().toString().trim()));
                        myHotelModel.setCurrencyCode(currencyCode);
                        myHotelModel.setFreeWIFI(chkFreeWIFI.isChecked());
                        myHotelModel.setAirConditioned(chkAirConditioned.isChecked());
                        myHotelModel.setFreeBreakfast(chkFreeBreakfast.isChecked());
                        myHotelModel.setTeaCoffee(chkTeaCoffee.isChecked());
                        myHotelModel.setBar(chkBar.isChecked());
                        myHotelModel.setRoomService(chkRoomService.isChecked());
                        myHotelModel.setTelevision(chkTelevision.isChecked());
                        myHotelModel.setPool(chkPool.isChecked());
                        myHotelModel.setSpa(chkSpa.isChecked());
                        myHotelModel.setParking(chkParking.isChecked());

                        if (isHotelCoverUpdated) {
                            if (hotelCoverImageURI != null) {
                                Functions.UpdateProgress("Uploading Cover 0%");
                                StorageReference hotelStorageReference = storageReference.child("Images/Hotels/" + myHotelModel.getHotelCode() + "/CoverImages/Image-1");

                                hotelStorageReference.putFile(hotelCoverImageURI)
                                        .addOnSuccessListener(
                                                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        hotelStorageReference.getDownloadUrl()
                                                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        myHotelModel.setHotelImage(uri.toString());
                                                                        hotelsReference.document(myHotelModel.getID()).set(myHotelModel)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        hotelActivity.runOnUiThread(new Runnable() {
                                                                                            public void run() {
                                                                                                Functions.HideProgressBar();
                                                                                                finish();
                                                                                                Toast.makeText(HotelActivity.this, "Hotel Updated !", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        hotelActivity.runOnUiThread(new Runnable() {
                                                                                            public void run() {
                                                                                                Functions.HideProgressBar();
                                                                                                new Functions().ShowErrorDialog("Hotel Update Failure !", "Try Again", HotelActivity.this);
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                });
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        hotelActivity.runOnUiThread(new Runnable() {
                                                                            public void run() {
                                                                                Functions.HideProgressBar();
                                                                                new Functions().ShowErrorDialog("Hotel Update Failure !", "Try Again", HotelActivity.this);
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                    }
                                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                hotelActivity.runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        Functions.HideProgressBar();
                                                        new Functions().ShowErrorDialog("Hotel Cover Image Upload Failed !", "Try Again", HotelActivity.this);
                                                    }
                                                });
                                            }
                                        })
                                        .addOnProgressListener(
                                                new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                                        Functions.UpdateProgress("Uploading Cover " + (int) progress + "%");
                                                    }
                                                });
                            }
                        } else {
                            hotelsReference.document(myHotelModel.getID()).set(myHotelModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            hotelActivity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Functions.HideProgressBar();
                                                    finish();
                                                    Toast.makeText(HotelActivity.this, "Hotel Updated !", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            hotelActivity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Functions.HideProgressBar();
                                                    new Functions().ShowErrorDialog("Hotel Update Failure !", "Try Again", HotelActivity.this);
                                                }
                                            });
                                        }
                                    });
                        }
                    }
                }).start();*/
            }
        }
    }

    private boolean ValidateHotel() {
        if (!isHotelCoverSelected) {
            return new Functions().ShowErrorDialog("Select Hotel Cover Image !", "Okay", this);
        }

        String hotelName = txtHotelName.getText().toString().trim();
        if (hotelName.equals("")) {
            return new Functions().ShowErrorDialog("Hotel Name Required !", "Okay", this);
        } else if (hotelName.length() < 5) {
            return new Functions().ShowErrorDialog("Hotel Name (5 Chars Min) !", "Try Again", this);
        }

        String hotelDescription = txtHotelDescription.getText().toString().trim();
        if (hotelDescription.equals("")) {
            return new Functions().ShowErrorDialog("Hotel Description Required !", "Okay", this);
        } else if (hotelDescription.length() < 10) {
            return new Functions().ShowErrorDialog("Describe More About Hotel !", "Try Again", this);
        }

        String hotelLocation = tvHotelLocation.getText().toString().trim();
        if (hotelLocation.equals("Set Hotel Location")) {
            return new Functions().ShowErrorDialog("Hotel Location Required !", "Okay", this);
        } else if (hotelLocation.length() < 3) {
            return new Functions().ShowErrorDialog("Enter Valid Hotel Location !", "Try Again", this);
        }

        String hotelClass = tvHotelClass.getText().toString().trim();
        if (hotelClass.equals("")) {
            return new Functions().ShowErrorDialog("Select Hotel Class !", "Okay", this);
        }

        String telephoneNo = txtTelephoneNo.getText().toString().trim();
        if (telephoneNo.equals("")) {
            return new Functions().ShowErrorDialog("Enter Telephone No !", "Okay", this);
        } else if (!Regex.ValidatePhoneNo(telephoneNo)) {
            return new Functions().ShowErrorDialog("Enter Valid Phone Number !", "Try Again", this);
        }

        String checkInTime = txtCheckIn.getText().toString().trim();
        if (checkInTime.equals("")) {
            return new Functions().ShowErrorDialog("Enter Check-In Time !", "Okay", this);
        }

        String checkOutTime = txtCheckOut.getText().toString().trim();
        if (checkOutTime.equals("")) {
            return new Functions().ShowErrorDialog("Enter Check-Out Time !", "Okay", this);
        }

        String roomBudget = txtBudget.getText().toString().trim();
        if (roomBudget.equals("")) {
            return new Functions().ShowErrorDialog("Enter Room Budget !", "Okay", this);
        } else if (!Regex.ValidateDecimalsOnly(roomBudget)) {
            return new Functions().ShowErrorDialog("Room Budget: Enter Decimals Only !", "Try Again", this);
        } else if (Double.parseDouble(roomBudget) <= 0) {
            return new Functions().ShowErrorDialog("Room Budget: More Than 0 !", "Try Again", this);
        }

        String currency = tvCurrency.getText().toString().trim();
        if (currency.equals("")) {
            return new Functions().ShowErrorDialog("Select Currency Unit !", "Okay", this);
        }

        if (!chkFreeWIFI.isChecked() && !chkAirConditioned.isChecked() && !chkFreeBreakfast.isChecked() && !chkTeaCoffee.isChecked() && !chkBar.isChecked() && !chkRoomService.isChecked() && !chkTelevision.isChecked() && !chkPool.isChecked() && !chkSpa.isChecked() && !chkParking.isChecked())
            return new Functions().ShowErrorDialog("Select 1 Or More Amenities !", "Okay", this);

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSaveHotel: {
                SaveHotel();
                break;
            }
            case R.id.tvHotelClass: {
                SelectHotelClass();
                break;
            }
            case R.id.tvCurrency: {
                SelectCurrencyUnit();
                break;
            }
            case R.id.tvHotelLocation: {
                SelectHotelLocation();
                break;
            }
            case R.id.imgViewSelectCoverImage: {
                SelectHotelCoverImage();
                break;
            }
            case R.id.btnManageRooms: {
                ManageHotelRooms();
                break;
            }
        }
    }

    private void SelectHotelLocation() {
        Intent intent = new Intent(HotelActivity.this, ConfigureLocationActivity.class);
        if(locationId != null && locationName != null) {
            intent.putExtra("LocationId", locationId);
            intent.putExtra("LocationName", locationName);
        }
        if(latitude != null && longitude != null) {
            intent.putExtra("Latitude", latitude);
            intent.putExtra("Longitude", longitude);
        }
        startActivityForResult(intent, Constants.LOCATION_CONFIGURE_REQUEST_CODE);
    }

    private void SelectHotelCoverImage() {
        Intent imagePickIntent = new Intent();

        imagePickIntent.setType("image/*");
        imagePickIntent.setAction(Intent.ACTION_GET_CONTENT);

        imagePickIntent.putExtra("crop", true);
        imagePickIntent.putExtra("scale", true);
        imagePickIntent.putExtra("aspectX", 1);
        imagePickIntent.putExtra("aspectY", 1);
        imagePickIntent.putExtra("outputX", 100);
        imagePickIntent.putExtra("outputY", 100);

        startActivityForResult(imagePickIntent, Constants.HOTEL_COVER_IMAGE_PICK_REQUEST);
    }

    private void SelectCurrencyUnit() {
        List<SelectorItemModel> itemList = new ArrayList<>();
        for (NewCurrencyModel currencyModel : UserHomeActivity.currencyList) {
            itemList.add(new SelectorItemModel(currencyModel.getCurrencyId().toString(), currencyModel.getCode()));
        }
        new Functions().ShowItemSelector("Select Currency Unit", itemList, hotelActivity, tvCurrency, null);
    }

    private void SelectHotelClass() {
        List<SelectorItemModel> itemList = new ArrayList<>();
        itemList.add(new SelectorItemModel("ITM1", "1 Star"));
        itemList.add(new SelectorItemModel("ITM2", "2 Star"));
        itemList.add(new SelectorItemModel("ITM3", "3 Star"));
        itemList.add(new SelectorItemModel("ITM4", "4 Star"));
        itemList.add(new SelectorItemModel("ITM5", "5 Star"));
        new Functions().ShowItemSelector("Select Hotel Class", itemList, hotelActivity, tvHotelClass, null);
    }

    public void ShowSetupRoomsConfirmationDialog() {
        if (!isSetupRoomsConfirmationDialogVisible) {
            Button btnOkay, btnSetupLater;

            setupRoomConfirmationDialog = new Dialog(this);
            setupRoomConfirmationDialog.setContentView(R.layout.room_setup_dialog);
            setupRoomConfirmationDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            Window window = setupRoomConfirmationDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            window.getAttributes().windowAnimations = R.style.DialogAnimation;

            btnOkay = setupRoomConfirmationDialog.findViewById(R.id.btnOkay);
            btnSetupLater = setupRoomConfirmationDialog.findViewById(R.id.btnSetupLater);

            setupRoomConfirmationDialog.setCancelable(false);
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setupRoomConfirmationDialog.show();
            isSetupRoomsConfirmationDialogVisible = true;

            btnOkay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setupRoomConfirmationDialog.dismiss();
                    ManageHotelRooms();
                }
            });

            btnSetupLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setupRoomConfirmationDialog.dismiss();
                    finish();
                }
            });
        }
    }

    private void ManageHotelRooms() {
        Intent hotelRoomsIntent = new Intent(HotelActivity.this, HotelRoomsActivity.class);
        //hotelRoomsIntent.putExtra("HotelID", myHotelModel.getID());
        hotelRoomsIntent.putExtra("Hotel", new Gson().toJson(myHotelModel));
        //finish();
        startActivity(hotelRoomsIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.HOTEL_COVER_IMAGE_PICK_REQUEST && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                hotelCoverImageURI = data.getData();

                try {
                    hotelCoverBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), hotelCoverImageURI);
                    imgViewHotelImage.setImageBitmap(hotelCoverBitmap);
                    isHotelCoverSelected = isHotelCoverUpdated = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if(requestCode == Constants.LOCATION_CONFIGURE_REQUEST_CODE && resultCode == RESULT_OK) {
            locationId = data.getIntExtra("LocationId", 0);
            locationName = data.getStringExtra("LocationName");
            tvHotelLocation.setText(locationName);
            latitude = data.getDoubleExtra("Latitude", 0);
            longitude = data.getDoubleExtra("Longitude", 0);
        }
    }
}