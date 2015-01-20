package com.codepath.todoapp.activities;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.todoapp.fragments.DatePickerFragment;
import com.codepath.todoapp.R;
import com.codepath.todoapp.models.TodoItem;
import com.codepath.todoapp.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddNewActivity extends ActionBarActivity
        implements DatePickerDialog.OnDateSetListener {

    private EditText etTitle;
    private EditText etNotes;
    private Spinner priority;
    private TextView tvDate;
    private Date itemDate = null;
    private TodoItem todoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        todoItem = new TodoItem();
        etTitle = (EditText) findViewById(R.id.etAddTitle);
        etNotes = (EditText) findViewById(R.id.etAddNotes);
        priority = (Spinner) findViewById(R.id.spAddPriority);
        tvDate = (TextView) findViewById(R.id.tvAddDate);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        populateSpinner();
        priority.setSelection(1);
    }

    private void populateSpinner() {
        ArrayAdapter<CharSequence> aAdapter = ArrayAdapter.createFromResource(this, R.array.todo_priority, android.R.layout.simple_spinner_item);
        aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setAdapter(aAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                TodoItem todoItem = prepareTodoItem();
                if (todoItem != null) {
                    Intent intent = new Intent();
                    intent.putExtra("item", todoItem);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                return true;
            case R.id.action_cancel:
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private TodoItem prepareTodoItem() {
        String title = etTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Please add a title!", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            todoItem.setTitle(title);
        }

        String notes = etNotes.getText().toString();
        if (!TextUtils.isEmpty(notes)) {
            todoItem.setNotes(notes);
        }

        String pri = priority.getSelectedItem().toString();
        todoItem.setPriority(pri);

        if (itemDate == null) {
            Toast.makeText(this, "Please set a due date", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            todoItem.setDueDate(itemDate);
        }

        return todoItem;
    }

    public void showDatePickerDialog(View v) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment dialogFragment = DatePickerFragment.newInstance("");
        dialogFragment.show(ft, "date_dialog");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        itemDate = calendar.getTime();
        tvDate.setText(DateUtils.getDateString(itemDate));
    }
}
