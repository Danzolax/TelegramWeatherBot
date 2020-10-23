package WeatherData;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import PersonData.PersonData;

public class JsonData {
    public static DailyWeatherResponse getWeather(Double lat, Double lng){
        Gson g = new Gson();
        return g.fromJson(getJsonString(lat, lng), DailyWeatherResponse.class);

    }
    @NotNull
    public static String getJsonString(@NotNull Double lat, @NotNull Double lng){
        final String apiKey = PersonData.WEATHER_API_KEY.get();
        String latString = lat.toString();
        String lngString = lng.toString();
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + latString
                + "&lon=" + lngString
                + "&exclude=current,minutely,hourly"
                +"&appid=" + apiKey
                + "&units=metric";
        try {
            return downloadURL(url);
        } catch (IOException e) {
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            System.out.println("Не смог считать данные");
            e.printStackTrace();
            return "";
        }
    }
    @NotNull
    private static String downloadURL(String string) throws IOException {
        URL url = new URL(string);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();

        String line = "";
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        String data = builder.toString();
        reader.close();
        return data;
    }
}