package com.codepath.todoapp.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    private DatePickerDialog.OnDateSetListener dateSetListener;

    public DatePickerFragment() {
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
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }
}
