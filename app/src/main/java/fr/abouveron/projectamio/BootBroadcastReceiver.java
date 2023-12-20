package fr.abouveron.projectamio;

import static android.content.Context.MODE_PRIVATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MyBootBroadcastReceiver", "onReceive");
        if (context == null) return;
       // if (!MainActivity.getBootActivatedInConfig(context)) return;

        Intent serviceIntent = new Intent(context, MainService.class);
        context.startForegroundService(serviceIntent);
        Toast.makeText(context, "Service AMIO Lancé", Toast.LENGTH_LONG).show();


        /*if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            SharedPreferences settings = context.getSharedPreferences("settings", MODE_PRIVATE);
            boolean startAtBoot = settings.getBoolean("startAtBoot", false);

            if (startAtBoot) {
                Intent serviceIntent = new Intent(context, MainService.class);
                context.startService(serviceIntent);
            }*/

    }
}
