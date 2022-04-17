package lightweight.lightchess.client.net;

import javafx.application.Platform;
import lightweight.lightchess.Main;
import lightweight.lightchess.net.CommandTypes;
import lightweight.lightchess.net.Data;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class ProcessIncoming implements Runnable{
    boolean DEBUG_MODE = false;


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
        String gameboard_fen = din.content;
        String move = din.content2;

        Platform.runLater(()->{
            client.chessBoard.handleOpponnentsMove(gameboard_fen, move);
        });
    }

    public void handlePlayRequestAccepted(Data din){
        client.startMatch(din.sender);
    }

    public void handleSetColor(Data data){
        if(!client.hasUI) return;
        String color = "BLACK";
        client.chessBoard.isBlack = true;
        if(data.cmd==CommandTypes.play_white){
            color = "WHITE";
            client.chessBoard.isBlack = false;
        } else {
            Platform.runLater(()->{
                client.chessBoard.rotate();
            });
        }
        System.out.println("You are playing " + color);
    }

    public void handleStartTournamentMatch(Data dObj){
        System.out.println("Starting tournament match with " + dObj.content);
        client.startMatch(dObj.content);
    }

    public void handleLoginResponse(Data din) throws IOException {
        String isOk = din.content;
        boolean ok = isOk.equals("success");
        Platform.runLater(()-> {
            try {
                client.main.loginResponse(ok);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void handleSignUpResponse(Data din) throws IOException {
        String isOk = din.content;
        boolean ok = isOk.equals("success");
        Platform.runLater(()-> {
            try {
                System.out.println("Ok here " + ok);
                client.main.signUpResponse(ok);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

                    case request_to_play -> {
                        handlePlayRequest((Data)data.clone());
                        break;
                    }
                    case playrequest_accepted -> {
                        handlePlayRequestAccepted((Data)data.clone());
                        break;
                    }
                    case play_black, play_white -> {
                        handleSetColor((Data)data.clone());
                    }

                    case update_gameboard -> {
                        handleGameBoardUpdate((Data) data.clone());
                    }

                    case start_tournament_match -> {
                        handleStartTournamentMatch((Data) data.clone());
                    }

                    case login_response -> {
                        handleLoginResponse(data);
                    }

                    case signup_response -> {
                        handleSignUpResponse(data);
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
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
