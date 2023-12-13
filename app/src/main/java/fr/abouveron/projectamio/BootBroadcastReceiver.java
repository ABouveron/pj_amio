package fr.abouveron.projectamio;

import static android.content.Context.MODE_PRIVATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MyBootBroadcastReceiver", "onReceive");

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, MainService.class);
            SharedPreferences settings = context.getSharedPreferences("settings", MODE_PRIVATE);
            boolean checkboxState = settings.getBoolean("checkboxState", false);
            if (checkboxState) {
                context.startService(serviceIntent);
            }
            Log.d("MyBootBroadcastReceiver", "startService : " + checkboxState);
        }
    }
}
