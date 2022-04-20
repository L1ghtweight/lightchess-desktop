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
import lightweight.lightchess.server.tournament.Tournament;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class ReaderWriterServer implements Runnable {
    boolean DEBUG_MODE=false;

    String username="",id;
    NetworkConnection nc;
    HashMap<String, Information> clientList;
    HashMap<String, Information> loggedInList;
    Random rand = new Random();
    JDBC jdbc;
    boolean isLoggedIn = false;
    Tournament tournament;

    public ReaderWriterServer(String userID, NetworkConnection netConnection, HashMap<String, Information> cList, HashMap<String, Information> loggedInLIst, JDBC jdbc, Tournament t) {
        tournament = t;
        id = userID;
        nc = netConnection;
        clientList = cList;
        this.loggedInList = loggedInLIst;
        this.jdbc = jdbc;

    }

    public void msgFromServer(String msg){
        Data data = new Data("Server",id, CommandTypes.msg,msg);
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
        StringBuilder msgToSend = new StringBuilder();
        for (Map.Entry<String, Information> entry : loggedInList.entrySet()) {
            String key = entry.getKey();
            msgToSend.append(key).append(":").append(jdbc.getPreferredTimeFormat(key)).append('\n');
        }
        Data d = new Data();
        d.cmd = CommandTypes.users_list;
        d.sender = "Server";
        d.content = msgToSend.toString();
        responseFromServer(d);
    }

    public void sendIp(){
        String msgToSend = nc.getSocket().getLocalAddress().getHostAddress();
        msgFromServer(msgToSend);
    }

    public void sendToClient(Data data){
        if(!loggedInList.containsKey(data.receiver)){
            msgFromServer(data.receiver + " not connected");
            return;
        }
        Information inf = loggedInList.get(data.receiver);
        inf.netConnection.write(data);
    }

    public void sendUpdatedUserInfo(){
        Data d = new Data(CommandTypes.update_user_info);
        d.content = jdbc.getUserDetails(username);
        responseFromServer(d);
    }

    public void responseFromServer(Data data){
        data.sender = "Server";
        nc.write(data);
    }

    public void setColor(Data data){
        int i = rand.nextInt(2);
        Data d1 = new Data();
        Data d2 = new Data();
        d1.sender = d2.sender = "Server";
        d1.receiver = data.receiver;
        if(i==0) d1.cmd = CommandTypes.play_black;
        else d1.cmd = CommandTypes.play_white;


        d2.receiver = data.sender;
        if(d1.cmd == CommandTypes.play_black)
            d2.cmd = CommandTypes.play_white;
        else
            d2.cmd = CommandTypes.play_black;

        sendToClient(d1);
        sendToClient(d2);

    }

    public void setColor(String player1, String player2){
        int i = rand.nextInt(2);
        Data d1 = new Data();
        Data d2 = new Data();
        d1.sender = d2.sender = "Server";
        d1.receiver = player1;
        if(i==0) d1.cmd = CommandTypes.play_black;
        else d1.cmd = CommandTypes.play_white;


        d2.receiver = player2;
        if(d1.cmd == CommandTypes.play_black)
            d2.cmd = CommandTypes.play_white;
        else
            d2.cmd = CommandTypes.play_black;

        sendToClient(d1);
        sendToClient(d2);

    }


    public void sendSignupSuccess(){
        Data d = new Data();
        d.receiver = username;
        d.content = "success";
        d.cmd = CommandTypes.signup_response;
        responseFromServer(d);
    }

    public void sendSignupFailure(){
        Data d = new Data();
        d.receiver = username;
        d.content = "failure";
        d.cmd = CommandTypes.signup_response;
        responseFromServer(d);
    }


    public void signupClient(Data dObj){
        if(isLoggedIn){
            msgFromServer("You are already logged in");
            return;
        }

        String msg;
        if(jdbc.createUser(dObj.content,dObj.content2)){
            msg = "Signup successfull";
            username = dObj.content;
            System.out.println("Signed up " + username);
            sendSignupSuccess();
        } else {
            msg = "Signup Error";
            sendSignupFailure();
        }
        msgFromServer(msg);
    }

    public void sendLoginSuccess(){
        Data d = new Data();
        d.receiver = username;
        d.cmd = CommandTypes.login_response;
        d.content = "success";
        d.content2 = jdbc.getUserDetails(username);
        System.out.println(d.content2);
        responseFromServer(d);
    }

    public void sendLoginFailure(){
        Data d = new Data();
        d.receiver = username;
        d.cmd = CommandTypes.login_response;
        d.content = "failure";
        responseFromServer(d);
    }


    public void loginClient(Data dObj){
        if(isLoggedIn){
            msgFromServer("You are already logged in");
            return;
        }

        String msg;
        username = dObj.content;

        if(loggedInList.containsKey(username)){
            msgFromServer("You are already logged in some other device, logout first");
            sendLoginFailure();
            return;
        }

        if(jdbc.checkPassword(dObj.content,dObj.content2)){
            msg = "Login successfull as " + username;
            loggedInList.put(username,new Information(username,nc));
            isLoggedIn = true;
            sendLoginSuccess();
        } else {
            msg = "Login Error";
            sendLoginFailure();
        }
        msgFromServer(msg);
    }

    public void logOut(Data dObj){
        loggedInList.remove(username);
        msgFromServer("Logged out "+username);
        isLoggedIn = false;
        tournament.readyList.remove(username);
    }

    public void sendScoreBoard(){
        Data d = new Data();
        d.cmd = CommandTypes.score_board;
        d.content = tournament.scoreBoard();
        responseFromServer(d);
    }

    public void sendUserInfo(Data din){
        String requested_user = din.content;
        System.out.println(requested_user + " info asked by " + din.sender);
        Data d = new Data(CommandTypes.get_user_info);
        d.content = jdbc.getUserDetails(requested_user);
        responseFromServer(d);
    }

    public void updateTimeFormat(Data din){
        String newFormat = din.content;
        jdbc.updateTimeFormat(username, newFormat);
    }

    public void sendTournamentDetails(){
        Data d = new Data(CommandTypes.get_tournament_details);
        d.content = tournament.get_tournament_details();
        responseFromServer(d);
    }


    @Override
    public void run() {
        while (true) {
            Data dataObj = (Data) nc.read();
            if(dataObj == null){
                clientList.remove(id);
                if(username.equals("")) break;
                loggedInList.remove(username);
                System.out.println(username + " disconnected");
                break;
            }
            dataObj.sender = username;

            if(dataObj.cmd == CommandTypes.signup){
                signupClient(dataObj);
            } else if(dataObj.cmd == CommandTypes.login){
                loginClient(dataObj);
            }

            if(!isLoggedIn){
                msgFromServer("You are not logged in, login/signup:username:password");
                continue;
            }


            switch (dataObj.cmd) {
                case list_clients  -> {
                    sendClientList();
                }

                case update_time_format -> {
                    updateTimeFormat(dataObj);
                }

                case users_list -> {
                    sendLoggedInClientList();
                }

                case get_ip -> {
                    sendIp();
                }

                case update_user_info -> {
                    sendUpdatedUserInfo();
                }

                case msg, request_to_play,move, update_gameboard,resign_from_match ->{
                    sendToClient(dataObj);
                }

                case playrequest_accepted ->{
                    setColor(dataObj);
                    sendToClient(dataObj);
                }

                case logout -> {
                    logOut(dataObj);
                }

                case login,signup -> {}

                case get_tournament_details -> {
                    sendTournamentDetails();
                }

                case register_for_tournament -> {
                    tournament.register(username);
                }

                case ready_to_play -> {
                    tournament.addToreadyList(username);
                }

                case casual_match_end -> {
                    jdbc.updateFromMatchResult(username, Integer.parseInt(dataObj.content));
                }

                case tournament_match_end -> {
                    tournament.updateScore(username, Integer.parseInt(dataObj.content));
                    jdbc.updateFromMatchResult(username, Integer.parseInt(dataObj.content));
                    tournament.removePlayerFromInMatchList(username);
                }

                case score_board -> {
                    sendScoreBoard();
                }

                case get_user_info -> {
                    sendUserInfo(dataObj);
                }

                case update_elo -> {
                    jdbc.updateELO(username, Integer.parseInt(dataObj.content));
                }

                default -> {
                    msgFromServer("Invalid Command : "+ dataObj.cmd);
                }
            }
        }

    }

}
