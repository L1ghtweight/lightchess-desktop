package lightweight.lightchess.server.tournament;

import java.util.Random;

public class PairUp implements Runnable{
    Tournament tournament;
    Random rand = new Random();

    public PairUp(Tournament t){
        tournament = t;
    }

    public void startMatch(String player1, String player2){
    }

    @Override
    public void run() {
        while (true){
            if(tournament.readyList.size()<2){
                continue;
            }

            String player1 = tournament.readyList.get(0);
            int ind = rand.nextInt()%tournament.readyList.size();
            if(ind==0) ind++;
            String player2 = tournament.readyList.get(ind);

            tournament.readyList.remove(0);
            tournament.readyList.remove(ind);

            startMatch(player1,player2);
        }
    }
}
