package com.codepath.todoapp.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;

import com.codepath.todoapp.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {
    private DatePickerDialog.OnDateSetListener dateSetListener;

    public static DatePickerFragment newInstance(String dateString) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putString("date", dateString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof DatePickerDialog.OnDateSetListener) {
            dateSetListener = (DatePickerDialog.OnDateSetListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement DatePickerFragment.OnDateSetListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        String dateString = getArguments().getString("date", "");
        if (!TextUtils.isEmpty(dateString)) {
            Date date = DateUtils.getDateFromString(dateString);
            calendar.setTime(date);
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener, year, month, day);

        // Disable past time
        // http://stackoverflow.com/questions/23762231/how-to-disable-past-dates-in-android-date-picker
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        return datePickerDialog;
    }
}
