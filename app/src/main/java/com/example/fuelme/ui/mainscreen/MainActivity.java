/*
* IT19014128 - A.M.W.W.R.L. Wataketiya
* Enterprise Application Development - SE4040
* Start screen for FuelMe Application
*
*
* */

package com.example.fuelme.ui.mainscreen;
import com.example.fuelme.helpers.NightModeHelper;
import com.example.fuelme.ui.about_screen.AboutActivity;
import com.example.fuelme.ui.auth.LoginActivity;
import com.example.fuelme.ui.auth.ProfileActivity;
import com.example.fuelme.ui.customer_dashboard.CustomerDashboardActivity;
import com.example.fuelme.ui.mainscreen.adapters.PageAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelme.R;
import com.example.fuelme.ui.owner_dashboard_screen.OwnerDashboardActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

/**
 *  FuelMe
 *  Enterprise Application Development
 *
 *  IT19014128 - A.M.W.W.R.L. Wataketiya
 *  IT19180526 - S.A.N.L.D. Chandrasiri
 *  H.G. Malwatta - IT19240848
 *
 *  Main Activity of the app
 *
 *  References:
 *  https://developer.android.com/docs
 *  https://square.github.io/okhttp/
 *  https://youtu.be/h2TVVMKkn7Y
 *  https://youtu.be/jiD2fxn8iKA
 *  https://stackoverflow.com/questions/56197563/okhttp-parse-and-recyclerview-fragment
 *  https://www.vogella.com/tutorials/AndroidFileBasedPersistence/article.html
 *  https://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android
 *  https://stackoverflow.com/questions/3517063/android-popping-off-the-activity-stack
 *  https://stackoverflow.com/questions/11699819/how-do-i-get-the-dialer-to-open-with-phone-number-displayed
 *  https://stackoverflow.com/questions/2734749/opening-an-email-client-on-clicking-a-button
 *  https://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application
 *  https://www.youtube.com/watch?v=lt6xbth-yQo&list=PL5jb9EteFAOD8dlG1Il3fCiaVNPD_P7gh&index=3
 *  https://stackoverflow.com/questions/69630124/how-to-configure-toast-icon-in-android-12
 *  https://stackoverflow.com/questions/26615889/how-do-you-change-the-launcher-logo-of-an-app-in-android-studio
 *  https://stackoverflow.com/questions/63227292/make-viewpager-fill-the-remaining-space-in-a-constraintlayout
 *  https://stackoverflow.com/questions/38357479/recyclerview-and-swiperefreshlayout-crash-scroll-list
 * */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView textViewNav_username, textViewNav_email;

    String TAG = "demo";

    //assign views and instances
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    ViewPager pager;
    TabLayout tabLayout;
    TabItem allStationsItem, favouriteStationsItem;
    PageAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Login Data
        preferences = getSharedPreferences("login_data", MODE_PRIVATE);
        editor = preferences.edit();

        //set the toolbar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        //bind the views
        pager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        allStationsItem = findViewById(R.id.all_item);
        favouriteStationsItem = findViewById(R.id.favourites_item);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);

        // Assign User's name and email in Navigation menu
        View headerView = navigationView.getHeaderView(0);
        textViewNav_username = (TextView) headerView.findViewById(R.id.navHeader_displayName);
        textViewNav_email = (TextView) headerView.findViewById(R.id.navHeader_email);
        textViewNav_username.setText(preferences.getString("user_full_name", ""));
        textViewNav_email.setText(preferences.getString("user_email", ""));

        //set navigation item selected listener
        navigationView.setNavigationItemSelectedListener(this);

        //instantiate new toggle with drawerlayout
        toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open_drawer, R.string.close_drawer);

        //add toggle to drawer layout, set drawer indicator and sync toggle state
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        //set page adapter
        pagerAdapter = new PageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tabLayout.getTabCount());
        pager.setAdapter(pagerAdapter);

        //add selector for tab selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //set the tab layout color for themes
        setTabLayoutColorForTheme();

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    //set the tab layout color scheme for theme
    public void setTabLayoutColorForTheme(){
        String theme = NightModeHelper.getMode(this);
        switch (theme){
            case "light":
                //night mode is not enabled.
                //leave default background colors
                break;
            case "dark":
                //night mode is enabled
                //set the tab layout selected text to white
                tabLayout.setTabTextColors(Color.parseColor("#8b8d99"), Color.parseColor("#FFFFFFFF")); //normal color, selected color
                tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFFFF"));
                break;
            default:
                //not defined
                break;
        }
    }

    //method for clicking the drawer button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //method for handling click events of navigation drawer menu items
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navMenu_login:
                Intent intent_profile = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent_profile);
                break;
            case R.id.navMenu_logout:
                //handle logout here
                editor.clear();
                editor.apply();
                Intent intent_login = new Intent(MainActivity.this, LoginActivity.class);
                intent_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent_login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_login);
                break;
            case R.id.navMenu_about:
                //navigate to about
                Intent intentAbout = new Intent(this, AboutActivity.class);
                startActivity(intentAbout);
                break;
            case R.id.navMenu_ownerDashboard:
                //navigate to dashboard here
                if (("Customer").equals(preferences.getString("user_role", ""))){
                    Intent intent = new Intent(this, CustomerDashboardActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, OwnerDashboardActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.navMenu_settings:
                //settings here
                /*
                * Emoji referenced from, https://stackoverflow.com/questions/69630124/how-to-configure-toast-icon-in-android-12
                * */
                Toast.makeText(this, "No settings yet " + ("\ud83d\ude01"), Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.d(TAG,"Default ran in onNavigationItemSelected");
                break;
        }
        closeDrawer();
        return true;
    }

    //method for closing the drawer
    public void closeDrawer(){
        //check if the drawer is open
        //if open, close the drawer
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    //method for handling back press
    @Override
    public void onBackPressed() {
        //if the drawer is open, instead of closing the app, close the drawer
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            //else the default back press is maintained
            super.onBackPressed();
        }
    }
}