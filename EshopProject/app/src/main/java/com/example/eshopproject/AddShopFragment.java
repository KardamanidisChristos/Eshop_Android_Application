package com.example.eshopproject;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class AddShopFragment extends Fragment {
    Handler handler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.administrator_fragment_addshop, container, false);
        setListeners(view);
        return view;
    }

    public void setListeners(View view) {
        Button button = (Button) view.findViewById(R.id.btn_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Shop shop = new Shop();

                shop.setOwnerFirstName(((EditText) view.findViewById(R.id.tv_first_name)).getText().toString());
                shop.setOwnerLastName(((EditText) view.findViewById(R.id.tv_last_name)).getText().toString());
                shop.setAddress(((EditText) view.findViewById(R.id.tv_shop_address)).getText().toString());
                shop.setShopname(((EditText) view.findViewById(R.id.tv_shopname)).getText().toString());

                Log.d("Shop object data collected by view", shop.toString());

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        boolean addShopState = true;

                        try {

                            addShop(shop);

                        } catch (IOException e) {

                                e.printStackTrace();
                                addShopState = false;

                        } catch (JSONException e) {

                            e.printStackTrace();
                            addShopState = false;

                        }

                        boolean finalAddShopState = addShopState;
                        Log.d("Shop adding state: ", "==>" + finalAddShopState);


                        handler = new Handler(getActivity().getApplicationContext().getMainLooper());;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("Snackbar thread started", "==>" + finalAddShopState);

                                if (!finalAddShopState) {

                                    Snackbar.make(view, "An error occured posting the shop", Snackbar.LENGTH_LONG).show();

                                } else {

                                    Snackbar.make(view, "Shop posted successfully", Snackbar.LENGTH_LONG).show();

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void addShop(Shop shop) throws IOException, JSONException {
        JSONObject object = new JSONObject();

        object.put("shopname", shop.getShopname());
        object.put("address", shop.getAddress());
        object.put("firstName", shop.getOwnerFirstName());
        object.put("lastName", shop.getOwnerLastName());

        Log.d("Shop to be inserted(OBJECT)", shop.toString());
        Log.d("Shop to be inserted(JSONObject)",object.toString());
        RestClient restClient = new RestClient();
        restClient.doPost("http://192.168.1.70/ptyxiaki/index.php/api/v1/Administrators/Shops",object);

    }
}
