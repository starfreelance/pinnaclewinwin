package com.pinnacle.winwin.custom;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.listener.DatePickerDialogListener;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Activity mActivity;

    private DatePickerDialogListener pickerDialogListener;

    public static DatePickerDialogFragment newInstance() {

        Bundle args = new Bundle();

        DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
            pickerDialogListener = (DatePickerDialogListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, R.style.Theme_AppCompat_DayNight_Dialog,
                this, year, month, day);
        Calendar pastCalendar = Calendar.getInstance();
        pastCalendar.set(Calendar.YEAR, 1900);
        pastCalendar.set(Calendar.MONTH, Calendar.JANUARY);
        pastCalendar.set(Calendar.DATE, 1);
        datePickerDialog.getDatePicker().setMinDate(pastCalendar.getTimeInMillis());
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.add(Calendar.YEAR, -18);
        datePickerDialog.getDatePicker().setMaxDate(currentCalendar.getTimeInMillis());

        return datePickerDialog;

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, monthOfYear, dayOfMonth);
        if (pickerDialogListener != null) {
            pickerDialogListener.onDateSelectedListener(selectedDate.getTime());
        }
    }
}
