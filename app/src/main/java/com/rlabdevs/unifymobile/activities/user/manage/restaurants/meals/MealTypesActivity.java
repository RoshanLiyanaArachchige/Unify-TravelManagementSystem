package com.rlabdevs.unifymobile.activities.user.manage.restaurants.meals;

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
import com.rlabdevs.unifymobile.activities.user.manage.hotels.rooms.RoomActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.rooms.RoomTypesActivity;
import com.rlabdevs.unifymobile.adapters.MealTypesAdapter;
import com.rlabdevs.unifymobile.adapters.RoomTypesAdapter;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.enums.StatusCode;
import com.rlabdevs.unifymobile.models.IndexModel;
import com.rlabdevs.unifymobile.models.MealTypesModel;
import com.rlabdevs.unifymobile.models.RoomTypesModel;
import com.rlabdevs.unifymobile.models.SelectorItemModel;

import java.util.ArrayList;
import java.util.List;

public class MealTypesActivity extends AppCompatActivity implements View.OnClickListener {

    private MealTypesActivity mealTypesActivity;

    public static TextView tvMealTypeID, tvStatus;
    public static EditText txtStatusCode;
    public EditText txtMealTypeCode, txtMealType;
    public RelativeLayout relativeLytNewMealType;
    private RelativeLayout relativeLayoutMealTypes;
    private TextView tvNoAddMealsTypes;
    private RecyclerView recyclerViewMealTypes;
    private ImageView imgViewAddNewMeal;
    public Button btnSaveMealType;
    private SpinKitView spinKitProgress;

    private CollectionReference indexReference, mealTypesReference;

    private MealTypesAdapter mealTypesAdapter;
    private List<MealTypesModel> mealTypesList;
    private LinearLayoutManager linearLayoutManager;

