package com.codepath.todoapp.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.DatePicker;

import com.codepath.todoapp.R;
import com.codepath.todoapp.fragments.DatePickerFragment;
import com.codepath.todoapp.fragments.TodoItemFragment;
import com.codepath.todoapp.models.TodoItem;
import com.codepath.todoapp.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TodoItemActivity extends ActionBarActivity
        implements DatePickerDialog.OnDateSetListener,
                    TodoItemFragment.OnDatePickerClickedListener {

    TodoItemFragment fragment;
    FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todoitem);

        TodoItem todoItem = getIntent().getParcelableExtra("item");
        int position = getIntent().getIntExtra("position", 0);

        if (savedInstanceState == null) {
            fragment = TodoItemFragment.newInstatnce(todoItem, position);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fItemContainer, fragment);
            fragmentTransaction.commit();
        }
    }

    public void onDatePickerClicked(View v) {
        String dateString = "";
        Date date = fragment.getItemDate();
        if (date != null)
            dateString = DateUtils.getDateString(date);
        DialogFragment dialogFragment = DatePickerFragment.newInstance(dateString);
        dialogFragment.show(getSupportFragmentManager(), "date_dialog");
    }

    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        fragment.setItemDate(calendar.getTime());
    }
}
