package lightweight.lightchess.server.database;

import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;
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
        }
        return false;
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
        for(String i:jdbc.userSet){
            System.out.println(i + " " + jdbc.getUserPassword(i));
        }
    }
}

/*
CREATE TABLE USERS(
    username varchar(20) PRIMARY KEY,
    password char(64)
)
 */
