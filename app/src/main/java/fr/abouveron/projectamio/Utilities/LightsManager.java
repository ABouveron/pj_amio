package fr.abouveron.projectamio.Utilities;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.abouveron.projectamio.AppContext;
import fr.abouveron.projectamio.R;

public class LightsManager {
    private final TextView stringLight0 = AppContext.getMainActivity().findViewById(R.id.WebServiceDisplay0);
    private final TextView stringLightValue0 = AppContext.getMainActivity().findViewById(R.id.WebServiceDisplayValue0);
    private final TextView stringLight1 = AppContext.getMainActivity().findViewById(R.id.WebServiceDisplay1);
    private final TextView stringLightValue1 = AppContext.getMainActivity().findViewById(R.id.WebServiceDisplayValue1);
    private final TextView stringLight2 = AppContext.getMainActivity().findViewById(R.id.WebServiceDisplay2);
    private final TextView stringLightValue2 = AppContext.getMainActivity().findViewById(R.id.WebServiceDisplayValue2);
    private final TextView stringLight3 = AppContext.getMainActivity().findViewById(R.id.WebServiceDisplay3);
    private final TextView stringLightValue3 = AppContext.getMainActivity().findViewById(R.id.WebServiceDisplayValue3);

    private final List<TextView> stringLights = new ArrayList<TextView>(){
        {
            add(stringLight0);
            add(stringLight1);
            add(stringLight2);
            add(stringLight3);
        }
    };

    private final List<TextView> stringLightsValues = new ArrayList<TextView>(){
        {
            add(stringLightValue0);
            add(stringLightValue1);
            add(stringLightValue2);
            add(stringLightValue3);
        }
    };

    public TextView getStringLight0() {
        return stringLight0;
    }

    public TextView getStringLightValue0() {
        return stringLightValue0;
    }

    public TextView getStringLight1() {
        return stringLight1;
    }

    public TextView getStringLightValue1() {
        return stringLightValue1;
    }

    public TextView getStringLight2() {
        return stringLight2;
    }

    public TextView getStringLightValue2() {
        return stringLightValue2;
    }

    public TextView getStringLight3() {
        return stringLight3;
    }

    public TextView getStringLightValue3() {
        return stringLightValue3;
    }

    public List<TextView> getStringLights() {
        return stringLights;
    }

    public List<TextView> getStringLightsValues() {
        return stringLightsValues;
    }
}
