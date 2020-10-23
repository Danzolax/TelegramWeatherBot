package WeatherData;

public class DailyWeatherResponse {
    Double lat;
    Double lon;
    String timezone;
    Integer timezone_offset;
    Daily[] daily;

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public Integer getTimezone_offset() {
        return timezone_offset;
    }

    public Daily getDaily(int index) {
        return daily[index];
    }
}


