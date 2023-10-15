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
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.rlabdevs.unifymobile.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateSelectionDialog extends Dialog {
    private Context context;
    private TextView txtViewSelectDateTitle;
    private DatePicker datePicker;
    private TextView dateInputElement;
    private Button btnSelectDate, btnClose;

    public DateSelectionDialog(@NonNull Context context, boolean isCancelable, TextView dateInputElement) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.date_selection_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if(!isCancelable) setCancelable(false);

        this.context = context;

        datePicker = findViewById(R.id.datePicker);

        txtViewSelectDateTitle = findViewById(R.id.txtViewSelectDateTitle);
        btnSelectDate = findViewById(R.id.btnSelectDate);

        btnClose = findViewById(R.id.btnClose);

        btnClose.setOnClickListener(closeOnClickListener);

        this.dateInputElement = dateInputElement;
    }

    public void setSelectDateTitle(String title) {
        txtViewSelectDateTitle.setText(title);
    }

    public void setDateSelectOnClickListener(View.OnClickListener onDateSelectListener) {
        btnSelectDate.setOnClickListener(onDateSelectListener);
    }

    public void setSelectedDate(Date selectedDate) {
        datePicker.updateDate(selectedDate.getYear()+1900, (selectedDate.getMonth()), selectedDate.getDay());
    }

    public Date getSelectedDate() {
        Calendar calendar = Calendar.getInstance();

        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int dayOfMonth = datePicker.getDayOfMonth();

        calendar.set(year, month, dayOfMonth);

        return calendar.getTime();
    }

    public String getSelectedDateString() {
        Calendar calendar = Calendar.getInstance();

        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int dayOfMonth = datePicker.getDayOfMonth();

        calendar.set(year, month, dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String formattedDateTime = sdf.format(calendar.getTime());

        return formattedDateTime;
    }

    public TextView getDateInputElement() {
        return dateInputElement;
    }

    private View.OnClickListener closeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
}
