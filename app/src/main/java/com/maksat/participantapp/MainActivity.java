package com.maksat.participantapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maksat.participantapp.interfaces.IOnBackPressed;
import com.maksat.participantapp.ui.schedule.ScheduleFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public BottomNavigationView navView;
    public SharedPreferences preferences;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_schedule, R.id.navigation_b2b, R.id.navigation_voting, R.id.navigation_map, R.id.navigation_more)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       /* navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                Toast.makeText(MainActivity.this, destination.getLabel(), Toast.LENGTH_SHORT).show();
            }
        });*/
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        preferences =this.getSharedPreferences("event", Context.MODE_PRIVATE);
        View divider=findViewById(R.id.divider2);
        if (Variables.token==null){
            divider.setVisibility(View.GONE);
            navView.setVisibility(View.GONE);
            NavOptions.Builder navBuilder = new NavOptions.Builder();
            NavOptions navOptions = navBuilder.setLaunchSingleTop(true).setPopUpTo(R.id.navigation_schedule, true).build();
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.loginFragment, null, navOptions);
        }/*else {
            if (navView.getVisibility()==View.GONE){
                navView.setVisibility(View.VISIBLE);
            }
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.navigation_schedule);
        }*/
        if (Variables.token!=null){
            Requests preloadInfo=new Requests(this);
            preloadInfo.getProfile();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        /*Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {*/
            List<Fragment> fragments = new ArrayList<>(getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().getFragments());
            if (fragments.get(0) instanceof ScheduleFragment) {
                if (doubleBackToExitPressedOnce) {
                    finish();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Нажмите еще раз, чтобы выйти", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            } else {
                super.onBackPressed();
            }
        }
    //}
}