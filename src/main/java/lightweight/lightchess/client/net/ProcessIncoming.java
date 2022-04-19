package lightweight.lightchess.client.net;

import javafx.application.Platform;
import javafx.util.Pair;
import lightweight.lightchess.net.CommandTypes;
import lightweight.lightchess.net.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
        Platform.runLater(()->{
            try {
                client.main.gameRequest(din.sender);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
        String opponent = din.sender;
        client.startMatch(opponent);

        Platform.runLater(()->{
            try {
                client.main.startGame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
            Platform.runLater(()->{
                try {
                    client.main.changeTournamentGameStatus("In Game");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        client.isInTournamentMatch = true;
        client.match_time_format = client.tournament_info.get("time_format");
    }

    public void handleLoginResponse(Data din) throws IOException {
        String isOk = din.content;
        boolean ok = isOk.equals("success");
        Platform.runLater(()-> {
            try {
                client.main.loginResponse(ok);
                if(ok){
                    client.userInfo = client.parseUserInfo(din.content2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void handleUpdatedUserInfo(Data din){
        client.userInfo = client.parseUserInfo(din.content);
    }

    public void handleSignUpResponse(Data din) throws IOException {
        String isOk = din.content;
        boolean ok = isOk.equals("success");
        Platform.runLater(()-> {
            try {
                client.main.signUpResponse(ok);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void handleUsersList(Data din)  {
        String[] userList = din.content.split("\n",-1);
        ArrayList<Pair<String, String>> usersList = new ArrayList<>();
        for(String str: userList){
            if(str.length()<2) continue;
            String[] slices = str.split(":",2);
            String username = slices[0];
            String time_format = slices[1];

            usersList.add(new Pair<>(username,time_format));
        }

        client.usersList = usersList;
    }

    public void handleUserInfo(Data din){
        HashMap<String, String> requested_info  = client.parseUserInfo(din.content);
        client.requested_userInfo = requested_info;
        client.isUserInfoFetched = true;
    }

    public void handleTournamentDetails(Data din){
        HashMap<String, String> t_info = new HashMap<>();

        String slices[] = din.content.split("\n",-1);

        for(String str: slices){
            String[] tmp = str.split(":",2);
            if(tmp.length < 2) continue;;
            String key = tmp[0];
            String val = tmp[1];

            t_info.put(key,val);
        }

        client.tournament_info = t_info;
        client.isTournamentInfoFetched = true;
    }

    public void handleOpponentsResignation(){
        Platform.runLater(()->{
            client.chessBoard.gameWon();
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

                    case update_user_info -> {
                        handleUpdatedUserInfo((Data) data.clone());
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

                    case resign_from_match -> {
                        handleOpponentsResignation();
                    }

                    case update_gameboard -> {
                        handleGameBoardUpdate((Data) data.clone());
                    }

                    case start_tournament_match -> {
                        handleStartTournamentMatch((Data) data.clone());
                    }

                    case login_response -> {
                        handleLoginResponse((Data) data.clone());
                    }

                    case signup_response -> {
                        handleSignUpResponse((Data) data.clone());
                    }

                    case users_list -> {
                        handleUsersList((Data) data.clone());
                    }

                    case get_user_info -> {
                        handleUserInfo((Data) data.clone());
                    }
                    case get_tournament_details -> {
                        handleTournamentDetails((Data) data.clone());
                    }

                    case score_board -> {
                        System.out.println(data.content);
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
