/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweight.lightchess.server.net;

import lightweight.lightchess.net.CommandTypes;
import lightweight.lightchess.net.Data;
import lightweight.lightchess.net.Information;
import lightweight.lightchess.net.NetworkConnection;
import lightweight.lightchess.server.database.JDBC;
import lightweight.lightchess.server.tournament.PairUp;
import lightweight.lightchess.server.tournament.Tournament;

import javax.sql.StatementEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

public class ServerNet implements Runnable{
    HashMap<String, Information> clientList = new HashMap<>();
    HashMap<String, Information> loggedInClientList = new HashMap<>();
    ServerSocket serverSocket;
    JDBC jdbc = new JDBC();

    ReaderWriterServer tournament_readerwriterserver =  new ReaderWriterServer("1",null,clientList,loggedInClientList, jdbc,null);

    public Tournament tournament = new Tournament(loggedInClientList,tournament_readerwriterserver);;

    public void initSocket() throws IOException {
        serverSocket = new ServerSocket(12345);
        System.out.println("Server Started...");
        System.out.println(InetAddress.getLocalHost());



// Init a default tournament
//        tournament.name = "NICE";
//        tournament.startTime = LocalDateTime.now().plusMinutes(0);
//        tournament.endTime = tournament.startTime.plusMinutes(10);
//
//        tournament.startPairing();

//        System.out.println(tournament.name + " starts in " + Duration.between(LocalDateTime.now(), tournament.startTime).toMinutes() + "minutes and continues for " + Duration.between(tournament.startTime, tournament.endTime).toMinutes() + " minutes");
//        System.out.println(tournament.get_tournament_details());
//        new Thread(new ProcessCommands(tournament)).start();
    }


    @Override
    public void run() {
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                NetworkConnection nc = new NetworkConnection(socket);
                new Thread(new CreateConnection(clientList,loggedInClientList, nc, jdbc, tournament)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public void startServer() throws IOException {
        Thread serverThread = new Thread(this);
        initSocket();
        serverThread.start();
    }

    public static void main(String[] args) throws IOException {
        ServerNet serverNet = new ServerNet();
        serverNet.startServer();
//        serverNet.tournament.setValues("New Named", "122", "600", "5+1");
//        System.out.println(serverNet.tournament.name + " starts in " + Duration.between(LocalDateTime.now(), serverNet.tournament.startTime).toMinutes() + "minutes and continues for " + Duration.between(serverNet.tournament.startTime, serverNet.tournament.endTime).toMinutes() + " minutes");

    }

}
