package lightweight.lightchess.client.net;

import com.github.bhlangonijr.chesslib.Board;
import javafx.application.Platform;
import lightweight.lightchess.net.CommandTypes;
import lightweight.lightchess.net.Data;

import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class ProcessIncoming implements Runnable{
    boolean DEBUG_MODE = true;


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


    public void handlePlayRequest(Data din){
        System.out.println("Do you want to play with "+ din.sender + " ? if yes type accept:"+ din.sender);
        client.opponentUsername = din.sender;

        if(client.DEBUG_MODE){
            client.sendPlayRequestAccepted();
        }
    }

    public void handleGameBoardUpdate(Data din){
        Board board = new Board();
        board.loadFromFen(din.content);
        client.updateBoard(board);
    }

    public void handlePlayRequestAccepted(Data din){
        client.startMatch(din.sender);
    }

    public void handleSetColor(Data data){
        if(!client.hasUI) return;
        String color = "BLACK";
        client.chessBoard.isBlack = true;
        if(data.cmd==CommandTypes.playWhite){
            color = "WHITE";
            client.chessBoard.isBlack = false;
        } else {
            Platform.runLater(()->{
                client.chessBoard.rotate();
            });
        }
        System.out.println("You are playing " + color);
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
                    case msg -> {
                        handleMessage((Data) data.clone());
                        break;
                    }

                    case requestToPlay -> {
                        handlePlayRequest((Data)data.clone());
                        break;
                    }
                    case playRequestAccecpted -> {
                        handlePlayRequestAccepted((Data)data.clone());
                        break;
                    }
                    case playBlack,playWhite -> {
                        handleSetColor((Data)data.clone());
                    }

                    case updateGameBoard -> {
                        handleGameBoardUpdate((Data) data.clone());
                    }

                    default -> {
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
