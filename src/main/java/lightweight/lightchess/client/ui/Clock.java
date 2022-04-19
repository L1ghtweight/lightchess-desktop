package lightweight.lightchess.client.ui;

import java.util.concurrent.TimeUnit;

public class Clock implements Runnable{
    boolean tick = true;
    int totalMinutes, increment;
    int timeLeft = -1;
    public boolean isGameRunning = true;

    Clock(String format) {
        String[] args = format.split("\\+");
        totalMinutes = Integer.parseInt(args[0]);
        increment = Integer.parseInt(args[1]);

        timeLeft = totalMinutes * 60;
    }

    public void resume() {
        tick = true;
    }
    public void pause() {
        tick = false;
        timeLeft += increment;
    }
    public void tick() {
        if(tick)
            timeLeft--;
    }

    @Override
    public void run() {
        while(isGameRunning){

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            tick();
            if(timeLeft == 0) isGameRunning = false;

            System.out.println(getTimeString());
        }
    }

    public String getTimeString() {
        int totalSecondsLeft = timeLeft;
        int leftMinutes = (int) TimeUnit.SECONDS.toMinutes(totalSecondsLeft);
        int leftSeconds = totalSecondsLeft - leftMinutes * 60;

        String seconds = String.valueOf(leftSeconds);
        String minutes = String.valueOf(leftMinutes);

        if(leftMinutes < 10) minutes = "0" + minutes;
        if(leftSeconds < 10) seconds = "0" + seconds;

        return minutes + ":" + seconds;
    }

    public static void main(String[] args) {
        Thread clockThread = new Thread(new Clock("1+0"));
        clockThread.start();
    }

}
