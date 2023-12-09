package fr.abouveron.projectamio;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import fr.abouveron.projectamio.JsonModel.JsonResult;
import fr.abouveron.projectamio.JsonModel.SensorsData;

public class MainActivity extends ComponentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
        Intent serviceIntent = new Intent(this, MainService.class);

        BroadcastReceiver br = new MyBootBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
        boolean listenToBroadcastsFromOtherApps = true;
        int receiverFlags = listenToBroadcastsFromOtherApps
                ? ContextCompat.RECEIVER_EXPORTED
                : ContextCompat.RECEIVER_NOT_EXPORTED;
        ContextCompat.registerReceiver(getApplicationContext(), br, filter, receiverFlags);

        getSharedPreferences("settings", MODE_PRIVATE)
                .edit()
                .putBoolean("checkboxState", false)
                .apply();

        TextView stringTV2 = findViewById(R.id.TV2);
        findViewById(Switch.class, R.id.leftButton)
                .setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        Log.d("MainActivity", "leftIsChecked");
                        startService(serviceIntent);
                        stringTV2.setText(getString(R.string.TV2_str_on));
                    } else {
                        Log.d("MainActivity", "leftIsNotChecked");
                        stopService(serviceIntent);
                        stringTV2.setText(getString(R.string.TV2_str_off));
                    }
                });

        findViewById(Switch.class, R.id.rightButton)
                .setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        Log.d("MainActivity", "rightIsChecked");
                        startService(serviceIntent);
                    } else {
                        Log.d("MainActivity", "rightIsNotChecked");
                        stopService(serviceIntent);
                    }
                });

        findViewById(CheckBox.class, R.id.checkbox)
                .setOnCheckedChangeListener((buttonView, isChecked) -> {
                    Log.d("MainActivity", "SharedPreferences:checkboxState:" + isChecked);
                    getSharedPreferences("settings", MODE_PRIVATE)
                            .edit()
                            .putBoolean("checkboxState", isChecked)
                            .apply();
                });

        TextView stringLight = findViewById(R.id.WebServiceDisplayValue);
        findViewById(Button.class, R.id.buttonWebService).setOnClickListener(v -> {
            try {
                String result = new WebService().execute().get();
                Log.d("MainActivity", "WebService result: " + result);
                JsonResult jsonResult = new Gson().fromJson(result, JsonResult.class);
                for (SensorsData s : jsonResult.data){
                    Log.d("MainActivity", String.format("%s at %s: %s", s.label, s.timestamp, s.value));
                }
                stringLight.setText(String.valueOf(jsonResult.data.get(0).value));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Intent serviceIntent = new Intent(this, MainService.class);
        stopService(serviceIntent);
        super.onDestroy();
    }

    private <T> T findViewById(Class<T> viewClass, int id) {
        return viewClass.cast(findViewById(id));
    }
}
