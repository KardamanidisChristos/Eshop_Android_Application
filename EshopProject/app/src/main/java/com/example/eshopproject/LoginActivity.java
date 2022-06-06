package com.example.eshopproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.concurrent.CountDownLatch;

public class LoginActivity extends Activity {
    final LoginActivity activity = this;
    Button button;
    ImageButton showPasswordImb;
    EditText passwordEt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button = findViewById(R.id.button_login);
        showPasswordImb = findViewById(R.id.show_password_button);
        passwordEt = findViewById(R.id.passwordET);

        showPasswordImb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passwordEt.setTransformationMethod(null);

                new java.util.Timer().schedule(new java.util.TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                passwordEt.setTransformationMethod(new PasswordTransformationMethod());

                            }
                        });


                    }
                }, 1000);


            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = ((EditText) findViewById(R.id.usernameET)).getText().toString();
                String password = ((EditText) findViewById(R.id.passwordET)).getText().toString();


                String loginType = null;
                boolean login_successfull = false;

                if (username.startsWith("admin")) {
                    loginType = "admin";

                }
                else if (username.startsWith("manager")) {
                    loginType = "manager";
                } else {
                    Toast.makeText(getBaseContext()," Login failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("Username to submit", username);
                Log.d("Password to submit", password);
                Log.d("Login type to submit", loginType);


                try {
                    login_successfull = doLogin(username, password, loginType);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("Login result", "" + login_successfull);


                if (login_successfull) {

                    if(loginType.equals("admin")) {
                        startActivity(new Intent(LoginActivity.this, AdministratorManagement.class));
                        overridePendingTransition( R.anim.left_to_right, R.anim.left_to_right );
                        finish();
                    }
                    if(loginType.equals("manager")) {
                        startActivity(new Intent(LoginActivity.this, ManagerActivity.class));
                        overridePendingTransition( R.anim.left_to_right, R.anim.left_to_right );
                        finish();

                    }
                } else {

                    Toast.makeText(getBaseContext(),"Login not successfull", Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    public LoginActivity() {
        super();
    }


    public boolean doLogin(String username, String password, final String loginType) throws InterruptedException {

        final boolean[] login_successfull = new boolean[1];


        final CountDownLatch latch = new CountDownLatch(1);
        HandlerThread thread = new HandlerThread("NetworkThread") {
            @Override
            public void run() {

                Log.d("Network thread: ", "started");
                try {
                    if (loginType == "admin") {
                        login_successfull[0] = performLogin(username, password,loginType);
                    }

                    if (loginType == "manager") {

                        login_successfull[0] = performLogin(username, password, loginType);;

                    }


                    Log.d("Login returned", "" + login_successfull[0]);

                } catch (JSONException j) {

                    login_successfull[0] = false;
                    j.printStackTrace();
                    Log.d("Login status:", "Unexpected response from server");

                } finally {


                }

                Log.d("Network thread: ", "ended");
                latch.countDown();
            }
        };


        thread.start();
        latch.await();



        return login_successfull[0];

    }
    private boolean performLogin(final String username, final String password, final String loginType) throws JSONException {
        //Log.d("Perform Login:","started");
        //Log.d("LoginType", loginType);

        String uri = loginType.equals("admin") ? "http://192.168.1.70/ptyxiaki/index.php/api/v1/Administrators/Sessions" :"http://192.168.1.70/ptyxiaki/index.php/api/v1/Employees/Sessions";

        RestClient restClient = new RestClient();
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("username",username);
        jsonObject.put("password",password);

        Log.d("credentials", jsonObject.toString());
        Log.d("URI", uri);


        JSONObject jsonObjectResponse = new JSONObject();

        try {
            jsonObjectResponse = restClient.doPost(uri, jsonObject);

        } catch ( IOException i) {

            i.printStackTrace();
            Log.d("IO Exception", " caught");

            return false;

        }

        String tokken = null;

        if (loginType == "admin" && jsonObjectResponse.get("administratorTokken") != null) {

            tokken = (String) jsonObjectResponse.get("administratorTokken");

        } else if (loginType == "manager" && jsonObjectResponse.get("userTokken") != null) {

            tokken =  (String) jsonObjectResponse.get("userTokken");

        }

        Log.d("Tokken", tokken);

        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor=saved_values.edit();
        editor.putString("userTokken", tokken);
        editor.commit();

        if (tokken != null) {

            if (loginType == "admin") {

                editor.putString("administratorTokken", tokken);
                if (editor.commit()) {
                    Log.d("administratorTokken", " saved>" + tokken);
                }

            }
            else if (loginType == "manager") {

                editor.putString("userTokken", tokken);
                if (editor.commit()) {
                    Log.d("userTokken", " saved>" + tokken);
                }

            }

            Log.d("Tokken: ", " tokken saved in shared prefs, return true");

            return true;

        } else  {

            Log.d("Login", "not successull: tokken not received");

            return false;
        }
    }
}
