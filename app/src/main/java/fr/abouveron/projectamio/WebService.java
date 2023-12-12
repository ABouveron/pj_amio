package fr.abouveron.projectamio;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import fr.abouveron.projectamio.JsonModel.JsonResult;
import fr.abouveron.projectamio.JsonModel.SensorsData;

public class WebService extends AsyncTask<String, Void, String> {

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

        MainActivity activity = AppContext.getMainActivity();
        if (activity == null) {
            return;
        }

        TextView stringLight0 = activity.findViewById(R.id.WebServiceDisplay0);
        TextView stringLightValue0 = activity.findViewById(R.id.WebServiceDisplayValue0);
        TextView stringLight1 = activity.findViewById(R.id.WebServiceDisplay1);
        TextView stringLightValue1 = activity.findViewById(R.id.WebServiceDisplayValue1);
        TextView stringLight2 = activity.findViewById(R.id.WebServiceDisplay2);
        TextView stringLightValue2 = activity.findViewById(R.id.WebServiceDisplayValue2);
        TextView stringLight3 = activity.findViewById(R.id.WebServiceDisplay3);
        TextView stringLightValue3 = activity.findViewById(R.id.WebServiceDisplayValue3);

        Log.d("MainActivity", "WebService result: " + result);
        JsonResult jsonResult = new Gson().fromJson(result, JsonResult.class);
        JsonResults.addJsonResult(jsonResult);

        int ctr = 0;
        for (SensorsData s : jsonResult.data){
            Log.d("MainActivity", String.format("%s at %s: %s: %s", s.label, s.timestamp, s.value, s.getState()));
            Calendar rightNow = Calendar.getInstance();
            int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
            int currentMinute = rightNow.get(Calendar.MINUTE);
            if (currentHour >= 18 && currentHour < 23 || (currentHour == 23 && currentMinute == 0)) {
                int nbResults = JsonResults.getJsonResults().size();
                if (nbResults >= 2) {
                    double previousValue = JsonResults.getJsonResults().get(nbResults - 2).data.get(ctr).value;
                    if (previousValue >= (s.value + 50) || previousValue <= (s.value - 50)) {
                        NotificationTemplate.createNotification(AppContext.getMainActivity(), "Changement de luminosité", "Une des motes a repéré un changement brutal de luminosité.");
                        Vibrator v = (Vibrator) AppContext.getMainActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(1000);
                        new Email("xyz@example.com", "test", "testText").send();
                    }
                    ctr++;
                }
            }
        }

        if (jsonResult.data.get(0).getState()) {
            stringLight0.setTextColor(Color.parseColor("#FF0000"));
            stringLightValue0.setTextColor(Color.parseColor("#FF0000"));
        }
        if (jsonResult.data.get(1).getState()) {
            stringLight1.setTextColor(Color.parseColor("#FF0000"));
            stringLightValue1.setTextColor(Color.parseColor("#FF0000"));
        }
        if (jsonResult.data.get(2).getState()) {
            stringLight2.setTextColor(Color.parseColor("#FF0000"));
            stringLightValue2.setTextColor(Color.parseColor("#FF0000"));
        }
        if (jsonResult.data.get(3).getState()) {
            stringLight3.setTextColor(Color.parseColor("#FF0000"));
            stringLightValue3.setTextColor(Color.parseColor("#FF0000"));
        }
        stringLightValue0.setText(String.valueOf(jsonResult.data.get(0).value));
        stringLightValue1.setText(String.valueOf(jsonResult.data.get(1).value));
        stringLightValue2.setText(String.valueOf(jsonResult.data.get(2).value));
        stringLightValue3.setText(String.valueOf(jsonResult.data.get(3).value));
    }
}
