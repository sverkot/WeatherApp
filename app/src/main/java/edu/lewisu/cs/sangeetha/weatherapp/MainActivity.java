package edu.lewisu.cs.sangeetha.weatherapp;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends ListActivity {

    ArrayList<String> times = new ArrayList<String>();
    ArrayList<Integer> temps = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadWeather task = new DownloadWeather();
        task.execute("http://api.wunderground.com/api/58d80f27f53bf460/hourly10day/q/TX/Dallas.json");

    }

    public void buttonClick(View v) {

        Intent showChart = new Intent(MainActivity.this,ChartActivity.class);
        showChart.putStringArrayListExtra("times", times);
        showChart.putIntegerArrayListExtra("temps", temps);

        startActivity(showChart);
    }

    private class DownloadWeather extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(MainActivity.this, "", "Downloading Weather. Please wait...", true, false);

        }


        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... strings) {

            String jsonData = "";
            ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> hourForecast;
            String time;
            String temp;
            //used to find today's forecast
            Calendar now = Calendar.getInstance();
            int today = now.get(Calendar.DATE);
            try {

                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line = "";
                line = bufferedReader.readLine();
                while (line != null) {
                    jsonData += line;
                    line = bufferedReader.readLine();

                }
                //parse data
                JSONObject jsonObject = new JSONObject(jsonData);

                JSONArray forecasts = jsonObject.getJSONArray("hourly_forecast");

                for (int i = 0; i < forecasts.length(); i++) {

                    JSONObject forecast = forecasts.getJSONObject(i);
                    JSONObject timeObject = forecast.getJSONObject("FCTTIME");
                    int forecastDate = Integer.parseInt(timeObject.getString("mday"));
                    if (forecastDate == today) {
                        time = (forecast.getJSONObject("FCTTIME")).getString("civil");
                        temp = (forecast.getJSONObject("temp")).getString("english");

                        hourForecast = new HashMap<String, String>();
                        hourForecast.put("time", time);
                        hourForecast.put("temp", temp);
                        results.add(hourForecast);

                    }
                }
                return results;

            } catch (Exception e) {
                Log.e("error", e.toString());

            }
            return null;
        }
        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> hashMaps) {

            for(HashMap<String,String> hashMap :hashMaps)   {
                times.add(hashMap.get("time"));
                temps.add(Integer.parseInt(hashMap.get("temp")));
            }
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this,
                    hashMaps,
                    R.layout.row,
                    new String[]{"time","temp"},
                    new int[]{R.id.timeText,R.id.tempText});
            MainActivity.this.setListAdapter(adapter);

            if(dialog.isShowing())  {
                dialog.dismiss();
            }


        }

    }


}
