/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweight.lightchess.server.net;


import lightweight.lightchess.net.Information;
import lightweight.lightchess.net.NetworkConnection;
import lightweight.lightchess.server.database.JDBC;
import lightweight.lightchess.server.tournament.Tournament;

import java.util.HashMap;


public class CreateConnection implements Runnable{
    
    HashMap<String, Information> clientList;
    HashMap<String, Information> loggedInList;
    NetworkConnection nc;
    Tournament t;
    public String ids = "1";
    JDBC jdbc;

    public CreateConnection(HashMap<String, Information> cList, HashMap<String, Information> loggedInList, NetworkConnection nConnection, JDBC jdbc,Tournament tournament){
        clientList=cList;
        this.loggedInList = loggedInList;
        nc=nConnection;
        t = tournament;
        this.jdbc = jdbc;
    }
        
    
    @Override
    public void run() {
        clientList.put(ids,new Information(ids,nc));
        System.out.println("Client " + ids + " connected");
        new Thread(new ReaderWriterServer(ids,nc,clientList,loggedInList,jdbc,t)).start();

        int id = Integer.parseInt(ids);
        id++;
        ids = Integer.toString(id);
    }
    
}
