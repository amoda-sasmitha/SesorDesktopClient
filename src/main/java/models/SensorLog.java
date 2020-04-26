package models;

public class SensorLog {
    private int id;
    private String datetime;
    private int smoke_level;
    private int co2_level;

    public SensorLog() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getSmoke_level() {
        return smoke_level;
    }

    public void setSmoke_level(int smoke_level) {
        this.smoke_level = smoke_level;
    }

    public int getCo2_level() {
        return co2_level;
    }

    public void setCo2_level(int co2_level) {
        this.co2_level = co2_level;
    }
}
