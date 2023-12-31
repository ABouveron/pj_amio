package fr.abouveron.projectamio;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import androidx.annotation.Nullable;


public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        findPreference("back_button").setOnPreferenceClickListener(preference -> {
            finish();
            return true;
        });
    }
}