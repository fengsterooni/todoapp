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

import com.codepath.todoapp.Fragments.DatePickerFragment;
import com.codepath.todoapp.R;
import com.codepath.todoapp.models.TodoItem;
import com.codepath.todoapp.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class EditItemActivity extends ActionBarActivity
        implements DatePickerDialog.OnDateSetListener {
    private EditText etTitle;
    private EditText etNotes;
    private Spinner priority;
    private TextView tvDate;
    private Date itemDate;
    private TodoItem todoItem;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        todoItem = getIntent().getParcelableExtra("item");
        position = getIntent().getIntExtra("position", 0);

        etTitle = (EditText) findViewById(R.id.etAddTitle);
        etNotes = (EditText) findViewById(R.id.etAddNotes);
        priority = (Spinner) findViewById(R.id.spAddPriority);
        //priority.setSelection(TodoItem.Priority.DEFAULT);
        tvDate = (TextView) findViewById(R.id.tvAddDate);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        populateSpinner();
        updateUI();
    }

    private void populateSpinner() {
        ArrayAdapter<CharSequence> aAdapter = ArrayAdapter.
                createFromResource(this, R.array.todo_priority, android.R.layout.simple_spinner_item);
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
                    intent.putExtra("position", position);
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

    private TodoItem prepareTodoItem() {
        // Title
        String title = etTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(EditItemActivity.this, "Please add a title!", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            todoItem.setTitle(title);
        }
        // Notes
        String notes = etNotes.getText().toString();
        if (!TextUtils.isEmpty(notes)) {
            todoItem.setNotes(notes);
        }
        // Priority
        String pri = priority.getSelectedItem().toString();
        todoItem.setPriority(pri);
        // Date
        todoItem.setDueDate(itemDate);

        return todoItem;
    }

    public void showDatePickerDialog(View v) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(ft, "date_dialog");
    }

    @Override
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
}
