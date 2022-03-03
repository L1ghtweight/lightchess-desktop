/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweight.lightchess.server.net;

import lightweight.lightchess.net.CommandTypes;
import lightweight.lightchess.net.Data;
import lightweight.lightchess.net.Information;
import lightweight.lightchess.net.NetworkConnection;
import lightweight.lightchess.server.database.JDBC;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class ReaderWriterServer implements Runnable {

    String username;
    NetworkConnection nc;
    HashMap<String, Information> clientList;
    HashMap<String, Information> loggedInList;
    Random rand = new Random();
    JDBC jdbc = new JDBC();

    public ReaderWriterServer(String user, NetworkConnection netConnection, HashMap<String, Information> cList,HashMap<String, Information> loggedInLIst) {
        username = user;
        nc = netConnection;
        clientList = cList;
        this.loggedInList = loggedInLIst;
    }

    public void msgFromServer(String msg){
        Data data = new Data("Server",username, CommandTypes.msg,msg);
        nc.write(data);
    }

    public void sendClientList(){
        System.out.println("List asked By " + username);
        StringBuilder msgToSend = new StringBuilder("List of Clients...\n");
        for (Map.Entry<String, Information> entry : clientList.entrySet()) {
            String key = entry.getKey();
            msgToSend.append(key).append("\n");
        }
        msgFromServer(msgToSend.toString());
    }

    public void sendLoggedInClientList(){
        System.out.println("List asked By " + username);
        StringBuilder msgToSend = new StringBuilder("List of Logged In Clients...\n");
        for (Map.Entry<String, Information> entry : loggedInList.entrySet()) {
            String key = entry.getKey();
            msgToSend.append(key).append("\n");
        }
        msgFromServer(msgToSend.toString());
    }

    public void sendIp(){
        String msgToSend = nc.getSocket().getLocalAddress().getHostAddress();
        msgFromServer(msgToSend);
    }

    public void sendToClient(Data data){
        if(!clientList.containsKey(data.receiver)){
            msgFromServer(data.receiver + " not connected");
            return;
        }
        Information inf = clientList.get(data.receiver);
        inf.netConnection.write(data);
    }

    public void setColor(Data data){
        int i = rand.nextInt(2);
        Data d1 = new Data();
        Data d2 = new Data();
        d1.sender = d2.sender = "Server";
        d1.receiver = data.receiver;
        if(i==0) d1.cmd = CommandTypes.playBlack;
        else d1.cmd = CommandTypes.playWhite;


        d2.receiver = data.sender;
        if(d1.cmd == CommandTypes.playBlack)
            d2.cmd = CommandTypes.playWhite;
        else
            d2.cmd = CommandTypes.playBlack;

        sendToClient(d1);
        sendToClient(d2);

    }

    public void signupClient(Data dObj){
        String msg;
        if(jdbc.createUser(dObj.content,dObj.content2)){
            msg = "Signup successfull";
            String username = dObj.content;
        } else {
            msg = "Signup Error";
        }
        msgFromServer(msg);
    }

    public void loginClient(Data dObj){
        String msg;
        String username = dObj.content;
        if(jdbc.checkPassword(dObj.content,dObj.content2)){
            msg = "Login successfull";
            loggedInList.put(username,new Information(username,nc));
        } else {
            msg = "Login Error";
        }
        msgFromServer(msg);
    }

    public void logOut(Data dObj){
        String username = dObj.content;
        loggedInList.remove(username);
        msgFromServer("Logged out "+username);
    }

    @Override
    public void run() {
        while (true) {
            Data dataObj = (Data) nc.read();
            if(dataObj == null){
                clientList.remove(username);
                System.out.println(username + " disconnected");
                break;
            }
            dataObj.sender = username;

            switch (dataObj.cmd) {
                case list_clients  -> {
                    sendClientList();
                }

                case list_loggedInClients -> {
                    sendLoggedInClientList();
                }

                case get_ip -> {
                    sendIp();
                }

                case msg,requestToPlay,move ->{
                    sendToClient(dataObj);
                }

                case playRequestAccecpted ->{
                    setColor(dataObj);
                    sendToClient(dataObj);
                }

                case signup -> {
                    signupClient(dataObj);
                }

                case login -> {
                    loginClient(dataObj);
                }

                case logout -> {
                    logOut(dataObj);
                }


                default -> {
                    msgFromServer("Invalid Command : "+ dataObj.cmd);
                }
            }
        }

    }

}
