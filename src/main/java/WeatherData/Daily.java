package WeatherData;

public class Daily {
    Integer dt;
    Integer sunrise;
    Integer sunset;
    Temp temp;
    FeelsLike feels_like;
    Integer pressure;
    Integer humidity;
    Double dew_point;
    Double wind_speed;
    Integer wind_deg;
    Weather[] weather;
    Integer clouds;
    Double pop;
    Double rain;
    Double uvi;

    public Integer getDt() {
        return dt;
    }

    public Integer getSunrise() {
        return sunrise;
    }

    public Integer getSunset() {
        return sunset;
    }

    public Temp getTemp() {
        return temp;
    }

    public FeelsLike getFeels_like() {
        return feels_like;
    }

    public Integer getPressure() {
        return pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public Double getDew_point() {
        return dew_point;
    }

    public Double getWind_speed() {
        return wind_speed;
    }

    public Integer getWind_deg() {
        return wind_deg;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public Integer getClouds() {
        return clouds;
    }

    public Double getPop() {
        return pop;
    }

    public Double getRain() {
        return rain;
    }

    public Double getUvi() {
        return uvi;
    }
}
