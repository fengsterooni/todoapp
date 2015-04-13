package com.codepath.todoapp.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.todoapp.R;
import com.codepath.todoapp.fragments.TodoFragment;
import com.codepath.todoapp.fragments.TodoPrefFragment;


public class TodoActivity extends ActionBarActivity {
    private static final String TAG = TodoActivity.class.getSimpleName();
    TodoFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        if (savedInstanceState == null) {
            fragment = TodoFragment.newInstatnce();
            getFragmentManager().beginTransaction()
                                .replace(R.id.fListContainer, fragment, "todo")
                                .addToBackStack("todo")
                                .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            getFragmentManager().beginTransaction()
                                .replace(R.id.fListContainer, new TodoPrefFragment(), "pref")
                                .addToBackStack("pref")
                                .commit();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Fragment prefFragment = getFragmentManager().findFragmentByTag("pref");

        if (prefFragment != null) {
            fragment = TodoFragment.newInstatnce();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fListContainer, fragment, "todo")
                    .addToBackStack("todo")
                    .commit();
        }
        else {
            super.onBackPressed();
        }
    }

}