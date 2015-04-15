package com.codepath.todoapp.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import com.codepath.todoapp.R;

public class TodoPrefFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String KEY_LIST_PREFERENCE = "list_preference";
    private static final String KEY_SWITCH_PREFERENCE = "switch_preference";
    private ListPreference listPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setupPreferences();
    }


    public void setupPreferences() {
        SwitchPreference switchPreference;
        PreferenceManager preferenceManager = getPreferenceManager();
        switchPreference = (SwitchPreference) preferenceManager.findPreference(KEY_SWITCH_PREFERENCE);
        if (preferenceManager.getSharedPreferences().getBoolean(KEY_SWITCH_PREFERENCE, true)) {
            switchPreference.setChecked(true);
        } else {
            switchPreference.setChecked(false);
        }

        listPreference = (ListPreference) preferenceManager.findPreference(KEY_LIST_PREFERENCE);
        listPreference.setSummary(listPreference.getEntry());
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_LIST_PREFERENCE)) {
            listPreference = (ListPreference) findPreference(key);
            int index = Integer.valueOf(sharedPreferences.getString(key, ""));
            listPreference.setSummary(
                    index >= 0
                            ? listPreference.getEntry()
                            : null);
        }
        else if (key.equals(KEY_SWITCH_PREFERENCE)) {
            // Preference switchPref = findPreference(key);
            boolean bool = sharedPreferences.getBoolean(key, true);
            if (bool == Boolean.FALSE) {
                NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancelAll();
            }
        }
    }
}
