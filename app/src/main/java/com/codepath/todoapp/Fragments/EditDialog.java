package com.codepath.todoapp.Fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.todoapp.R;
import com.codepath.todoapp.models.TodoItem;
import com.codepath.todoapp.utils.DateUtils;

import java.util.Date;

public class EditDialog extends DialogFragment {
    private TodoItem todoItem;
    private int position;
    private EditText etTitle;
    private EditText etNotes;
    private Spinner priority;
    private TextView tvDate;
    private Date itemDate;
    private Button btSave;
    private Button btCancel;

    public EditDialog() {

    }

    public static EditDialog newInstance(TodoItem todo, int position) {
        EditDialog frag = new EditDialog();
        Bundle args = new Bundle();
        args.putParcelable("item", todo);
        args.putInt("position", position);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position", 0);
        todoItem = getArguments().getParcelable("item");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle("Edit Item");

        View view = inflater.inflate(R.layout.fragment_edit_item, container);

        etTitle = (EditText) view.findViewById(R.id.etEditTitle);
        final String title = todoItem.getTitle();
        etTitle.setText(title);

        etNotes = (EditText) view.findViewById(R.id.etEditNotes);
        final String notes = todoItem.getNotes();
        if (!TextUtils.isEmpty(notes))
            etNotes.setText(notes);

        priority = (Spinner) view.findViewById(R.id.spEditPriority);

        populateSpinner();

        final String pri = todoItem.getPriority();
        priority.setSelection(getIndex(priority, pri));

        tvDate = (TextView) view.findViewById(R.id.tvEditDate);
        final String date = DateUtils.getDateString(todoItem.getDueDate());
        final Date oldDate = todoItem.getDueDate();
        if (!TextUtils.isEmpty(date))
            tvDate.setText(date);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TodoActivity.showDatePickerDialog(v);
            }
        });

        btSave = (Button) view.findViewById(R.id.btnSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = etTitle.getText().toString();
                if (!title.equals(newTitle))
                    todoItem.setTitle(newTitle);

                String newNotes = etNotes.getText().toString();
                if (!notes.equals(newNotes) &&
                        !TextUtils.isEmpty(newNotes))
                    todoItem.setNotes(newNotes);

                String newPri = priority.getSelectedItem().toString();
                if (!newPri.equals(pri))
                    todoItem.setPriority(newPri);

                if (!itemDate.equals(oldDate))
                    todoItem.setDueDate(itemDate);

                Intent intent = new Intent();
                intent.putExtra("item", todoItem);
                intent.putExtra("position", position);

                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });

        btCancel = (Button) view.findViewById(R.id.btnCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                getActivity().setResult(Activity.RESULT_CANCELED, intent);
                getActivity().finish();
            }
        });
        return view;
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
}
