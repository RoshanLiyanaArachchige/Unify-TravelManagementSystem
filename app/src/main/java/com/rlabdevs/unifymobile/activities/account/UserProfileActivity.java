package com.rlabdevs.unifymobile.activities.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.MainActivity;
import com.rlabdevs.unifymobile.activities.UserHomeActivity;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.Regex;
import com.rlabdevs.unifymobile.common.enums.StatusCode;
import com.rlabdevs.unifymobile.common.enums.UserRole;
import com.rlabdevs.unifymobile.models.IndexModel;
import com.rlabdevs.unifymobile.models.LoginModel;
import com.rlabdevs.unifymobile.models.UserDetailsModel;

import java.util.List;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private UserProfileActivity userProfileActivity;

    private LinearLayout linearLytLogoHaveAccount;
    private EditText txtUserDetailsCode, txtFirstName, txtLastName, txtAddress, txtNIC, txtMobilePhone, txtEmail, txtUsername, txtPassword;
    private Button btnUpdateRegister;
    private TextView tvLogin, tvUserProfileTitle, tvRegisterContinue;
    private ImageView imgViewIconShowPassword;

    private CollectionReference indexReference, userDetailsReference, loginDetailsReference;

    private UserDetailsModel userDetailModel;
    private LoginModel loginModel;
    private IndexModel userDetailsIndex, loginDetailsIndex;

    private String userDetailsCode, loginCode;
    private boolean isPasswordVisible, isUserLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        userProfileActivity = this;
        indexReference = MainActivity.firestoreDB.collection("Index");
        userDetailsReference = MainActivity.firestoreDB.collection("UserDetails");
        loginDetailsReference = MainActivity.firestoreDB.collection("LoginDetails");
        InitUI();
    }

    private void InitUI() {

        linearLytLogoHaveAccount = findViewById(R.id.linearLytLogoHaveAccount);

        tvUserProfileTitle = findViewById(R.id.tvUserProfileTitle);
        tvRegisterContinue = findViewById(R.id.tvRegisterContinue);

        txtUserDetailsCode = findViewById(R.id.txtUserDetailsCode);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtAddress = findViewById(R.id.txtAddress);
        txtNIC = findViewById(R.id.txtNIC);
        txtMobilePhone = findViewById(R.id.txtMobilePhone);
        txtEmail = findViewById(R.id.txtEmail);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);

        btnUpdateRegister = findViewById(R.id.btnUpdateRegister);
        tvLogin = findViewById(R.id.tvLogin);
        imgViewIconShowPassword = findViewById(R.id.imgViewIconShowPassword);

        btnUpdateRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        imgViewIconShowPassword.setOnClickListener(this);

        isUserLoggedIn = MainActivity.sharedPref.getBoolean("IsUserLoggedIn", false);

        if (isUserLoggedIn) InitUserProfile();
        else InitRegistration();
    }

    private void InitUserProfile() {

        linearLytLogoHaveAccount.setVisibility(View.GONE);
        tvRegisterContinue.setVisibility(View.GONE);
        tvUserProfileTitle.setText("My Profile");
        btnUpdateRegister.setText("Update");

        userDetailsCode = MainActivity.sharedPref.getString("UserDetailsCode", "").toString();
        loginCode = MainActivity.sharedPref.getString("LoginCode", "").toString();

        new Thread(new Runnable() {
            @Override
            public void run() {

                userProfileActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(UserProfileActivity.this, "Connecting to server...", "Please wait !");
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
                                        txtUserDetailsCode.setText(userDetailModel.getUserDetailsCode());
                                        txtFirstName.setText(userDetailModel.getFirstName());
                                        txtLastName.setText(userDetailModel.getLastName());
                                        txtAddress.setText(userDetailModel.getAddress());
                                        txtNIC.setText(userDetailModel.getNIC());
                                        txtMobilePhone.setText(userDetailModel.getMobileNo());
                                        txtEmail.setText(userDetailModel.getEmail());
                                    }

                                    loginDetailsReference.whereEqualTo("loginCode", loginCode).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    if (!queryDocumentSnapshots.isEmpty()) {
                                                        List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                                        loginModel = documentSnapshotList.get(0).toObject(LoginModel.class);
                                                        loginModel.setID(documentSnapshotList.get(0).getId());

                                                        if (loginModel != null) {
                                                            txtUsername.setText(loginModel.getUserName());
                                                            txtPassword.setText(loginModel.getPassword());
                                                        }

                                                        userProfileActivity.runOnUiThread(new Runnable() {
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
                                                    userProfileActivity.runOnUiThread(new Runnable() {
                                                        public void run() {
                                                            Functions.HideProgressBar();
                                                            new Functions().ShowErrorDialog("Profile Load Error !", "Try Again", UserProfileActivity.this);
                                                        }
                                                    });
                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                userProfileActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                        new Functions().ShowErrorDialog("Profile Load Error !", "Try Again", UserProfileActivity.this);
                                    }
                                });
                            }
                        });
            }
        }).start();

    }

    private void InitRegistration() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                userProfileActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(UserProfileActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                indexReference.whereEqualTo("indexName", "LoginDetails").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                    loginDetailsIndex = documentSnapshotList.get(0).toObject(IndexModel.class);
                                    loginDetailsIndex.setID(documentSnapshotList.get(0).getId());
                                    if (loginDetailsIndex != null) {
                                        loginCode = loginDetailsIndex.getPrefix() + (loginDetailsIndex.getCurrentCount() + 1);
                                    }

                                    indexReference.whereEqualTo("indexName", "UserDetails").get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    if (!queryDocumentSnapshots.isEmpty()) {
                                                        List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                                        userDetailsIndex = documentSnapshotList.get(0).toObject(IndexModel.class);
                                                        userDetailsIndex.setID(documentSnapshotList.get(0).getId());
                                                        if (userDetailsIndex != null) {
                                                            userDetailsCode = userDetailsIndex.getPrefix() + (userDetailsIndex.getCurrentCount() + 1);
                                                            txtUserDetailsCode.setText(userDetailsCode + " (Registration Code)");
                                                        }

                                                        userProfileActivity.runOnUiThread(new Runnable() {
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
                                                    userProfileActivity.runOnUiThread(new Runnable() {
                                                        public void run() {
                                                            Functions.HideProgressBar();
                                                        }
                                                    });
                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                userProfileActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Functions.HideProgressBar();
                                    }
                                });
                            }
                        });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateRegister: {
                SaveUserProfile();
                break;
            }
            case R.id.tvLogin: {
                new Functions().StartActivityAndFinishCurrent(this, LoginActivity.class);
                break;
            }
            case R.id.imgViewIconShowPassword: {
                isPasswordVisible = !isPasswordVisible;
                new Functions().ShowHidePassword(isPasswordVisible, txtPassword);
                break;
            }

        }
    }

    private void SaveUserProfile() {
        if (ValidateRegistration()) {
            if(isUserLoggedIn)
            {
                loginModel.setUserName(txtUsername.getText().toString().trim());
                loginModel.setPassword(txtPassword.getText().toString().trim());
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        userProfileActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(UserProfileActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });

                        loginDetailsReference.document(loginModel.getID()).set(loginModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        userDetailModel.setFirstName(txtFirstName.getText().toString().trim());
                                        userDetailModel.setLastName(txtLastName.getText().toString().trim());
                                        userDetailModel.setAddress(txtAddress.getText().toString().trim());
                                        userDetailModel.setNIC(txtNIC.getText().toString().trim());
                                        userDetailModel.setMobileNo(txtMobilePhone.getText().toString().trim());
                                        userDetailModel.setEmail(txtEmail.getText().toString().trim());

                                        userDetailsReference.document(userDetailModel.getID()).set(userDetailModel)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        userProfileActivity.runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                Functions.HideProgressBar();
                                                                new Functions().StartActivityAndFinishCurrent(UserProfileActivity.this, UserHomeActivity.class);
                                                                Toast.makeText(UserProfileActivity.this, "Profile Updated !", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        userProfileActivity.runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                Functions.HideProgressBar();
                                                                new Functions().ShowErrorDialog("Profile Update Failure !", "Try Again", UserProfileActivity.this);
                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        userProfileActivity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Functions.HideProgressBar();
                                                new Functions().ShowErrorDialog("Profile Update Failure !", "Try Again", UserProfileActivity.this);
                                            }
                                        });
                                    }
                                });
                    }
                }).start();
            }
            else
            {
                String username = txtUsername.getText().toString().trim();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        userProfileActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(UserProfileActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });

                        loginDetailsReference.whereEqualTo("userName", username).whereEqualTo("statusCode", StatusCode.Active.getStatusCode()).get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                            loginModel = documentSnapshotList.get(0).toObject(LoginModel.class);

                                            if (loginModel != null) {
                                                userProfileActivity.runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        Functions.HideProgressBar();
                                                        new Functions().ShowErrorDialog("Username Already Exists !", "Try Again", UserProfileActivity.this);
                                                    }
                                                });
                                            }
                                        } else {
                                            String password = txtPassword.getText().toString().trim();

                                            LoginModel loginDetail = new LoginModel(loginCode, userDetailsCode, username, password,
                                                    UserRole.User.getUserRole(), StatusCode.Active.getStatusCode());

                                            loginDetailsReference.add(loginDetail)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {

                                                            //UserDetailsCode;
                                                            String firstName = txtFirstName.getText().toString().trim();
                                                            String lastName = txtLastName.getText().toString().trim();
                                                            String address = txtAddress.getText().toString().trim();
                                                            String nic = txtNIC.getText().toString().trim();
                                                            String mobileNo = txtMobilePhone.getText().toString().trim();
                                                            String email = txtEmail.getText().toString().trim();

                                                            userDetailModel = new UserDetailsModel(userDetailsCode, firstName, lastName, address, nic, mobileNo, email, StatusCode.Active.getStatusCode());
                                                            userDetailsReference.add(userDetailModel)
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentReference documentReference) {
                                                                            MainActivity.sharedPrefEditor.putString("UserName", username);
                                                                            MainActivity.sharedPrefEditor.putString("FLName", firstName + " " + lastName);
                                                                            MainActivity.sharedPrefEditor.putString("Email", email);
                                                                            MainActivity.sharedPrefEditor.putString("UserDetailsCode", userDetailsCode);
                                                                            MainActivity.sharedPrefEditor.putString("LoginCode", loginCode);
                                                                            MainActivity.sharedPrefEditor.putBoolean("IsUserLoggedIn", true);
                                                                            MainActivity.sharedPrefEditor.commit();

                                                                            loginDetailsIndex.setCurrentCount(loginDetailsIndex.getCurrentCount()+1);
                                                                            indexReference.document(loginDetailsIndex.getID()).set(loginDetailsIndex)
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void unused) {
                                                                                            userDetailsIndex.setCurrentCount(userDetailsIndex.getCurrentCount()+1);
                                                                                            indexReference.document(userDetailsIndex.getID()).set(userDetailsIndex)
                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void unused) {
                                                                                                            userProfileActivity.runOnUiThread(new Runnable() {
                                                                                                                public void run() {
                                                                                                                    Functions.HideProgressBar();
                                                                                                                    Toast.makeText(UserProfileActivity.this, "Logged in as " + username, Toast.LENGTH_SHORT).show();
                                                                                                                }
                                                                                                            });

                                                                                                            new Functions().StartActivityAndFinishCurrent(UserProfileActivity.this, UserHomeActivity.class);
                                                                                                        }
                                                                                                    })
                                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                                        @Override
                                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                                            userProfileActivity.runOnUiThread(new Runnable() {
                                                                                                                public void run() {
                                                                                                                    Functions.HideProgressBar();
                                                                                                                    new Functions().ShowErrorDialog("Error Occurred !", "Try Again", UserProfileActivity.this);
                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            userProfileActivity.runOnUiThread(new Runnable() {
                                                                                                public void run() {
                                                                                                    Functions.HideProgressBar();
                                                                                                    new Functions().ShowErrorDialog("Error Occurred !", "Try Again", UserProfileActivity.this);
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    });
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            userProfileActivity.runOnUiThread(new Runnable() {
                                                                                public void run() {
                                                                                    Functions.HideProgressBar();
                                                                                    new Functions().ShowErrorDialog("Registration Error Occurred !", "Try Again", UserProfileActivity.this);
                                                                                }
                                                                            });
                                                                        }
                                                                    });
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            userProfileActivity.runOnUiThread(new Runnable() {
                                                                public void run() {
                                                                    Functions.HideProgressBar();
                                                                    new Functions().ShowErrorDialog("Registration Error Occurred !", "Try Again", UserProfileActivity.this);
                                                                }
                                                            });
                                                        }
                                                    });
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        userProfileActivity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Functions.HideProgressBar();
                                                new Functions().ShowErrorDialog("Error Occurred !", "Try Again", UserProfileActivity.this);
                                            }
                                        });
                                    }
                                });
                    }
                }).start();
            }
        }
    }

    private boolean ValidateRegistration() {

        String firstName = txtFirstName.getText().toString().trim();
        if (firstName.equals("")) {
            new Functions().ShowErrorDialog("First Name Required !", "Okay", this);
            return false;
        } else if (firstName.length() < 3) {
            new Functions().ShowErrorDialog("First Name (3 Chars Min) !", "Try Again", this);
            return false;
        } else if (!Regex.ValidateLettersOnly(firstName)) {
            new Functions().ShowErrorDialog("First Name: Enter Letters Only !", "Try Again", this);
            return false;
        }

        String lastName = txtLastName.getText().toString().trim();
        if (lastName.equals("")) {
            new Functions().ShowErrorDialog("Last Name Required !", "Okay", this);
            return false;
        } else if (lastName.length() < 3) {
            new Functions().ShowErrorDialog("Last Name (3 Chars Min) !", "Try Again", this);
            return false;
        } else if (!Regex.ValidateLettersAndSpacesOnly(lastName)) {
            new Functions().ShowErrorDialog("Last Name: Enter Letters And Spaces Only !", "Try Again", this);
            return false;
        }

        String address = txtAddress.getText().toString().trim();
        if (address.equals("")) {
            new Functions().ShowErrorDialog("Address Required !", "Okay", this);
            return false;
        }

        String nic = txtNIC.getText().toString().trim();
        if (nic.equals("")) {
            new Functions().ShowErrorDialog("NIC Required !", "Okay", this);
            return false;
        } else if (!Regex.ValidateNIC(nic)) {
            new Functions().ShowErrorDialog("Enter Valid NIC !", "Try Again", this);
            return false;
        }

        String mobilePhone = txtMobilePhone.getText().toString().trim();
        if (mobilePhone.equals("")) {
            new Functions().ShowErrorDialog("Mobile No Required !", "Okay", this);
            return false;
        }

        String email = txtEmail.getText().toString().trim();
        if (email.equals("")) {
            new Functions().ShowErrorDialog("Email Address Required !", "Okay", this);
            return false;
        } else if (!Regex.ValidateEmail(email)) {
            new Functions().ShowErrorDialog("Enter Valid Email Address !", "Try Again", this);
            return false;
        }

        String userName = txtUsername.getText().toString().trim();
        if (userName.equals("")) {
            new Functions().ShowErrorDialog("Username Required !", "Okay", this);
            return false;
        } else if (!Regex.ValidateLettersAndNumbersOnly(userName)) {
            new Functions().ShowErrorDialog("Username: Enter Letters And Numbers Only!", "Try Again", this);
            return false;
        }

        String password = txtPassword.getText().toString().trim();
        if (password.equals("")) {
            new Functions().ShowErrorDialog("Password Required !", "Okay", this);
            return false;
        } else if (password.length() < 6) {
            new Functions().ShowErrorDialog("Password (6 Chars Min) !", "Try Again", this);
            return false;
        }

        return true;
    }
}