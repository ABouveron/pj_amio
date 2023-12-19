package fr.abouveron.projectamio;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

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
import fr.abouveron.projectamio.Utilities.TimePickerPreference;
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
                AppContext.getMainActivity().runOnUiThread(() -> Toast.makeText(AppContext.getMainActivity(), "Erreur lors de la récupération des données.", Toast.LENGTH_SHORT).show());

                Log.d("MainActivity", "Erreur " + connection.getResponseCode() + " lors de la récupération des données.");

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
            AppContext.getMainActivity().runOnUiThread(() -> Toast.makeText(AppContext.getMainActivity(), "Erreur lors de la récupération des données.", Toast.LENGTH_SHORT).show());
            e.printStackTrace();
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        if (contextReference.get() == null) {
            AppContext.getMainActivity().runOnUiThread(() -> Toast.makeText(AppContext.getMainActivity(), "Erreur lors de la récupération des données.", Toast.LENGTH_SHORT).show());
            return;
        }
        if (result == null) {
            AppContext.getMainActivity().runOnUiThread(() -> Toast.makeText(AppContext.getMainActivity(), "Erreur lors de la récupération des données.", Toast.LENGTH_SHORT).show());
            return;
        }
        super.onPostExecute(result);

        Log.d("MainActivity", "WebService result: " + result);
        JsonResults jsonResults = new Gson().fromJson(result, JsonResults.class);
        JsonResultsManager.addJsonResult(jsonResults);

        int ctr = 0;
        if (jsonResults.data.isEmpty()) {
            AppContext.getMainActivity().runOnUiThread(() -> Toast.makeText(AppContext.getMainActivity(), "Erreur lors de la récupération des données.", Toast.LENGTH_SHORT).show());
            Log.d("MainActivity", "WebService result is empty");
        }
        else {
            for (MoteLight s : jsonResults.data){
                Log.d("MainActivity", String.format("%s at %s: %s: %s", s.getLabel(), s.getTimestamp(), s.getValue(), s.getState()));
                Calendar rightNow = Calendar.getInstance();
                int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
                int currentMinute = rightNow.get(Calendar.MINUTE);
                int currentDay = rightNow.get(Calendar.DAY_OF_WEEK);
                int startHour;
                int startMinute;
                int endHour;
                int endMinute;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(contextReference.get());
                switch (currentDay) {
                    case Calendar.MONDAY:
                        startHour = TimePickerPreference.getHour(preferences.getString("monday_start_time", "18:00"));
                        startMinute = TimePickerPreference.getMinute(preferences.getString("monday_start_time", "18:00"));
                        endHour = TimePickerPreference.getHour(preferences.getString("monday_end_time", "18:00"));
                        endMinute = TimePickerPreference.getMinute(preferences.getString("monday_end_time", "18:00"));

                        if ((currentHour > startHour || (currentHour == startHour && currentMinute >= startMinute))
                                && (currentHour < endHour || (currentHour == endHour && currentMinute <= endMinute))) {
                            verificationJson(s, ctr);
                        }
                        break;
                    case Calendar.TUESDAY:
                        startHour = TimePickerPreference.getHour(preferences.getString("tuesday_start_time", "18:00"));
                        startMinute = TimePickerPreference.getMinute(preferences.getString("tuesday_start_time", "18:00"));
                        endHour = TimePickerPreference.getHour(preferences.getString("tuesday_end_time", "18:00"));
                        endMinute = TimePickerPreference.getMinute(preferences.getString("tuesday_end_time", "18:00"));

                        if ((currentHour > startHour || (currentHour == startHour && currentMinute >= startMinute))
                                && (currentHour < endHour || (currentHour == endHour && currentMinute <= endMinute))) {
                            verificationJson(s, ctr);
                        }
                        break;
                    case Calendar.WEDNESDAY:
                        startHour = TimePickerPreference.getHour(preferences.getString("wednesday_start_time", "18:00"));
                        startMinute = TimePickerPreference.getMinute(preferences.getString("wednesday_start_time", "18:00"));
                        endHour = TimePickerPreference.getHour(preferences.getString("wednesday_end_time", "18:00"));
                        endMinute = TimePickerPreference.getMinute(preferences.getString("wednesday_end_time", "18:00"));

                        if ((currentHour > startHour || (currentHour == startHour && currentMinute >= startMinute))
                                && (currentHour < endHour || (currentHour == endHour && currentMinute <= endMinute))) {
                            verificationJson(s, ctr);
                        }
                        break;
                    case Calendar.THURSDAY:
                        startHour = TimePickerPreference.getHour(preferences.getString("thursday_start_time", "18:00"));
                        startMinute = TimePickerPreference.getMinute(preferences.getString("thursday_start_time", "18:00"));
                        endHour = TimePickerPreference.getHour(preferences.getString("thursday_end_time", "18:00"));
                        endMinute = TimePickerPreference.getMinute(preferences.getString("thursday_end_time", "18:00"));

                        if ((currentHour > startHour || (currentHour == startHour && currentMinute >= startMinute))
                                && (currentHour < endHour || (currentHour == endHour && currentMinute <= endMinute))) {
                            verificationJson(s, ctr);
                        }
                        break;
                    case Calendar.FRIDAY:
                        startHour = TimePickerPreference.getHour(preferences.getString("friday_start_time", "18:00"));
                        startMinute = TimePickerPreference.getMinute(preferences.getString("friday_start_time", "18:00"));
                        endHour = TimePickerPreference.getHour(preferences.getString("friday_end_time", "18:00"));
                        endMinute = TimePickerPreference.getMinute(preferences.getString("friday_end_time", "18:00"));

                        if ((currentHour > startHour || (currentHour == startHour && currentMinute >= startMinute))
                                && (currentHour < endHour || (currentHour == endHour && currentMinute <= endMinute))) {
                            verificationJson(s, ctr);
                        }
                        break;
                    case Calendar.SATURDAY:
                        startHour = TimePickerPreference.getHour(preferences.getString("saturday_start_time", "18:00"));
                        startMinute = TimePickerPreference.getMinute(preferences.getString("saturday_start_time", "18:00"));
                        endHour = TimePickerPreference.getHour(preferences.getString("saturday_end_time", "18:00"));
                        endMinute = TimePickerPreference.getMinute(preferences.getString("saturday_end_time", "18:00"));

                        if ((currentHour > startHour || (currentHour == startHour && currentMinute >= startMinute))
                                && (currentHour < endHour || (currentHour == endHour && currentMinute <= endMinute))) {
                            verificationJson(s, ctr);
                        }
                        break;
                    case Calendar.SUNDAY:
                        startHour = TimePickerPreference.getHour(preferences.getString("sunday_start_time", "18:00"));
                        startMinute = TimePickerPreference.getMinute(preferences.getString("sunday_start_time", "18:00"));
                        endHour = TimePickerPreference.getHour(preferences.getString("sunday_end_time", "18:00"));
                        endMinute = TimePickerPreference.getMinute(preferences.getString("sunday_end_time", "18:00"));

                        if ((currentHour > startHour || (currentHour == startHour && currentMinute >= startMinute))
                                && (currentHour < endHour || (currentHour == endHour && currentMinute <= endMinute))) {
                            verificationJson(s, ctr);
                        }
                        break;
                }
                ctr++;
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
                ctr++;
            }
        }
    }

    protected void verificationJson(MoteLight s, int ctr) {
        int nbResults = JsonResultsManager.getJsonResults().size();
        if (nbResults >= 2) {
            double previousValue = JsonResultsManager.getJsonResults().get(nbResults - 2).data.get(ctr).getValue();
            if (previousValue >= (s.getValue() + 50) || previousValue <= (s.getValue() - 50)) {
                new Notification("Changement de luminosité", String.format("%s: %s a repéré un changement brutal de luminosité.", s.getLabel(), s.getMote()), contextReference.get()).send();
                new Vibrator(contextReference.get()).vibrate(1000);
                String receiver = PreferenceManager.getDefaultSharedPreferences(contextReference.get()).getString("email", "xyz@example.com");
                new Email(receiver, "test", "testText", contextReference.get()).send();
            }
        }
    }
}
