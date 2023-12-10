package fr.abouveron.projectamio.JsonModel;

public class SensorsData {
    public long timestamp;
    public String label;
    public double value;
    public double mote;

    public boolean getState() {
        return this.value >= 240;
    }
}
