package com.rlabdevs.unifymobile.activities.account;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.MainActivity;
import com.rlabdevs.unifymobile.activities.UserHomeActivity;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.Regex;
import com.rlabdevs.unifymobile.common.enums.ApiResponse;
import com.rlabdevs.unifymobile.common.enums.IndexReference;
import com.rlabdevs.unifymobile.models.account.NewLoginDetailModel;
import com.rlabdevs.unifymobile.models.account.NewUserModel;
import com.rlabdevs.unifymobile.services.RetrofitClient;
import com.rlabdevs.unifymobile.services.interfaces.account.IUserService;
import com.rlabdevs.unifymobile.services.interfaces.other.IMasterService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private UserProfileActivity userProfileActivity;

    private LinearLayout linearLytLogoHaveAccount;
    private EditText txtUserDetailsCode, txtFirstName, txtLastName, txtAddress, txtNIC, txtMobilePhone, txtEmail, txtUsername, txtPassword;
    private Button btnUpdateRegister;
    private TextView tvUserProfileTitle;
    private TextView tvRegisterContinue;

    private Integer userDetailsID;
    private String userDetailsCode;
    private boolean isPasswordVisible, isUserLoggedIn;

    private IUserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        userService = RetrofitClient.getClient().create(IUserService.class);
        userProfileActivity = this;
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
        TextView tvLogin = findViewById(R.id.tvLogin);
        ImageView imgViewIconShowPassword = findViewById(R.id.imgViewIconShowPassword);

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

        userDetailsID = MainActivity.sharedPref.getInt("UserDetailsID", 0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                userProfileActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(UserProfileActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                Call<NewUserModel> getProfileDetailsRequest = userService.getProfileDetails(MainActivity.sharedPref.getString("AuthToken", ""));
                getProfileDetailsRequest.enqueue(new Callback<NewUserModel>() {
                    @Override
                    public void onResponse(Call<NewUserModel> call, Response<NewUserModel> response) {
                        NewUserModel apiResponse = response.body();

                        txtUserDetailsCode.setText(apiResponse.getRegistrationCode() + " (Registration Code)");
                        txtFirstName.setText(apiResponse.getFirstName());
                        txtLastName.setText(apiResponse.getLastName());
                        txtAddress.setText(apiResponse.getAddress());
                        txtNIC.setText(apiResponse.getNIC());
                        txtMobilePhone.setText(apiResponse.getMobileNo());
                        txtEmail.setText(apiResponse.getEmail());
                        txtUsername.setText(apiResponse.getLoginDetail().getUsername());
                        txtPassword.setText(MainActivity.sharedPref.getString("Password", ""));

                        Functions.HideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<NewUserModel> call, Throwable t) {
                        Functions.HideProgressBar();
                        Toast.makeText(userProfileActivity, "An error occurred while retrieving profile details.", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
            }
        }).start();
    }

    private void InitRegistration() {
        new Functions().ShowProgressBar(UserProfileActivity.this, "Connecting to server...", "Please wait !");

        IMasterService masterService = RetrofitClient.getClient().create(IMasterService.class);
        Call<String> indexReferenceRequest = masterService.getIndexReferenceCode(IndexReference.Users.getValue());
        indexReferenceRequest.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Functions.HideProgressBar();
                userDetailsCode = response.body();
                txtUserDetailsCode.setText(userDetailsCode + " (Registration Code)");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Functions.HideProgressBar();
                Toast.makeText(userProfileActivity, "An error occurred while getting registration code.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
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
        if (ValidateUserDetails()) {
            String username, password, firstName, lastName, address, nic, mobileNo, email;

            NewLoginDetailModel newLoginDetailModel = new NewLoginDetailModel();
            newLoginDetailModel.setUsername(username = txtUsername.getText().toString().trim());
            newLoginDetailModel.setPassword(password = txtPassword.getText().toString().trim());

            NewUserModel newUserModel = new NewUserModel();
            newUserModel.setUserDetailId(userDetailsID);
            newUserModel.setRegistrationCode(userDetailsCode);
            newUserModel.setFirstName(firstName = txtFirstName.getText().toString().trim());
            newUserModel.setLastName(lastName = txtLastName.getText().toString().trim());
            newUserModel.setAddress(address = txtAddress.getText().toString().trim());
            newUserModel.setNIC(nic = txtNIC.getText().toString().trim());
            newUserModel.setMobileNo(mobileNo = txtMobilePhone.getText().toString().trim());
            newUserModel.setEmail(email = txtEmail.getText().toString().trim());
            newUserModel.setLoginDetail(newLoginDetailModel);

            if(isUserLoggedIn) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        userProfileActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(UserProfileActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });

                        Call<NewUserModel> updateProfileRequest = userService.updateProfile(newUserModel);
                        updateProfileRequest.enqueue(new Callback<NewUserModel>() {
                            @Override
                            public void onResponse(Call<NewUserModel> call, Response<NewUserModel> response) {
                                NewUserModel apiResponse = response.body();
                                Functions.HideProgressBar();
                                setUserProfileSharedPreferences(username, password, firstName, lastName, address, nic, mobileNo, email);
                                new Functions().StartActivityAndFinishCurrent(UserProfileActivity.this, UserHomeActivity.class);
                                Toast.makeText(UserProfileActivity.this, "User profile updated successfully.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<NewUserModel> call, Throwable t) {
                                Functions.HideProgressBar();
                                Toast.makeText(userProfileActivity, "An error occurred while updating the profile.", Toast.LENGTH_SHORT).show();
                                t.printStackTrace();
                            }
                        });
                    }
                }).start();
            }
            else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        userProfileActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                new Functions().ShowProgressBar(UserProfileActivity.this, "Connecting to server...", "Please wait !");
                            }
                        });

                        Call<NewUserModel> registerRequest = userService.register(newUserModel);
                        registerRequest.enqueue(new Callback<NewUserModel>() {
                            @Override
                            public void onResponse(Call<NewUserModel> call, Response<NewUserModel> response) {
                                NewUserModel apiResponse = response.body();
                                Functions.HideProgressBar();
                                userDetailsID = apiResponse.getUserDetailId();
                                setUserProfileSharedPreferences(username, password, firstName, lastName, address, nic, mobileNo, email);
                                Toast.makeText(UserProfileActivity.this, "Logged in as " + username, Toast.LENGTH_SHORT).show();
                                new Functions().StartActivityAndFinishCurrent(UserProfileActivity.this, UserHomeActivity.class);
                            }

                            @Override
                            public void onFailure(Call<NewUserModel> call, Throwable t) {
                                Functions.HideProgressBar();
                                Toast.makeText(userProfileActivity, "An error occurred while registering the user.", Toast.LENGTH_SHORT).show();
                                t.printStackTrace();
                            }
                        });
                    }
                }).start();
            }
        }
    }

    private boolean ValidateUserDetails() {
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

    private void setUserProfileSharedPreferences(String username, String password, String firstName, String lastName, String address, String nic, String mobileNo, String email) {
        MainActivity.sharedPrefEditor.putString("UserName", username);
        MainActivity.sharedPrefEditor.putString("Password", password);
        MainActivity.sharedPrefEditor.putString("FLName", firstName + " " + lastName);
        MainActivity.sharedPrefEditor.putString("FirstName", firstName);
        MainActivity.sharedPrefEditor.putString("LastName", lastName);
        MainActivity.sharedPrefEditor.putString("Address", address);
        MainActivity.sharedPrefEditor.putString("NIC", nic);
        MainActivity.sharedPrefEditor.putString("MobileNo", mobileNo);
        MainActivity.sharedPrefEditor.putString("Email", email);
        MainActivity.sharedPrefEditor.putInt("UserDetailsID", userDetailsID);
        MainActivity.sharedPrefEditor.putString("UserDetailsCode", userDetailsCode);
        MainActivity.sharedPrefEditor.putBoolean("IsUserLoggedIn", true);
        MainActivity.sharedPrefEditor.commit();
    }
}