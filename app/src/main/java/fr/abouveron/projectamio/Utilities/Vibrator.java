package fr.abouveron.projectamio.Utilities;

import android.content.Context;

public class Vibrator {
    private final android.os.Vibrator vibrator;
    private final Context context;

    public Vibrator(Context context) {
        this.context = context;
        vibrator = (android.os.Vibrator) this.context.getSystemService(android.content.Context.VIBRATOR_SERVICE);
    }

    public void vibrate(int duration) {
        vibrator.vibrate(duration);
    }
}
