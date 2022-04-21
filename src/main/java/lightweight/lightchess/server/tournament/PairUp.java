package lightweight.lightchess.server.tournament;

import lightweight.lightchess.net.CommandTypes;
import lightweight.lightchess.net.Data;
import lightweight.lightchess.net.Information;
import lightweight.lightchess.server.database.JDBC;
import lightweight.lightchess.server.net.ReaderWriterServer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PairUp implements Runnable{
    Tournament tournament;
    Random rand = new Random();
    int sleep_time = 2000;
    ReaderWriterServer readerWriterServer;
    JDBC jdbc;
    public PairUp(Tournament t, JDBC jdbc){
        tournament = t;
        readerWriterServer = t.readerWriterServer;
        this.jdbc = jdbc;
    }

    public void startMatch(String player1, String player2){
        tournament.playersInMatch.add(player1);
        tournament.playersInMatch.add(player2);

        tournament.matchPairs.put(player1, player2);

        System.out.println("Match starting between "+player1 + " and "+ player2);

        Data d1 = new Data();
        Data d2 = new Data();
        d1.cmd = d2.cmd = CommandTypes.start_tournament_match;
        d1.sender = d2.sender = "Server";
        d1.receiver = player1; d1.content = player2;
        d2.receiver = player2; d2.content = player1;

        readerWriterServer.setColor(player1, player2);
        readerWriterServer.sendToClient(d1);
        readerWriterServer.sendToClient(d2);
    }

    public String extractBestPlayer(){
        String bestPlayer = tournament.readyList.get(0);
        int score = -1;

        for(String player: tournament.readyList){
            int s = tournament.registeredList.get(player).score;
            if(s > score){
                bestPlayer = player;
                score = s;
            }
        }

        tournament.readyList.remove(bestPlayer);
        return bestPlayer;
    }

    public String highestScoringPlayer(){
        if(tournament.registeredList==null || tournament.registeredList.size()<1)
            return null;

        int score = -1;
        String bestPlayer = null;

        for(Map.Entry<String, Information>P :tournament.registeredList.entrySet()){
            Information inf = P.getValue();
            if(inf.score > score){
                bestPlayer = P.getKey();
                score = inf.score;
            }
        }
        return bestPlayer;
    }

    public void sendTournamentEndedMsg(){
        if(tournament.registeredList==null || tournament.registeredList.size()<1)
            return;

        Data d = new Data(CommandTypes.tournament_ended);

        for(Map.Entry<String, Information>P :tournament.registeredList.entrySet()){
            d.receiver = P.getKey();
            tournament.readerWriterServer.sendToClient(d);
        }
    }

    @Override
    public void run() {
        while (!tournament.is_tournament_ended()){
            if(tournament.readyList.size()<2 || !tournament.is_tournament_started()){
                try {
                    Thread.sleep(sleep_time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                continue;
            }

            String player1 = extractBestPlayer();
            String player2 = extractBestPlayer();
            if(player1.equals(player2)) continue;

            startMatch(player1,player2);
        }


        String winner = highestScoringPlayer();

        if(winner != null){
            System.out.println(tournament.name + " won by " + winner);
            jdbc.updateFromMatchResult(winner, 3);
        }

        sendTournamentEndedMsg();
    }
}
