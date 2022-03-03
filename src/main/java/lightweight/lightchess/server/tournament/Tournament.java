package lightweight.lightchess.server.tournament;

import lightweight.lightchess.net.Information;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Tournament {
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public String name;
    public HashMap<String, Information>loggedInList;
    public HashMap<String, Information>registeredList = new HashMap<>();
    public ArrayList<String> readyList = new ArrayList<>();

    public int getMinutesRemaining(){
        return (int) Duration.between(LocalDateTime.now(),endTime).toMinutes();
    }

    public Tournament(HashMap<String, Information> loggedInClientList){
        loggedInList = loggedInClientList;
        startTime = endTime = LocalDateTime.now();
    }

    public void register(String username){
        registeredList.put(username, loggedInList.get(username));
    }

    public void addToreadyList(String username){
        readyList.add(username);
    }

    public int timeToStart(){
        return (int) Duration.between(LocalDateTime.now(),startTime).toMinutes();
    }

    public String get_tournament_details() {
        if (getMinutesRemaining() < 1) {
            return "No tournament running right now";
        }
        int startsIn = timeToStart();
        if (startsIn > 1) {
            return "Tournament " + name + " will start in " + startsIn + "minutes";
        }
        return "Tournament " + name + " will continue for " + getMinutesRemaining() + " more minutes";
    }

    public static void main(String[] args) {
        LocalDateTime startTime = LocalDateTime.parse("2022-03-03T23:30");
        LocalDateTime endTime = startTime.plusMinutes(120);
        Duration d =Duration.between(LocalDateTime.now(),endTime);
    }
}
