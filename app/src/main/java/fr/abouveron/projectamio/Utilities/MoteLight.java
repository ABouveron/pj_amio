package fr.abouveron.projectamio.Utilities;

public class MoteLight {
    private long timestamp;
    private String label;
    private double value;
    private double mote;

    public long getTimestamp() {
        return timestamp;
    }

    public String getLabel() {
        return label;
    }

    public double getValue() {
        return value;
    }

    public double getMote() {
        return mote;
    }

    public boolean getState() {
        return this.value >= 240;
    }
}
