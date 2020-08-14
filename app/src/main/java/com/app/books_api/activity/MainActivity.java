package com.app.books_api.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.app.books_api.R;
import com.app.books_api.listener.NetworkListener;
import com.app.books_api.ui.BottomBar;
import com.app.books_api.utils.AnimationUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomBar {
    private BroadcastReceiver broadcastReceiver;

    private BottomNavigationView bottomNavigationView;

    private boolean disconnected = false;

    private MenuItem dashboardItem;
    private MenuItem homeItem;
    private MenuItem aboutItem;

    private boolean barIsActive = true;

    public final int DASHBOARD_ITEM = 0;
    public final int HOME_ITEM = 1;
    public final int ABOUT_ITEM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        bottomNavigationView = findViewById(R.id.nav_bottom_bar);

        dashboardItem = bottomNavigationView.getMenu().getItem(DASHBOARD_ITEM);
        homeItem = bottomNavigationView.getMenu().getItem(HOME_ITEM);
        aboutItem = bottomNavigationView.getMenu().getItem(ABOUT_ITEM);

        enableBarItem(HOME_ITEM);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_dashboard:
                    navController.popBackStack(R.id.dashboardFragment, true);
                    navController.navigate(R.id.dashboardFragment, null, AnimationUtils.fade());
                    return true;

                case R.id.menu_home:
                    navController.popBackStack(R.id.mainFragment, true);
                    navController.navigate(R.id.mainFragment, null, AnimationUtils.fade());
                    return true;

                case R.id.menu_about:
                    navController.popBackStack(R.id.aboutFragment, true);
                    navController.navigate(R.id.aboutFragment, null, AnimationUtils.fade());
                    return true;
            }
            return false;
        });

        broadcastNetwork();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void broadcastNetwork() {
        TextView connectionText = findViewById(R.id.connection_status_text);
        connectionText.setVisibility(View.GONE);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                Fragment fragment = getCurrentFragment();

                if (networkInfo != null && networkInfo.isConnected()) {
                    if (disconnected) {
                        connectionText.setText(getResources().getString(R.string.connected));
                        connectionText.setBackgroundColor(getResources().getColor(R.color.colorGreen, null));
                        connectionText.setVisibility(View.VISIBLE);

                        disconnected = false;
                        if (fragment != null) {
                            ((NetworkListener) fragment).onBroadcastStart();
                        }

                        new Handler().postDelayed(() -> {
                            if (!disconnected) {
                                connectionText.setVisibility(View.GONE);
                            }
                        }, 5000);
                    }
                } else {
                    connectionText.setText(getResources().getString(R.string.disconnected));
                    connectionText.setBackgroundColor(getResources().getColor(R.color.colorRed, null));
                    connectionText.setVisibility(View.VISIBLE);

                    disconnected = true;
                    if (fragment != null) {
                        ((NetworkListener) fragment).onBroadcastStop();
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private Fragment getCurrentFragment() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
            if (fragment != null) {
                if (fragment instanceof NetworkListener) {
                    return fragment;
                }
            }
        }
        return null;
    }

    @Override
    public void enableBarItem(int id) {
        switch (id) {
            case DASHBOARD_ITEM:
                dashboardItem.setEnabled(false);
                bottomNavigationView.setSelectedItemId(dashboardItem.getItemId());
                break;

            case HOME_ITEM:
                homeItem.setEnabled(false);
                bottomNavigationView.setSelectedItemId(homeItem.getItemId());
                break;

            case ABOUT_ITEM:
                aboutItem.setEnabled(false);
                bottomNavigationView.setSelectedItemId(aboutItem.getItemId());
                break;
        }
    }

    @Override
    public void disableBarItem(int id) {
        switch (id) {
            case DASHBOARD_ITEM:
                dashboardItem.setEnabled(true);
                break;

            case HOME_ITEM:
                homeItem.setEnabled(true);
                break;

            case ABOUT_ITEM:
                aboutItem.setEnabled(true);
                break;
        }
    }

    @Override
    public void showBar() {
        if (!barIsActive) {
            TranslateAnimation animate = new TranslateAnimation(0, 0, bottomNavigationView.getHeight(), 0);
            animate.setDuration(getResources().getInteger(R.integer.anim_duration));
            bottomNavigationView.setVisibility(View.VISIBLE);
            bottomNavigationView.startAnimation(animate);
            barIsActive = true;
        }
    }

    @Override
    public void hideBar() {
        if (barIsActive) {
            TranslateAnimation animate = new TranslateAnimation(0, 0, 0, bottomNavigationView.getHeight());
            animate.setDuration(getResources().getInteger(R.integer.anim_duration));
            bottomNavigationView.setVisibility(View.GONE);
            bottomNavigationView.startAnimation(animate);
            barIsActive = false;
        }
    }
}