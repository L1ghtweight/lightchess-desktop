/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweight.lightchess.client.net;

import javafx.application.Platform;
import lightweight.lightchess.client.ui.ChessBoard;
import lightweight.lightchess.logic.Logic;
import lightweight.lightchess.net.CommandTypes;
import lightweight.lightchess.net.Data;
import lightweight.lightchess.net.NetworkConnection;

import java.io.IOException;
import java.net.Socket;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import com.github.bhlangonijr.chesslib.Board;

public class ClientNet {
    public boolean DEBUG_MODE=true;

    Socket socket;
    NetworkConnection nc;
    Scanner scan = new Scanner(System.in);
    public  LinkedBlockingQueue<Data> QOut, QIn;
    Thread enqueueIn,processOut,enqueueOut,processInThread, matchThread;
    public String username,opponentUsername;
    boolean isInMatch = false;
    boolean hasUI = false;
    String color;
    ChessBoard chessBoard;

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
        QOut.add(dOut);
    }



    public void sendGameBoard(Board gameboard){
        Data d = new Data();
        d.cmd = CommandTypes.updateGameBoard;
        d.sender = username;
        d.receiver = opponentUsername;
        d.content = gameboard.getFen();
        QOut.add(d);
    }

    public void sendLoginRequest(String username,String password){
        this.username = username;

        Data  d = new Data();
        d.cmd = CommandTypes.login;
        d.sender = username;
        d.content = username;
        d.content2 = password;

        QOut.add(d);
    }

    public void sendPlayRequest(String opponentUsername){
        Data d=new Data();
        d.cmd = CommandTypes.requestToPlay;
        d.sender = username;
        d.receiver = opponentUsername;

        QOut.add(d);
    }

    public void sendPlayRequestAccepted(){
        System.out.println("Accepting match with + "+ opponentUsername);
        Data data = new Data();
        data.cmd = CommandTypes.playRequestAccecpted;
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


    public void updateBoard(Board gameboard){
        if(!hasUI) return;
        Platform.runLater(() ->{
            chessBoard.updateBoard(gameboard);
        });
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

        enqueueIn = new Thread(new EnqueueIncoming(nc, QIn));
        processOut = new Thread(new ProcessOutgoing(nc, QOut));
        enqueueOut = new Thread(new EnqueueOutgoing(nc, QOut, this));
        processInThread = new Thread(new ProcessIncoming(QIn,this));

        enqueueIn.start();
        processOut.start();
        enqueueOut.start();
        processInThread.start();

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
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ClientNet c = new ClientNet();
        c.start(null);
    }
}