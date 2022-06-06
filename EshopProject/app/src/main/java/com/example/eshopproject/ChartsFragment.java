package com.example.eshopproject;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChartsFragment extends Fragment {

    BarChart barChart;
    //ArrayList<BarEntry> barEntryArrayList;
    //ArrayList<String> labelsNames;
    public ChartsFragment() {
    }

    public ChartsFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manager_chart_fragment, container, false);
        barChart = view.findViewById(R.id.barChart);

        BarDataSet barDataSet = new BarDataSet(barValues(), "DataSet 1");

        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        Description description = new Description();
        description.setText("Days");
        barChart.setDescription(description);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        //we need to set XAXIS value formatter
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter());

        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        xAxis.setGranularity(1f);
        xAxis.setLabelCount(barValues().size());
        xAxis.setLabelRotationAngle(270);
        barChart.animateY(2000);
        barChart.invalidate();

        getProfitPerDate();
        fillBarChart(barChart);



        return view;
    }
    private ArrayList<BarEntry> barValues() {
        ArrayList<BarEntry> data = new ArrayList<BarEntry>();
        data.add(new BarEntry(0,3));
        data.add(new BarEntry(1,2));
        data.add(new BarEntry(2,1));
        data.add(new BarEntry(3,3));
        return data;
    }

    private void fillBarChart(BarChart barChart) {
        Log.d("Heyyy", "Hashmap extracted");
        getProfitPerDate().entrySet().forEach(stringDoubleEntry -> { Log.d(stringDoubleEntry.getKey(), " reeeee" + stringDoubleEntry.getValue());});
    }
    private HashMap <String, Double> getProfitPerDate () {
        HashMap <String, Double> stringIntegerHashMap = new HashMap <String, Double>();
        RestClient restClient = new RestClient();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                     JSONArray ordersJsonArray = restClient.doGetRequest("https://192.168.1.70/ptyxiaki/index.php/api/v1/Customers/Orders/All");
                    Gson gson = new Gson();
                    final Order[] ordersArray = gson.fromJson(ordersJsonArray.toString(), Order[].class);
                    for (Order o: ordersArray) {
                        //Log.d("Order", "===>" + o.toString());
                        //Log.d("User detail", o.getUser().toString());
                        //Log.i("Order date", o.getOrderTime().split(" ")[0]);
                        double sum = 0 ;
                        for (Product p: o.getProducts()) {
                            sum += p.getCost();
                        }
                        stringIntegerHashMap.put(o.getOrderTime().split(" ")[0], sum);

                       // Log.i("Total cost", "" + sum);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Log.d("HASHMAP SIZE", "" + stringIntegerHashMap.size());
        return stringIntegerHashMap;
    }

}
