/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweight.lightchess.client.net;

import javafx.application.Platform;
import lightweight.lightchess.Main;
import lightweight.lightchess.client.ui.ChessBoard;
import lightweight.lightchess.net.CommandTypes;
import lightweight.lightchess.net.Data;
import lightweight.lightchess.net.NetworkConnection;

import java.io.IOException;
import java.net.Socket;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import com.github.bhlangonijr.chesslib.Board;

public class ClientNet {
    public boolean DEBUG_MODE=false;
    public boolean autologin = true;
    public boolean tournament_DEBUG_MODE = true;
    public boolean isLoggedIn = false;

    Socket socket;
    NetworkConnection nc;
    Scanner scan = new Scanner(System.in);
    public  LinkedBlockingQueue<Data> QOut, QIn;
    Thread enqueueIn,processOut,enqueueOut,processInThread, matchThread;
    public String username,opponentUsername;
    boolean isInMatch = false;
    boolean hasUI = false;
    String color;
    public ChessBoard chessBoard;
    public Main main;
    public HashMap<String, String> userInfo = new HashMap<>();

    public void parseUserInfo(String inf){
        String[] slices = inf.split("\n",-1);

        for(String str:slices){
            String[] s = str.split(":",2);
            String key = s[0];
            String value = s[1];
            userInfo.put(key,value);
        }
    }

    public ClientNet(ChessBoard ub){
        hasUI = true;
        chessBoard = ub;
    }
    public ClientNet(){

    }

    public void startMatch(String opponentUsername){
        isInMatch = true;
        this.opponentUsername =  opponentUsername;
        System.out.println("Match started with " + opponentUsername);
    }


    public void sendData(Data dOut){
        if(QOut == null){
            System.out.println("Qout is null");
            return;
        }
        QOut.add(dOut);
    }



    public void sendGameBoard(String gameboard_fen, String move){
        Data d = new Data();
        d.cmd = CommandTypes.update_gameboard;
        d.sender = username;
        d.receiver = opponentUsername;
        d.content = gameboard_fen;
        d.content2 = move;

        sendData(d);
    }

    public void sendLoginRequest(String username,String password){
        this.username = username;

        Data  d = new Data();
        d.cmd = CommandTypes.login;
        d.sender = username;
        d.content = username;
        d.content2 = password;

        sendData(d);
    }

    public void sendSignupRequest(String username,String password){
        this.username = username;

        Data  d = new Data();
        d.cmd = CommandTypes.signup;
        d.sender = username;
        d.content = username;
        d.content2 = password;

        sendData(d);
    }

    public void sendPlayRequest(String opponentUsername){
        Data d=new Data();
        d.cmd = CommandTypes.request_to_play;
        d.sender = username;
        d.receiver = opponentUsername;

        QOut.add(d);
    }



    public void sendPlayRequestAccepted(){
        System.out.println("Accepting match with + "+ opponentUsername);
        Data data = new Data();
        data.cmd = CommandTypes.playrequest_accepted;
        data.sender = username;
        data.receiver = opponentUsername;
        startMatch(opponentUsername);

        QOut.add(data);
    }

    public void sendMsg(String msg){
        Data d = new Data();
        d.cmd = CommandTypes.msg;
        d.content = msg;
        d.sender = username;
        d.receiver = opponentUsername;
        QOut.add(d);
    }

    public void sendRegisterRequest(){
        Data d = new Data();
        d.cmd = CommandTypes.register_for_tournament;
        sendData(d);
    }

    public void sendReadyToPlayConfirmation(){
        Data d = new Data();
        d.cmd = CommandTypes.ready_to_play;
        sendData(d);
    }

    public void updateBoard(Board gameboard){
        if(!hasUI) return;
        Platform.runLater(() ->{
            chessBoard.updateBoard(gameboard);
        });
    }

    public void sendTournamentMatchFinishedMsg(String points){
        Data d = new Data();
        d.cmd = CommandTypes.tournament_match_end;
        d.content = points;
        sendData(d);
    }

    public void start(List<String> args) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 12345);
            System.out.println("Client Started--- ");
            nc = new NetworkConnection(socket);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cant connect");
            return;
        }

        QOut = new LinkedBlockingQueue<>();
        QIn = new LinkedBlockingQueue<>();

        if(QOut == null) {
            System.out.println("is null here");
        } else {
            System.out.println("Value " + QOut);
        }

        enqueueIn = new Thread(new EnqueueIncoming(nc, QIn));
        processOut = new Thread(new ProcessOutgoing(nc, QOut));
        enqueueOut = new Thread(new EnqueueOutgoing(nc, QOut, this));
        processInThread = new Thread(new ProcessIncoming(QIn,this));

        enqueueIn.start();
        processOut.start();
        enqueueOut.start();
        processInThread.start();

        if(autologin && args!=null){
            String uname = args.get(0);
            String pass = args.get(1);
            sendLoginRequest(uname,pass);


            if(tournament_DEBUG_MODE){
                sendRegisterRequest();
                sendReadyToPlayConfirmation();
            }
        }


        if(DEBUG_MODE && args != null){
            String uname = args.get(0);
            String pass = args.get(1);
            sendLoginRequest(uname,pass);

            if(args.size() > 2){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Requesting "+ args.get(2));
                sendPlayRequest(args.get(2));
            }
        }

        try {
            enqueueIn.join();
        } catch (Exception e) {
            System.out.println("Thread exited");
        }
    }


    public static void main(String[] args) {
        ClientNet c = new ClientNet();
        List<String> arguments = Arrays.asList(args);
        c.start(arguments);
    }
}