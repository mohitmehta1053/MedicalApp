package com.app.android.medicalapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class HomeNavActivity extends AppCompatActivity implements ReminderFragment.OnFragmentInteractionListener,AccountFragment.OnFragmentInteractionListener,ArticleFragment.OnFragmentInteractionListener,NotificationFragment.OnFragmentInteractionListener{

    private DrawerLayout mDrawerLayout;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_nav);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_list_white_24px);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_frame,new HomeFragment());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Home Fragment");



        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );



         navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                 new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight

                        switch (menuItem.getItemId())
                        {
                            case R.id.nav_home:
                                fragmentTransaction=getSupportFragmentManager().beginTransaction();
                                //fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(),new HomeFragment(),)
                                fragmentTransaction.replace(R.id.content_frame,new HomeFragment());
                                fragmentTransaction.commit();
                                getSupportActionBar().setTitle("Home Fragment");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                break;

                            case R.id.nav_reminder:
                                fragmentTransaction=getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame,new ReminderFragment());
                                fragmentTransaction.commit();
                                getSupportActionBar().setTitle("Reminder Fragment");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                break;

                            case R.id.nav_article:
                                fragmentTransaction=getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame,new ArticleFragment());
                                fragmentTransaction.commit();
                                getSupportActionBar().setTitle("Article Fragment");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                break;

                            case R.id.nav_notification:
                                fragmentTransaction=getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame,new NotificationFragment());
                                fragmentTransaction.commit();
                                getSupportActionBar().setTitle("Notification Fragment");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                break;

                            case R.id.nav_account:
                                fragmentTransaction=getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame,new AccountFragment());
                                fragmentTransaction.commit();
                                getSupportActionBar().setTitle("Account Fragment");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                break;
                        }


                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}


