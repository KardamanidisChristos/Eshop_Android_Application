package com.example.eshopproject;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class ShopsFragment extends Fragment {
    ListView listView;
    //String[] shopnames;// = {"KOTSOBOLOS1","KOTSOBOLOS2","KOTSOBOLOS3","KOTSOBOLOS4","KOTSOBOLOS5","KOTSOBOLOS6","KOTSOBOLOS7"};
    //String[] ownerfullnames;// = {"CHRIS1", "CHRIS2","CHRIS3","CHRIS4","CHRIS5","CHRIS6","CHRIS7"};
    //String[] shopaddresses ;// = {"23101","23102","23103", "23104","23105","23106","23107"};
    Handler handler;
    private String CHANNEL_ID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         final View view = inflater.inflate(R.layout.administrator_fragment_shops,container,false);

         //createNotification();
         fillShopListView(view);
         setListeners(view);

        return view;

    }

    public ShopsFragment() {

    }

    private void refreshView(View view) {
        fillShopListView(view);
        setListeners(view);
    }
    private void fillShopListView(View view) {
        Log.d("Thread# ", Thread.currentThread().getName());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Thread# ", Thread.currentThread().getName());

                final Shop[] shops = retrieveAllShops();

                handler = new Handler(getActivity().getApplicationContext().getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Thread# ", Thread.currentThread().getName());
                        listView = (ListView) view.findViewById(R.id.shops_listview);
                        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.shop_progressbar);

                        if (shops != null) {
                            String[] shopnames = Arrays.stream(shops).map(s -> s.getShopname()).toArray(size -> new String[shops.length]);
                            String[] ownerfullnames = Arrays.stream(shops).map(s -> s.getOwnerFirstName() + " " + s.getOwnerLastName()).toArray(size -> new String[shops.length]);
                            String[] shopaddresses  = Arrays.stream(shops).map(s -> s.getAddress()).toArray(size -> new String[shops.length]);
                            progressBar.setVisibility(View.GONE);

                            ShopsAdapter shopsAdapter = new ShopsAdapter(getContext(), shopnames, ownerfullnames, shopaddresses);
                            listView.setAdapter(shopsAdapter);
                        } else {
                            Snackbar.make(view,"Error retrieving shops",Snackbar.LENGTH_LONG).show();

                            listView.setVisibility(View.VISIBLE);
                            ImageButton imageButton = (ImageButton) view.findViewById(R.id.shopsRefreshBtn);
                            imageButton.setVisibility(View.VISIBLE);

                            progressBar.setVisibility(View.GONE);
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    ImageButton imageButton = (ImageButton) view.findViewById(R.id.shopsRefreshBtn);
                                    imageButton.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.VISIBLE);

                                    refreshView(view);
                                }
                            });

                        }


                    }
                });

            }
        });
        thread.start();
    }
    private void setListeners(View view) {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.shop_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Fab triggered:","TRUE");


                Fragment fragment = new AddShopFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

                fragmentTransaction.replace(R.id.administrator_fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        });


    }

    public class ShopsAdapter extends ArrayAdapter<String> {
        Context context;
        String[] rshopnames;
        String[] raddresses;
        String[] rownerfullnames;
        ShopsAdapter(Context c, String shopnames[], String[] addresses, String[] ownerfullnames) {
            super(c, R.layout.shop_row,R.id.shopname,shopnames);
            this.context = c;
            this.rshopnames = shopnames;
            this.raddresses = addresses;
            this.rownerfullnames = ownerfullnames;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View shop_row = layoutInflater.inflate(R.layout.shop_row, parent,false);
            Log.d("ShopAdapter row loaded:","=>" + position);

            TextView shopNameTv = shop_row.findViewById(R.id.shopname);
            TextView ownerFullNameTv = shop_row.findViewById(R.id.ownerfullname);
            TextView ownerTelephoneTv = shop_row.findViewById(R.id.ownertelephone);

            //set resource data to the row
            shopNameTv.setText(rshopnames[position]);
            ownerFullNameTv.setText(rownerfullnames[position]);
            ownerTelephoneTv.setText(raddresses[position]);

            Log.d("Data", " shopname " + rshopnames[position] + " full name" + rownerfullnames[position] + " address" + raddresses[position]);


            return shop_row;
        }
    }
    private JSONArray getShops() {
        RestClient rc = new RestClient();
        try {
            return rc.doGetRequest("http://192.168.1.70/ptyxiaki/index.php/api/v1/Administrators/Shops");
        } catch (SocketTimeoutException  e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Shop[] retrieveAllShops() {
       Gson gson = new Gson();
       Log.d("Time started retrieving shops", "==>" +System.currentTimeMillis());
       JSONArray shops = getShops();
        Log.d("Time stopped retrieving shops", "==>" +System.currentTimeMillis());
        if (shops == null) {
            return null;
        }
       final Shop[] shoparray = gson.fromJson(shops.toString(), Shop[].class);

       return shoparray;

    }


    private void createNotification() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Name";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_icons8_box)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());






    }
}
