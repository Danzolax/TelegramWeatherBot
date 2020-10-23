import Database.ChatTable;
import Database.Database;
import PersonData.PersonData;
import WeatherData.DailyWeatherResponse;
import WeatherData.JsonData;
import emoji.Icon;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class Bot extends TelegramLongPollingBot {
    private static final Database db = new Database();
    private static final ArrayList<ScheduledFuture> scheduledFutureArrayList = new ArrayList<>();

    private class WeatherTask implements Runnable {
        Integer chatID;

        public WeatherTask(Integer chatID) {
            this.chatID = chatID;
        }

        @Override
        public void run() {
            sendTodayWeather(chatID);
        }
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void updateSubscribeThreads(ArrayList<Integer> chatIDs) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        for (ScheduledFuture scheduledFuture : scheduledFutureArrayList) {
            scheduledFuture.cancel(false);
        }
        for (Integer chatID : chatIDs) {
            scheduledFutureArrayList.add(scheduler.scheduleAtFixedRate(new WeatherTask(chatID), 0, 5, TimeUnit.SECONDS));
        }
    }

    public Bot() {
        super();
        updateSubscribeThreads(db.getSubscribedChats());
    }

    public void onUpdateReceived(@NotNull Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        Integer chatID = Integer.parseInt(message.getChatId().toString());
        if (message.getLocation() != null) {
            Double lat = (double) message.getLocation().getLatitude();
            Double lng = (double) message.getLocation().getLongitude();
            if (db.isExsistIdChat(chatID)) {
                db.updateLocation(chatID, lat, lng);
            } else {
                db.addChatRow(chatID, lat, lng);
            }
            sendMsg(chatID.toString(), "Вы успешно обновили геопозицию!");
        } else if (text.equals("Погода на сегодня")) {
            sendTodayWeather(chatID);
        } else if (text.equals("Погода на завтра")) {
            sendTomorrowWeather(chatID);
        } else if (text.equals("Подписаться")) {
            if (!db.isSubscribe(chatID)) {
                db.Subscribe(chatID);
                updateSubscribeThreads(db.getSubscribedChats());
                sendMsg(chatID.toString(), "Вы успешно подписались!");
            } else
                sendMsg(chatID.toString(), "Вы уже подписаны!");
        } else if (text.equals("Отписаться")) {
            if (db.isSubscribe(chatID)) {
                db.unSubscribe(chatID);
                updateSubscribeThreads(db.getSubscribedChats());
                sendMsg(chatID.toString(), "Вы успешно отписались!");
            } else {
                sendMsg(chatID.toString(), "Вы уже отписаны!");
            }

        } else if (text.equals("zolax")) {
            sendMsg(message.getChatId().toString(), "Лучший!!!");
        } else if (text.equals("Слава")) {
            sendMsg(message.getChatId().toString(), "Лох");
        } else
            sendMsg(message.getChatId().toString(), "Выберите, пункт меню");


    }

    public synchronized void addKeyBoard(@NotNull SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardRow keyboardThirdRow = new KeyboardRow();

        KeyboardButton btn = new KeyboardButton();
        btn.setRequestLocation(true);
        btn.setText("Обновить геопозицию");


        keyboardFirstRow.add(btn);
        keyboardSecondRow.add(new KeyboardButton("Погода на сегодня"));
        keyboardSecondRow.add(new KeyboardButton("Погода на завтра"));
        keyboardThirdRow.add(new KeyboardButton("Подписаться"));
        keyboardThirdRow.add(new KeyboardButton("Отписаться"));
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

    }

    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        addKeyBoard(sendMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getWeatherTypeEmoji(@NotNull String type) {
        switch (type) {
            case "Rain":
                return Icon.RAIN.get();                              
            case "Clouds":
                return Icon.CLOUD.get();
            case "Snow":
                return Icon.SNOW.get();
            case "Sun":
                return Icon.SUN.get();
            default:
                return type;
        }

    }

    public String formatWeatherResponse(@NotNull DailyWeatherResponse dailyWeatherResponse, Integer day) {
        DecimalFormat decimalFormat = new DecimalFormat("#");
        StringBuffer buffer = new StringBuffer();
        buffer.append("Утром:").append("\n");
        buffer.append("   Температура: ").append(decimalFormat.format(dailyWeatherResponse.getDaily(day).getTemp().getMorn())).append("\n");
        buffer.append("   Чувствуется как: ").append(decimalFormat.format(dailyWeatherResponse.getDaily(day).getFeels_like().getMorn())).append("\n");

        buffer.append("Днем:").append("\n");
        buffer.append("   Температура: ").append(decimalFormat.format(dailyWeatherResponse.getDaily(day).getTemp().getDay())).append("\n");
        buffer.append("   Чувствуется как: ").append(decimalFormat.format(dailyWeatherResponse.getDaily(day).getFeels_like().getDay())).append("\n");

        buffer.append("Вечером:").append("\n");
        buffer.append("   Температура: ").append(decimalFormat.format(dailyWeatherResponse.getDaily(day).getTemp().getEve())).append("\n");
        buffer.append("   Чувствуется как: ").append(decimalFormat.format(dailyWeatherResponse.getDaily(day).getFeels_like().getEve())).append("\n");

        buffer.append("Ночью:").append("\n");
        buffer.append("   Температура: ").append(decimalFormat.format(dailyWeatherResponse.getDaily(day).getTemp().getNight())).append("\n");
        buffer.append("   Чувствуется как: ").append(decimalFormat.format(dailyWeatherResponse.getDaily(day).getFeels_like().getNight())).append("\n");
        buffer.append("Тип погоды: ").append(getWeatherTypeEmoji(dailyWeatherResponse.getDaily(day).getWeather()[0].getMain())).append("\n");

        return buffer.toString();
    }

    public DailyWeatherResponse getWeather(Integer chatID) {
        ChatTable data = db.getChatData(chatID);
        Double lat = (double) data.getLatitude();
        Double lng = (double) data.getLongitude();
        return JsonData.getWeather(lat, lng);
    }

    public void sendTodayWeather(Integer chatID) {
        DailyWeatherResponse dailyWeatherResponse = getWeather(chatID);
        sendMsg(chatID.toString(), formatWeatherResponse(dailyWeatherResponse, 0));
    }

    public void sendTomorrowWeather(Integer chatID) {
        DailyWeatherResponse dailyWeatherResponse = getWeather(chatID);
        sendMsg(chatID.toString(), formatWeatherResponse(dailyWeatherResponse, 1));
    }


    @Override
    public String getBotUsername() {
        return PersonData.BOT_NAME.get();
    }

    @Override
    public String getBotToken() {
        return PersonData.BOT_API_KEY.get();
    }
}


