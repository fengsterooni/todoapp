package com.codepath.todoapp.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.todoapp.R;
import com.codepath.todoapp.models.TodoItem;
import com.codepath.todoapp.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TodoItemFragment extends Fragment
        implements DatePickerDialog.OnDateSetListener{
    private TodoItem todoItem;
    private int position;
    private EditText etTitle;
    private EditText etNotes;
    private Spinner priority;
    private TextView tvDate;
    private Date itemDate;

    public TodoItemFragment() {
    }

    public static TodoItemFragment newInstance(TodoItem todo, int position) {
        TodoItemFragment fragment = new TodoItemFragment();
        Bundle args = null;
        if (todo != null) {
            args = new Bundle();
            args.putParcelable("item", todo);
            args.putInt("position", position);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            todoItem = getArguments().getParcelable("item");
            position = getArguments().getInt("position");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_item, container, false);
        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etNotes = (EditText) view.findViewById(R.id.etNotes);
        priority = (Spinner) view.findViewById(R.id.spPriority);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        if (todoItem != null) {
            getActivity().setTitle("Edit Item");
            updateUI();
        } else {
            getActivity().setTitle("Add New");
            todoItem = new TodoItem();
        }

        populateSpinner();
        return view;
    }

    private void updateUI() {
        if (todoItem == null) {
            Toast.makeText(getActivity(), "There is something wrong!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Please add a title!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Please set a due date", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            todoItem.setDueDate(itemDate);
        }

        return todoItem;
    }

    public void showDatePickerDialog(View v) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment dialogFragment = DatePickerFragment.newInstance("");
        //dialogFragment.show(getFragmentManager(), "date_dialog");
        ft.replace(android.R.id.content, dialogFragment, "date_dialog").commit();
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
        ArrayAdapter<CharSequence> aAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.todo_priority, android.R.layout.simple_spinner_item);
        aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setAdapter(aAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_todoitem, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Activity activity = getActivity();
        switch (item.getItemId()) {
            case R.id.action_done:
                TodoItem todoItem = prepareTodoItem();
                if (todoItem != null) {
                    Intent intent = new Intent();
                    intent.putExtra("item", todoItem);
                    intent.putExtra("position", position);
                    activity.setResult(Activity.RESULT_OK, intent);
                    activity.finish();
                }
                return true;
            case R.id.action_cancel:
                Intent intent = new Intent();
                activity.setResult(Activity.RESULT_CANCELED, intent);
                activity.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
