package com.codepath.todoapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.codepath.todoapp.R;


public class TodoActivity extends ActionBarActivity {
    private static final String TAG = TodoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
    }
}