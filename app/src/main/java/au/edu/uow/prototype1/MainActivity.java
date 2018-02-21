package au.edu.uow.prototype1;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import au.edu.uow.prototype1.Fragment.ContactsFragment;
import au.edu.uow.prototype1.Fragment.LogoutFragment;
import au.edu.uow.prototype1.Fragment.NotificationFragment;
import au.edu.uow.prototype1.Fragment.CoursesFragment;
import au.edu.uow.prototype1.Fragment.CalendarFragment;
import au.edu.uow.prototype1.Fragment.EventFragment;

/**
 * Created by Tony on 19/2/2018.
 */

public class MainActivity extends AppCompatActivity {
    //UI
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    @SuppressWarnings("FieldCanBeLocal")
    private NavigationView nvDrawer;

    //UserInfo
    private SharedPreferences userInfoSetting;
    @SuppressWarnings("FieldCanBeLocal")
    private static File userInfoFile;
    private String Username = null;
    private String Email = null;
    private String Password = null;

    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Find UserInfo.xml in /data/data/au.edu.uow.prototype1
        userInfoFile = new File(getApplicationContext().getFilesDir().getParent(), "UserInfo.xml");

        //
        ReadValue();

        // Set up the navigation menu view
        setContentView(R.layout.drawer_layout);


        // Set a Toolbar to replace the ActionBar.
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Find our drawer view
        nvDrawer = findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Lookup navigation view
        NavigationView navigationView = nvDrawer;
        // Inflate the header view at runtime
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header);

        //Setup header image/username/email

        ImageView ivHeaderPhoto = headerLayout.findViewById(R.id.headerImage);
        TextView usernameTV = headerLayout.findViewById(R.id.headUsername);
        TextView emailTV = headerLayout.findViewById(R.id.headerUserEmail);
        ivHeaderPhoto.setVisibility(View.INVISIBLE);
        usernameTV.setText(Email); //TODO Use Username instead of Email after Firebase implementation
        emailTV.setText(Email);

    }

    //Getting user's info from local file
    private void ReadValue() {
        userInfoSetting = getSharedPreferences("UserInfo", 0);
        Username = userInfoSetting.getString("Username", "");
        Email = userInfoSetting.getString("Email", "");
        Password = userInfoSetting.getString("Password", "");
    }

    //Useless stuff
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer_layout.
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                mDrawer.openDrawer(GravityCompat.START);
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
        //noinspection SimplifiableIfStatement
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;


        //TODO remove?
//        if (!userInfoFile.exists() || userInfoFile.exists() && Username.equals("")) {
//            Context context = getApplicationContext();
//            CharSequence text = "You have to login in order to use this function!";
//            int duration = Toast.LENGTH_LONG;
//
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
//
//
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//        }

        switch (menuItem.getItemId()) {

            //notification
            case R.id.nav_first_fragment:
                fragmentClass = NotificationFragment.class;
                break;
            //calendar
            case R.id.nav_second_fragment:
                fragmentClass = CalendarFragment.class;
                break;
            //events viewer
            case R.id.nav_third_fragment:
                fragmentClass = EventFragment.class;
                break;

            //courses
            case R.id.nav_fourth_fragment:
                fragmentClass = CoursesFragment.class;
                break;

            //contacts
            case R.id.personal_contact:
                fragmentClass = ContactsFragment.class;
                break;

            //logout
            case R.id.personal_logout:
                fragmentClass = LogoutFragment.class;
                break;
            default:
                fragmentClass = NotificationFragment.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }
}
