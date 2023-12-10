package fr.abouveron.projectamio;

import static fr.abouveron.projectamio.NotificationTemplate.PERMISSION_REQUEST_CODE;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends ComponentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppContext.setMainActivity(this);
        NotificationTemplate.createNotificationChannel(this);
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

        findViewById(Button.class, R.id.buttonWebService).setOnClickListener(v -> {
            try {
                new WebService().execute();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                NotificationTemplate.createNotification(
                        this,
                        "Nouveau message",
                        "Vous avez un nouveau message."
                );
            } else {
                Log.d("MainActivity", "Permission denied");
            }
        }
    }
}
