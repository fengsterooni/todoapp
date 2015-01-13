package com.codepath.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class EditItemActivity extends Activity {
    private EditText etItemEdit;
    private Button btnSave;
    private String oldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etItemEdit = (EditText) findViewById(R.id.etEditItem);
        oldText = getIntent().getStringExtra("item");
        etItemEdit.setText(oldText);
        final int position = getIntent().getIntExtra("position", 0);
        etItemEdit.requestFocus();
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String text = etItemEdit.getText().toString();
                intent.putExtra("position", position);
                intent.putExtra("item", text);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


}
