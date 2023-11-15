package fr.abouveron.projectamio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log


class MyBootBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("MyBootBroadcastReceiver", "onReceive")

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val serviceIntent = Intent(context, MainService::class.java)
            val settings = context.getSharedPreferences("settings", MODE_PRIVATE)
            val checkboxState = settings.getBoolean("checkboxState", false)
            if (checkboxState) {
                context.startService(serviceIntent)
            }
            Log.d("MyBootBroadcastReceiver", "startService : $checkboxState")
        }
    }
}