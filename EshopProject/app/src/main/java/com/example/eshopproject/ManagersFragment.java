package com.example.eshopproject;

import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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


public class ManagersFragment extends Fragment {
    //Shop[] shops;
    String[] shopnames;
    String[] shopids;
    Handler handler;
    Handler handler1;
    private int currentlyDisplayedShopId = -1;
    String currentlyDisplayedShopName = null;
    ListView managersListView;

    public void setCurrentlyDisplayedShopId(int currentlyDisplayedShopId) {
        this.currentlyDisplayedShopId = currentlyDisplayedShopId;
    }

    public void setCurrentlyDisplayedShopName(String currentlyDisplayedShopName) {
        this.currentlyDisplayedShopName = currentlyDisplayedShopName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.administrator_fragment_managers,container,false);
        Spinner spinner = (Spinner) view.findViewById(R.id.shop_spinner);
        managersListView = (ListView) view.findViewById(R.id.managers_listview);

        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (currentlyDisplayedShopId != -1 && currentlyDisplayedShopName != null) {
                    Log.d("TAG", "OOOOOOOOOOOOOOOOOOOOOOOOOOOOK");
                    displayShopManagers(currentlyDisplayedShopId, currentlyDisplayedShopName);
                }

            }
        }).start();
        */


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Shop[] shops = retrieveAllShops();
                shopnames = Arrays.stream(shops).map(s -> s.getShopname()).toArray(size -> new String [shops.length]);
                shopids = Arrays.stream(shops).map(s -> s.getId()).toArray(size -> new String[shops.length]);

                handler = new Handler(getActivity().getApplicationContext().getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,shopnames);

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner.setAdapter(adapter);
                        if (currentlyDisplayedShopName != null) {
                            spinner.setSelection(adapter.getPosition(currentlyDisplayedShopName));
                        }


                    }
                });

            }
        });
        thread.start();



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("SHOPS COMBO BOX SELECTED ID ","position=>" +position + " id " + id);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("SHOP ID SENT ==>", shopids[position]);

                        currentlyDisplayedShopId = Integer.parseInt(shopids[position]);
                        currentlyDisplayedShopName = shopnames[position];

                        displayShopManagers(currentlyDisplayedShopId, currentlyDisplayedShopName);


                    }
                }).start();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {






            }

        });
        FloatingActionButton managersFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.manager_add);
        managersFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentlyDisplayedShopId == -1 || currentlyDisplayedShopName.equals(null)) {
                    Snackbar.make(view,"No shop specified",Snackbar.LENGTH_LONG).show();
                    return;
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("shopname", currentlyDisplayedShopName);
                    bundle.putInt("shopid",currentlyDisplayedShopId);

                    Fragment fragment = new AddManagerFragment();
                    fragment.setArguments(bundle);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

                    fragmentTransaction.replace(R.id.administrator_fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }






            }
        });


        return view;

    }

    private void displayShopManagers(int shopid, String shopname) {




        Employee[] managers = retrieveEmployees("Managers", shopid);




        String[] usernames = managers != null ? Arrays.stream(managers).map(s->s.getUsername()).toArray(size->new String[managers.length]) : new String[]{};
        for ( String s : usernames) {
            Log.d("=====", s);
        }
        handler1 = new Handler(getActivity().getApplicationContext().getMainLooper());
        handler1.post(new Runnable() {
            @Override
            public void run() {

                ManagersAdapter managersAdapter = new ManagersAdapter(getContext(), managers, usernames);
                managersListView.setAdapter(managersAdapter);
                managersListView.destroyDrawingCache();
                managersListView.setVisibility(ListView.INVISIBLE);
                managersListView.setVisibility(ListView.VISIBLE);

            }
        });


    }
    public class ManagersAdapter extends ArrayAdapter<String> {
        Context context;
        String[] endofcotracts;
        String[] roles;
        String[] firstnames;
        String[] lastnames;
        String[] usernames;
        ManagersAdapter(Context c, Employee[] employees, String[] usernames) {
            super(c, R.layout.shop_row,R.id.managers_listview,usernames);
            this.context = c;

            if (employees != null) {
                endofcotracts = Arrays.stream(employees).map(s -> s.getEndOfContract()).toArray(size -> new String[employees.length]);
                roles = Arrays.stream(employees).map(s -> s.getRole()).toArray(size -> new String[employees.length]);
                firstnames = Arrays.stream(employees).map(s -> s.getFirstName()).toArray(size -> new String[employees.length]);
                lastnames = Arrays.stream(employees).map(s -> s.getLastName()).toArray(size -> new String[employees.length]);
            } else {
                endofcotracts = new String[]{};
                roles = new String[]{};
                firstnames = new String[]{};
                lastnames = new String[]{};
            }
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View manager_row = layoutInflater.inflate(R.layout.manager_row, parent,false);
            Log.d("ShopAdapter row loaded:","=>" + position);

            TextView endOfContractTv = manager_row.findViewById(R.id.eoc_tv);
            TextView roleTv = manager_row.findViewById(R.id.role_tv);
            TextView firstNameTv = manager_row.findViewById(R.id.firstnameTV);
            TextView lastNameTv = manager_row.findViewById(R.id.lastnameTV);
            TextView usernameTv = manager_row.findViewById(R.id.usrnm_tv);


            //set resource data to the row
            if (endOfContractTv != null) {
                endOfContractTv.setText(endofcotracts[position]);
            } else {
                Log.d("Row assert status","TextView not found");
            }

            endOfContractTv.setText(endofcotracts[position]);
            roleTv.setText(roles[position]);
            firstNameTv.setText(firstnames[position]);
            lastNameTv.setText(lastnames[position]);
            //usernameTv.setText(usernames[position]);

            //roleTv.setText(roles[position]);
            //firstNameTv.setText(firstnames[position]);


            return manager_row;
        }
    }
    private JSONArray getShops() {
        RestClient rc = new RestClient();
        try {
            return rc.doGetRequest("http://192.168.1.70/ptyxiaki/index.php/api/v1/Administrators/Shops");
        } catch (SocketTimeoutException e) {
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

    private Employee[] retrieveEmployees(String roleOfEmployee , int shopid) {
        Gson gson = new Gson();
        Log.d("Time started retrieving shops", "==>" +System.currentTimeMillis());
        JSONArray employees = geEmployees(roleOfEmployee, shopid);
        Log.d("Time stopped retrieving shops", "==>" +System.currentTimeMillis());
        if (employees == null) {
            return null;
        }
        final Employee[] employeesArray = gson.fromJson(employees.toString(), Employee[].class);

        for (Employee e : employeesArray){
            Log.d("OK", "===================================");
            e.toString();
            Log.d("Employee", e.toString());

        }

        return employeesArray;

    }
    private JSONArray geEmployees(String roleOfEmployee, int shopid) {
        RestClient rc = new RestClient();
        try {
            return rc.doGetRequest("http://192.168.1.70/ptyxiaki/index.php/api/v1/Administrators/Employees/" + roleOfEmployee + "?shopid=" + shopid);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
