package com.rlabdevs.unifymobile.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.LayoutTransition;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.MainActivity;
import com.rlabdevs.unifymobile.activities.account.UserProfileActivity;
import com.rlabdevs.unifymobile.activities.user.manage.hotels.MyHotelsActivity;
import com.rlabdevs.unifymobile.activities.user.manage.restaurants.MyRestaurantsActivity;
import com.rlabdevs.unifymobile.activities.user.manage.taxiservices.MyTaxiServicesActivity;
import com.rlabdevs.unifymobile.common.Functions;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout relativeLayoutMenuActivity;
    private LinearLayout linearLytMyServices, linearLytMyServicesList, linearLytManageHotels, linearLytManageRestaurants, linearLytTaxiService, linearLytViewBookings, linearLytUpdateProfile, linearLytLogout;
    private TextView tvFirstLastName, tvMailAddress;

    private Dialog logoutConfirmationDialog;

    private boolean isLogoutConfirmationDialogVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        InitUI();
    }

    private void InitUI() {
        relativeLayoutMenuActivity = findViewById(R.id.relativeLayoutMenuActivity);
        linearLytMyServices = findViewById(R.id.linearLytMyServices);
        linearLytMyServicesList = findViewById(R.id.linearLytMyServicesList);
        linearLytManageHotels = findViewById(R.id.linearLytManageHotels);
        linearLytManageRestaurants = findViewById(R.id.linearLytManageRestaurants);
        linearLytTaxiService = findViewById(R.id.linearLytTaxiService);
        linearLytViewBookings = findViewById(R.id.linearLytViewBookings);
        linearLytUpdateProfile = findViewById(R.id.linearLytUpdateProfile);
        linearLytLogout = findViewById(R.id.linearLytLogout);
        tvFirstLastName = findViewById(R.id.tvFirstLastName);
        tvMailAddress = findViewById(R.id.tvMailAddress);

        relativeLayoutMenuActivity.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        tvFirstLastName.setText(MainActivity.sharedPref.getString("FLName", ""));
        tvMailAddress.setText(MainActivity.sharedPref.getString("Email", ""));

        linearLytMyServices.setOnClickListener(this);
        linearLytManageHotels.setOnClickListener(this);
        linearLytManageRestaurants.setOnClickListener(this);
        linearLytTaxiService.setOnClickListener(this);
        linearLytViewBookings.setOnClickListener(this);
        linearLytUpdateProfile.setOnClickListener(this);
        linearLytLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.linearLytMyServices:
            {
                linearLytMyServicesList.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.linearLytManageHotels:
            {
                new Functions().StartActivity(MenuActivity.this, MyHotelsActivity.class);
                linearLytMyServicesList.setVisibility(View.GONE);
                break;
            }
            case R.id.linearLytManageRestaurants:
            {
                new Functions().StartActivity(MenuActivity.this, MyRestaurantsActivity.class);
                linearLytMyServicesList.setVisibility(View.GONE);
                break;
            }
            case R.id.linearLytTaxiService:
            {
                new Functions().StartActivity(MenuActivity.this, MyTaxiServicesActivity.class);
                linearLytMyServicesList.setVisibility(View.GONE);
                break;
            }
            case R.id.linearLytViewBookings:
            {
                new Functions().StartActivity(MenuActivity.this, ViewBookingsActivity.class);
                linearLytMyServicesList.setVisibility(View.GONE);
                break;
            }
            case R.id.linearLytUpdateProfile:
            {
                new Functions().StartActivity(MenuActivity.this, UserProfileActivity.class);
                linearLytMyServicesList.setVisibility(View.GONE);
                break;
            }
            case R.id.linearLytLogout:
            {
                ShowLogoutConfirmationDialog();
                break;
            }
        }
    }

    public void ShowLogoutConfirmationDialog() {
        if (!isLogoutConfirmationDialogVisible) {

            Button btnLogout, btnCancel;

            logoutConfirmationDialog = new Dialog(this);
            logoutConfirmationDialog.setContentView(R.layout.logout_dialog);
            logoutConfirmationDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            Window window = logoutConfirmationDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            window.getAttributes().windowAnimations = R.style.DialogAnimation;

            btnLogout = logoutConfirmationDialog.findViewById(R.id.btnLogout);
            btnCancel = logoutConfirmationDialog.findViewById(R.id.btnCancel);

            logoutConfirmationDialog.setCancelable(false);
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            logoutConfirmationDialog.show();
            isLogoutConfirmationDialogVisible = true;

            //logoutConfirmationDialog.setOnDismissListener(this);

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logoutConfirmationDialog.dismiss();
                    new Functions().ShowProgressBar(MenuActivity.this, "Logging out...", "Please wait");

                    MainActivity.sharedPrefEditor.remove("UserName");
                    MainActivity.sharedPrefEditor.remove("FLName");
                    MainActivity.sharedPrefEditor.remove("Email");
                    MainActivity.sharedPrefEditor.remove("UserDetailsCode");
                    MainActivity.sharedPrefEditor.remove("LoginCode");
                    MainActivity.sharedPrefEditor.remove("IsUserLoggedIn");
                    MainActivity.sharedPrefEditor.commit();
                    isLogoutConfirmationDialogVisible = false;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Functions.HideProgressBar();
                            new Functions().FinishAffinityAndStartActivity(MenuActivity.this, MainActivity.class);
                        }
                    }, 1500);
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logoutConfirmationDialog.dismiss();
                    isLogoutConfirmationDialogVisible = false;
                }
            });
        }
    }
}