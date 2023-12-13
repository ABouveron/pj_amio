package fr.abouveron.projectamio;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import fr.abouveron.projectamio.Utilities.Email;
import fr.abouveron.projectamio.Utilities.JsonResults;
import fr.abouveron.projectamio.Utilities.JsonResultsManager;
import fr.abouveron.projectamio.Utilities.LightsManager;
import fr.abouveron.projectamio.Utilities.MoteLight;
import fr.abouveron.projectamio.Utilities.Notification;
import fr.abouveron.projectamio.Utilities.Vibrator;

public class WebService extends AsyncTask<String, Void, String> {

    private final WeakReference<Context> contextReference;

    public WebService(Context context) {
        this.contextReference = new WeakReference<>(context);
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

        Log.d("MainActivity", "WebService result: " + result);
        JsonResults jsonResults = new Gson().fromJson(result, JsonResults.class);
        JsonResultsManager.addJsonResult(jsonResults);

        int ctr = 0;
        for (MoteLight s : jsonResults.data){
            Log.d("MainActivity", String.format("%s at %s: %s: %s", s.getLabel(), s.getTimestamp(), s.getValue(), s.getState()));
            Calendar rightNow = Calendar.getInstance();
            int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
            int currentMinute = rightNow.get(Calendar.MINUTE);
            if (currentHour >= 18 && currentHour < 23 || (currentHour == 23 && currentMinute == 0)) {
                int nbResults = JsonResultsManager.getJsonResults().size();
                if (nbResults >= 2) {
                    double previousValue = JsonResultsManager.getJsonResults().get(nbResults - 2).data.get(ctr).getValue();
                    if (previousValue >= (s.getValue() + 50) || previousValue <= (s.getValue() - 50)) {
                        new Notification("Changement de luminosité", "Une des motes a repéré un changement brutal de luminosité.", contextReference.get()).send();
                        new Vibrator(contextReference.get()).vibrate(1000);
                        new Email("xyz@example.com", "test", "testText", contextReference.get()).send();
                    }
                    ctr++;
                }
            }
        }

        if (AppContext.getMainActivity() != null) {
            ctr = 0;
            for (MoteLight s : jsonResults.data) {
                new LightsManager().getStringLightsValues().get(ctr).setText(String.valueOf(s.getValue()));
                if (s.getState()) {
                    new LightsManager().getStringLights().get(ctr).setTextColor(Color.parseColor("#FF0000"));
                    new LightsManager().getStringLightsValues().get(ctr).setTextColor(Color.parseColor("#FF0000"));
                }
                Log.d("MainActivity", "LightsManager: " + new LightsManager().getStringLights().get(ctr).getTextColors().toString());
                ctr++;
            }
        }
    }
}