    public IndexModel mealTypeIndex;
    private String mealTypeCode, restaurantCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_types);
        mealTypesActivity = this;
        indexReference = firestoreDB.collection("Index");
        mealTypesReference = firestoreDB.collection("MealTypes");
        restaurantCode = getIntent().getStringExtra("RestaurantCode");
        InitUI();
        InitRecyclerViewMealTypes();
        FetchRestaurantMealsTypes();
    }

    private void InitUI() {
        relativeLayoutMealTypes = findViewById(R.id.relativeLayoutMealTypes);
        relativeLytNewMealType = findViewById(R.id.relativeLytNewMealType);
        tvNoAddMealsTypes = findViewById(R.id.tvNoAddMealsTypes);
        tvMealTypeID = findViewById(R.id.tvMealTypeID);
        txtMealTypeCode = findViewById(R.id.txtMealTypeCode);
        txtMealType = findViewById(R.id.txtMealType);
        tvStatus = findViewById(R.id.tvStatus);
        txtStatusCode = findViewById(R.id.txtStatusCode);
        imgViewAddNewMeal = findViewById(R.id.imgViewAddNewMeal);
        btnSaveMealType = findViewById(R.id.btnSaveMealType);
        spinKitProgress = findViewById(R.id.spinKitProgress);

        tvNoAddMealsTypes.setOnClickListener(this);
        imgViewAddNewMeal.setOnClickListener(this);
        tvStatus.setOnClickListener(this);
        btnSaveMealType.setOnClickListener(this);
    }

    private void InitRecyclerViewMealTypes() {
        recyclerViewMealTypes = findViewById(R.id.recyclerViewMealTypes);
        mealTypesList = new ArrayList<>();
        mealTypesAdapter = new MealTypesAdapter(this, mealTypesList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewMealTypes.setLayoutManager(linearLayoutManager);
        recyclerViewMealTypes.setAdapter(mealTypesAdapter);
    }

    private void FetchRestaurantMealsTypes() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                mealTypesActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        spinKitProgress.setVisibility(View.VISIBLE);
                        new Functions().ShowProgressBar(MealTypesActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                mealTypesReference.whereEqualTo("restaurantCode", restaurantCode).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                                        MealTypesModel mealType = documentSnapshot.toObject(MealTypesModel.class);
                                        mealType.setID(documentSnapshot.getId());
                                        mealTypesList.add(mealType);
                                        mealTypesAdapter.notifyDataSetChanged();
                                    }
                                    imgViewAddNewMeal.setVisibility(View.VISIBLE);
                                    spinKitProgress.setVisibility(View.GONE);
                                    tvNoAddMealsTypes.setVisibility(View.GONE);
                                } else {
                                    mealTypesActivity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            spinKitProgress.setVisibility(View.GONE);
                                            tvNoAddMealsTypes.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }

                                mealTypesActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mealTypesActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                        new Functions().ShowErrorDialog("Error Occurred", "Try Again", MealTypesActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    private void ShowNewMealType() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealTypesActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(MealTypesActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                indexReference.whereEqualTo("indexName", "MealTypes").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    mealTypeIndex = documentSnapshotList.get(0).toObject(IndexModel.class);
                                    mealTypeIndex.setID(documentSnapshotList.get(0).getId());
                                    if (mealTypeIndex != null) {
                                        mealTypeCode = mealTypeIndex.getPrefix() + (mealTypeIndex.getCurrentCount() + 1);
                                        mealTypesActivity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                txtMealType.setText("");
                                                tvStatus.setText("");
                                                txtMealTypeCode.setText(mealTypeCode + " (Reg. Code)");
                                                if (relativeLytNewMealType.getVisibility() == View.GONE)
                                                    relativeLytNewMealType.setVisibility(View.VISIBLE);
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
                                mealTypesActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                        new Functions().ShowErrorDialog("New Meal Type Failed !", "Try Again", MealTypesActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    private void SelectMealTypeStatus() {
        List<SelectorItemModel> itemList = new ArrayList<>();
        itemList.add(new SelectorItemModel(StatusCode.Active.getStatusCode(), StatusCode.Active.name()));
        itemList.add(new SelectorItemModel(StatusCode.Deactive.getStatusCode(), StatusCode.Deactive.name()));
        new Functions().ShowItemSelector("Select Meal Type Status", itemList, mealTypesActivity, tvStatus, null);
    }

    private void SaveMealType() {
        if (ValidateMealType()) {
            if (mealTypeIndex != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mealTypesActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(MealTypesActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });
                        DocumentReference mealTypeDoc = mealTypesReference.document();
                        MealTypesModel mealType = new MealTypesModel(restaurantCode, mealTypeCode, txtMealType.getText().toString(), StatusCode.Active.getStatusCode());
                        mealType.setID(mealTypeDoc.getId());
                        mealTypeDoc.set(mealType)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        mealTypesList.add(mealType);
                                        MealActivity.mealTypesList.add(mealType);
                                        mealTypesAdapter.notifyDataSetChanged();
                                        mealTypeIndex.setCurrentCount(mealTypeIndex.getCurrentCount() + 1);
                                        indexReference.document(mealTypeIndex.getID()).set(mealTypeIndex)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        mealTypesActivity.runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                Functions.HideProgressBar();
                                                                mealTypeIndex = null;
                                                                relativeLytNewMealType.setVisibility(View.GONE);
                                                                Toast.makeText(MealTypesActivity.this, "Meal Type Created !", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        mealTypesActivity.runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                Functions.HideProgressBar();
                                                                new Functions().ShowErrorDialog("Error Occurred !", "Try Again", MealTypesActivity.this);
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
                                        new Functions().ShowErrorDialog("Meal Type Save Failed !", "Try Again", MealTypesActivity.this);
                                    }
                                });
                    }
                }).start();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mealTypesActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(MealTypesActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });
                        String status = tvStatus.getText().toString().replace("(Status)", "").trim();
                        MealTypesModel mealType = new MealTypesModel(restaurantCode, txtMealTypeCode.getText().toString(), txtMealType.getText().toString(), GetStatusCodeFromName(status));
                        mealType.setID(tvMealTypeID.getText().toString());
                        mealTypesReference.document(mealType.getID()).set(mealType)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        MealActivity.mealTypesList = new ArrayList<>();
                                        for (MealTypesModel mealTypeListItem : mealTypesList) {
                                            if (mealTypeListItem.getMealTypeCode().equals(mealType.getMealTypeCode())) {
                                                mealTypeListItem.setMealTypeName(mealType.getMealTypeName());
                                                mealTypeListItem.setStatusCode(mealType.getStatusCode());
                                                mealTypesAdapter.notifyDataSetChanged();
                                            }
                                            MealActivity.mealTypesList.add(mealTypeListItem);
                                        }
                                        Functions.HideProgressBar();
                                        relativeLytNewMealType.setVisibility(View.GONE);
                                        mealTypeIndex = null;
                                        Toast.makeText(MealTypesActivity.this, "Meal Type Updated !", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Functions.HideProgressBar();
                                        new Functions().ShowErrorDialog("Meal Type Update Failed !", "Try Again", MealTypesActivity.this);
                                    }
                                });
                    }
                }).start();
            }
        }
    }

    private boolean ValidateMealType() {
        String mealType = txtMealType.getText().toString().trim();
        if (mealType.equals("")) {
            return new Functions().ShowErrorDialog("Meal Type Required !", "Okay", this);
        }

        String mealTypeStatus = tvStatus.getText().toString().trim();
        if (mealTypeStatus.equals("")) {
            return new Functions().ShowErrorDialog("Select Meal Type Status !", "Okay", this);
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvNoAddMealsTypes:
            case R.id.imgViewAddNewMeal: {
                ShowNewMealType();
                break;
            }
            case R.id.tvStatus: {
                SelectMealTypeStatus();
                break;
            }
            case R.id.btnSaveMealType: {
                SaveMealType();
                break;
            }
        }
    }
}