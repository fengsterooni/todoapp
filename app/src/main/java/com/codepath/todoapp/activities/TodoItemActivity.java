package com.codepath.todoapp.activities;

import android.app.Activity;
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

import com.codepath.todoapp.R;
import com.codepath.todoapp.fragments.DatePickerFragment;
import com.codepath.todoapp.models.TodoItem;
import com.codepath.todoapp.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TodoItemActivity extends ActionBarActivity
        implements DatePickerDialog.OnDateSetListener {
    private final int DEFAULT_PRIORITY = 1;

    private TodoItem todoItem;
    private int position;
    private EditText etTitle;
    private EditText etNotes;
    private Spinner priority;
    private TextView tvDate;
    private Date itemDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todoitem);

        todoItem = getIntent().getParcelableExtra("item");
        position = getIntent().getIntExtra("position", 0);

        etTitle = (EditText) findViewById(R.id.etTitle);
        etNotes = (EditText) findViewById(R.id.etNotes);
        priority = (Spinner) findViewById(R.id.spPriority);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        populateSpinner();

        if (todoItem != null) {
            setTitle("Edit Item");
            updateUI();
        } else {
            setTitle("Add New");
            todoItem = new TodoItem();
            priority.setSelection(DEFAULT_PRIORITY);
        }
    }

    private void updateUI() {
        if (todoItem == null) {
            Toast.makeText(this, "There is something wrong!", Toast.LENGTH_SHORT).show();
            return;
        }

        etTitle.setText(todoItem.getTitle());
        String notes = todoItem.getNotes();
        if (!TextUtils.isEmpty(notes))
            etNotes.setText(notes);

        String pri = todoItem.getPriority();
        priority.setSelection(getIndex(priority, pri));

        itemDate = todoItem.getDueDate();
        String date = DateUtils.getDateString(itemDate);
        if (!TextUtils.isEmpty(date))
            tvDate.setText(date);
    }

    public TodoItem prepareTodoItem() {
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
        String dateString = "";
        if (itemDate != null)
            dateString = DateUtils.getDateString(itemDate);
        DialogFragment dialogFragment = DatePickerFragment.newInstance(dateString);
        dialogFragment.show(getFragmentManager(), "date_dialog");
    }

    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        itemDate = calendar.getTime();
        tvDate.setText(DateUtils.getDateString(itemDate));
    }

    private int getIndex(Spinner spinner, String string) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(string)) {
                index = i;
            }
        }

        return index;
    }

    private void populateSpinner() {
        ArrayAdapter<CharSequence> aAdapter = ArrayAdapter.createFromResource(this, R.array.todo_priority, android.R.layout.simple_spinner_item);
        aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setAdapter(aAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todoitem, menu);
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
                    intent.putExtra("position", position);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                return true;
            case R.id.action_cancel:
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
