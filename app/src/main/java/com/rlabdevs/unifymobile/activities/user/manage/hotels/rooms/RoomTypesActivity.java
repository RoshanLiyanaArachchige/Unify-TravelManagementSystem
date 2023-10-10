package com.rlabdevs.unifymobile.activities.user.manage.hotels.rooms;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;
import static com.rlabdevs.unifymobile.common.Functions.GetStatusCodeFromName;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.MainActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.HotelActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.MyHotelsActivity;
import com.rlabdevs.unifymobile.adapters.HotelRoomsAdapter;
import com.rlabdevs.unifymobile.adapters.RoomTypesAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.enums.StatusCode;
import com.rlabdevs.unifymobile.common.enums.UserRole;
import com.rlabdevs.unifymobile.models.HotelModel;
import com.rlabdevs.unifymobile.models.IndexModel;
import com.rlabdevs.unifymobile.models.RoomTypesModel;
import com.rlabdevs.unifymobile.models.SelectorItemModel;

import java.util.ArrayList;
import java.util.List;

public class RoomTypesActivity extends AppCompatActivity implements View.OnClickListener {

    private RoomTypesActivity roomTypesActivity;

    public static TextView tvRoomTypeID, tvStatus;
    public static EditText txtStatusCode;
    public EditText txtRoomTypeCode, txtRoomType;
    public RelativeLayout relativeLytNewRoomType;
    private RelativeLayout relativeLayoutRoomTypes;
    private TextView tvNoAddRoomsTypes;
    private RecyclerView recyclerViewRoomTypes;
    private ImageView imgViewAddNewRoom;
    public Button btnSaveRoomType;
    private SpinKitView spinKitProgress;

    private CollectionReference indexReference, roomTypesReference;

    private RoomTypesAdapter roomTypesAdapter;
    private List<RoomTypesModel> roomTypesList;
    private LinearLayoutManager linearLayoutManager;

