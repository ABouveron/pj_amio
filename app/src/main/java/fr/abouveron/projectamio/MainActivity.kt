package fr.abouveron.projectamio

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.Switch
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accueil)
        val serviceIntent = Intent(this, MainService::class.java)

        val br: BroadcastReceiver = MyBootBroadcastReceiver()
        val filter = IntentFilter(Intent.ACTION_BOOT_COMPLETED)
        val listenToBroadcastsFromOtherApps = true
        val receiverFlags = if (listenToBroadcastsFromOtherApps) {
            ContextCompat.RECEIVER_EXPORTED
        } else {
            ContextCompat.RECEIVER_NOT_EXPORTED
        }
        ContextCompat.registerReceiver(applicationContext, br, filter, receiverFlags)

        val settings = getSharedPreferences("settings", MODE_PRIVATE)
        val editor = settings.edit()
        editor.putBoolean("checkboxState", false)
        editor.apply()

        val stringTV2 = findViewById<TextView>(R.id.TV2)
        findViewById<Switch>(R.id.leftButton)
            .setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    Log.d("MainActivity", "leftIsChecked")
                    startService(serviceIntent)
                    stringTV2.text = getString(R.string.TV2_str_on)
                } else {
                    Log.d("MainActivity", "leftIsNotChecked")
                    stopService(serviceIntent)
                    stringTV2.text = getString(R.string.TV2_str_off)
                }
            }

        findViewById<Switch>(R.id.rightButton)
            .setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    Log.d("MainActivity", "rightIsChecked")
                    startService(serviceIntent)
                } else {
                    Log.d("MainActivity", "rightIsNotChecked")
                    stopService(serviceIntent)
                }
            }

        findViewById<CheckBox>(R.id.checkbox)
            .setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    Log.d("MainActivity", "checkboxIsChecked")
                    editor.putBoolean("checkboxState", true)
                } else {
                    Log.d("MainActivity", "checkboxIsNotChecked")
                    editor.putBoolean("checkboxState", false)
                }
                editor.apply()
                Log.d("MainActivity", "SharedPreferences:checkboxState:${findViewById<CheckBox>(R.id.checkbox).isChecked}")
            }

        findViewById<Button>(R.id.buttonWebService).setOnClickListener {
            var result = WebService().execute().get()
            Log.d("MainActivity", "WebService result: $result")
        }
    }

    override fun onDestroy() {
        val serviceIntent = Intent(this, MainService::class.java)
        stopService(serviceIntent)
        super.onDestroy()
    }
}