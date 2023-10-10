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

import com.rlabdevs.unifymobile.R;
import com.rlabdevs.unifymobile.adapters.ItemSelectorAdapter;
import com.rlabdevs.unifymobile.common.enums.CurrencyCode;
import com.rlabdevs.unifymobile.common.enums.StatusCode;
import com.rlabdevs.unifymobile.common.enums.UserRole;
import com.rlabdevs.unifymobile.models.IndexModel;
import com.rlabdevs.unifymobile.models.SelectorItemModel;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Functions implements DialogInterface.OnDismissListener {

    private static Dialog errorDialog, itemSelectorDialog;
    public static Dialog progressDialog;

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
        else if(adults > 1 && children == 1) { return adults + " adults & " + children + " child"; }
        else if(adults == 1 && children == 1) { return adults + " adult & " + children + " child"; }
        return "";
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

    public static IndexModel GetDocumentIndex(String CollectionName) {
        //get index model
        return new IndexModel();
    }

    public static void UpdateDocumentIndex(String CollectionName, int currentCount) {
        //update index
    }
}
