package com.example.eshopproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class AdministratorManagement extends AppCompatActivity {
    TextView textView;
    Handler handler;
    final ShopsFragment defaultFragment = new ShopsFragment();
    public BottomNavigationView bottomNavigationView;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_management);
        bottomNavigationView = findViewById(R.id.administrator_bottomnvigationtoolbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.administrator_fragment_container, defaultFragment).commit();
        //Context context = AdministratorManagement.this;
        //Intent serviceIntent = new Intent(context, NotificationService.class);

        //getApplicationContext().startService(serviceIntent);


        /*
        handler = new Handler(getApplicationContext().getMainLooper());
        //final String data = null;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //final String data = getCategories();
                final String data = addexampleCategory().toString();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("MESSAGE", "re broooooooooooooooooooo");
                        //fillTv(data);
                        textView = findViewById(R.id.categoriestv);
                        textView.setText(data);


                    }
                });



            }


        });
        thread.start();

*/





    }

    private JSONObject addCategory(JSONObject requestbody) throws IOException, JSONException {
        RestClient restClient = new RestClient();
        return restClient.doPost("http://192.168.1.70/ptyxiaki/index.php/api/v1/Categories", requestbody);
    }

    private JSONObject addexampleCategory() {
        JSONObject object = new JSONObject();
        try {
            object.put("categoryname","autokolita");
            JSONObject jsonObject = addCategory(object);
            return jsonObject;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFrag = null;
                    switch (menuItem.getItemId()) {
                        case R.id.nav_managers : selectedFrag = new ManagersFragment(); break;
                        case R.id.nav_server_details : selectedFrag = new ServersFragment(); break;
                        case R.id.nav_sql : selectedFrag = new SQLFragment(); break;
                        case R.id.nav_shops : selectedFrag = new ShopsFragment(); break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.administrator_fragment_container, selectedFrag).commit();
                    return true;
                }
            };
}
