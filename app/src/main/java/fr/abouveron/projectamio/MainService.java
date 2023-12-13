package fr.abouveron.projectamio;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class MainService extends Service {
    private int temps = 0;
    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate() {
        super.onCreate();
        temps = 0;

        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                new WebService().execute();
                Toast.makeText(
                        getApplicationContext(),
                        Integer.toString(temps),
                        Toast.LENGTH_SHORT
                ).show();
                Log.d("MainService", Integer.toString(temps));

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AppContext.getMainActivity());
                for (String key : preferences.getAll().keySet()) {
                    Log.d("Preferences", key + " -> " + preferences.getAll().get(key));
                }

                temps += 5;
                handler.postDelayed(this, 5000);
            }
        };

        handler.post(runnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MainService", "onStartCommand");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        Log.d("MainService", "Service destroyed");
    }
}
