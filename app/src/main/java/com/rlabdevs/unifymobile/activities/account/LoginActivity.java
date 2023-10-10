package com.rlabdevs.unifymobile.activities.account;

import static com.rlabdevs.unifymobile.activities.MainActivity.firestoreDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.MainActivity;
import com.rlabdevs.unifymobile.activities.UserHomeActivity;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.enums.UserRole;
import com.rlabdevs.unifymobile.models.LoginModel;
import com.rlabdevs.unifymobile.models.UserDetailsModel;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginActivity loginActivity;

    private EditText txtUsername, txtPassword;
    private TextView tvRegister;
    private Button btnLogin;
    private ImageView imgViewIconShowPassword;

    private boolean isPasswordVisible;

    private CollectionReference loginReference, userDetailsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginReference = firestoreDB.collection("LoginDetails");
        userDetailsReference = MainActivity.firestoreDB.collection("UserDetails");
        loginActivity = this;
        InitUI();
    }

    private void InitUI() {
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        tvRegister = findViewById(R.id.tvRegister);
        btnLogin = findViewById(R.id.btnLogin);
        imgViewIconShowPassword = findViewById(R.id.imgViewIconShowPassword);

        imgViewIconShowPassword.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin: {
                LoginUser();
                break;
            }
            case R.id.imgViewIconShowPassword: {
                isPasswordVisible = !isPasswordVisible;
                new Functions().ShowHidePassword(isPasswordVisible, txtPassword);
                break;
            }
            case R.id.tvRegister: {
                new Functions().StartActivityAndFinishCurrent(this, UserProfileActivity.class);
                break;
            }
        }
    }

    private void LoginUser() {

        String username = txtUsername.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if (username.equals("")) {
            new Functions().ShowErrorDialog("Username must enter", "Okay", this);
        } else {
            if (password.equals("")) {
                new Functions().ShowErrorDialog("Password must enter", "Okay", this);
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        loginActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(LoginActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });

                        loginReference.whereEqualTo("userName", username).get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                            LoginModel loginModel = documentSnapshotList.get(0).toObject(LoginModel.class);

                                            if (loginModel != null) {
                                                if (loginModel.getPassword().equals(password)) {
                                                    if (loginModel.getUserRoleCode().equals(UserRole.User.getUserRole())) {

                                                        userDetailsReference.whereEqualTo("userDetailsCode", loginModel.getUserDetailsCode()).get()
                                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                                          @Override
                                                                                          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                                              if (!queryDocumentSnapshots.isEmpty()) {
                                                                                                  List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                                                                                                  UserDetailsModel userDetailModel = documentSnapshotList.get(0).toObject(UserDetailsModel.class);

                                                                                                  if (userDetailModel != null) {
                                                                                                      MainActivity.sharedPrefEditor.putString("UserName", username);
                                                                                                      MainActivity.sharedPrefEditor.putString("FLName", userDetailModel.getFirstName() + " " + userDetailModel.getLastName());
                                                                                                      MainActivity.sharedPrefEditor.putString("Email", userDetailModel.getEmail());
                                                                                                      MainActivity.sharedPrefEditor.putString("UserDetailsCode", loginModel.getUserDetailsCode());
                                                                                                      MainActivity.sharedPrefEditor.putString("LoginCode", loginModel.getLoginCode());
                                                                                                      MainActivity.sharedPrefEditor.putBoolean("IsUserLoggedIn", true);
                                                                                                      MainActivity.sharedPrefEditor.commit();
                                                                                                  }
                                                                                              }
                                                                                          }
                                                                                      });
                                                        loginActivity.runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                Functions.HideProgressBar();
                                                            }
                                                        });

                                                        new Functions().StartActivityAndFinishCurrent(LoginActivity.this, UserHomeActivity.class);
                                                    }
                                                } else {
                                                    loginActivity.runOnUiThread(new Runnable() {
                                                        public void run() {
                                                            Functions.HideProgressBar();
                                                            new Functions().ShowErrorDialog("Invalid password", "Try Again", LoginActivity.this);
                                                        }
                                                    });
                                                }
                                            }
                                        } else {
                                            loginActivity.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Functions.HideProgressBar();
                                                    new Functions().ShowErrorDialog("Invalid username", "Try Again", LoginActivity.this);
                                                }
                                            });
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loginActivity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Functions.HideProgressBar();
                                                new Functions().ShowErrorDialog("Error occurred", "Try Again", LoginActivity.this);
                                            }
                                        });
                                    }
                                });
                    }
                }).start();
            }
        }
    }
}