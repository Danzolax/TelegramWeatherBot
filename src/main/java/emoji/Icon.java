package emoji;

import com.vdurmont.emoji.EmojiParser;

public enum Icon {
    CLOUD(":cloud:"),
    RAIN("\uD83C\uDF27"),
    SUN(":sunny:"),
    SNOW(":snow:");

    private final String value;

    public String get() {
        return EmojiParser.parseToUnicode(value);
    }

    Icon(String value) {
        this.value = value;
    }
}
