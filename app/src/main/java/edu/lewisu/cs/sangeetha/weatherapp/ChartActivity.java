package edu.lewisu.cs.sangeetha.weatherapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        LineChart chart = (LineChart) findViewById(R.id.chart);
        chart.setDescription("Hourly Temperatures");
        Intent intent = getIntent();
        ArrayList<String> times = intent.getExtras().getStringArrayList("times");
        ArrayList<Integer> temps = intent.getExtras().getIntegerArrayList("temps");

        ArrayList<Entry> tempData = new ArrayList<Entry>();
        for (int i = 0; i < temps.size(); i++) {
            Entry e = new Entry(temps.get(i), i);
            tempData.add(e);
        }

        LineDataSet tempSet = new LineDataSet(tempData, "Temperatures");
        tempSet.setColor(Color.RED);
        tempSet.setAxisDependency((YAxis.AxisDependency.LEFT));
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setStartAtZero(false);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(tempSet);
        LineData data = new LineData(times,dataSets);

        chart.setData(data);
        chart.invalidate();

    }


}
