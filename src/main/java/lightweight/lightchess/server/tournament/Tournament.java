package lightweight.lightchess.server.tournament;

import lightweight.lightchess.net.CommandTypes;
import lightweight.lightchess.net.Data;
import lightweight.lightchess.net.Information;
import lightweight.lightchess.server.net.ReaderWriterServer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tournament {
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public String name;
    public HashMap<String, Information>loggedInList;
    public HashMap<String, Information>registeredList = new HashMap<>();
    public ArrayList<String> readyList = new ArrayList<>();
    public  ReaderWriterServer readerWriterServer;
    Thread pairUpThread;

    public int getMinutesRemaining(){
        return (int) Duration.between(LocalDateTime.now(),endTime).toMinutes();
    }

    public Tournament(HashMap<String, Information> loggedInClientList, ReaderWriterServer rs){
        loggedInList = loggedInClientList;
        startTime = endTime = LocalDateTime.now();
        readerWriterServer = rs;
        pairUpThread = new Thread(new PairUp(this));
        pairUpThread.start();
    }

    public void sendMsg(String username, String msg){
        Data d = new Data();
        d.sender = "Server";
        d.receiver=username;
        d.cmd = CommandTypes.msg;
        d.content = msg;
        readerWriterServer.sendToClient(d);
    }

    public void register(String username){
        if(registeredList.containsKey(username)){
            sendMsg(username,"Already Registered");
            return;
        }
        registeredList.put(username, loggedInList.get(username));
        System.out.println(username + " registered for tournament " + name);
        sendMsg(username, "Successfully registered for tournament " + name);
    }

    public void addToreadyList(String username){
        if(!is_tournament_started()){
            sendMsg(username,"Tournament not yet started");
            return;
        }
        readyList.add(username);
        System.out.println(username + " is added to ready list");
        sendMsg(username, "Wait to be paired up");
    }

    public int timeToStart(){
        return (int) Duration.between(LocalDateTime.now(),startTime).toMinutes();
    }
    public boolean is_tournament_started(){
        return LocalDateTime.now().isAfter(startTime);
    }

    public boolean is_tournament_ended(){
        return LocalDateTime.now().isAfter(endTime);
    }

    public String get_tournament_details() {
        if (getMinutesRemaining() < 1) {
            return "No tournament running right now";
        }
        if (is_tournament_started()) {
            return "Tournament " + name + " will continue for " + getMinutesRemaining() + " more minutes";
        }
        return "Tournament " + name + " will start in " + timeToStart() + " minutes";
    }

    public void updateScore(String player, int score){
        Information inf = registeredList.get(player);
        inf.score += score;
    }

    public String scoreBoard(){
        StringBuilder msgToSend = new StringBuilder();
        for (Map.Entry<String, Information> entry : registeredList.entrySet()) {
            String key = entry.getKey();
            int point = entry.getValue().score;
            msgToSend.append(key).append(" : " + point + "\n");
        }
        return  msgToSend.toString();
    }

    public static void main(String[] args) {
//        LocalDateTime startTime = LocalDateTime.parse("2022-03-03T23:30");
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusMinutes(120);
        Duration d =Duration.between(LocalDateTime.now(),endTime);

    }
}
