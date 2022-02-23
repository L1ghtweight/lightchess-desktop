/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweight.lightchess.client.net;

import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import lightweight.lightchess.client.ui.UIBoard;
import lightweight.lightchess.logic.Logic;
import lightweight.lightchess.net.CommandTypes;
import lightweight.lightchess.net.Data;
import lightweight.lightchess.net.NetworkConnection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import com.github.bhlangonijr.chesslib.Board;

public class ClientNet {
    Socket socket;
    NetworkConnection nc;
    Scanner scan = new Scanner(System.in);
    LinkedBlockingQueue<Data> QOut, QIn;
    Thread enqueueIn,processOut,enqueueOut,processInThread, matchThread;
    String username,opponentUsername;
    boolean isInMatch = false;
    boolean isMyTurn = false;
    Board board;
    Logic logic;
    String color;
    UIBoard uiBoard;

    public ClientNet(UIBoard ub){
        uiBoard = ub;
    }

    public void startMatch(String opponentUsername){
        isInMatch = true;
        board = new Board();
        logic = new Logic();
        this.opponentUsername =  opponentUsername;
    }

    public void makeOpponnentsMove(String move){
        if(isMyTurn){
            System.out.println("Not opponnents move");
            sendMsg("Not your move");
            return;
        }
        System.out.println("Opponent made move : "+ move);
        boolean f = logic.makeMove(board, move);
        System.out.println(board.toString());
        isMyTurn = true;
        updateBoard();
    }

    public void sendData(Data dOut){
        QOut.add(dOut);
    }

    public void sendMove(String move){
        Data d = new Data();
        d.cmd = CommandTypes.move;
        d.content = move;
        d.sender = username;
        d.receiver = opponentUsername;
        QOut.add(d);
    }

    public void sendMsg(String msg){
        Data d = new Data();
        d.cmd = CommandTypes.msg;
        d.content = msg;
        d.sender = username;
        d.receiver = opponentUsername;
        QOut.add(d);
    }

    public void updateBoard(){
        Platform.runLater(() ->{
            uiBoard.updateBoard(board);
        });
    }

    public void start() {

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


        System.out.println("Enter your username");
        Scanner in = new Scanner(System.in);
        username = in.next();
        nc.write(username);

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

        try {
            enqueueIn.join();
        } catch (Exception e) {
            System.out.println("Thread exited");
        }
    }

//    public static void main(String[] args) {
//        ClientNet c = new ClientNet();
//        c.start();
//    }
}