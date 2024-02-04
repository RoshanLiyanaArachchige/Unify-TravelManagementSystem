package com.rlabdevs.unifymobile.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.activities.UserHomeActivity;
import com.rlabdevs.unifymobile.adapters.ItemSelectorAdapter;
import com.rlabdevs.unifymobile.common.enums.CurrencyCode;
import com.rlabdevs.unifymobile.common.enums.StatusCode;
import com.rlabdevs.unifymobile.dialog.DateSelectionDialog;
import com.rlabdevs.unifymobile.dialog.DateTimeSelectionDialog;
import com.rlabdevs.unifymobile.models.IndexModel;
import com.rlabdevs.unifymobile.models.SelectorItemModel;
import com.rlabdevs.unifymobile.models.master.NewCuisineTypeModel;
import com.rlabdevs.unifymobile.models.master.NewCurrencyModel;
import com.rlabdevs.unifymobile.models.master.NewLocationModel;
import com.rlabdevs.unifymobile.services.RetrofitClient;
import com.rlabdevs.unifymobile.services.interfaces.other.IMasterService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Functions implements DialogInterface.OnDismissListener {

    private static Dialog errorDialog, itemSelectorDialog;
    public static Dialog progressDialog;
    public static DateTimeSelectionDialog dateTimeSelectionDialog;
    public static DateSelectionDialog dateSelectionDialog;

    private List<SelectorItemModel> selectorItemsList;
    public ItemSelectorAdapter itemSelectorAdapter;
    private LinearLayoutManager linearLayoutManager;

    private static boolean isProgressDialogVisible, isErrorDialogVisible, isConfirmationDialogVisible, isItemSelectorVisible, isUserNameExists;


    public void StartActivity(Activity activity, Class nextClass) {
        activity.startActivity(new Intent(activity, nextClass));
    }

    public void StartActivityAndFinishCurrent(Activity activity, Class nextClass) {
        Intent intent = new Intent(activity, nextClass);
        activity.startActivity(intent);
        activity.finish();
    }

    public void FinishAffinityAndStartActivity(Activity activity, Class nextClass) {
        Intent intent = new Intent(activity, nextClass);
        activity.finishAffinity();
        activity.startActivity(intent);
    }

    public static String GetStatusCodeFromName(String StatusName) {
        for(StatusCode status : StatusCode.values())
        {
            if(StatusName.equals(status.toString()))
                return status.getStatusCode();
        }
        return "";
    }

    public static String GetStatusNameFromCode(String statusCode) {
        for(StatusCode status : StatusCode.values())
        {
            if(statusCode.equals(status.getStatusCode()))
                return status.name();
        }
        return "";
    }

    public static String GetCurrencyCodeFromName(String CurrencyName) {
        for(CurrencyCode currency : CurrencyCode.values())
        {
            if(CurrencyName.equals(currency.toString()))
                return currency.getCurrencyCode();
        }
        return "";
    }

    public static String GetCurrencyNameFromCode(String currencyCode) {
        for(CurrencyCode currency : CurrencyCode.values())
        {
            if(currencyCode.equals(currency.getCurrencyCode()))
                return currency.name();
        }
        return "";
    }

    public static String RoomCapacityText(int adults, int children) {
        if(adults > 1 && children > 1) { return adults + " adults & " + children + " children"; }
        else if(adults == 1 && children > 1) { return adults + " adult & " + children + " children"; }
        else if(adults > 1 && children == 1) { return adults + " adults & " + children + " child"; }
        else if(adults == 1 && children == 1) { return adults + " adult & " + children + " child"; }
        else if(adults == 1 && children == 0) { return adults + " adult"; }
        else if(adults > 1 && children == 0) { return adults + " adults"; }
        return "";
    }

    public static int convertDimensionToPixels(Activity activity, float dimens) {
        float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dimens * scale + 0.5f);
    }

    public boolean ShowErrorDialog(String alertMessage, String buttonAction, Activity activity) {
        if (!isErrorDialogVisible) {
            ImageView imgViewDialogType;
            TextView tvDialogMessage;
            Button btnErrorAlertAction;

            errorDialog = new Dialog(activity);
            errorDialog.setContentView(R.layout.error_alert_dialog);
            errorDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            Window window = errorDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            window.getAttributes().windowAnimations = R.style.DialogAnimation;

            imgViewDialogType = errorDialog.findViewById(R.id.imgViewDialogType);
            tvDialogMessage = errorDialog.findViewById(R.id.tvDialogMessage);
            btnErrorAlertAction = errorDialog.findViewById(R.id.btnAction);

            imgViewDialogType.setImageResource(R.drawable.alert_error);
            tvDialogMessage.setText(alertMessage);
            btnErrorAlertAction.setText(buttonAction);

            errorDialog.setCancelable(false);
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            errorDialog.show();
            isErrorDialogVisible = true;

            errorDialog.setOnDismissListener(this);

            btnErrorAlertAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    errorDialog.dismiss();
                    isErrorDialogVisible = false;
                }
            });
        }

        return false;
    }

    public void ShowProgressBar(Activity activity, String progressHeader, String progressBody) {
        if (!isProgressDialogVisible) {
            TextView tvProgressHeader, tvProgressBody;

            progressDialog = new Dialog(activity);
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            Window window = progressDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            //window.Attributes.WindowAnimations = Resource.Style.AppTheme_DialogAnimation;

            tvProgressHeader = progressDialog.findViewById(R.id.tvProgressHeader);
            tvProgressBody = progressDialog.findViewById(R.id.tvProgressBody);

            tvProgressHeader.setText(progressHeader);
            tvProgressBody.setText(progressBody);
            isProgressDialogVisible = true;

            progressDialog.setOnDismissListener(this);

            progressDialog.setCancelable(false);
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            progressDialog.show();
        }
    }

    public static void UpdateProgress(String progressTitle) {
        TextView tvProgressHeader = progressDialog.findViewById(R.id.tvProgressHeader);
        tvProgressHeader.setText(progressTitle);
    }

    public static void HideProgressBar() {
        if(isProgressDialogVisible)
            progressDialog.dismiss();
    }

    public void ShowItemSelector(String header, List<SelectorItemModel> itemList, Activity activity, TextView tvSet, EditText txtSet) {
        if (!isItemSelectorVisible)
        {
            TextView tvItemSelectorHeading;
            RecyclerView recyclerViewItemsList;

            itemSelectorDialog = new Dialog(activity);
            itemSelectorDialog.setContentView(R.layout.dialog_item_selector);
            itemSelectorDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Window window = itemSelectorDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            window.getAttributes().windowAnimations = R.style.DialogAnimation;

            tvItemSelectorHeading = itemSelectorDialog.findViewById(R.id.tvItemSelectorHeading);

            tvItemSelectorHeading.setText(header);
            InitRecyclerViewItemsList(itemSelectorDialog, activity, tvSet, txtSet);
            LoadSelectorItems(itemList);

            itemSelectorDialog.setOnDismissListener(this);
            itemSelectorDialog.setCancelable(true);
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemSelectorDialog.show();
            isItemSelectorVisible = true;
        }
    }

    public static void HideItemSelector()
    {
        itemSelectorDialog.dismiss();
    }

    private void LoadSelectorItems(List<SelectorItemModel> itemList) {
        for(SelectorItemModel selectorItem : itemList)
        {
            selectorItemsList.add(selectorItem);
            itemSelectorAdapter.notifyDataSetChanged();
        }
    }

    public static void showDateTimeSelectionDialog(Activity activity, EditText dateTimeInputElement, Date selectedDateTime, boolean isCancelable, String selectDateTimeTitle, View.OnClickListener onDateTimeSelectClickListener) {
        if (dateTimeSelectionDialog == null || !dateTimeSelectionDialog.isShowing()) {
            dateTimeSelectionDialog = new DateTimeSelectionDialog(activity, isCancelable, dateTimeInputElement);
            dateTimeSelectionDialog.setSelectDateTimeTitle(selectDateTimeTitle);
            dateTimeSelectionDialog.setDateTimeSelectOnClickListener(onDateTimeSelectClickListener);

            if (selectedDateTime != null) {
                dateTimeSelectionDialog.setSelectedDateTime(selectedDateTime);
            }

            dateTimeSelectionDialog.switchToDatePickerView();
            dateTimeSelectionDialog.show();
        }
    }

    public static void showDateSelectionDialog(Activity activity, TextView dateInputElement, Date selectedDate, boolean isCancelable, String selectDateTitle, View.OnClickListener onDateSelectClickListener) {
        if (dateSelectionDialog == null || !dateSelectionDialog.isShowing()) {
            dateSelectionDialog = new DateSelectionDialog(activity, isCancelable, dateInputElement);
            dateSelectionDialog.setSelectDateTitle(selectDateTitle);
            dateSelectionDialog.setDateSelectOnClickListener(onDateSelectClickListener);

            if (selectedDate != null) {
                dateSelectionDialog.setSelectedDate(selectedDate);
            }

            dateSelectionDialog.show();
        }
    }

    private void InitRecyclerViewItemsList(Dialog itemSelectorDialog, Activity activity, TextView tvSet, EditText txtSet) {
        RecyclerView recyclerViewItemsList = itemSelectorDialog.findViewById(R.id.recyclerViewItemsList);
        selectorItemsList = new ArrayList<>();
        itemSelectorAdapter = new ItemSelectorAdapter(activity, selectorItemsList, tvSet, txtSet);
        linearLayoutManager = new LinearLayoutManager(activity);
        recyclerViewItemsList.setLayoutManager(linearLayoutManager);
        recyclerViewItemsList.setAdapter(itemSelectorAdapter);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        isErrorDialogVisible = false;
        isProgressDialogVisible = false;
        isItemSelectorVisible = false;
    }

    /*public void ShowErrorDialog(String alertMessage, String buttonAction, Activity activity) {
        isErrorDialogVisible = false;
        if (!isErrorDialogVisible) {
            ImageView imgViewDialogType;
            TextView tvDialogMessage;
            Button btnErrorAlertAction;

            errorDialog = new BottomSheetDialog(activity);
            errorDialog.setContentView(R.layout.error_alert_dialog2);
            errorDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            Window window = errorDialog.getWindow();
            //window.setGravity(Gravity.CENTER);
            window.getAttributes().windowAnimations = R.style.DialogAnimation;

            imgViewDialogType = errorDialog.findViewById(R.id.imgViewDialogType);
            tvDialogMessage = errorDialog.findViewById(R.id.tvDialogMessage);
            btnErrorAlertAction = errorDialog.findViewById(R.id.btnAction);

            imgViewDialogType.setImageResource(R.drawable.alert_error);
            tvDialogMessage.setText(alertMessage);
            btnErrorAlertAction.setText(buttonAction);

            errorDialog.setCancelable(true);
            //window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            errorDialog.show();
            isErrorDialogVisible = true;

            btnErrorAlertAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    errorDialog.dismiss();
                    isErrorDialogVisible = false;
                }
            });
        }
    }*/

    public void ShowHidePassword(boolean isPasswordVisible, EditText txtPassword) {
        if (isPasswordVisible)
            txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        else
            txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
    }

    public static void getCurrencyTypeList(Activity activity) {
        new Thread(() -> {
            IMasterService masterService = RetrofitClient.getClient().create(IMasterService.class);
            masterService.getCurrencies().enqueue(new Callback<List<NewCurrencyModel>>() {
                @Override
                public void onResponse(Call<List<NewCurrencyModel>> call, Response<List<NewCurrencyModel>> response) {
                    if(response.isSuccessful()) {
                        UserHomeActivity.currencyList = response.body();
                    }
                }

                @Override
                public void onFailure(Call<List<NewCurrencyModel>> call, Throwable t) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "An error occurred while getting currencies.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }).start();
    }

    public static void getLocationList(Activity activity) {
        new Thread(() -> {
            IMasterService masterService = RetrofitClient.getClient().create(IMasterService.class);
            masterService.getLocations().enqueue(new Callback<List<NewLocationModel>>() {
                @Override
                public void onResponse(Call<List<NewLocationModel>> call, Response<List<NewLocationModel>> response) {
                    if(response.isSuccessful()) {
                        UserHomeActivity.locationList = response.body();
                    }
                }

                @Override
                public void onFailure(Call<List<NewLocationModel>> call, Throwable t) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "An error occurred while getting locations.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }).start();
    }

    public static void getCuisineTypeList(Activity activity) {
        new Thread(() -> {
            IMasterService masterService = RetrofitClient.getClient().create(IMasterService.class);
            masterService.getCuisineTypes().enqueue(new Callback<List<NewCuisineTypeModel>>() {
                @Override
                public void onResponse(Call<List<NewCuisineTypeModel>> call, Response<List<NewCuisineTypeModel>> response) {
                    if(response.isSuccessful()) {
                        UserHomeActivity.cuisineTypeList = response.body();
                    }
                }

                @Override
                public void onFailure(Call<List<NewCuisineTypeModel>> call, Throwable t) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "An error occurred while getting cuisine types.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }).start();
    }

    public static IndexModel GetDocumentIndex(String CollectionName) {
        //get index model
        return new IndexModel();
    }

    public static void UpdateDocumentIndex(String CollectionName, int currentCount) {
        //update index
    }

    public static List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng(lat / 1E5, lng / 1E5);
            poly.add(p);
        }
        return poly;
    }
}
