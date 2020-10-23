package Database;

import PersonData.PersonData;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static final String url = PersonData.DB_URL.get();
    private static final String user = PersonData.DB_USER.get();
    private static final String password = PersonData.DB_PWD.get();
    private static final String dbName = PersonData.DB_NAME.get();
    private static Statement stmt;

    public Database() {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Can't connect mysql");
        }
    }

    public void addChatRow(Integer idChat, Double latitude, Double longitude) {
        String query = "INSERT INTO "+ dbName +".chat (idChat, longitude, latitude, isSubscribed) " +
                "VALUES ( " + idChat + "," + longitude + "," + latitude + "," + 0 + ");";
        try {
            stmt.executeUpdate(query);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.out.println("Can't insert row in table: chat");
        }
    }
    public boolean isExsistIdChat(Integer idChat){
        try {
            getChatData(idChat);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }
    public ChatTable getChatData(Integer idChat) {
        String query = "SELECT * FROM "+ dbName +".chat where idChat = " + idChat + ";";
        ChatTable result = new ChatTable();
        try {
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()) {
                throw new RuntimeException("ID is not valid");
            } else {
                result.setIdChat(rs.getInt(1));
                result.setLongitude(rs.getFloat(2));
                result.setLatitude(rs.getFloat(3));
                result.setIsSubscribed(rs.getByte(4));
                return result;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.out.println("Can't SELECT from chat table");
            return result;
        }
    }

    public boolean isSubscribe(Integer idChat) {
        ChatTable chatTable = getChatData(idChat);
        return chatTable.getIsSubscribed() != 0;
    }
    public void Subscribe(Integer idChat) {
        if (isSubscribe(idChat)) {
        } else {
            String query = "UPDATE "+ dbName +".chat SET isSubscribed = 1 WHERE (idChat = " + idChat + ");";
            try {
                stmt.executeUpdate(query);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                System.out.println("Can't update subscribe status in chat table");
            }
        }
    }
    public void unSubscribe(Integer idChat){
        if (!isSubscribe(idChat)) {
        } else {
            String query = "UPDATE "+ dbName +".chat SET isSubscribed = 0 WHERE (idChat = " + idChat + ");";
            try {
                stmt.executeUpdate(query);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                System.out.println("Can't update subscribe status in chat table");
            }
        }
    }
    public ArrayList<Integer> getSubscribedChats(){
        ArrayList<Integer> result = new ArrayList<>();
        String query = "SELECT * FROM "+ dbName +".chat WHERE isSubscribed = 1;";
        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                result.add(rs.getInt(1));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.out.println("Can't take rows from chat table");
        }
        return result;
    }

    public  void updateLocation(Integer idChat, Double latitude, Double longitude){
        String query = "UPDATE "+ dbName +".chat " +
                "SET latitude = "+ latitude +", longitude = "+ longitude +" " +
                "WHERE (idChat = " + idChat + ");";
        try {
            stmt.executeUpdate(query);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.out.println("Can't update location");
        }
    }


}
