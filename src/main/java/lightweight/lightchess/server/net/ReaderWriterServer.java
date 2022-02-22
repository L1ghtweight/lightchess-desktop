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

import java.util.HashMap;
import java.util.Map;


public class ReaderWriterServer implements Runnable {

    String username;
    NetworkConnection nc;
    HashMap<String, Information> clientList;

    public ReaderWriterServer(String user, NetworkConnection netConnection, HashMap<String, Information> cList) {
        username = user;
        nc = netConnection;
        clientList = cList;
    }

    public void msgFromServer(String msg){
        Data data = new Data("Server",username, CommandTypes.msg,msg);
        nc.write(data);
    }

    public void sendClientList(){
        System.out.println("List asked By " + username);
        StringBuilder msgToSend = new StringBuilder("List of Clients...\n");
        for (Map.Entry<String, Information> entry : clientList.entrySet()) {
            String key = entry.getKey();
            msgToSend.append(key).append("\n");
        }
        msgFromServer(msgToSend.toString());
    }

    public void sendIp(){
        String msgToSend = nc.getSocket().getLocalAddress().getHostAddress();
        msgFromServer(msgToSend);
    }

    public void sendToClient(Data data){
        if(!clientList.containsKey(data.receiver)){
            msgFromServer(data.receiver + " not connected");
            return;
        }
        Information inf = clientList.get(data.receiver);
        inf.netConnection.write(data);
    }

    @Override
    public void run() {
        while (true) {
            Data dataObj = (Data) nc.read();
            if(dataObj == null){
                clientList.remove(username);
                System.out.println(username + " disconnected");
                break;
            }
            dataObj.sender = username;

            switch (dataObj.cmd) {
                case list_clients  -> {
                    sendClientList();
                    break;
                }
                case get_ip -> {
                    sendIp();
                    break;
                }
                case msg -> {
                    sendToClient(dataObj);
                    break;
                }
                default -> {
                    msgFromServer("Invalid Command : "+ dataObj.cmd);
                }
            }
        }

    }

}
