package fr.abouveron.projectamio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.os.Handler;
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
                Toast.makeText(
                        getApplicationContext(),
                        Integer.toString(temps),
                        Toast.LENGTH_SHORT
                ).show();
                Log.d("MainService", Integer.toString(temps));
                temps += 5;

                // Repeat after 5 seconds
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
