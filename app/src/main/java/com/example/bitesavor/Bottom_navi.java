package com.example.bitesavor;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class Bottom_navi extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    AboutUsFragment aboutUsFragment = new AboutUsFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    BookFragment bookFragment = new BookFragment();
    HomeFragment homeFragment = new HomeFragment();
    DiscoverFragment discoverFragment = new DiscoverFragment();
    CartFragment cartFragment = new CartFragment();

    DBHelper dbHelper;
    TextView navName;
    TextView navEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navi);

        dbHelper = new DBHelper(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;

                    case R.id.discover:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, discoverFragment).commit();
                        return true;
                    case R.id.cart:

                        getSupportFragmentManager().beginTransaction().replace(R.id.container, cartFragment).commit();
                        return true;
                    case R.id.book:

                        getSupportFragmentManager().beginTransaction().replace(R.id.container,bookFragment).commit();
                        return true;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                        return true;
                }
                return false;
            }
        });

        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);

        drawerLayout.setScrimColor(Color.TRANSPARENT);

        dbHelper = new DBHelper(this);

        Cursor cursor = dbHelper.getUser();
        if (cursor.moveToFirst()) {
            navName = navigationView.getHeaderView(0).findViewById(R.id.navName);
            navEmail = navigationView.getHeaderView(0).findViewById(R.id.navEmail);

            navName.setText("" + cursor.getString(0));
            navEmail.setText("" + cursor.getString(2));
        }

        cursor.close();

        navigationView.setCheckedItem(R.id.home);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_logout:
                        dbHelper.clearSession();
                        Intent intent = new Intent(getApplicationContext(), login.class);
                        startActivity(intent);
                        break;
                    case R.id.us:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, aboutUsFragment).commit();
                        break;
                }


                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        toolbar.setNavigationIcon(R.drawable.profile);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });



    }


}
