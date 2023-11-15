package fr.abouveron.projectamio

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.coroutineContext

class WebService : AsyncTask<String, Void, String>() {

    public override fun doInBackground(vararg params: String?): String {
        val urlstring = "http://iotlab.telecomnancy.eu:8080/iotlab/rest/data/1/light1/last"
        val url = URL(urlstring)
        var result = ""

        with(url.openConnection() as HttpURLConnection) {
            Log.d("MainActivity", "$urlstring Response Code : $responseCode")

            if (responseCode != 200) {
                return "Error $responseCode"
            }

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    result += line
                }
            }
        }
        return result
    }
}