package com.example.eshopproject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ManagerActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        drawerLayout = findViewById(R.id.manager_drawer_layout);
        navigationView = findViewById(R.id.managerActivityNavigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("Item id ","Navigation click item id " + item.getItemId());
                switch (item.getItemId()) {
                    case R.id.nav_products:
                        Log.d("NAV====", "Products");

                        getSupportFragmentManager().beginTransaction().replace(R.id.manager_fragment_container, new AddProductFragment()).commitNow();
                        break;
                    case R.id.nav_customers: Log.d("Item id ","Navigation click item id " + item.getItemId());
                        Log.d("NAV ====", "Customers");
                        break;
                    case R.id.nav_reports:
                        Log.d("Item id ","Navigation click item id " + item.getItemId());
                        Log.d("NAV ====", "Reports");
                        getSupportFragmentManager().beginTransaction().replace(R.id.manager_fragment_container, new ChartsFragment()).commitNow();
                        break;
                    case R.id.nav_about: Log.d("Item id ","Navigation click item id " + item.getItemId());
                        Log.d("NAV ====", " about");
                    break;
                    case R.id.nav_logout:

                        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                        Log.d("Logout", " triggered");
                        Log.d("Tokken retried", saved_values.getString("userTokken", null));

                        boolean logout_successfull = false;
                        try {

                            logout_successfull = logout(saved_values.getString("userTokken", null));

                        } catch (InterruptedException i) {

                            logout_successfull = false;
                            i.printStackTrace();

                        } catch (JSONException j) {

                            logout_successfull = false;
                            j.printStackTrace();

                        }


                        break;
                }

                navigationView.getMenu().getItem(6).setChecked(true);
                item.setChecked(true);
                drawerLayout.closeDrawers();
                Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

    private boolean logout(String tokken) throws JSONException, InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final JSONObject[] jsonObject = new JSONObject[1];

        jsonObject[0].put("userTokken", tokken);

        HandlerThread thread = new HandlerThread("NetworkThread") {
            @Override
            public void run() {
                RestClient restClient = new RestClient();
                try {

                    restClient.doPost("http://192.168.1.70/ptyxiaki/index.php/api/v1/Employees/Sessions", jsonObject[0]);

                } catch (IOException i) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(),"Could not connect to server", Toast.LENGTH_LONG);
                        }
                    });


                } catch (JSONException j) {

                }

                latch.countDown();
            }
        };
        thread.run();
        latch.await();
        return true;
    }
}
