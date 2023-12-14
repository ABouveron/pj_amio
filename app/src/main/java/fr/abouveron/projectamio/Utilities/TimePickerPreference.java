package fr.abouveron.projectamio.Utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimePickerPreference extends DialogPreference {

    private int lastHour = 0;
    private int lastMinute = 0;
    private TimePicker picker;
    private CharSequence summary;

    public TimePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPositiveButtonText("OK");
        setNegativeButtonText("Annuler");
    }

    @Override
    protected View onCreateDialogView() {
        picker = new TimePicker(getContext());

        picker.setHour(lastHour);
        picker.setMinute(lastMinute);

        picker.setIs24HourView(true);

        return picker;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            lastHour = picker.getHour();
            lastMinute = picker.getMinute();

            String time = String.format("%02d:%02d", lastHour, lastMinute);

            if (callChangeListener(time)) {
                persistString(time);
                setSummary(getSummary());
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        String time;

        if (restorePersistedValue) {
            if (defaultValue == null) {
                time = getPersistedString("12:00");
            } else {
                time = getPersistedString(defaultValue.toString());
            }
        } else {
            time = defaultValue.toString();
        }

        lastHour = getHour(time);
        lastMinute = getMinute(time);
    }

    public static int getHour(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            date = new Date();
        }
        assert date != null;
        return date.getHours();
    }

    public static int getMinute(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            date = new Date();
        }
        assert date != null;
        return date.getMinutes();
    }
}
