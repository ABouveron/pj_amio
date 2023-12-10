package fr.abouveron.projectamio;

public class AppContext {
    private static MainActivity mainActivity;

    public static void setMainActivity(MainActivity activity) {
        mainActivity = activity;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }
}
