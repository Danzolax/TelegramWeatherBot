package Database;

public class ChatTable {
    Integer idChat;
    Float latitude;
    Float longitude;
    Byte isSubscribed;
    public ChatTable() {

    }
    public ChatTable(Integer idChat, Float latitude, Float longitude, Byte isSubscribed) {
        this.idChat = idChat;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isSubscribed = isSubscribed;
    }

    public void setIdChat(Integer idChat) {
        this.idChat = idChat;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public void setIsSubscribed(Byte isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public Integer getIdChat() {
        return idChat;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public Byte getIsSubscribed() {
        return isSubscribed;
    }

    @Override
    public String toString() {
        return "Database.ChatTable{" +
                "idChat=" + idChat +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", isSubscribed=" + isSubscribed +
                '}';
    }
}
