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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.MainActivity;
import com.rlabdevs.unifymobile.activities.UserHomeActivity;
import com.rlabdevs.unifymobile.common.Functions;
import com.rlabdevs.unifymobile.common.enums.ApiResponse;
import com.rlabdevs.unifymobile.common.enums.UserRole;
import com.rlabdevs.unifymobile.models.LoginModel;
import com.rlabdevs.unifymobile.models.UserDetailsModel;
import com.rlabdevs.unifymobile.models.account.NewLoginDetailModel;
import com.rlabdevs.unifymobile.models.account.NewUserModel;
import com.rlabdevs.unifymobile.services.RetrofitClient;
import com.rlabdevs.unifymobile.services.account.IUserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginActivity loginActivity;

    private EditText txtUsername, txtPassword;
    private TextView tvRegister;
    private Button btnLogin;
    private ImageView imgViewIconShowPassword;

    private boolean isPasswordVisible;

    private IUserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userService = RetrofitClient.getClient().create(IUserService.class);
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

    private void LoginUser() {
        String username = txtUsername.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if(username.equals("")) {
            new Functions().ShowErrorDialog("Username is required.", "Okay", this);
            return;
        }

        if(password.equals("")) {
            new Functions().ShowErrorDialog("Password is required.", "Okay", this);
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                loginActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        new Functions().ShowProgressBar(LoginActivity.this, "Connecting to server...", "Please wait !");
                    }
                });

                NewLoginDetailModel loginRequestData = new NewLoginDetailModel();
                loginRequestData.setUsername(username);
                loginRequestData.setPassword(password);

                Call<NewUserModel> loginRequest = userService.login(loginRequestData);

                loginRequest.enqueue(new Callback<NewUserModel>() {
                    @Override
                    public void onResponse(Call<NewUserModel> call, Response<NewUserModel> response) {
                        NewUserModel apiResponse = response.body();
                        if(apiResponse.getApiResponseStatus().equals(ApiResponse.Success.getValue())) {
                            Functions.HideProgressBar();
                            MainActivity.sharedPrefEditor.putString("UserName", username);
                            MainActivity.sharedPrefEditor.putString("Password", password);
                            MainActivity.sharedPrefEditor.putString("FLName", apiResponse.getFirstName() + " " + apiResponse.getLastName());
                            MainActivity.sharedPrefEditor.putString("FirstName", apiResponse.getFirstName());
                            MainActivity.sharedPrefEditor.putString("LastName", apiResponse.getLastName());
                            MainActivity.sharedPrefEditor.putString("Address", apiResponse.getAddress());
                            MainActivity.sharedPrefEditor.putString("NIC", apiResponse.getNIC());
                            MainActivity.sharedPrefEditor.putString("MobileNo", apiResponse.getMobileNo());
                            MainActivity.sharedPrefEditor.putString("Email", apiResponse.getEmail());
                            MainActivity.sharedPrefEditor.putInt("UserDetailsID", apiResponse.getUserDetailId());
                            MainActivity.sharedPrefEditor.putString("UserDetailsCode", apiResponse.getRegistrationCode());
                            MainActivity.sharedPrefEditor.putBoolean("IsUserLoggedIn", true);
                            MainActivity.sharedPrefEditor.commit();
                            new Functions().StartActivityAndFinishCurrent(LoginActivity.this, UserHomeActivity.class);
                        } else {
                            Functions.HideProgressBar();
                            Toast.makeText(loginActivity, apiResponse.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<NewUserModel> call, Throwable t) {
                        Functions.HideProgressBar();
                        Toast.makeText(loginActivity, "An error occurred while login into your account.", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
            }
        }).start();
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
}