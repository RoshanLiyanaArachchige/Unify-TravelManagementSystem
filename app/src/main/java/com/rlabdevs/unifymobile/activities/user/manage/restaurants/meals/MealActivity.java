package com.rlabdevs.unifymobile.activities.user.manage.restaurants.meals;

import static com.rlabdevs.unifymobile.activities.MainActivity.firebaseStorage;
import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;
import static com.rlabdevs.unifymobile.common.Constants.ROOM_COVER_IMAGE_PICK_REQUEST;

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
import android.text.Html;
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
import com.rlabdevs.unifymobile.activities.UserHomeActivity;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.Regex;
import com.rlabdevs.unifymobile.common.enums.StatusCode;
import com.rlabdevs.unifymobile.models.CurrencyModel;
import com.rlabdevs.unifymobile.models.IndexModel;
import com.rlabdevs.unifymobile.models.MealModel;
import com.rlabdevs.unifymobile.models.MealTypesModel;
import com.rlabdevs.unifymobile.models.RestaurantModel;
import com.rlabdevs.unifymobile.models.SelectorItemModel;
import com.rlabdevs.unifymobile.models.master.NewCurrencyModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MealActivity extends AppCompatActivity implements View.OnClickListener {

    private MealActivity mealActivity;

    public static TextView tvCurrency, tvMealType;
    public static EditText txtMealTypeCode;
    private RelativeLayout relativeLayoutMeal;
    private EditText txtMealCode, txtMealName, txtMealDescription, txtMealPrice, txtNoOfPersons;
    private Button btnManageMealsTypes, btnReducePersonCount, btnIncreasePersonCount, btnSaveMeal;
    private ImageView imgViewMealImage, imgViewSelectCoverImage;

    private Dialog setupMealConfirmationDialog;

    private StorageReference storageReference;
    private CollectionReference indexReference, restaurantsReference, mealsReference, mealTypesReference;

    public static List<MealTypesModel> mealTypesList;

    private RestaurantModel restaurantModel;
    private MealModel mealModel;
    private IndexModel mealIndex;

    private Uri mealCoverImageURI;
    private Bitmap mealCoverBitmap;

    public static String currencyCode;
    private String mealCode;
    private boolean isMealCoverSelected, isMealCoverUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        InitActivity();
    }

    private void InitActivity() {

        relativeLayoutMeal = findViewById(R.id.relativeLayoutMeal);
        txtMealCode = findViewById(R.id.txtMealCode);
        txtMealName = findViewById(R.id.txtMealName);
        tvMealType = findViewById(R.id.tvMealType);
        txtMealTypeCode = findViewById(R.id.txtMealTypeCode);
        txtMealDescription = findViewById(R.id.txtMealDescription);
        txtMealPrice = findViewById(R.id.txtMealPrice);
        tvCurrency = findViewById(R.id.tvCurrency);
        txtNoOfPersons = findViewById(R.id.txtNoOfPersons);
        btnReducePersonCount = findViewById(R.id.btnReducePersonCount);
        btnIncreasePersonCount = findViewById(R.id.btnIncreasePersonCount);
        btnManageMealsTypes = findViewById(R.id.btnManageMealsTypes);
        btnSaveMeal = findViewById(R.id.btnSaveMeal);
        imgViewMealImage = findViewById(R.id.imgViewMealImage);
        imgViewSelectCoverImage = findViewById(R.id.imgViewSelectCoverImage);

        imgViewSelectCoverImage.setOnClickListener(this);
        tvMealType.setOnClickListener(this);
        tvCurrency.setOnClickListener(this);
        btnReducePersonCount.setOnClickListener(this);
        btnIncreasePersonCount.setOnClickListener(this);
        btnManageMealsTypes.setOnClickListener(this);
        btnSaveMeal.setOnClickListener(this);

        mealActivity = this;
        restaurantModel = new Gson().fromJson(getIntent().getStringExtra("Restaurant"), RestaurantModel.class);
        if (getIntent().hasExtra("Meal")) {
            mealModel = new Gson().fromJson(getIntent().getStringExtra("Meal"), MealModel.class);
        }

        storageReference = firebaseStorage.getReference();
        indexReference = firestoreDB.collection("Index");
        restaurantsReference = firestoreDB.collection("Restaurants");
        mealsReference = firestoreDB.collection("Meals");
        mealTypesReference = firestoreDB.collection("MealTypes");

        if (mealModel != null) InitMeal();
        else InitMealRegistration();

    }

    private void InitMeal() {
        btnSaveMeal.setText("Update Meal");

        new Thread(new Runnable() {
            @Override
            public void run() {
                mealActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(MealActivity.this, "Connecting to server...", "Please wait !");
                        Picasso.with(mealActivity).load(mealModel.getMealImage()).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                //linearLytImageProgress.setVisibility(View.GONE);
                                imgViewMealImage.setImageBitmap(bitmap);
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

                isMealCoverSelected = true;
                btnManageMealsTypes.setVisibility(View.VISIBLE);
                txtMealCode.setText(mealModel.getMealCode() + " (Registration Code)");
                txtMealName.setText(mealModel.getMealName());
                txtMealTypeCode.setText(mealModel.getMealTypeCode());
                txtMealDescription.setText(Html.fromHtml(mealModel.getMealDescription()));
                txtMealPrice.setText(String.valueOf(mealModel.getMealPrice()));

                currencyCode = mealModel.getCurrencyCode();
                String currencySymbol = UserHomeActivity.currencyList.stream().filter(c -> c.getCurrencyId().equals(currencyCode)).findFirst().get().getSymbol();
                tvCurrency.setText(currencySymbol);

                txtNoOfPersons.setText(mealModel.getPortionSize() > 1 ? mealModel.getPortionSize() + " Persons" : mealModel.getPortionSize() + " Person");

                GetMealTypesList();
            }
        }).start();
    }

    private void InitMealRegistration() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(MealActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                indexReference.whereEqualTo("indexName", "Meals").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    mealIndex = documentSnapshotList.get(0).toObject(IndexModel.class);
                                    mealIndex.setID(documentSnapshotList.get(0).getId());
                                    if (mealIndex != null) {
                                        mealCode = mealIndex.getPrefix() + (mealIndex.getCurrentCount() + 1);
                                        txtMealCode.setText(mealCode + " (Registration Code)");
                                    }
                                    GetMealTypesList();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mealActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    private void GetMealTypesList() {
        mealTypesReference.whereEqualTo("restaurantCode", restaurantModel.getRestaurantCode()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            mealTypesList = new ArrayList<>();
                            List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot documentSnapshot : documentSnapshotList) {
                                MealTypesModel mealType = documentSnapshot.toObject(MealTypesModel.class);
                                mealType.setID(documentSnapshot.getId());
                                if (mealModel != null)
                                    if (mealModel.getMealTypeCode().equals(mealType.getMealTypeCode()))
                                        tvMealType.setText(mealType.getMealTypeName());
                                mealTypesList.add(mealType);
                            }
                        }

                        mealActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                Functions.HideProgressBar();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mealActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                Functions.HideProgressBar();
                                new Functions().ShowErrorDialog("Meal Types Load Error !", "Try Again", MealActivity.this);
                            }
                        });
                    }
                });
    }

    private void SelectCurrencyUnit() {
        List<SelectorItemModel> itemList = new ArrayList<>();
        for (NewCurrencyModel currencyModel : UserHomeActivity.currencyList) {
            itemList.add(new SelectorItemModel(currencyModel.getCurrencyId().toString(), currencyModel.getSymbol()));
        }
        new Functions().ShowItemSelector("Select Currency Unit", itemList, mealActivity, tvCurrency, null);
    }

    private void SelectMealCoverImage() {
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

    private void SelectMealType() {
        if(mealTypesList.size() > 0)
        {
            List<SelectorItemModel> itemList = new ArrayList<>();
            for (MealTypesModel mealType : mealTypesList) {
                itemList.add(new SelectorItemModel(mealType.getMealTypeCode(), mealType.getMealTypeName()));
            }
            new Functions().ShowItemSelector("Select Meal Type", itemList, mealActivity, tvMealType, null);
        }
        else
        {
            new Functions().ShowErrorDialog("Configure Meal Types !", "Okay", mealActivity);
        }
    }

    private void UpdateNoPersonPortion(int count) {
        String strNoOfMeals = txtNoOfPersons.getText().toString().trim();
        if(strNoOfMeals.equals(""))
        {
            if(count > 0)
                txtNoOfPersons.setText("1 Person");
        }
        else
        {
            int noOfMeals = Integer.parseInt(strNoOfMeals.replace(" Persons", "").replace(" Person", ""));
            if(count > 0)
                txtNoOfPersons.setText(++noOfMeals + " Persons");
            else if(noOfMeals > 2)
                txtNoOfPersons.setText(--noOfMeals + " Persons");
            else if(noOfMeals == 2)
                txtNoOfPersons.setText(--noOfMeals + " Person");
        }
    }

    private void ManageMealTypes() {
        Intent mealTypesIntent = new Intent(MealActivity.this, MealTypesActivity.class);
        mealTypesIntent.putExtra("RestaurantCode", restaurantModel.getRestaurantCode());
        //finish();
        startActivity(mealTypesIntent);
    }

    private boolean ValidateMeal() {
        if (!isMealCoverSelected) {
            return new Functions().ShowErrorDialog("Select Meal Cover Image !", "Okay", this);
        }

        String mealType = tvMealType.getText().toString().trim();
        if (mealType.equals("")) {
            return new Functions().ShowErrorDialog("Select Meal Type !", "Okay", this);
        }

        String mealName = txtMealName.getText().toString().trim();
        if (mealName.equals("")) {
            return new Functions().ShowErrorDialog("Meal Name Required !", "Okay", this);
        }

        String mealDescription = txtMealDescription.getText().toString().trim();
        if (mealDescription.equals("")) {
            return new Functions().ShowErrorDialog("Meal Description Required !", "Okay", this);
        } else if (mealDescription.length() < 10) {
            return new Functions().ShowErrorDialog("Description: Min 10 Chars !", "Try Again", this);
        }

        String mealPrice = txtMealPrice.getText().toString().trim();
        if (mealPrice.equals("")) {
            return new Functions().ShowErrorDialog("Enter Meal Price !", "Okay", this);
        } else if (!Regex.ValidateDecimalsOnly(mealPrice)) {
            return new Functions().ShowErrorDialog("Meal Price: Enter Decimals Only !", "Try Again", this);
        } else if (Double.parseDouble(mealPrice) <= 0) {
            return new Functions().ShowErrorDialog("Meal Price: More Than 0 !", "Try Again", this);
        }

        String currency = tvCurrency.getText().toString().trim();
        if (currency.equals("")) {
            return new Functions().ShowErrorDialog("Select Currency Unit !", "Okay", this);
        }

        String portionSize = txtNoOfPersons.getText().toString().trim();
        if (portionSize.equals("")) {
            return new Functions().ShowErrorDialog("Enter No. Persons !", "Okay", this);
        }

        return true;
    }

    private void SaveMeal() {
        if(ValidateMeal())
        {
            if (mealModel == null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mealActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(MealActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });
                        mealModel = new MealModel();
                        mealModel.setRestaurantCode(restaurantModel.getRestaurantCode());
                        mealModel.setMealCode(mealCode);
                        mealModel.setMealName(txtMealName.getText().toString());
                        mealModel.setMealTypeCode(txtMealTypeCode.getText().toString());
                        mealModel.setMealDescription(txtMealDescription.getText().toString().trim());
                        mealModel.setMealPrice(Double.parseDouble(txtMealPrice.getText().toString().trim()));

                        String strPortionSize = txtNoOfPersons.getText().toString().replace("Persons", "").replace("Person", "").trim();
                        try {
                            mealModel.setPortionSize(Integer.parseInt(strPortionSize));
                        } catch (Exception ex) {
                        }

                        mealModel.setCurrencyCode(currencyCode);
                        mealModel.setStatusCode(StatusCode.Active.getStatusCode());

                        if (mealCoverImageURI != null) {
                            Functions.UpdateProgress("Uploading Cover 0%");
                            StorageReference mealStorageReference = storageReference.child("Images/Restaurants/" + restaurantModel.getRestaurantCode() + "/Meals/" + mealModel.getMealCode() + "/CoverImages/Image-1");

                            mealStorageReference.putFile(mealCoverImageURI)
                                    .addOnSuccessListener(
                                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    mealStorageReference.getDownloadUrl()
                                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    mealModel.setMealImage(uri.toString());
                                                                    DocumentReference mealDoc = mealsReference.document();
                                                                    mealModel.setID(mealDoc.getId());
                                                                    mealDoc.set(mealModel)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    mealIndex.setCurrentCount(mealIndex.getCurrentCount() + 1);
                                                                                    indexReference.document(mealIndex.getID()).set(mealIndex)
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    mealActivity.runOnUiThread(new Runnable() {
                                                                                                        public void run() {
                                                                                                            Functions.HideProgressBar();
                                                                                                            Toast.makeText(MealActivity.this, "Meal Registered !", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            })
                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                @Override
                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                    mealActivity.runOnUiThread(new Runnable() {
                                                                                                        public void run() {
                                                                                                            Functions.HideProgressBar();
                                                                                                            new Functions().ShowErrorDialog("Error Occurred !", "Try Again", MealActivity.this);
                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            });
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    mealActivity.runOnUiThread(new Runnable() {
                                                                                        public void run() {
                                                                                            Functions.HideProgressBar();
                                                                                            new Functions().ShowErrorDialog("Meal Registration Failure !", "Try Again", MealActivity.this);
                                                                                        }
                                                                                    });
                                                                                }
                                                                            });
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    mealActivity.runOnUiThread(new Runnable() {
                                                                        public void run() {
                                                                            Functions.HideProgressBar();
                                                                            new Functions().ShowErrorDialog("Cover Upload Failure !", "Try Again", MealActivity.this);
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                }
                                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            mealActivity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Functions.HideProgressBar();
                                                    new Functions().ShowErrorDialog("Meal Cover Image Upload Failed !", "Try Again", MealActivity.this);
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
                        mealActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(MealActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });

                        mealModel.setRestaurantCode(restaurantModel.getRestaurantCode());
                        mealModel.setMealCode(mealModel.getMealCode());
                        mealModel.setMealName(txtMealName.getText().toString());
                        mealModel.setMealTypeCode(txtMealTypeCode.getText().toString());
                        mealModel.setMealDescription(txtMealDescription.getText().toString().trim());
                        mealModel.setMealPrice(Double.parseDouble(txtMealPrice.getText().toString().trim()));

                        String strPortionSize = txtNoOfPersons.getText().toString().replace("Persons", "").replace("Person", "").trim();
                        try {
                            mealModel.setPortionSize(Integer.parseInt(strPortionSize));
                        } catch (Exception ex) {
                        }

                        mealModel.setCurrencyCode(currencyCode);

                        if (isMealCoverUpdated) {
                            if (mealCoverImageURI != null) {
                                Functions.UpdateProgress("Uploading Cover 0%");
                                StorageReference mealStorageReference = storageReference.child("Images/Restaurants/" + restaurantModel.getRestaurantCode() + "/Meals/" + mealModel.getMealCode() + "/CoverImages/Image-1");

                                mealStorageReference.putFile(mealCoverImageURI)
                                        .addOnSuccessListener(
                                                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        mealStorageReference.getDownloadUrl()
                                                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        mealModel.setMealImage(uri.toString());
                                                                        mealsReference.document(mealModel.getID()).set(mealModel)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        mealActivity.runOnUiThread(new Runnable() {
                                                                                            public void run() {
                                                                                                Functions.HideProgressBar();
                                                                                                finish();
                                                                                                Toast.makeText(MealActivity.this, "Meal Updated !", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        mealActivity.runOnUiThread(new Runnable() {
                                                                                            public void run() {
                                                                                                Functions.HideProgressBar();
                                                                                                new Functions().ShowErrorDialog("Meal Update Failure !", "Try Again", MealActivity.this);
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                });
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        mealActivity.runOnUiThread(new Runnable() {
                                                                            public void run() {
                                                                                Functions.HideProgressBar();
                                                                                new Functions().ShowErrorDialog("Meal Update Failure !", "Try Again", MealActivity.this);
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                    }
                                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                mealActivity.runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        Functions.HideProgressBar();
                                                        new Functions().ShowErrorDialog("Meal Cover Image Upload Failed !", "Try Again", MealActivity.this);
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
                            mealsReference.document(mealModel.getID()).set(mealModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            mealActivity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Functions.HideProgressBar();
                                                    finish();
                                                    Toast.makeText(MealActivity.this, "Meal Updated !", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            mealActivity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Functions.HideProgressBar();
                                                    new Functions().ShowErrorDialog("Meal Update Failure !", "Try Again", MealActivity.this);
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
                mealCoverImageURI = data.getData();

                try {
                    mealCoverBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mealCoverImageURI);
                    imgViewMealImage.setImageBitmap(mealCoverBitmap);
                    isMealCoverSelected = isMealCoverUpdated = true;
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
                SelectMealCoverImage();
                break;
            }
            case R.id.tvMealType: {
                SelectMealType();
                break;
            }
            case R.id.tvCurrency: {
                SelectCurrencyUnit();
                break;
            }
            case R.id.btnReducePersonCount: {
                UpdateNoPersonPortion(-1);
                break;
            }
            case R.id.btnIncreasePersonCount: {
                UpdateNoPersonPortion(+1);
                break;
            }
            case R.id.btnManageMealsTypes: {
                ManageMealTypes();
                break;
            }
            case R.id.btnSaveMeal: {
                SaveMeal();
                break;
            }
        }
    }
}