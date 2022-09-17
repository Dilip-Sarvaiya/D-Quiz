package com.dilip_sarvaiya_700.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.dilip_sarvaiya_700.quiz.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.dilip_sarvaiya_700.quiz.DBQuery.loadTestData;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private BottomNavigationView bottomNavigationView;
    private FrameLayout main_frame;

    private Toolbar toolbar;

    private TextView drawerProfileName,drawerProfileText;

    private ImageView back_arrow;

    @Override
    public void onBackPressed() {
            finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        main_frame = findViewById(R.id.frameLayout);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        toolbar.setTitle("Quiz");

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);


        DrawerLayout drawer = (DrawerLayout)binding.drawerLayout;

        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_account, R.id.nav_leaderboard)
                .setDrawerLayout(drawer)
                .build();

        navigationView.setNavigationItemSelectedListener(this);
        drawerProfileName = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_name);
        drawerProfileText = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_text_img);

        String name = DBQuery.myProfile.getName();


        drawerProfileName.setText(name);

        drawerProfileText.setText(name.toUpperCase().substring(0,1));

        setFragment(new CategoryFragment());
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                //Working code
                switch (item.getItemId())
                {
                    case R.id.nav_home:
                        bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
                        setFragment(new CategoryFragment());
                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_leaderboard:
                        bottomNavigationView.getMenu().findItem(R.id.navigation_leaderboard).setChecked(true);
                        setFragment(new LeaderBoardFragment());
                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_account:
                        bottomNavigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);
                        setFragment(new AccountFragment());
                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_bookmark:
                        Intent intent = new Intent(MainActivity.this,BookmarksActivity.class);
                        startActivity(intent);
                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_share:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                                "https://play.google.com/store/search?q=pub%3A%20Dilip%20Sarvaiya&c=apps&hl=en");
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_contact:
                        setFragment(new ContactusFragment());
                        drawer.closeDrawers();
                        return true;
                }
               drawer.closeDrawers();
                return true;
            }
        });
        Intent intent = getIntent();
        String flag_on = intent.getStringExtra("flag_on");
        String ld = intent.getStringExtra("ld");

        if(flag_on != null)
        {
            navigationView.setCheckedItem(R.id.nav_account);
            bottomNavigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);
            setFragment(new AccountFragment());
        }

        if(ld != null)
        {
            navigationView.setCheckedItem(R.id.nav_leaderboard);
            bottomNavigationView.getMenu().findItem(R.id.navigation_leaderboard).setChecked(true);
            setFragment(new LeaderBoardFragment());
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener  onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                    switch (item.getItemId())
                    {
                        case R.id.navigation_home:
                            setFragment(new CategoryFragment());
                            return true;

                        case R.id.navigation_leaderboard:
                            setFragment(new LeaderBoardFragment());
                            return true;

                        case R.id.navigation_account:
                            setFragment(new AccountFragment());
                            return true;

                    }

                    return false;
                }
            };

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction  = getSupportFragmentManager().beginTransaction();
        transaction.replace(main_frame.getId(),fragment).commit();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {



        return false;
    }

//    @Override
//    public void onBackPressed() {
//        tellFragments();
//        super.onBackPressed();
//    }
//
//    private void tellFragments(){
//
//    }

}