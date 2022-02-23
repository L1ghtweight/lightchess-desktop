/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweight.lightchess.client.net;

import lightweight.lightchess.net.CommandTypes;
import lightweight.lightchess.net.Data;
import lightweight.lightchess.net.NetworkConnection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;


public class ClientNet {
    Socket socket;
    NetworkConnection nc;
    LinkedBlockingQueue<Data> QOut, QIn;
    Thread enqueueIn,processOut,enqueueOut,processInThread;
    String username,opponentUsername;
    boolean isInMatch = false;

    public void startMatch(String opponentUsername){
        isInMatch = true;
        this.opponentUsername =  opponentUsername;
    }

    public void sendData(Data dOut){
        QOut.add(dOut);
    }

    public void sendMove(String move,String opponentUsername){
        Data d = new Data();
        d.cmd = CommandTypes.move;
        d.content = move;
        d.sender = username;
        d.receiver = opponentUsername;
    }


    public void start(String[] args) throws IOException {

        Socket socket = null;
        try {
            socket = new Socket("localhost", 12345);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cant connect");
            return;
        }
        System.out.println("Client Started--- ");
        NetworkConnection nc = new NetworkConnection(socket);

        System.out.println("Enter your username");
        Scanner in = new Scanner(System.in);
        username = in.next();
        nc.write(username);

        QOut = new LinkedBlockingQueue<>();
        QIn = new LinkedBlockingQueue<>();

        enqueueIn = new Thread(new EnqueueIncoming(nc, QIn));
        processOut = new Thread(new ProcessOutgoing(nc, QOut));
        enqueueOut = new Thread(new EnqueueOutgoing(nc, QOut));
        processInThread = new Thread(new ProcessIncoming(QIn));

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
}