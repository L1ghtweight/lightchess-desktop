package lightweight.lightchess.server.database;

import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;

public class JDBC {
    String driver="org.mariadb.jdbc.Driver";
    Connection con;
    HashSet<String> userSet;

    public JDBC(){
        con = getConnection();
        userSet = getUserSet();
    }

    public JDBC(String url, String username, String password){
        con = getConnection(url,username,password);
        userSet = getUserSet();
    }

    public Connection getConnection (String url,String username, String password){
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url,username,password);
            return con;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    String incrementColumnValue(String username, String column){
        return String.format("UPDATE USERS SET %s=%s+1 WHERE username='%s'", column, column, username);
    }

    public boolean updateTimeFormat(String username, String time_format){
        try {
            Statement st = con.createStatement();
            st.executeUpdate(String.format("UPDATE USERS SET time_format='%s' WHERE username='%s'", time_format, username));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateFromMatchResult(String username, int won){
        try {
            Statement st = con.createStatement();
            if(won==2){
                st.executeUpdate(incrementColumnValue(username,"matchs_won"));
            } else if(won==1){
                st.executeUpdate(incrementColumnValue(username, "matchs_drawn"));
            } else {
                st.executeUpdate(incrementColumnValue(username, "matchs_lost"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getUserDetails(String username){
        try {
            StringBuilder info = new StringBuilder("");
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery(String.format("SELECT * FROM USERS WHERE username='%s'",username));
            while(res.next()){
                int elo = res.getInt("elo");
                int matchs_won = res.getInt("matchs_won");
                int matchs_lost = res.getInt("matchs_lost");
                int matchs_drawn = res.getInt("matchs_drawn");
                int tournaments_won = res.getInt("tournaments_won");
                String time_format = res.getString("time_format");

                info.append("elo:"+elo).append('\n');
                info.append("matchs_won:"+matchs_won).append('\n');
                info.append("matchs_lost:"+matchs_lost).append('\n');
                info.append("matchs_drawn:"+matchs_drawn).append('\n');
                info.append("tournaments_won:"+tournaments_won).append('\n');
                info.append("time_format:"+time_format);
            }
            return info.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateELO(String username, int newELO){
        try {
            Statement st = con.createStatement();
            st.executeUpdate(String.format("UPDATE USERS SET elo=%d WHERE username='%s'", newELO, username));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPreferredTimeFormat(String username){
        try {
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery(String.format("SELECT time_format FROM USERS WHERE username='%s'",username));
            if(res.next()){
                return res.getString("time_format");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Connection getConnection(){
        return getConnection("jdbc:mariadb://localhost:3306/lightchess",System.getenv("DB_USER"),System.getenv("DB_PASS"));
    }

    public HashSet<String> getUserSet(){
        try{
            HashSet<String> arr = new HashSet<>();
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT username FROM USERS");
            while (res.next()){
                arr.add(res.getString(1));
            }
            return arr;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getUserPassword(String username){
        try{
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery(String.format("SELECT password FROM USERS WHERE username='%s'",username));
            if(!res.next()) return null;
            return res.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createUser(String username,String password){
        if(userSet.contains(username)){
            return false;
        }
        try {
            Statement st = con.createStatement();
            password = DigestUtils.sha256Hex(password);
            st.executeUpdate(String.format("INSERT INTO USERS (username,password) VALUES ('%s','%s')",username,password));
            st.close();
            userSet.add(username);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkPassword(String username, String password){
        password = DigestUtils.sha256Hex(password);
        String actualPassword = getUserPassword(username);
        if(actualPassword!=null){
            return password.equals(actualPassword);
        }
        return false;
    }

    public void setPassword(String username,String password){
        try {
            password = DigestUtils.sha256Hex(password);
            Statement st = con.createStatement();
            st.executeUpdate(String.format("UPDATE USERS SET password='%s' WHERE USERNAME='%s'",password,username));
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean doesUserExists(String username){
        return userSet.contains(username);
    }

    public static void main(String[] args) {
        JDBC jdbc = new JDBC();
//        System.out.println(jdbc.getPreferredTimeFormat("a"));
        String details = jdbc.getUserDetails("a");
        String[] slices = details.split("\n",-1);
        HashMap<String, String> infos = new HashMap<>();

        for(String str:slices){
            String[] s = str.split(":",2);
            String key = s[0];
            String value = s[1];
            infos.put(key,value);
//            System.out.println(key + "-" + value);
//            System.out.println(str + "this is it");
        }
        System.out.println(infos.get("time_format"));

        //        for(String i:jdbc.userSet){
//            System.out.println(i + " " + jdbc.getUserPassword(i));
//        }
    }
}

/*
CREATE TABLE USERS(
    username varchar(20) PRIMARY KEY,
    password char(64),
    elo INT DEFAULT 1200,
    matchs_won INT DEFAULT 0,
    matchs_lost INT DEFAULT 0,
    matchs_drawn INT DEFAULT 0,
    tournaments_won INT DEFAULT 0,
    time_format varchar(10) DEFAULT '5+0'
);

 */
