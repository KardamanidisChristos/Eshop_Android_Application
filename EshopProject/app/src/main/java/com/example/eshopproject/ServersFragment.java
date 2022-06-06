package com.example.eshopproject;


import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;



public class ServersFragment extends Fragment {

    Handler handler;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.administrator_fragment_server,container,false);
        initView(view);
        return view;
    }

    private JSONObject getDetails() throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(1);
        final JSONObject[] jsonObject = new JSONObject[1];

        HandlerThread thread = new HandlerThread("NetworkThread") {
            @Override
            public void run() {
                RestClient restClient = new RestClient();
                try {
                    jsonObject[0] = restClient.doGetRequestSingleObject("http://192.168.1.70/ptyxiaki/index.php/api/v1/ServerDetails");
                    latch.countDown();

                } catch (IOException e) {
                    e.printStackTrace();

                } catch (JSONException j) {
                    j.printStackTrace();
                }
            };
        };

        thread.start();
        latch.await();

        return jsonObject[0];

    }
    private void initView(View view) {

        JSONObject serverDetailsJO = null;
        try {
            serverDetailsJO = getDetails();
        } catch (InterruptedException e) {
            Snackbar.make(view,"Thread error. Please contact development team", BaseTransientBottomBar.LENGTH_LONG);
        }

        TextView apiVersionTV = view.findViewById(R.id.server_details_api_version);
        TextView mysqlVersionTV = view.findViewById(R.id.server_details_mysql_version);
        TextView phpVersionTV = view.findViewById(R.id.server_details_php_version);
        TextView serverIpTV = view.findViewById(R.id.server_details_server_ip);

        try {
            if (serverDetailsJO.equals(null)) {
                Snackbar.make(view, "Error getting server data", BaseTransientBottomBar.LENGTH_LONG);
                return;
            }
            apiVersionTV.setText(serverDetailsJO.get("serverip").toString());
            mysqlVersionTV.setText(serverDetailsJO.get("mysqlversion").toString());
            phpVersionTV.setText(serverDetailsJO.get("phpversion").toString());
            serverIpTV.setText(serverDetailsJO.get("apiversion").toString());

        } catch ( JSONException j){
            j.printStackTrace();

        }
    }
}
