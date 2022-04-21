package lightweight.lightchess.server.tournament;

import lightweight.lightchess.net.CommandTypes;
import lightweight.lightchess.net.Data;
import lightweight.lightchess.net.Information;
import lightweight.lightchess.server.database.JDBC;
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
    public String time_format = "5+0";
    public ArrayList<String> playersInMatch = new ArrayList<>();
    public HashMap<String, String> matchPairs = new HashMap<>();
    Thread pairUpThread;
    JDBC jdbc;

    public int getMinutesRemaining(){
        return (int) Duration.between(LocalDateTime.now(),endTime).toMinutes();
    }

    public Tournament(HashMap<String, Information> loggedInClientList, ReaderWriterServer rs, JDBC jdbc){
        loggedInList = loggedInClientList;
        startTime = LocalDateTime.now().plusYears(69);
        endTime = startTime.minusYears(96);
        readerWriterServer = rs;
        this.jdbc = jdbc;
        pairUpThread = new Thread(new PairUp(this, jdbc));
        pairUpThread.start();
    }

    public boolean removePlayerFromInMatchList(String username){
        playersInMatch.remove(username);

        HashMap<String, String> tmp = (HashMap<String, String>) matchPairs.clone();

        for(Map.Entry<String, String> M : tmp.entrySet()){
            if(M.getKey().equals(username) || M.getValue().equals(username)){
                matchPairs.remove(M.getKey());
                if(!M.getKey().equals(username)){
                    playersInMatch.remove(M.getKey());
                }
                return true;
            }
        }

        return false;
    }

    public boolean is_tournament_running(){
        return is_tournament_started() && !is_tournament_ended();
    }

    public void printTournamentDetails(){
        System.out.println(name + " starts in " + Duration.between(LocalDateTime.now(), startTime).toMinutes() + "minutes and continues for " + Duration.between(startTime, endTime).toMinutes() + " minutes");
    }
    public boolean setValues(String name, String starts_in, String duration, String time_format){
        if(is_tournament_running()){
            return false;
        }
        this.name = name;
        this.startTime = LocalDateTime.now().plusSeconds(Integer.parseInt(starts_in));
        this.endTime = startTime.plusSeconds(Integer.parseInt(duration));
        this.time_format = time_format;

        startPairing();

        return true;
    }

    public void startPairing(){
        if(pairUpThread.isAlive()) return;
        pairUpThread = new Thread(new PairUp(this, jdbc));
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
        StringBuilder str = new StringBuilder();
        str.append("name:").append(name).append('\n');
        str.append("start_time:").append(startTime.toString()).append('\n');
        str.append("end_time:").append(endTime.toString()).append('\n');
        str.append("time_format:").append(time_format);
        return  str.toString();
    }

    public void updateScore(String player, int score){
        Information inf = registeredList.get(player);
        if(inf == null) {
            System.out.println("Got null inf in updateScore for " + player);
        }
        inf.score += score;
    }

    public String scoreBoard(){
        StringBuilder msgToSend = new StringBuilder();
        for (Map.Entry<String, Information> entry : registeredList.entrySet()) {
            String key = entry.getKey();
            int point = entry.getValue().score;
            msgToSend.append(key).append(":" + point + "\n");
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
