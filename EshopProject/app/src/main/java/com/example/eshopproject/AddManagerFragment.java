package com.example.eshopproject;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddManagerFragment extends Fragment {
    Handler handler;
    private int shopid;
    private String shopname;
    String startDate;
    String endDate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.administrator_fragment_addmanager, container, false);
        shopname = getArguments().getString("shopname");
        shopid = getArguments().getInt("shopid");
        TextView textView = (TextView) view.findViewById(R.id.shopName);
        textView.setText(shopname);
        setListeners(view);


        return view;
    }
    public void setListeners(View view) {

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.startOfContractChooser);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        Button button = (Button) view.findViewById(R.id.btn_add_manager);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Employee manager = new Employee();
                manager.setStartOfContract("2020-02-12");
                manager.setEndOfContract("2020-02-12 22:33:35");
                manager.setTitle(((EditText)(view.findViewById(R.id.add_manager_title))).getText().toString());
                manager.setIdentity(((EditText)(view.findViewById(R.id.add_manager_identity))).getText().toString());
                manager.setWorksIn(String.valueOf(shopid));

                manager.setUsername(((EditText)(view.findViewById(R.id.add_manager_username))).getText().toString());
                manager.setFirstName(((EditText)(view.findViewById(R.id.add_manager_first_name))).getText().toString());
                manager.setLastName(((EditText)(view.findViewById(R.id.add_manager_last_name))).getText().toString());
                manager.setPassword(((EditText)(view.findViewById(R.id.add_manager_password))).getText().toString());
                manager.setPrimaryemail(((EditText)(view.findViewById(R.id.add_manager_primary_email))).getText().toString());

                manager.setSecondaryemail(((EditText)(view.findViewById(R.id.add_manager_secondary_email))).getText().toString());
                manager.setAddress(((EditText)(view.findViewById(R.id.add_manager_address))).getText().toString());

                manager.setMobilephone(((EditText)(view.findViewById(R.id.add_manager_mobile_phone))).getText().toString());

                manager.setHomephone(((EditText)(view.findViewById(R.id.add_manager_home_phone))).getText().toString());

                manager.setGender(((EditText)(view.findViewById(R.id.add_manager_gender))).getText().toString());




                Log.d("Employee object data collected by view", manager.toString());

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        boolean addManagerState = true;

                        try {
                            addManager(manager, shopid);

                        } catch (IOException e) {

                            e.printStackTrace();
                            addManagerState = false;

                        } catch (JSONException e) {

                            e.printStackTrace();
                            addManagerState = false;

                        }

                        boolean finalAddManagerState = addManagerState;
                        Log.d("Manager adding state: ", "==>" + addManagerState);


                        handler = new Handler(getActivity().getApplicationContext().getMainLooper());;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("Snackbar thread started", "==>" + finalAddManagerState);

                                if (!finalAddManagerState) {

                                    Snackbar.make(view, "An error occured posting the manager", Snackbar.LENGTH_LONG).show();

                                } else {

                                    Snackbar.make(view, "Manager posted successfully", Snackbar.LENGTH_LONG).show();

                                    ManagersFragment selectedFrag = null;
                                    selectedFrag = new ManagersFragment();
                                    selectedFrag.setCurrentlyDisplayedShopId(shopid);
                                    selectedFrag.setCurrentlyDisplayedShopName(shopname);
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.administrator_fragment_container, selectedFrag).commit();


                                }

                            }
                        });
                    }
                }
                );
                thread.start();
            }
        });



    }
    void addManager(Employee employee, int shopid) throws IOException,JSONException {
        JSONObject object = new JSONObject();

        object.put("endOfContract", employee.getEndOfContract());
        object.put("startOfContract", employee.getStartOfContract());
        object.put("title", employee.getTitle());
        object.put("identity", employee.getIdentity());
        object.put("worksIn", shopid);

        object.put("username", employee.getUsername());
        object.put("firstname", employee.getFirstName());
        object.put("lastname", employee.getLastName());
        object.put("password", employee.getPassword());
        object.put("primaryemail", employee.getPrimaryemail());

        object.put("secondaryemail", employee.getSecondaryemail());
        object.put("address", employee.getAddress());
        object.put("mobilephone", employee.getMobilephone());
        object.put("homephone", employee.getHomephone());
        object.put("gender", employee.getGender());



        Log.d("Manager to be inserted(OBJECT)", employee.toString());
        Log.d("Manager to be inserted(JSONObject)",object.toString());
        RestClient restClient = new RestClient();
        restClient.doPost("http://192.168.1.70/ptyxiaki/index.php/api/v1/Administrators/Employees/Managers", object);


    }

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
            Log.d("Date", sdf.format(myCalendar.getTime()));

        }
    };


}