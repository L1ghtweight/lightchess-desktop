package lightweight.lightchess.client.ui;

public class ClockRunner implements Runnable{

    Clock playerClock, opponentsClock;


    public ClockRunner() {}

    public ClockRunner(Clock playerClock, Clock opponentsClock){
        this.playerClock = playerClock;
        this.opponentsClock = opponentsClock;
    }


    @Override
    public void run() {

    }
}
