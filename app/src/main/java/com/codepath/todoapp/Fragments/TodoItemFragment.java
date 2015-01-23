package com.codepath.todoapp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.todoapp.R;
import com.codepath.todoapp.models.TodoItem;
import com.codepath.todoapp.utils.DateUtils;

import java.util.Date;

public class TodoItemFragment extends Fragment {

    private final int DEFAULT_PRIORITY = 1;
    private TodoItem todoItem;
    private int position;
    private EditText etTitle;
    private EditText etNotes;
    private Spinner priority;
    private TextView tvDate;
    private Date itemDate;
    private Activity activity;

    private OnDatePickerClickedListener listener;

    public TodoItemFragment() {
        // Required empty public constructor
    }

    public static TodoItemFragment newInstatnce(TodoItem todoItem, int position) {
        TodoItemFragment fragment = new TodoItemFragment();
        Bundle args = new Bundle();
        args.putParcelable("item", todoItem);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnDatePickerClickedListener) {
            listener = (OnDatePickerClickedListener) activity;
        } else {
            throw new ClassCastException(
                    activity.toString()
                            + " must implement TodoItemFragment.OnDatePickerClickedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoItem = getArguments().getParcelable("item");
        position = getArguments().getInt("position");
        activity = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo_item, container, false);

        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etNotes = (EditText) view.findViewById(R.id.etNotes);
        priority = (Spinner) view.findViewById(R.id.spPriority);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDatePickerClicked(v);
            }
        });

        populateSpinner();

        if (todoItem != null) {
            activity.setTitle("Edit Item");
            updateUI();
        } else {
            activity.setTitle("Add New");
            todoItem = new TodoItem();
            priority.setSelection(DEFAULT_PRIORITY);
        }

        return view;
    }

    private void updateUI() {
        if (todoItem == null) {
            Toast.makeText(activity, "There is something wrong!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(activity, "Please add a title!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(activity, "Please set a due date", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            todoItem.setDueDate(itemDate);
        }

        return todoItem;
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
        ArrayAdapter<CharSequence> aAdapter = ArrayAdapter.createFromResource(activity, R.array.todo_priority, android.R.layout.simple_spinner_item);
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

    public void setItemDate(Date date) {
        itemDate = date;
        tvDate.setText(DateUtils.getDateString(itemDate));
    }

    public Date getItemDate() {
        return itemDate;
    }

    public interface OnDatePickerClickedListener {
        public void onDatePickerClicked(View view);
    }
}
