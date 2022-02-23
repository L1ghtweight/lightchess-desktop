package lightweight.lightchess.client.net;

import lightweight.lightchess.net.CommandTypes;
import lightweight.lightchess.net.Data;

import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class ProcessIncoming implements Runnable{
    public LinkedBlockingQueue<Data> Q;
    ClientNet client;
    Scanner scan = new Scanner(System.in);

    public ProcessIncoming(LinkedBlockingQueue QIn, ClientNet c){
        client = c;
        Q = QIn;
    }

    public void handleMessage(Data din){
        System.out.println(din.sender + " : " + din.content);
    }

    public void handleOpponentsMove(Data din){
        String move = din.content;
        client.makeOpponnentsMove(move);
    }

    public void handlePlayRequest(Data din){
        System.out.println("Do you want to play with "+ din.sender + " ? if yes type accept:"+ din.sender);
        client.opponentUsername = din.sender;
    }

    public void handlePlayRequestAccepted(Data din){
        client.isMyTurn = true;
        client.startMatch(din.sender);
        System.out.println("Match Started with "+ din.sender);
        System.out.println("You are playing WHITE");
        System.out.println(client.board.toString());
    }

    @Override
    public void run() {
        Data data;

        while(true){
            if(Q.isEmpty()){
                try {
                    Thread.sleep(100);
                    continue;
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            try {
                data = Q.take();
                switch (data.cmd) {
                    case msg: {
                        handleMessage((Data) data.clone());
                        break;
                    }
                    case move:{
                        handleOpponentsMove((Data)data.clone());
                        break;
                    }
                    case requestToPlay:{
                        handlePlayRequest((Data)data.clone());
                        break;
                    }
                    case playRequestAccecpted:{
                        handlePlayRequestAccepted((Data)data.clone());
                        break;
                    }
                    default: {
                        System.out.println("Invalid incoming command : " + data.cmd);
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

        }
    }
}
