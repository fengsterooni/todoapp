package com.codepath.todoapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.codepath.todoapp.R;
import com.codepath.todoapp.fragments.TodoFragment;


public class TodoActivity extends ActionBarActivity {
    private static final String TAG = TodoActivity.class.getSimpleName();

    TodoFragment fragment;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        if (savedInstanceState == null) {
            fragment = TodoFragment.newInstatnce();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fListContainer, fragment);
            fragmentTransaction.commit();
        }
    }
}