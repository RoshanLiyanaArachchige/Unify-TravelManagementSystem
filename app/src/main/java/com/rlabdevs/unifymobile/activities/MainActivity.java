package com.rlabdevs.unifymobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.common.Functions;

public class MainActivity extends AppCompatActivity {

    private final int SPLASH_SCREEN_TIMEOUT = 2000;

    public static FirebaseFirestore firestoreDB;
    public static FirebaseStorage firebaseStorage;
    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor sharedPrefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestoreDB = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        sharedPref = getSharedPreferences("UnifyMobilePref", MODE_PRIVATE);
        sharedPrefEditor = sharedPref.edit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Functions().StartActivityAndFinishCurrent(MainActivity.this, UserHomeActivity.class);
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }
}