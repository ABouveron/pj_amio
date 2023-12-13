package fr.abouveron.projectamio.Utilities;

import fr.abouveron.projectamio.AppContext;

public class Vibrator {
    private final android.os.Vibrator vibrator;

    public Vibrator() {
        vibrator = (android.os.Vibrator) AppContext.getMainActivity().getSystemService(android.content.Context.VIBRATOR_SERVICE);
    }

    public void vibrate(int duration) {
        vibrator.vibrate(duration);
    }
}
