package com.becode.humhub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static com.becode.humhub.R.id.tab_home;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public boolean doubleBackToExitPressedOnce = false;
    private InterstitialAd interstitial;
    private NavigationView navigationView;
    public Timer AdTimer;
    private boolean open_from_push = false;

    // GCM
    public static final String PROPERTY_REG_ID = "notifyId";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    SharedPreferences preferences;
    String reg_cgm_id;
    static final String TAG = "MainActivity";
    private boolean first_fragment = false;
    private double latitude;
    private double longitude;

    @Override
    protected void onPause() {
        super.onPause();
        if (AdTimer != null) {
            AdTimer.cancel();
            AdTimer = null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            /*case R.id.share_button:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String sAux = getString(R.string.share_text) + "\n";
                    sAux = sAux + getString(R.string.share_link) + "\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) { //e.toString();
                }
                return true;
             */
            case R.id.search:
                Bundle bundle = new Bundle();
                bundle.putString("type", getString(R.string.search_type));
                bundle.putString("url", getString(R.string.search_url));
                Fragment fragment = new FragmentWebInteractive();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "FragmentWebInteractive").commit();
                first_fragment = true;
                setTitle(item.getTitle());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if (getString(R.string.rtl_version).equals("true")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*Action for floating button uncomment for enable*/
       /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        */
        // New bottom BAR tabs
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId ) {
                if (tabId == tab_home) {
                    Bundle bar_home = new Bundle();
                    bar_home.putString("type", getString(R.string.home_type));
                    bar_home.putString("url", getString(R.string.home_url));
                    Fragment bar_home_h = new FragmentWebInteractive();
                    bar_home_h.setArguments(bar_home);
                    FragmentManager bar_home_hFragment = getSupportFragmentManager();
                    bar_home_hFragment.beginTransaction().replace(R.id.frame_container, bar_home_h, "FragmentWebInteractive").commit();
                    setTitle(getString(R.string.home_label));
                    first_fragment = true;

                } else if (tabId == R.id.tab_spaces) {
                    Bundle bar_spaces = new Bundle();
                    bar_spaces.putString("type", getString(R.string.spaces_type));
                    bar_spaces.putString("url", getString(R.string.spaces_url));
                    Fragment bar_spaces_s = new FragmentWebInteractive();
                    bar_spaces_s.setArguments(bar_spaces);
                    FragmentManager bar_spaces_sFragment = getSupportFragmentManager();
                    bar_spaces_sFragment.beginTransaction().replace(R.id.frame_container, bar_spaces_s, "FragmentWebInteractive").commit();
                    setTitle(getString(R.string.spaces_label));
                    first_fragment = true;
                } else if (tabId == R.id.tab_notification){
                    Bundle bar_notify = new Bundle();
                    bar_notify.putString("type", getString(R.string.notification_type));
                    bar_notify.putString("url", getString(R.string.notification_url));
                    Fragment bar_notification = new FragmentWebInteractive();
                    bar_notification.setArguments(bar_notify);
                    FragmentManager bar_notificationFragment = getSupportFragmentManager();
                    bar_notificationFragment.beginTransaction().replace(R.id.frame_container, bar_notification, "FragmentWebInteractive").commit();
                    setTitle(getString(R.string.notification_label));
                    first_fragment = true;

                }
            }
        });
        // ON RESELECTED ITEM RETURN SAME URL

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == tab_home) {
                    Bundle bar_home = new Bundle();
                    bar_home.putString("type", getString(R.string.home_type));
                    bar_home.putString("url", getString(R.string.home_url));
                    Fragment bar_home_h = new FragmentWebInteractive();
                    bar_home_h.setArguments(bar_home);
                    FragmentManager bar_home_hFragment = getSupportFragmentManager();
                    bar_home_hFragment.beginTransaction().replace(R.id.frame_container, bar_home_h, "FragmentWebInteractive").commit();
                    setTitle(getString(R.string.home_label));
                    first_fragment = true;
                }
                else if (tabId == R.id.tab_spaces) {
                    Bundle bar_spaces = new Bundle();
                    bar_spaces.putString("type", getString(R.string.spaces_type));
                    bar_spaces.putString("url", getString(R.string.spaces_url));
                    Fragment bar_spaces_s = new FragmentWebInteractive();
                    bar_spaces_s.setArguments(bar_spaces);
                    FragmentManager bar_spaces_sFragment = getSupportFragmentManager();
                    bar_spaces_sFragment.beginTransaction().replace(R.id.frame_container, bar_spaces_s, "FragmentWebInteractive").commit();
                    setTitle(getString(R.string.spaces_label));
                    first_fragment = true;
                } else if (tabId == R.id.tab_notification){
                    Bundle bar_notify = new Bundle();
                    bar_notify.putString("type", getString(R.string.notification_type));
                    bar_notify.putString("url", getString(R.string.notification_url));
                    Fragment bar_notification = new FragmentWebInteractive();
                    bar_notification.setArguments(bar_notify);
                    FragmentManager bar_notificationFragment = getSupportFragmentManager();
                    bar_notificationFragment.beginTransaction().replace(R.id.frame_container, bar_notification, "FragmentWebInteractive").commit();
                    setTitle(getString(R.string.notification_label));
                    first_fragment = true;
                }

            }

        });
        // End bottom BAR tabs
        // Go to first fragment
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().getString("link", null) != null && !intent.getExtras().getString("link", null).equals("")) {
            open_from_push = true;
            String url = null;
            if (intent.getExtras().getString("link").contains("http")) {
                url = intent.getExtras().getString("link");
            } else {
                url = "https://" + intent.getExtras().getString("link");
            }

            Bundle bundle = new Bundle();
            bundle.putString("type", "url");
            bundle.putString("url", url);
            Fragment fragment = new FragmentWebInteractive();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "FragmentWebInteractive").commit();
            first_fragment = true;

        } else if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putString("type", getString(R.string.home_type));
            bundle.putString("url", getString(R.string.home_url));
            Fragment fragment = new FragmentWebInteractive();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "FragmentWebInteractive").commit();
            first_fragment = true;
        }

        // Enable for insert Ad inside the application (uncomment row inside -> layout->content_main xml)
        // -------------------------------  AdMob Banner ------------------------------------------------------------
       /* AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);

        // -------------------------------- AdMob Interstitial ----------------------------
        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(MainActivity.this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        // Load ads into Interstitial Ads
        interstitial.loadAd(adRequest);

        AdTimer = new Timer();

        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function with timer
                if (AdTimer != null) {
                    AdTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    displayInterstitial();
                                }
                            });
                        }
                    }, Integer.parseInt(getString(R.string.admob_interstiial_delay)));
                }
            }
        });
        */

        if (preferences.getBoolean("pref_geolocation_update", true)) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                // create class object
                GPSTracker gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();


                    int appVersion = getAppVersion(this);
                    Log.i(TAG, "Saving regId on app version " + appVersion);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("latitude", "" + latitude);
                    editor.putString("longitude", "" + longitude);
                    editor.putString(PROPERTY_APP_VERSION, ""+appVersion);
                    editor.commit();


                    Log.d("GPS", "Latitude: " + latitude + ", Longitude: " + longitude);


                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    if (preferences.getBoolean("pref_gps_remember", false)) {
                        gps.showSettingsAlert();
                    }
                }
            } else {
                // Request permission to the user
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1
                );
            }
        }


        // Save token on server
        sendRegistrationIdToBackend();

    }

    public void displayInterstitial() {
        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        Fragment webviewfragment = getSupportFragmentManager().findFragmentByTag("FragmentWebInteractive");
        if (webviewfragment instanceof FragmentWebInteractive) {
            if (((FragmentWebInteractive) webviewfragment).canGoBack()) {
                ((FragmentWebInteractive) webviewfragment).GoBack();


                return;
            }
        }

        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        } else {
            if (first_fragment == false) {
                super.onBackPressed();
            }
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1500);


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        String tag = null;
        first_fragment = false;

        if (id == R.id.people) {
            Bundle bundle = new Bundle();
            bundle.putInt("item_position", 0);
            bundle.putString("type", getString(R.string.people_type));
            bundle.putString("url", getString(R.string.people_url));
            fragment = new FragmentWebInteractive();
            fragment.setArguments(bundle);
            tag = "FragmentWebInteractive";
            first_fragment = true;
        } else if (id == R.id.messages) {
            Bundle bundle = new Bundle();
            bundle.putInt("item_position", 1);
            bundle.putString("type", getString(R.string.messages_type));
            bundle.putString("url", getString(R.string.messages_url));
            fragment = new FragmentWebInteractive();
            fragment.setArguments(bundle);
            tag = "FragmentWebInteractive";

        } else if (id == R.id.documentation) {
            Bundle bundle = new Bundle();
            bundle.putInt("item_position", 2);
            bundle.putSerializable("item_id", R.id.documentation);
            bundle.putString("type", getString(R.string.documentation_type));
            bundle.putString("url", getString(R.string.documentation_url));
            fragment = new FragmentWebInteractive();
            fragment.setArguments(bundle);
            tag = "FragmentWebInteractive";
        } else if (id == R.id.faq) {

            Bundle bundle = new Bundle();
            bundle.putInt("item_position", 3);
            bundle.putSerializable("item_id", R.id.faq);
            bundle.putString("type", getString(R.string.faq_type));
            bundle.putString("url", getString(R.string.faq_url));
            fragment = new FragmentWebInteractive();
            fragment.setArguments(bundle);
            tag = "FragmentWebInteractive";

        }

        // ##################### --------------- EXAMPLE ----------------------- #################

        else if (id == R.id.nav_1) {
            Intent i = new Intent(getBaseContext(), SettingsActivity.class);
            startActivity(i);
            return true;

        } else if (id == R.id.logout) {
            // ---------------------------------  Load WebiView with Remote URL -------------------- //
            Bundle bundle = new Bundle();
            bundle.putString("type", getString(R.string.logout_type));
            bundle.putString("url", getString(R.string.logout_url));
            fragment = new FragmentWebInteractive();
            fragment.setArguments(bundle);
            tag = "FragmentWebInteractive";

        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, tag).addToBackStack(null).commit();

        setTitle(item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void SetItemChecked(int position) {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(position).setChecked(true);
    }


    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     * REFRESH TOKEN TO THE SERVER
     */
    private void sendRegistrationIdToBackend() {

        Log.d(TAG, "Start update data to server...");

        String latitude = preferences.getString("latitude", null);
        String longitude = preferences.getString("longitude", null);
        String appVersion = preferences.getString("appVersion", null);
        String token = preferences.getString("fcm_token", null);

        // Register FCM Token ID to server
        if (token != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair(getString(R.string.db_field_token), token));
            nameValuePairs.add(new BasicNameValuePair(getString(R.string.db_field_latitude), "" + latitude));
            nameValuePairs.add(new BasicNameValuePair(getString(R.string.db_field_longitude), "" + longitude));
            nameValuePairs.add(new BasicNameValuePair(getString(R.string.db_field_appversion), "" + appVersion));
            new HttpTask(null, MainActivity.this, getString(R.string.server_url), nameValuePairs, false).execute();
        }

    }


}
