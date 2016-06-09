package com.mpgs.asma.preferences;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.mpgs.asma.BuildConfig;
import com.mpgs.asma.R;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * There is no layout corresponding to this activity; it is using fragments with
 * a standard Android layout (provided by PreferenceActivity via AppCompatPreferenceActivity).
 * <p/>
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class PreferencesActivity extends AppCompatPreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Make the home button take us back up a level
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || UserPreferenceFragment.class.getName().equals(fragmentName)
                || AboutAppPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows info about the app itself.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class AboutAppPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_about_app);
            setHasOptionsMenu(true);

            // Set the app version number
            findPreference("app_version").setSummary(getAppVersion());
        }

        // Recover the application version name
        private String getAppVersion() {
            String version = "-1";

            try {
                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, 0);
                version = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            return version;
        }

    }

    /**
     * This fragment shows preferences for the user account.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class UserPreferenceFragment extends DynamicPreferenceFragment {

        // Obviously, getting this from a datasource would be the thing to do IRL.
        // You can put this in an XML resource except then you need to update the Java anyway
        // to bind the preference listener for each control individually, hence this:
        private Collection<DynamicPreferenceFragment.CustomPreferenceGroup> preferenceGroups =
                Arrays.asList(new DynamicPreferenceFragment.CustomPreferenceGroup("aboutYou", R.string.pref_category_about_you,
                                Arrays.asList(new DynamicPreferenceFragment.CustomPreference("forename", R.string.pref_default_forename, R.string.pref_title_forename),
                                        new DynamicPreferenceFragment.CustomPreference("surname", R.string.pref_default_surname, R.string.pref_title_surname),
                                        new DynamicPreferenceFragment.CustomPreference("email", R.string.pref_default_email, R.string.pref_title_email),
                                        new DynamicPreferenceFragment.CustomPreference("phone", R.string.pref_default_phone, R.string.pref_title_phone)
                                )),
                        new DynamicPreferenceFragment.CustomPreferenceGroup("deliveryDetails", R.string.pref_category_address,
                                Arrays.asList(
                                        new DynamicPreferenceFragment.CustomPreference("street", R.string.pref_default_street, R.string.pref_title_street),
                                        new DynamicPreferenceFragment.CustomPreference("street2", R.string.pref_default_street2, R.string.pref_title_street2),
                                        new DynamicPreferenceFragment.CustomPreference("city", R.string.pref_default_city, R.string.pref_title_city),
                                        new DynamicPreferenceFragment.CustomPreference("state", R.string.pref_default_state, R.string.pref_title_state),
                                        new DynamicPreferenceFragment.CustomPreference("country", R.string.pref_default_country, R.string.pref_title_country),
                                        new DynamicPreferenceFragment.CustomPreference("postcode", R.string.pref_default_postcode, R.string.pref_title_postcode)
                                        )));


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState, preferenceGroups);
        }

    }

}
