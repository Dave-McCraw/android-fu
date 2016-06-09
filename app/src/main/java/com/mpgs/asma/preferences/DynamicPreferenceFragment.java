package com.mpgs.asma.preferences;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.mpgs.asma.R;

import java.util.Collection;

/**
 * Created by e036307 on 09/06/2016.
 */
public class DynamicPreferenceFragment extends PreferenceFragment {

    public class CustomPreferenceGroup {

        String name;
        int title;
        Collection<CustomPreference> preferences;

        CustomPreferenceGroup(String name, int title, Collection<CustomPreference> preferences) {
            this.name = name;
            this.title = title;
            this.preferences = preferences;

        }
    }

    public class CustomPreference {

        String name;
        int title;
        int defaultValue;

        CustomPreference(String name, int defaultValue, int title) {
            this.name = name;
            this.title = title;
            this.defaultValue = defaultValue;
        }
    }

    public void onCreate(Bundle savedInstanceState, Collection<CustomPreferenceGroup> groups) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        addPreferencesFromResource(R.xml.pref_user);

        PreferenceScreen screen = this.getPreferenceScreen(); // "null". See onViewCreated.

        for (CustomPreferenceGroup group : groups) {
            PreferenceCategory category = new PreferenceCategory(screen.getContext());
            category.setTitle(group.title);
            category.setKey(group.name);
            screen.addPreference(category);

            for (CustomPreference key : group.preferences) {

                EditTextPreference pref = new EditTextPreference(screen.getContext());
                pref.setKey(key.name);
                pref.setTitle(key.title);
                pref.setSummary(key.defaultValue);

                category.addPreference(pref);

                bindPreferenceSummaryToValue(pref);
            }
        }
/*
        for (CustomPreference key : CustomPreference.values()) {

            EditTextPreference pref = new EditTextPreference(screen.getContext());
            pref.setKey(key.name());
            pref.setTitle(key.title);
            pref.setSummary(key.defaultValue);

            ((PreferenceGroup) screen.findPreference(key.group.name())).addPreference(pref);

            bindPreferenceSummaryToValue(pref);
        }
*/
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        if (null == preference) {
            return;
        }
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            preference.setSummary(value.toString());

            return true;
        }
    };
}
