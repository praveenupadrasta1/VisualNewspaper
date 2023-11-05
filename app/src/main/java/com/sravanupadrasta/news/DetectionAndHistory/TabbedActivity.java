package com.praveenupadrasta.news.DetectionAndHistory;

import java.util.Locale;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.praveenupadrasta.news.DeviceLocations.MainActivity;
import com.praveenupadrasta.news.DetectionAndHistory.LandslideDetection.LandslideDetectionFragment;
import com.praveenupadrasta.news.DetectionAndHistory.LandslideHistory.LandslideHistoryFragment;
import com.praveenupadrasta.news.R;

import org.json.JSONObject;

public class TabbedActivity extends AppCompatActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    private String locationName;
    private boolean isNotification;
    private String dateTime;
    private String isRainfall;
    private double accelerometer_level;
    private String latitude;
    private String longitude;
    private double moistureLevel;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_activity1);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        String extras = (String) getIntent().getExtras().get("extras");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(extras);
            isNotification = (boolean) jsonObject.get("isNotification");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if(!isNotification) {
            mViewPager.setCurrentItem((int) getIntent().getExtras().get("tab"));
            bundle = new Bundle();
            bundle.putString("location", getIntent().getExtras().get("location").toString());
            bundle.putBoolean("isNotification", isNotification);
        }
        else {
            mViewPager.setCurrentItem(0);
            readNotificationData(jsonObject);
            setIntentExtras();
        }
    }

    private void readNotificationData(JSONObject jsonObject)
    {
        try {
            dateTime             = jsonObject.get("date_time").toString();
            locationName         = jsonObject.get("location").toString();
            isRainfall           = jsonObject.get("rainfall")=="yes"? "Yes":"No";
            accelerometer_level  = Double.parseDouble(jsonObject.get("accelerometer_level").toString());
            latitude             = jsonObject.get("latitude").toString();
            longitude            = jsonObject.get("longitude").toString();
            moistureLevel        = Double.parseDouble(jsonObject.get("moisture_level").toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void setIntentExtras()
    {
        bundle = new Bundle();
        bundle.putString("location", locationName);
        bundle.putString("date_time", dateTime);
        bundle.putString("rainfall", isRainfall);
        bundle.putDouble("accelerometer_level", accelerometer_level);
        bundle.putDouble("moisture_level", moistureLevel);
        bundle.putString("latitude", latitude);
        bundle.putString("longitude", longitude);
        bundle.putBoolean("isNotification", isNotification);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position)
            {
                case 0:
                    LandslideDetectionFragment landslideDetection = new LandslideDetectionFragment();
                    landslideDetection.setArguments(bundle);
                    return landslideDetection;
                case 1:
                    LandslideHistoryFragment landslideHistory = new LandslideHistoryFragment();
                    landslideHistory.setArguments(bundle);
                    return landslideHistory;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }
}
