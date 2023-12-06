package fr.abouveron.projectamio;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
}
