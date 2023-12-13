package fr.abouveron.projectamio.Utilities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import fr.abouveron.projectamio.AppContext;
import fr.abouveron.projectamio.R;

public class Notification {

    private static final int PERMISSION_REQUEST_CODE = 123;

    private final String CHANNEL_ID = "ChannelID";

    private final String title;
    private final String content;

    public Notification(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void send() {
        createNotificationChannel(AppContext.getMainActivity());
        createNotification(AppContext.getMainActivity());
    }

    public static int getPERMISSION_REQUEST_CODE() {
        return PERMISSION_REQUEST_CODE;
    }

    private void createNotification(Context context) {
        createNotificationChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.png_icon)
                .setContentTitle(this.title)
                .setContentText(this.content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (content != null) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                int NOTIFICATION_ID = 1;
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            } else {
                ActivityCompat.requestPermissions(AppContext.getMainActivity(), new String[]{Manifest.permission.VIBRATE}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Notification Channel";
            String description = "Channel for my notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            if (context != null) {
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
