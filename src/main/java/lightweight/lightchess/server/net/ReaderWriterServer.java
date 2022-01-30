/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweight.lightchess.server.net;

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

    @Override
    public void run() {
        while (true) {
            Object obj = nc.read();
            Data dataObj = (Data) obj;

            switch (dataObj.cmd) {
                case "list" -> {
                    System.out.println("List asked By" + username);
                    System.out.println("Client List: \n" + clientList);
                    StringBuilder msgToSend = new StringBuilder("List of Clients...\n");
                    for (Map.Entry<String, Information> entry : clientList.entrySet()) {
                        String key = entry.getKey();
                        msgToSend.append(key).append("\n");
                    }
                    System.out.println("sending.." + msgToSend);
                    nc.write(msgToSend.toString());
                    break;
                }
                case "ip" -> {
                    System.out.println("Client List: \n" + clientList);
                    String msgToSend = nc.getSocket().getLocalAddress().getHostAddress();
                    System.out.println("sending.." + msgToSend);
                    nc.write(msgToSend);
                    break;
                }
                case "send" -> {
                    if(!clientList.containsKey(dataObj.receiver)) break;
                    String msgToSend = username + " says: " + dataObj.message;
                    System.out.println("sending.." + msgToSend);
                    Information info = clientList.get(dataObj.receiver);
                    info.netConnection.write(msgToSend);
                    break;
                }
                default -> {
                    nc.write("Invalid command");
                }
            }
        }

    }

}