    public IndexModel roomTypeIndex;
    private String roomTypeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_types);
        roomTypesActivity = this;
        indexReference = firestoreDB.collection("Index");
        roomTypesReference = firestoreDB.collection("Hotels").document(getIntent().getStringExtra("HotelID")).collection("RoomTypes");
        InitUI();
        InitRecyclerViewRoomTypes();
        FetchHotelRoomsTypes();
    }

    private void InitUI() {
        relativeLayoutRoomTypes = findViewById(R.id.relativeLayoutRoomTypes);
        relativeLytNewRoomType = findViewById(R.id.relativeLytNewRoomType);
        tvNoAddRoomsTypes = findViewById(R.id.tvNoAddRoomsTypes);
        tvRoomTypeID = findViewById(R.id.tvRoomTypeID);
        txtRoomTypeCode = findViewById(R.id.txtRoomTypeCode);
        txtRoomType = findViewById(R.id.txtRoomType);
        tvStatus = findViewById(R.id.tvStatus);
        txtStatusCode = findViewById(R.id.txtStatusCode);
        imgViewAddNewRoom = findViewById(R.id.imgViewAddNewRoom);
        btnSaveRoomType = findViewById(R.id.btnSaveRoomType);
        spinKitProgress = findViewById(R.id.spinKitProgress);

        tvNoAddRoomsTypes.setOnClickListener(this);
        imgViewAddNewRoom.setOnClickListener(this);
        tvStatus.setOnClickListener(this);
        btnSaveRoomType.setOnClickListener(this);
    }

    private void InitRecyclerViewRoomTypes() {
        recyclerViewRoomTypes = findViewById(R.id.recyclerViewRoomTypes);
        roomTypesList = new ArrayList<>();
        roomTypesAdapter = new RoomTypesAdapter(this, roomTypesList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewRoomTypes.setLayoutManager(linearLayoutManager);
        recyclerViewRoomTypes.setAdapter(roomTypesAdapter);
    }

    private void FetchHotelRoomsTypes() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                roomTypesActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        spinKitProgress.setVisibility(View.VISIBLE);
                        new Functions().ShowProgressBar(RoomTypesActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                roomTypesReference.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(!queryDocumentSnapshots.isEmpty())
                                {
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot documentSnapshot: documentSnapshotList) {
                                        RoomTypesModel roomType = documentSnapshot.toObject(RoomTypesModel.class);
                                        roomType.setID(documentSnapshot.getId());
                                        roomTypesList.add(roomType);
                                        roomTypesAdapter.notifyDataSetChanged();
                                    }
                                    imgViewAddNewRoom.setVisibility(View.VISIBLE);
                                    spinKitProgress.setVisibility(View.GONE);
                                }
                                else
                                {
                                    roomTypesActivity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            spinKitProgress.setVisibility(View.GONE);
                                            tvNoAddRoomsTypes.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }

                                roomTypesActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                roomTypesActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                        new Functions().ShowErrorDialog("Error Occurred", "Try Again", RoomTypesActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    private void ShowNewRoomType() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                roomTypesActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(RoomTypesActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                indexReference.whereEqualTo("indexName", "RoomTypes").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    roomTypeIndex = documentSnapshotList.get(0).toObject(IndexModel.class);
                                    roomTypeIndex.setID(documentSnapshotList.get(0).getId());
                                    if (roomTypeIndex != null) {
                                        roomTypeCode = roomTypeIndex.getPrefix() + (roomTypeIndex.getCurrentCount() + 1);
                                        roomTypesActivity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                txtRoomType.setText("");
                                                tvStatus.setText("");
                                                txtRoomTypeCode.setText(roomTypeCode + " (Reg. Code)");
                                                if(relativeLytNewRoomType.getVisibility() == View.GONE)
                                                    relativeLytNewRoomType.setVisibility(View.VISIBLE);
                                                Functions.HideProgressBar();
                                            }
                                        });
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                roomTypesActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                        new Functions().ShowErrorDialog("New Room Type Failed !", "Try Again", RoomTypesActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    private void SelectRoomTypeStatus() {
        List<SelectorItemModel> itemList = new ArrayList<>();
        itemList.add(new SelectorItemModel(StatusCode.Active.getStatusCode(), StatusCode.Active.name()));
        itemList.add(new SelectorItemModel(StatusCode.Deactive.getStatusCode(), StatusCode.Deactive.name()));
        new Functions().ShowItemSelector("Select Room Type Status", itemList, roomTypesActivity, tvStatus, null);
    }

    private void SaveRoomType() {
        if(ValidateRoomType())
        {
            if(roomTypeIndex != null)
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        roomTypesActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(RoomTypesActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });
                        DocumentReference roomTypeDoc = roomTypesReference.document();
                        RoomTypesModel roomType = new RoomTypesModel(roomTypeCode, txtRoomType.getText().toString(), StatusCode.Active.getStatusCode());
                        roomType.setID(roomTypeDoc.getId());
                        roomTypeDoc.set(roomType)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        roomTypesList.add(roomType);
                                        roomTypesAdapter.notifyDataSetChanged();
                                        roomTypeIndex.setCurrentCount(roomTypeIndex.getCurrentCount()+ 1);
                                        indexReference.document(roomTypeIndex.getID()).set(roomTypeIndex)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        roomTypesActivity.runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                Functions.HideProgressBar();
                                                                roomTypeIndex = null;
                                                                relativeLytNewRoomType.setVisibility(View.GONE);
                                                                Toast.makeText(RoomTypesActivity.this, "Room Type Created !", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        roomTypesActivity.runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                Functions.HideProgressBar();
                                                                new Functions().ShowErrorDialog("Error Occurred !", "Try Again", RoomTypesActivity.this);
                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Functions.HideProgressBar();
                                        new Functions().ShowErrorDialog("Room Type Save Failed !", "Try Again", RoomTypesActivity.this);
                                    }
                                });
                    }
                }).start();
            }
            else
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        roomTypesActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(RoomTypesActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });
                        String status = tvStatus.getText().toString().replace("(Status)", "").trim();
                        RoomTypesModel roomType = new RoomTypesModel(txtRoomTypeCode.getText().toString(), txtRoomType.getText().toString(), GetStatusCodeFromName(status));
                        roomType.setID(tvRoomTypeID.getText().toString());
                        roomTypesReference.document(roomType.getID()).set(roomType)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        for (RoomTypesModel roomTypeListItem :roomTypesList) {
                                            if(roomTypeListItem.getRoomTypeCode().equals(roomType.getRoomTypeCode()))
                                            {
                                                roomTypeListItem.setRoomType(roomType.getRoomType());
                                                roomTypeListItem.setStatusCode(roomType.getStatusCode());
                                                roomTypesAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        Functions.HideProgressBar();
                                        relativeLytNewRoomType.setVisibility(View.GONE);
                                        roomTypeIndex = null;
                                        Toast.makeText(RoomTypesActivity.this, "Room Type Updated !", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Functions.HideProgressBar();
                                        new Functions().ShowErrorDialog("Room Type Update Failed !", "Try Again", RoomTypesActivity.this);
                                    }
                                });
                    }
                }).start();
            }
        }
    }

    private boolean ValidateRoomType() {
        String roomType = txtRoomType.getText().toString().trim();
        if (roomType.equals("")) { return new Functions().ShowErrorDialog("Room Type Required !", "Okay", this); }

        String roomTypeStatus = tvStatus.getText().toString().trim();
        if (roomTypeStatus.equals("")) { return new Functions().ShowErrorDialog("Select Room Type Status !", "Okay", this); }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvNoAddRoomsTypes :
            case R.id.imgViewAddNewRoom : {
                ShowNewRoomType();
                break;
            }
            case R.id.tvStatus : {
                SelectRoomTypeStatus();
                break;
            }
            case R.id.btnSaveRoomType : {
                SaveRoomType();
                break;
            }
        }
    }
}