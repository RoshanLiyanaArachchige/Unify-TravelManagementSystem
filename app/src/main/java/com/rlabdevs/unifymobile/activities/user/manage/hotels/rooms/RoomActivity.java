package com.rlabdevs.unifymobile.activities.user.manage.hotels.rooms;

import static com.rlabdevs.unifymobile.activities.MainActivity.firebaseStorage;
import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.rlabdevs.unifymobile.activities.user.manage.hotels.HotelActivity;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.Regex;
import com.rlabdevs.unifymobile.common.enums.StatusCode;
import com.rlabdevs.unifymobile.models.CurrencyModel;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.IndexModel;
import com.rlabdevs.unifymobile.models.RoomModel;
import com.rlabdevs.unifymobile.models.RoomTypesModel;
import com.rlabdevs.unifymobile.models.SelectorItemModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ROOM_COVER_IMAGE_PICK_REQUEST = 16500;

    private RoomActivity roomActivity;

    public static TextView tvCurrency, tvRoomType, tvNoOfAdults, tvNoOfChildren;
    public static EditText txtRoomTypeCode;
    private RelativeLayout relativeLayoutRoom;
    private EditText txtRoomCode, txtRoomDescription, txtRoomPrice, txtNoOfTotalRooms;
    private CheckBox chkFreeWIFI, chkAirConditioned, chkFreeBreakfast, chkTeaCoffee, chkBar, chkRoomService, chkTelevision, chkPool, chkSpa, chkParking;
    private Button btnManageRoomsTypes, btnReduceRoomCount, btnIncreaseRoomCount, btnSaveRoom;
    private ImageView imgViewRoomImage, imgViewSelectCoverImage;

    private Dialog setupRoomConfirmationDialog;

    private StorageReference storageReference;
    private CollectionReference indexReference, currencyReference, hotelsReference, roomsReference, roomTypesReference;

    private List<RoomTypesModel> roomTypesList;
    private List<CurrencyModel> currencyList;

    private HotelModel hotelModel;
    private RoomModel roomModel;
    private IndexModel roomIndex;

    private Uri roomCoverImageURI;
    private Bitmap roomCoverBitmap;

    public static String currencyCode;
    private String roomCode;
    private boolean isRoomCoverSelected, isRoomCoverUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        InitActivity();
    }

    private void InitActivity() {

        relativeLayoutRoom = findViewById(R.id.relativeLayoutRoom);
        txtRoomCode = findViewById(R.id.txtRoomCode);
        tvRoomType = findViewById(R.id.tvRoomType);
        txtRoomTypeCode = findViewById(R.id.txtRoomTypeCode);
        txtRoomDescription = findViewById(R.id.txtRoomDescription);
        txtRoomPrice = findViewById(R.id.txtRoomPrice);
        tvCurrency = findViewById(R.id.tvCurrency);
        tvNoOfAdults = findViewById(R.id.tvNoOfAdults);
        tvNoOfChildren = findViewById(R.id.tvNoOfChildren);
        txtNoOfTotalRooms = findViewById(R.id.txtNoOfTotalRooms);
        btnReduceRoomCount = findViewById(R.id.btnReduceRoomCount);
        btnIncreaseRoomCount = findViewById(R.id.btnIncreaseRoomCount);
        chkFreeWIFI = findViewById(R.id.chkFreeWIFI);
        chkAirConditioned = findViewById(R.id.chkAirConditioned);
        chkFreeBreakfast = findViewById(R.id.chkFreeBreakfast);
        chkTeaCoffee = findViewById(R.id.chkTeaCoffee);
        chkBar = findViewById(R.id.chkBar);
        chkRoomService = findViewById(R.id.chkRoomService);
        chkTelevision = findViewById(R.id.chkTelevision);
        chkPool = findViewById(R.id.chkPool);
        chkSpa = findViewById(R.id.chkSpa);
        chkParking = findViewById(R.id.chkParking);
        btnManageRoomsTypes = findViewById(R.id.btnManageRoomsTypes);
        btnSaveRoom = findViewById(R.id.btnSaveRoom);
        imgViewRoomImage = findViewById(R.id.imgViewRoomImage);
        imgViewSelectCoverImage = findViewById(R.id.imgViewSelectCoverImage);

        imgViewSelectCoverImage.setOnClickListener(this);
        tvRoomType.setOnClickListener(this);
        tvCurrency.setOnClickListener(this);
        tvNoOfAdults.setOnClickListener(this);
        tvNoOfChildren.setOnClickListener(this);
        btnReduceRoomCount.setOnClickListener(this);
        btnIncreaseRoomCount.setOnClickListener(this);
        btnManageRoomsTypes.setOnClickListener(this);
        btnSaveRoom.setOnClickListener(this);

        roomActivity = this;
        hotelModel = new Gson().fromJson(getIntent().getStringExtra("Hotel"), HotelModel.class);
        if (getIntent().hasExtra("Room")) {
            roomModel = new Gson().fromJson(getIntent().getStringExtra("Room"), RoomModel.class);
        }

        storageReference = firebaseStorage.getReference();
        indexReference = firestoreDB.collection("Index");
        currencyReference = firestoreDB.collection("Currency");
        hotelsReference = firestoreDB.collection("Hotels");
        roomsReference = hotelsReference.document(hotelModel.getID()).collection("Rooms");
        roomTypesReference = hotelsReference.document(hotelModel.getID()).collection("RoomTypes");

        if (roomModel != null) InitRoom();
        else InitRoomRegistration();

    }

    private void InitRoom() {
        btnSaveRoom.setText("Update Room");

        new Thread(new Runnable() {
            @Override
            public void run() {
                roomActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(RoomActivity.this, "Connecting to server...", "Please wait !");
                        Picasso.with(roomActivity).load(roomModel.getRoomImage()).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                //linearLytImageProgress.setVisibility(View.GONE);
                                imgViewRoomImage.setImageBitmap(bitmap);
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

                isRoomCoverSelected = true;
                btnManageRoomsTypes.setVisibility(View.VISIBLE);
                txtRoomCode.setText(roomModel.getRoomCode() + " (Registration Code)");
                txtRoomTypeCode.setText(roomModel.getRoomTypeCode());
                txtRoomDescription.setText(roomModel.getRoomDescription());
                txtRoomPrice.setText(String.valueOf(roomModel.getRoomPrice()));
                currencyCode = roomModel.getCurrencyCode();
                tvNoOfAdults.setText(roomModel.getNoOfAdults() > 1 ? roomModel.getNoOfAdults() + " Adults" : roomModel.getNoOfAdults() + " Adult");
                tvNoOfChildren.setText(roomModel.getNoOfChildren() > 1 ? roomModel.getNoOfChildren() + " Children" : roomModel.getNoOfChildren() + " Child");
                txtNoOfTotalRooms.setText(roomModel.getNoOfTotalRooms() > 1 ? roomModel.getNoOfTotalRooms() + " Rooms" : roomModel.getNoOfTotalRooms() + " Room");
                chkFreeWIFI.setChecked(roomModel.isFreeWIFI());
                chkAirConditioned.setChecked(roomModel.isAirConditioned());
                chkFreeBreakfast.setChecked(roomModel.isFreeBreakfast());
                chkTeaCoffee.setChecked(roomModel.isTeaCoffee());
                chkBar.setChecked(roomModel.isBar());
                chkRoomService.setChecked(roomModel.isRoomService());
                chkTelevision.setChecked(roomModel.isTelevision());
                chkPool.setChecked(roomModel.isPool());
                chkSpa.setChecked(roomModel.isSpa());
                chkParking.setChecked(roomModel.isParking());

                GetCurrencyListAndRoomTypesList();
            }
        }).start();
    }

    private void InitRoomRegistration() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                roomActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(RoomActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                indexReference.whereEqualTo("indexName", "Hotels").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    roomIndex = documentSnapshotList.get(0).toObject(IndexModel.class);
                                    roomIndex.setID(documentSnapshotList.get(0).getId());
                                    if (roomIndex != null) {
                                        roomCode = roomIndex.getPrefix() + (roomIndex.getCurrentCount() + 1);
                                        txtRoomCode.setText(roomCode + " (Registration Code)");
                                    }
                                    GetCurrencyListAndRoomTypesList();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                roomActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    private void GetCurrencyListAndRoomTypesList() {
        currencyReference.whereEqualTo("statusCode", StatusCode.Active.getStatusCode()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            currencyList = new ArrayList<>();
                            List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                                CurrencyModel currency = documentSnapshot.toObject(CurrencyModel.class);
                                currencyList.add(currency);
                                if (roomModel != null)
                                    if (roomModel.getCurrencyCode().equals(currency.getCurrencyCode()))
                                    {
                                        currencyCode = currency.getCurrencyCode();
                                        tvCurrency.setText(currency.getSymbol());
                                    }
                            }
                        }

                        roomTypesReference.get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            roomTypesList = new ArrayList<>();
                                            List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                            for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                                                RoomTypesModel roomType = documentSnapshot.toObject(RoomTypesModel.class);
                                                roomType.setID(documentSnapshot.getId());
                                                if (roomModel != null)
                                                    if (roomModel.getRoomTypeCode().equals(roomType.getRoomTypeCode()))
                                                        tvRoomType.setText(roomType.getRoomType());
                                                roomTypesList.add(roomType);
                                            }

                                            roomActivity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Functions.HideProgressBar();
                                                }
                                            });
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        roomActivity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Functions.HideProgressBar();
                                                new Functions().ShowErrorDialog("Room Types Load Error !", "Try Again", RoomActivity.this);
                                            }
                                        });
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        roomActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                Functions.HideProgressBar();
                                new Functions().ShowErrorDialog("Currency Load Error !", "Try Again", RoomActivity.this);
                            }
                        });
                    }
                });
    }

    private void SelectCurrencyUnit() {
        List<SelectorItemModel> itemList = new ArrayList<>();
        for (CurrencyModel currencyModel : currencyList) {
            itemList.add(new SelectorItemModel(currencyModel.getCurrencyCode(), currencyModel.getSymbol()));
        }
        new Functions().ShowItemSelector("Select Currency Unit", itemList, roomActivity, tvCurrency, null);
    }

    private void SelectRoomCoverImage() {
        Intent imagePickIntent = new Intent();

        imagePickIntent.setType("image/*");
        imagePickIntent.setAction(Intent.ACTION_GET_CONTENT);

        imagePickIntent.putExtra("crop", true);
        imagePickIntent.putExtra("scale", true);
        imagePickIntent.putExtra("aspectX", 1);
        imagePickIntent.putExtra("aspectY", 1);
        imagePickIntent.putExtra("outputX", 100);
        imagePickIntent.putExtra("outputY", 100);

        startActivityForResult(imagePickIntent, ROOM_COVER_IMAGE_PICK_REQUEST);
    }

    private void SelectRoomType() {
        if(roomTypesList.size() > 0)
        {
            List<SelectorItemModel> itemList = new ArrayList<>();
            for (RoomTypesModel roomType : roomTypesList) {
                itemList.add(new SelectorItemModel(roomType.getRoomTypeCode(), roomType.getRoomType()));
            }
            new Functions().ShowItemSelector("Select Room Type", itemList, roomActivity, tvRoomType, null);
        }
        else
        {
            new Functions().ShowErrorDialog("Configure Room Types !", "Okay", roomActivity);
        }
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

    private void SelectNoOfAdultsOrChildren(TextView tvSet, String type) {
        List<SelectorItemModel> itemList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            if(i==1) itemList.add(new SelectorItemModel("ITM" + i, i + " " + type));
            else if (type.equals("Adult")) itemList.add(new SelectorItemModel("ITM" + i, i + " Adults"));
            else if (type.equals("Child")) itemList.add(new SelectorItemModel("ITM" + i, i + " Children"));
        }
        new Functions().ShowItemSelector("Select No. Of" + ((type.equals("Adult") ? " Adults" : " Children")), itemList, roomActivity, tvSet, null);
    }

    private void ManageRoomTypes() {
        Intent roomTypesIntent = new Intent(RoomActivity.this, RoomTypesActivity.class);
        roomTypesIntent.putExtra("HotelID", hotelModel.getID());
        //finish();
        startActivity(roomTypesIntent);
    }

    private boolean ValidateRoom() {
        if (!isRoomCoverSelected)
        return new Functions().ShowErrorDialog("Select Room Cover Image !", "Okay", this);

        String roomType = tvRoomType.getText().toString().trim();
        if (roomType.equals("")) {
            return new Functions().ShowErrorDialog("Select Room Type !", "Okay", this);
        }

        String roomDescription = txtRoomDescription.getText().toString().trim();
        if (roomDescription.equals("")) {
            return new Functions().ShowErrorDialog("Room Description Required !", "Okay", this);
        } else if (roomDescription.length() < 25) {
            return new Functions().ShowErrorDialog("Description: Min 25 Chars !", "Try Again", this);
        }

        String roomPrice = txtRoomPrice.getText().toString().trim();
        if (roomPrice.equals("")) {
            return new Functions().ShowErrorDialog("Enter Room Price !", "Okay", this);
        } else if (!Regex.ValidateDecimalsOnly(roomPrice)) {
            return new Functions().ShowErrorDialog("Room Price: Enter Decimals Only !", "Try Again", this);
        } else if (Double.parseDouble(roomPrice) <= 0) {
            return new Functions().ShowErrorDialog("Room Price: More Than 0 !", "Try Again", this);
        }

        String currency = tvCurrency.getText().toString().trim();
        if (currency.equals("")) {
            return new Functions().ShowErrorDialog("Select Currency Unit !", "Okay", this);
        }

        String noOfAdults = tvNoOfAdults.getText().toString().trim();
        if (noOfAdults.equals("")) {
            return new Functions().ShowErrorDialog("Select No. Of Adults !", "Okay", this);
        }

        String noOfRooms = txtNoOfTotalRooms.getText().toString().trim();
        if (noOfRooms.equals("")) {
            return new Functions().ShowErrorDialog("Enter Total Room Qty !", "Okay", this);
        }

        if (!chkFreeWIFI.isChecked() && !chkAirConditioned.isChecked() && !chkFreeBreakfast.isChecked() && !chkTeaCoffee.isChecked() && !chkBar.isChecked() && !chkRoomService.isChecked() && !chkTelevision.isChecked() && !chkPool.isChecked() && !chkSpa.isChecked() && !chkParking.isChecked())
            return new Functions().ShowErrorDialog("Select 1 Or More Amenities !", "Okay", this);

        return true;
    }

    private void SaveRoom() {
        if(ValidateRoom())
        {
            if (roomModel == null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        roomActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(RoomActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });
                        roomModel = new RoomModel();
                        roomModel.setHotelCode(hotelModel.getHotelCode());
                        roomModel.setRoomCode(roomCode);
                        roomModel.setRoomTypeCode(txtRoomTypeCode.getText().toString());
                        roomModel.setRoomDescription(txtRoomDescription.getText().toString().trim());
                        roomModel.setRoomPrice(Double.parseDouble(txtRoomPrice.getText().toString().trim()));
                        roomModel.setNoOfAdults(Integer.parseInt(tvNoOfAdults.getText().toString().trim().replace("Adults", "").replace("Adult", "")));
                        roomModel.setNoOfChildren(Integer.parseInt(tvNoOfChildren.getText().toString().trim().replace("Children", "").replace("Child", "")));
                        roomModel.setNoOfTotalRooms(Integer.parseInt(txtNoOfTotalRooms.getText().toString().trim().replace("Rooms", "").replace("Room", "")));
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

                        if (roomCoverImageURI != null) {
                            Functions.UpdateProgress("Uploading Cover 0%");
                            StorageReference roomStorageReference = storageReference.child("Images/Hotels/" + hotelModel.getHotelCode() + "/Rooms/" + roomModel.getRoomCode() + "/CoverImages/Image-1");

                            roomStorageReference.putFile(roomCoverImageURI)
                                    .addOnSuccessListener(
                                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    roomStorageReference.getDownloadUrl()
                                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    roomModel.setRoomImage(uri.toString());
                                                                    DocumentReference roomDoc = roomsReference.document();
                                                                    roomModel.setID(roomDoc.getId());
                                                                    roomDoc.set(roomModel)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    roomIndex.setCurrentCount(roomIndex.getCurrentCount() + 1);
                                                                                    indexReference.document(roomIndex.getID()).set(roomIndex)
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    roomActivity.runOnUiThread(new Runnable() {
                                                                                                        public void run() {
                                                                                                            Functions.HideProgressBar();
                                                                                                            Toast.makeText(RoomActivity.this, "Room Registered !", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            })
                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                @Override
                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                    roomActivity.runOnUiThread(new Runnable() {
                                                                                                        public void run() {
                                                                                                            Functions.HideProgressBar();
                                                                                                            new Functions().ShowErrorDialog("Error Occurred !", "Try Again", RoomActivity.this);
                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            });
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    roomActivity.runOnUiThread(new Runnable() {
                                                                                        public void run() {
                                                                                            Functions.HideProgressBar();
                                                                                            new Functions().ShowErrorDialog("Room Registration Failure !", "Try Again", RoomActivity.this);
                                                                                        }
                                                                                    });
                                                                                }
                                                                            });
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    roomActivity.runOnUiThread(new Runnable() {
                                                                        public void run() {
                                                                            Functions.HideProgressBar();
                                                                            new Functions().ShowErrorDialog("Cover Upload Failure !", "Try Again", RoomActivity.this);
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                }
                                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            roomActivity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Functions.HideProgressBar();
                                                    new Functions().ShowErrorDialog("Room Cover Image Upload Failed !", "Try Again", RoomActivity.this);
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
                    }
                }).start();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        roomActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(RoomActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });

                        roomModel.setHotelCode(hotelModel.getHotelCode());
                        roomModel.setRoomCode(roomModel.getRoomCode());
                        roomModel.setRoomTypeCode(txtRoomTypeCode.getText().toString());
                        roomModel.setRoomDescription(txtRoomDescription.getText().toString().trim());
                        roomModel.setRoomPrice(Double.parseDouble(txtRoomPrice.getText().toString().trim()));
                        roomModel.setNoOfAdults(Integer.parseInt(tvNoOfAdults.getText().toString().trim().replace("Adults", "").replace("Adult", "").trim()));
                        roomModel.setNoOfChildren(Integer.parseInt(tvNoOfChildren.getText().toString().trim().replace("Children", "").replace("Child", "").trim()));
                        roomModel.setNoOfTotalRooms(Integer.parseInt(txtNoOfTotalRooms.getText().toString().trim().replace("Rooms", "").replace("Room", "").trim()));
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

                        if (isRoomCoverUpdated) {
                            if (roomCoverImageURI != null) {
                                Functions.UpdateProgress("Uploading Cover 0%");
                                StorageReference roomStorageReference = storageReference.child("Images/Hotels/" + hotelModel.getHotelCode() + "/Rooms/" + roomModel.getRoomCode() + "/CoverImages/Image-1");

                                roomStorageReference.putFile(roomCoverImageURI)
                                        .addOnSuccessListener(
                                                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        roomStorageReference.getDownloadUrl()
                                                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        roomModel.setRoomImage(uri.toString());
                                                                        roomsReference.document(roomModel.getID()).set(roomModel)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        roomActivity.runOnUiThread(new Runnable() {
                                                                                            public void run() {
                                                                                                Functions.HideProgressBar();
                                                                                                finish();
                                                                                                Toast.makeText(RoomActivity.this, "Room Updated !", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        roomActivity.runOnUiThread(new Runnable() {
                                                                                            public void run() {
                                                                                                Functions.HideProgressBar();
                                                                                                new Functions().ShowErrorDialog("Room Update Failure !", "Try Again", RoomActivity.this);
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                });
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        roomActivity.runOnUiThread(new Runnable() {
                                                                            public void run() {
                                                                                Functions.HideProgressBar();
                                                                                new Functions().ShowErrorDialog("Room Update Failure !", "Try Again", RoomActivity.this);
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                    }
                                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                roomActivity.runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        Functions.HideProgressBar();
                                                        new Functions().ShowErrorDialog("Room Cover Image Upload Failed !", "Try Again", RoomActivity.this);
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
                            roomsReference.document(roomModel.getID()).set(roomModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            roomActivity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Functions.HideProgressBar();
                                                    finish();
                                                    Toast.makeText(RoomActivity.this, "Room Updated !", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            roomActivity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Functions.HideProgressBar();
                                                    new Functions().ShowErrorDialog("Room Update Failure !", "Try Again", RoomActivity.this);
                                                }
                                            });
                                        }
                                    });
                        }
                    }
                }).start();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ROOM_COVER_IMAGE_PICK_REQUEST && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                roomCoverImageURI = data.getData();

                try {
                    roomCoverBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), roomCoverImageURI);
                    imgViewRoomImage.setImageBitmap(roomCoverBitmap);
                    isRoomCoverSelected = isRoomCoverUpdated = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgViewSelectCoverImage: {
                SelectRoomCoverImage();
                break;
            }
            case R.id.tvRoomType: {
                SelectRoomType();
                break;
            }
            case R.id.tvCurrency: {
                SelectCurrencyUnit();
                break;
            }
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
            case R.id.btnManageRoomsTypes: {
                ManageRoomTypes();
                break;
            }
            case R.id.btnSaveRoom: {
                SaveRoom();
                break;
            }
        }
    }
}