package fr.abouveron.projectamio;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.abouveron.projectamio.JsonModel.JsonResult;
import fr.abouveron.projectamio.JsonModel.SensorsData;

public class WebService extends AsyncTask<String, Void, String> {
    private final WeakReference<MainActivity> activityReference;

    public WebService(MainActivity activity) {
        this.activityReference = new WeakReference<>(activity);
    }

    @Override
    protected String doInBackground(String... params) {
        String urlstring = "http://iotlab.telecomnancy.eu:8080/iotlab/rest/data/1/light1/last";
        URL url;
        StringBuilder result = new StringBuilder();

        try {
            url = new URL(urlstring);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Log.d("MainActivity", urlstring + " Response Code : " + connection.getResponseCode());

            if (connection.getResponseCode() != 200) {
                return "Error " + connection.getResponseCode();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        MainActivity activity = activityReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }

        TextView stringLight0 = activity.findViewById(R.id.WebServiceDisplayValue0);
        TextView stringLight1 = activity.findViewById(R.id.WebServiceDisplayValue1);
        TextView stringLight2 = activity.findViewById(R.id.WebServiceDisplayValue2);
        TextView stringLight3 = activity.findViewById(R.id.WebServiceDisplayValue3);

        Log.d("MainActivity", "WebService result: " + result);
        JsonResult jsonResult = new Gson().fromJson(result, JsonResult.class);
        for (SensorsData s : jsonResult.data){
            Log.d("MainActivity", String.format("%s at %s: %s", s.label, s.timestamp, s.value));
        }

        stringLight0.setText(String.valueOf(jsonResult.data.get(0).value));
        stringLight1.setText(String.valueOf(jsonResult.data.get(1).value));
        stringLight2.setText(String.valueOf(jsonResult.data.get(2).value));
        stringLight3.setText(String.valueOf(jsonResult.data.get(3).value));
    }
}
