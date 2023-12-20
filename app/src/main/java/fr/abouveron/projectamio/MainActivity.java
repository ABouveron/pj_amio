package fr.abouveron.projectamio;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import fr.abouveron.projectamio.Utilities.Notification;

public class MainActivity extends ComponentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppContext.setMainActivity(this);
        Log.d("MainActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
        Intent serviceIntent = new Intent(this, MainService.class);
        Intent settingsIntent = new Intent(this, SettingsActivity.class);

        BroadcastReceiver br = new BootBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);

        createNotificationChannel();


        @SuppressLint("ResourceType") View view = LayoutInflater.from(this).inflate(R.xml.preferences, null);
        CheckBox checkBox = view.findViewById(R.id.checkbox);

        checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                startForegroundService(new Intent(MainActivity.this, MainService.class));
                startService(serviceIntent);
            }});

        boolean listenToBroadcastsFromOtherApps = true;
        int receiverFlags = listenToBroadcastsFromOtherApps
                ? ContextCompat.RECEIVER_EXPORTED
                : ContextCompat.RECEIVER_NOT_EXPORTED;
        ContextCompat.registerReceiver(this, br, filter, receiverFlags);

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

        findViewById(Button.class, R.id.buttonSettings).setOnClickListener(v -> startActivity(settingsIntent));
    }

    private void createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        "test",
                        "YourChannelName",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                NotificationManager manager = getSystemService(NotificationManager.class);
                if (manager != null) {
                    manager.createNotificationChannel(channel);
                }
            }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Notification.getPERMISSION_REQUEST_CODE()) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new Notification("Nouveau message","Vous avez un nouveau message.", this);
            } else {
                Log.d("MainActivity", "Permission denied");
            }
        }
    }
}
