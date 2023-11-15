package fr.abouveron.projectamio

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.*

class MainService : Service() {
    private var temps = 0
    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()
        temps = 0

        job = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        temps.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("MainService", temps.toString())
                    delay(5000) // 5 second delay
                    temps += 5
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MainService", "onStartCommand")
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        Log.d("MainService", "Service destroyed")
    }
}
