package com.codepath.todoapp.fragments;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import com.codepath.todoapp.R;

public class TodoPrefFragment extends PreferenceFragment {
    private ListPreference listPreference;
    private SwitchPreference switchPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setupPreferences();
    }

    public void setupPreferences() {
        PreferenceManager preferenceManager = getPreferenceManager();
        switchPreference = (SwitchPreference) preferenceManager.findPreference("switch_preference");
        if (preferenceManager.getSharedPreferences().getBoolean("switch_preference", true)) {
            switchPreference.setChecked(true);
        } else {
            switchPreference.setChecked(false);
        }

        listPreference = (ListPreference) preferenceManager.findPreference("list_preference");
        listPreference.setSummary(listPreference.getEntry());
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringValue = newValue.toString();
                // insert custom code
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
                return true;
            }
        });
    }
}
