package com.rlabdevs.unifymobile.activities.user.manage.restaurants;

import static com.rlabdevs.unifymobile.activities.MainActivity.firebaseStorage;
import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;
import static com.rlabdevs.unifymobile.common.Constants.RESTAURANT_COVER_IMAGE_PICK_REQUEST;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
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
import com.rlabdevs.unifymobile.activities.UserHomeActivity;
import com.rlabdevs.unifymobile.activities.location.ConfigureLocationActivity;
import com.rlabdevs.unifymobile.activities.user.manage.restaurants.meals.RestaurantMealsActivity;
import com.rlabdevs.unifymobile.common.Constants;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.Regex;
import com.rlabdevs.unifymobile.common.enums.StatusCode;
import com.rlabdevs.unifymobile.models.CurrencyModel;
import com.rlabdevs.unifymobile.models.IndexModel;
import com.rlabdevs.unifymobile.models.RestaurantModel;
import com.rlabdevs.unifymobile.models.SelectorItemModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity implements View.OnClickListener {
    private RestaurantActivity restaurantActivity;

    public static TextView tvRestaurantClass, tvCurrency, tvRestaurantLocation, txtOpenTime, txtCloseTime;
    private ImageView imgViewRestaurantImage, imgViewSelectCoverImage;
    private EditText txtRestaurantCode, txtRestaurantName, txtRestaurantDescription, txtTelephoneNo, txtBudget;
    private CheckBox chkFreeWIFI, chkBeverages, chkTakeAway, chkParking;
    private MaterialButtonToggleGroup toggleButtonCuisineTypes;
    private Button btnSaveRestaurant, btnManageMeals;

    private Dialog setupMealConfirmationDialog;

    private StorageReference storageReference;
    private CollectionReference indexReference, currencyReference, restaurantsReference, cuisineTypesReference;

    public RestaurantModel myRestaurantModel;
    private IndexModel restaurantDetailsIndex;

    private Uri restaurantCoverImageURI;
    private Bitmap restaurantCoverBitmap;

    public static String currencyCode;
    private String restaurantCode, locationCode, locationName;
    private Double latitude, longitude;
    private boolean isRestaurantCoverSelected, isRestaurantCoverUpdated, isSetupMealsConfirmationDialogVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        InitActivity();
    }

    private void InitActivity() {
        imgViewRestaurantImage = findViewById(R.id.imgViewRestaurantImage);
        imgViewSelectCoverImage = findViewById(R.id.imgViewSelectCoverImage);
        txtRestaurantCode = findViewById(R.id.txtRestaurantCode);
        txtRestaurantName = findViewById(R.id.txtRestaurantName);
        txtRestaurantDescription = findViewById(R.id.txtRestaurantDescription);
        txtTelephoneNo = findViewById(R.id.txtTelephoneNo);
        txtBudget = findViewById(R.id.txtBudget);
        tvCurrency = findViewById(R.id.tvCurrency);
        tvRestaurantClass = findViewById(R.id.tvRestaurantClass);
        tvRestaurantLocation = findViewById(R.id.tvRestaurantLocation);
        txtOpenTime = findViewById(R.id.txtOpenTime);
        txtCloseTime = findViewById(R.id.txtCloseTime);
        chkFreeWIFI = findViewById(R.id.chkFreeWIFI);
        chkBeverages = findViewById(R.id.chkBeverages);
        chkTakeAway = findViewById(R.id.chkTakeAway);
        chkParking = findViewById(R.id.chkParking);
        toggleButtonCuisineTypes = findViewById(R.id.toggleButtonCuisineTypes);
        btnManageMeals = findViewById(R.id.btnManageMeals);
        btnSaveRestaurant = findViewById(R.id.btnSaveRestaurant);

        imgViewSelectCoverImage.setOnClickListener(this);
        btnManageMeals.setOnClickListener(this);
        btnSaveRestaurant.setOnClickListener(this);
        tvRestaurantClass.setOnClickListener(this);
        tvCurrency.setOnClickListener(this);
        tvRestaurantLocation.setOnClickListener(this);

        restaurantCode = getIntent().getStringExtra("RestaurantCode");

        if (getIntent().hasExtra("MyRestaurant")) {
            myRestaurantModel = new Gson().fromJson(getIntent().getStringExtra("MyRestaurant"), RestaurantModel.class);
        }

        storageReference = firebaseStorage.getReference();
        indexReference = firestoreDB.collection("Index");
        currencyReference = firestoreDB.collection("Currency");
        restaurantsReference = firestoreDB.collection("Restaurants");
        cuisineTypesReference = firestoreDB.collection("CuisineTypes");
        restaurantActivity = this;

        if (myRestaurantModel != null) InitRestaurant();
        else InitRestaurantRegistration();
    }

    private void InitRestaurantRegistration() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                restaurantActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(RestaurantActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                cuisineTypesReference.whereEqualTo("statusCode", StatusCode.Active.getStatusCode()).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                        MaterialButton materialButton = new MaterialButton(restaurantActivity, null, R.attr.materialButtonOutlinedStyle);
                                        materialButton.setTag(documentSnapshot.get("cuisineTypeCode", String.class));
                                        materialButton.setText(documentSnapshot.get("cuisineTypeName", String.class));
                                        materialButton.setPadding(50, 0, 50, 0);
                                        materialButton.setCheckable(true);
                                        toggleButtonCuisineTypes.addView(materialButton);
                                    }
                                }

                                indexReference.whereEqualTo("indexName", "Restaurants").get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                if (!queryDocumentSnapshots.isEmpty()) {
                                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                                    restaurantDetailsIndex = documentSnapshotList.get(0).toObject(IndexModel.class);
                                                    restaurantDetailsIndex.setID(documentSnapshotList.get(0).getId());
                                                    if (restaurantDetailsIndex != null) {
                                                        restaurantCode = restaurantDetailsIndex.getPrefix() + (restaurantDetailsIndex.getCurrentCount() + 1);
                                                        txtRestaurantCode.setText(restaurantCode + " (Registration Code)");
                                                    }
                                                }
                                                restaurantActivity.runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        Functions.HideProgressBar();
                                                    }
                                                });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                restaurantActivity.runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        Functions.HideProgressBar();
                                                    }
                                                });
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(restaurantActivity, "Failed to get cuisine types...", Toast.LENGTH_SHORT).show();
                                Functions.HideProgressBar();
                            }
                        });
            }
        }).start();
    }

    private void InitRestaurant() {
        btnSaveRestaurant.setText("Update Restaurant");

        new Thread(new Runnable() {
            @Override
            public void run() {
                restaurantActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(RestaurantActivity.this, "Connecting to server...", "Please wait !");
                        Picasso.with(restaurantActivity).load(myRestaurantModel.getRestaurantImage()).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                imgViewRestaurantImage.setImageBitmap(bitmap);
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

                isRestaurantCoverSelected = true;
                btnManageMeals.setVisibility(View.VISIBLE);
                txtRestaurantCode.setText(myRestaurantModel.getRestaurantCode() + " (Registration Code)");
                txtRestaurantName.setText(myRestaurantModel.getRestaurantName());
                txtRestaurantDescription.setText(Html.fromHtml(myRestaurantModel.getRestaurantDescription()));
                tvRestaurantLocation.setText(myRestaurantModel.getRestaurantLocation());
                tvRestaurantClass.setText(myRestaurantModel.getRestaurantClass() + " Star");
                txtTelephoneNo.setText(myRestaurantModel.getRestaurantTelNo());
                txtBudget.setText(String.valueOf(myRestaurantModel.getAveragePrice()));
                chkFreeWIFI.setChecked(myRestaurantModel.isFreeWIFI());
                chkBeverages.setChecked(myRestaurantModel.isBeverages());
                chkTakeAway.setChecked(myRestaurantModel.isTakeaway());
                chkParking.setChecked(myRestaurantModel.isParking());

                locationCode = myRestaurantModel.getLocationCode();
                locationName = UserHomeActivity.locationList.stream().filter(c -> c.getLocationCode().equals(locationCode)).findFirst().get().getLocationName();

                latitude = myRestaurantModel.getLatitude();
                longitude = myRestaurantModel.getLongitude();

                txtOpenTime.setText(myRestaurantModel.getOpeningHour());
                txtCloseTime.setText(myRestaurantModel.getClosingHour());

                currencyCode = myRestaurantModel.getCurrencyCode();
                String currencySymbol = UserHomeActivity.currencyList.stream().filter(c -> c.getCurrencyCode().equals(currencyCode)).findFirst().get().getSymbol();
                tvCurrency.setText(currencySymbol);

                cuisineTypesReference.whereEqualTo("statusCode", StatusCode.Active.getStatusCode()).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<String> selectedCuisineTypes = myRestaurantModel.getCuisineTypes();
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                        MaterialButton materialButton = new MaterialButton(restaurantActivity, null, R.attr.materialButtonOutlinedStyle);
                                        String cuisineTypeCode = documentSnapshot.get("cuisineTypeCode", String.class);
                                        materialButton.setTag(cuisineTypeCode);
                                        materialButton.setText(documentSnapshot.get("cuisineTypeName", String.class));
                                        materialButton.setPadding(50, 0, 50, 0);
                                        materialButton.setCheckable(true);
                                        materialButton.setChecked(selectedCuisineTypes.stream().anyMatch(c -> c.equals(cuisineTypeCode)));
                                        toggleButtonCuisineTypes.addView(materialButton);
                                    }
                                }

                                restaurantActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                    }});
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(restaurantActivity, "Failed to get cuisine types...", Toast.LENGTH_SHORT).show();
                                Functions.HideProgressBar();
                            }
                        });
                }
        }).start();
    }

    private void SaveRestaurant() {
        if (ValidateRestaurant()) {
            if (myRestaurantModel == null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        restaurantActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(RestaurantActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });
                        myRestaurantModel = new RestaurantModel();
                        myRestaurantModel.setRestaurantCode(restaurantCode);
                        myRestaurantModel.setRestaurantName(txtRestaurantName.getText().toString().trim());
                        myRestaurantModel.setRestaurantDescription(txtRestaurantDescription.getText().toString().trim());
                        myRestaurantModel.setLocationCode(locationCode);
                        myRestaurantModel.setRestaurantLocation(tvRestaurantLocation.getText().toString().trim());
                        myRestaurantModel.setLatitude(latitude);
                        myRestaurantModel.setLongitude(longitude);
                        myRestaurantModel.setRestaurantClass(Integer.parseInt(tvRestaurantClass.getText().toString().replace(" Star", "")));
                        myRestaurantModel.setRestaurantTelNo(txtTelephoneNo.getText().toString().trim());
                        myRestaurantModel.setOpeningHour(txtOpenTime.getText().toString().trim());
                        myRestaurantModel.setClosingHour(txtCloseTime.getText().toString().trim());
                        myRestaurantModel.setAveragePrice(Double.parseDouble(txtBudget.getText().toString().trim()));
                        myRestaurantModel.setCurrencyCode(currencyCode);
                        myRestaurantModel.setFreeWIFI(chkFreeWIFI.isChecked());
                        myRestaurantModel.setBeverages(chkBeverages.isChecked());
                        myRestaurantModel.setTakeaway(chkTakeAway.isChecked());
                        myRestaurantModel.setParking(chkParking.isChecked());
                        myRestaurantModel.setUserDetailsCode(MainActivity.sharedPref.getString("UserDetailsCode", ""));
                        myRestaurantModel.setStatusCode(StatusCode.Active.getStatusCode());

                        myRestaurantModel.setCuisineTypes(getCuisineTypeList());

                        if (restaurantCoverImageURI != null) {
                            Functions.UpdateProgress("Uploading Cover 0%");
                            StorageReference restaurantStorageReference = storageReference.child("Images/Restaurants/" + restaurantCode + "/CoverImages/Image-1");

                            restaurantStorageReference.putFile(restaurantCoverImageURI)
                                    .addOnSuccessListener(
                                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    restaurantStorageReference.getDownloadUrl()
                                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    myRestaurantModel.setRestaurantImage(uri.toString());
                                                                    DocumentReference restaurantDoc = restaurantsReference.document();
                                                                    myRestaurantModel.setID(restaurantDoc.getId());
                                                                    restaurantDoc.set(myRestaurantModel)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    restaurantDetailsIndex.setCurrentCount(restaurantDetailsIndex.getCurrentCount() + 1);
                                                                                    indexReference.document(restaurantDetailsIndex.getID()).set(restaurantDetailsIndex)
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    restaurantActivity.runOnUiThread(new Runnable() {
                                                                                                        public void run() {
                                                                                                            Functions.HideProgressBar();
                                                                                                            Toast.makeText(RestaurantActivity.this, "Restaurant Registered !", Toast.LENGTH_SHORT).show();
                                                                                                            ShowSetupMealsConfirmationDialog();
                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            })
                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                @Override
                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                    restaurantActivity.runOnUiThread(new Runnable() {
                                                                                                        public void run() {
                                                                                                            Functions.HideProgressBar();
                                                                                                            new Functions().ShowErrorDialog("Error Occurred !", "Try Again", RestaurantActivity.this);
                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            });
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    restaurantActivity.runOnUiThread(new Runnable() {
                                                                                        public void run() {
                                                                                            Functions.HideProgressBar();
                                                                                            new Functions().ShowErrorDialog("Restaurant Registration Failure !", "Try Again", RestaurantActivity.this);
                                                                                        }
                                                                                    });
                                                                                }
                                                                            });
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    restaurantActivity.runOnUiThread(new Runnable() {
                                                                        public void run() {
                                                                            Functions.HideProgressBar();
                                                                            new Functions().ShowErrorDialog("Cover Upload Failure !", "Try Again", RestaurantActivity.this);
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                }
                                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            restaurantActivity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Functions.HideProgressBar();
                                                    new Functions().ShowErrorDialog("Restaurant Cover Image Upload Failed !", "Try Again", RestaurantActivity.this);
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
                        restaurantActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(RestaurantActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });

                        myRestaurantModel.setRestaurantName(txtRestaurantName.getText().toString().trim());
                        myRestaurantModel.setRestaurantDescription(txtRestaurantDescription.getText().toString().trim());
                        myRestaurantModel.setLocationCode(locationCode);
                        myRestaurantModel.setRestaurantLocation(tvRestaurantLocation.getText().toString().trim());
                        myRestaurantModel.setLatitude(latitude);
                        myRestaurantModel.setLongitude(longitude);
                        myRestaurantModel.setRestaurantClass(Integer.parseInt(tvRestaurantClass.getText().toString().replace(" Star", "")));
                        myRestaurantModel.setRestaurantTelNo(txtTelephoneNo.getText().toString().trim());
                        myRestaurantModel.setOpeningHour(txtOpenTime.getText().toString().trim());
                        myRestaurantModel.setClosingHour(txtCloseTime.getText().toString().trim());
                        myRestaurantModel.setAveragePrice(Double.parseDouble(txtBudget.getText().toString().trim()));
                        myRestaurantModel.setCurrencyCode(currencyCode);
                        myRestaurantModel.setFreeWIFI(chkFreeWIFI.isChecked());
                        myRestaurantModel.setBeverages(chkBeverages.isChecked());
                        myRestaurantModel.setTakeaway(chkTakeAway.isChecked());
                        myRestaurantModel.setParking(chkParking.isChecked());

                        myRestaurantModel.setCuisineTypes(getCuisineTypeList());

                        if (isRestaurantCoverUpdated) {
                            if (restaurantCoverImageURI != null) {
                                Functions.UpdateProgress("Uploading Cover 0%");
                                StorageReference restaurantStorageReference = storageReference.child("Images/Restaurants/" + myRestaurantModel.getRestaurantCode() + "/CoverImages/Image-1");

                                restaurantStorageReference.putFile(restaurantCoverImageURI)
                                        .addOnSuccessListener(
                                                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        restaurantStorageReference.getDownloadUrl()
                                                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        myRestaurantModel.setRestaurantImage(uri.toString());
                                                                        restaurantsReference.document(myRestaurantModel.getID()).set(myRestaurantModel)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        restaurantActivity.runOnUiThread(new Runnable() {
                                                                                            public void run() {
                                                                                                Functions.HideProgressBar();
                                                                                                finish();
                                                                                                Toast.makeText(RestaurantActivity.this, "Restaurant Updated !", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        restaurantActivity.runOnUiThread(new Runnable() {
                                                                                            public void run() {
                                                                                                Functions.HideProgressBar();
                                                                                                new Functions().ShowErrorDialog("Restaurant Update Failure !", "Try Again", RestaurantActivity.this);
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                });
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        restaurantActivity.runOnUiThread(new Runnable() {
                                                                            public void run() {
                                                                                Functions.HideProgressBar();
                                                                                new Functions().ShowErrorDialog("Restaurant Update Failure !", "Try Again", RestaurantActivity.this);
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                    }
                                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                restaurantActivity.runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        Functions.HideProgressBar();
                                                        new Functions().ShowErrorDialog("Restaurant Cover Image Upload Failed !", "Try Again", RestaurantActivity.this);
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
                            restaurantsReference.document(myRestaurantModel.getID()).set(myRestaurantModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            restaurantActivity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Functions.HideProgressBar();
                                                    finish();
                                                    Toast.makeText(RestaurantActivity.this, "Restaurant Updated !", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            restaurantActivity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Functions.HideProgressBar();
                                                    new Functions().ShowErrorDialog("Restaurant Update Failure !", "Try Again", RestaurantActivity.this);
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

    private List<String> getCuisineTypeList() {
        List<String> selectedCuisineTypes = new ArrayList<>();

        for (int i = 0; i < toggleButtonCuisineTypes.getChildCount(); i++) {
            View child = toggleButtonCuisineTypes.getChildAt(i);
            if (child instanceof MaterialButton) {
                MaterialButton cuisineType = (MaterialButton) child;
                if (cuisineType.isChecked()) {
                    selectedCuisineTypes.add((String) cuisineType.getTag());
                }
            }
        }

        return selectedCuisineTypes;
    }

    private boolean ValidateRestaurant() {
        if (!isRestaurantCoverSelected) {
            return new Functions().ShowErrorDialog("Select Restaurant Cover Image !", "Okay", this);
        }

        String restaurantName = txtRestaurantName.getText().toString().trim();
        if (restaurantName.equals("")) {
            return new Functions().ShowErrorDialog("Restaurant Name Required !", "Okay", this);
        } else if (restaurantName.length() < 5) {
            return new Functions().ShowErrorDialog("Restaurant Name (5 Chars Min) !", "Try Again", this);
        }

        String restaurantDescription = txtRestaurantDescription.getText().toString().trim();
        if (restaurantDescription.equals("")) {
            return new Functions().ShowErrorDialog("Restaurant Description Required !", "Okay", this);
        } else if (restaurantDescription.length() < 10) {
            return new Functions().ShowErrorDialog("Describe More About Restaurant !", "Try Again", this);
        }

        String restaurantLocation = tvRestaurantLocation.getText().toString().trim();
        if (restaurantLocation.equals("Set Restaurant Location")) {
            return new Functions().ShowErrorDialog("Restaurant Location Required !", "Okay", this);
        } else if (restaurantLocation.length() < 3) {
            return new Functions().ShowErrorDialog("Enter Valid Restaurant Location !", "Try Again", this);
        }

        String restaurantClass = tvRestaurantClass.getText().toString().trim();
        if (restaurantClass.equals("")) {
            return new Functions().ShowErrorDialog("Select Restaurant Class !", "Okay", this);
        }

        String telephoneNo = txtTelephoneNo.getText().toString().trim();
        if (telephoneNo.equals("")) {
            return new Functions().ShowErrorDialog("Enter Telephone No !", "Okay", this);
        } else if (!Regex.ValidatePhoneNo(telephoneNo)) {
            return new Functions().ShowErrorDialog("Enter Valid Phone Number !", "Try Again", this);
        }

        String openTime = txtOpenTime.getText().toString().trim();
        if (openTime.equals("")) {
            return new Functions().ShowErrorDialog("Enter Open Time !", "Okay", this);
        }

        String closeTime = txtCloseTime.getText().toString().trim();
        if (closeTime.equals("")) {
            return new Functions().ShowErrorDialog("Enter Close Time !", "Okay", this);
        }

        String mealBudget = txtBudget.getText().toString().trim();
        if (mealBudget.equals("")) {
            return new Functions().ShowErrorDialog("Enter Average Price !", "Okay", this);
        } else if (!Regex.ValidateDecimalsOnly(mealBudget)) {
            return new Functions().ShowErrorDialog("Average Price: Enter Decimals Only !", "Try Again", this);
        } else if (Double.parseDouble(mealBudget) <= 0) {
            return new Functions().ShowErrorDialog("Average Price: More Than 0 !", "Try Again", this);
        }

        String currency = tvCurrency.getText().toString().trim();
        if (currency.equals("")) {
            return new Functions().ShowErrorDialog("Select Currency Unit !", "Okay", this);
        }

        if (!chkFreeWIFI.isChecked() && !chkBeverages.isChecked() && !chkTakeAway.isChecked() && !chkParking.isChecked())
            return new Functions().ShowErrorDialog("Select 1 Or More Amenities !", "Okay", this);

        if(getCuisineTypeList().size() == 0) {
            return new Functions().ShowErrorDialog("Select 1 Or More Cuisine Types !", "Okay", this);
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSaveRestaurant: {
                SaveRestaurant();
                break;
            }
            case R.id.tvRestaurantClass: {
                SelectRestaurantClass();
                break;
            }
            case R.id.tvCurrency: {
                SelectCurrencyUnit();
                break;
            }
            case R.id.tvRestaurantLocation: {
                SelectRestaurantLocation();
                break;
            }
            case R.id.imgViewSelectCoverImage: {
                SelectRestaurantCoverImage();
                break;
            }
            case R.id.btnManageMeals: {
                ManageRestaurantMeals();
                break;
            }
        }
    }

    private void SelectRestaurantLocation() {
        Intent intent = new Intent(RestaurantActivity.this, ConfigureLocationActivity.class);
        if (locationCode != null && locationName != null) {
            intent.putExtra("LocationCode", locationCode);
            intent.putExtra("LocationName", locationName);
        }
        if (latitude != null && longitude != null) {
            intent.putExtra("Latitude", latitude);
            intent.putExtra("Longitude", longitude);
        }
        startActivityForResult(intent, Constants.LOCATION_CONFIGURE_REQUEST_CODE);
    }

    private void SelectRestaurantCoverImage() {
        Intent imagePickIntent = new Intent();

        imagePickIntent.setType("image/*");
        imagePickIntent.setAction(Intent.ACTION_GET_CONTENT);

        imagePickIntent.putExtra("crop", true);
        imagePickIntent.putExtra("scale", true);
        imagePickIntent.putExtra("aspectX", 1);
        imagePickIntent.putExtra("aspectY", 1);
        imagePickIntent.putExtra("outputX", 100);
        imagePickIntent.putExtra("outputY", 100);

        startActivityForResult(imagePickIntent, RESTAURANT_COVER_IMAGE_PICK_REQUEST);
    }

    private void SelectCurrencyUnit() {
        List<SelectorItemModel> itemList = new ArrayList<>();
        for (CurrencyModel currencyModel : UserHomeActivity.currencyList) {
            itemList.add(new SelectorItemModel(currencyModel.getCurrencyCode(), currencyModel.getSymbol()));
        }
        new Functions().ShowItemSelector("Select Currency Unit", itemList, restaurantActivity, tvCurrency, null);
    }

    private void SelectRestaurantClass() {
        List<SelectorItemModel> itemList = new ArrayList<>();
        itemList.add(new SelectorItemModel("ITM1", "2 Star"));
        itemList.add(new SelectorItemModel("ITM2", "3 Star"));
        itemList.add(new SelectorItemModel("ITM3", "4 Star"));
        itemList.add(new SelectorItemModel("ITM4", "5 Star"));
        new Functions().ShowItemSelector("Select Restaurant Class", itemList, restaurantActivity, tvRestaurantClass, null);
    }

    public void ShowSetupMealsConfirmationDialog() {
        if (!isSetupMealsConfirmationDialogVisible) {
            Button btnOkay, btnSetupLater;

            setupMealConfirmationDialog = new Dialog(this);
            setupMealConfirmationDialog.setContentView(R.layout.meal_setup_dialog);
            setupMealConfirmationDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            Window window = setupMealConfirmationDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            window.getAttributes().windowAnimations = R.style.DialogAnimation;

            btnOkay = setupMealConfirmationDialog.findViewById(R.id.btnOkay);
            btnSetupLater = setupMealConfirmationDialog.findViewById(R.id.btnSetupLater);

            setupMealConfirmationDialog.setCancelable(false);
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setupMealConfirmationDialog.show();
            isSetupMealsConfirmationDialogVisible = true;

            btnOkay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setupMealConfirmationDialog.dismiss();
                    ManageRestaurantMeals();
                }
            });

            btnSetupLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setupMealConfirmationDialog.dismiss();
                    finish();
                }
            });
        }
    }

    private void ManageRestaurantMeals() {
        Intent restaurantMealsIntent = new Intent(RestaurantActivity.this, RestaurantMealsActivity.class);
        //restaurantMealsIntent.putExtra("RestaurantID", myRestaurantModel.getID());
        restaurantMealsIntent.putExtra("Restaurant", new Gson().toJson(myRestaurantModel));
        //finish();
        startActivity(restaurantMealsIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESTAURANT_COVER_IMAGE_PICK_REQUEST && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                restaurantCoverImageURI = data.getData();

                try {
                    restaurantCoverBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), restaurantCoverImageURI);
                    imgViewRestaurantImage.setImageBitmap(restaurantCoverBitmap);
                    isRestaurantCoverSelected = isRestaurantCoverUpdated = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == Constants.LOCATION_CONFIGURE_REQUEST_CODE && resultCode == RESULT_OK) {
            locationCode = data.getStringExtra("LocationCode");
            locationName = data.getStringExtra("LocationName");
            tvRestaurantLocation.setText(locationName);
            latitude = data.getDoubleExtra("Latitude", 0);
            longitude = data.getDoubleExtra("Longitude", 0);
        }
    }
}