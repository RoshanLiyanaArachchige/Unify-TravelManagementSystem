package com.rlabdevs.unifymobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.rlabdevs.unifymobile.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeSelectionDialog extends Dialog {
    private Context context;
    private TextView txtViewSelectDateTimeTitle;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText dateTimeInputElement;
    private Button btnDateView, btnTimeView, btnSelectDateTime, btnDeselectDateTime, btnClose;

    public DateTimeSelectionDialog(@NonNull Context context, boolean isCancelable, EditText dateTimeInputElement) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.date_time_selection_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if(!isCancelable) setCancelable(false);

        this.context = context;

        txtViewSelectDateTimeTitle = findViewById(R.id.txtViewSelectDateTimeTitle);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    switchToTimePickerView();
                }
            });
        }

        btnDateView = findViewById(R.id.btnDateView);
        btnDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToDatePickerView();
            }
        });

        btnTimeView = findViewById(R.id.btnTimeView);
        btnTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToTimePickerView();
            }
        });

        btnSelectDateTime = findViewById(R.id.btnSelectDateTime);

        btnClose = findViewById(R.id.btnClose);

        btnClose.setOnClickListener(closeOnClickListener);

        this.dateTimeInputElement = dateTimeInputElement;
    }

    public void switchToDatePickerView() {
        btnTimeView.setElevation(0f);
        btnTimeView.setBackground(null);
        btnTimeView.setBackground(context.getResources().getDrawable(R.drawable.button_bg_light_inactive_rad_10));
        btnTimeView.setTextColor(context.getResources().getColor(R.color.grey_regular));
        timePicker.setVisibility(View.GONE);
        datePicker.setVisibility(View.VISIBLE);
        btnDateView.setElevation(8f);
        btnDateView.setBackground(null);
        btnDateView.setBackground(context.getResources().getDrawable(R.drawable.button_bg_light_active_rad_10));
        btnDateView.setTextColor(context.getResources().getColor(R.color.white));
    }

    public void switchToTimePickerView() {
        btnDateView.setElevation(0f);
        btnDateView.setBackground(null);
        btnDateView.setBackground(context.getResources().getDrawable(R.drawable.button_bg_light_inactive_rad_10));
        btnDateView.setTextColor(context.getResources().getColor(R.color.grey_regular));
        datePicker.setVisibility(View.GONE);
        timePicker.setVisibility(View.VISIBLE);
        btnTimeView.setElevation(8f);
        btnTimeView.setBackground(null);
        btnTimeView.setBackground(context.getResources().getDrawable(R.drawable.button_bg_light_active_rad_10));
        btnTimeView.setTextColor(context.getResources().getColor(R.color.white));
    }

    public void setSelectDateTimeTitle(String title) {
        txtViewSelectDateTimeTitle.setText(title);
    }

    public void setDateTimeSelectOnClickListener(View.OnClickListener onDateTimeSelectListener) {
        btnSelectDateTime.setOnClickListener(onDateTimeSelectListener);
    }

    public void setSelectedDateTime(Date selectedDateTime) {
        datePicker.updateDate(selectedDateTime.getYear()+1900, (selectedDateTime.getMonth()), selectedDateTime.getDay());
        timePicker.setHour(selectedDateTime.getHours());
        timePicker.setMinute(selectedDateTime.getMinutes());
    }

    public Date getSelectedDateTime() {
        Calendar calendar = Calendar.getInstance();

        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int dayOfMonth = datePicker.getDayOfMonth();

        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        calendar.set(year, month, dayOfMonth, hour, minute);

        return calendar.getTime();
    }

    public String getSelectedDateTimeString() {
        Calendar calendar = Calendar.getInstance();

        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int dayOfMonth = datePicker.getDayOfMonth();

        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        calendar.set(year, month, dayOfMonth, hour, minute);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        String formattedDateTime = sdf.format(calendar.getTime());

        return formattedDateTime;
    }

    public EditText getDateTimeInputElement() {
        return dateTimeInputElement;
    }

    private View.OnClickListener closeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
}
