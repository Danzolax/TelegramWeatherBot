package PersonData;

public enum PersonData {
    WEATHER_API_KEY(""), // Open Weather API key
    DB_URL(""), //MySQL database url
    DB_USER(""), // MySQL username
    DB_PWD(""), // MySQL user password
    DB_NAME(""), // MySQL database name
    BOT_NAME(""), // Telegram bot name
    BOT_API_KEY(""),//Telegram Bot Token
    SUB_NOT_INT("12") //subscription notification interval in hours
    ;
    private final String value;
    public String get(){
        return this.value;
    }

    PersonData(String value) {
        this.value = value;
    }
}
