/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweight.lightchess.client.net;

import lightweight.lightchess.net.CommandTypes;
import lightweight.lightchess.net.Data;
import lightweight.lightchess.net.NetworkConnection;

import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;


public class EnqueueOutgoing implements Runnable{
    public NetworkConnection netConnection;
    public LinkedBlockingQueue<Data> Q;
    public EnqueueOutgoing(NetworkConnection nc, LinkedBlockingQueue<Data> Q){
        netConnection=nc;
        this.Q = Q;
    }

    @Override
    public void run() {
        
        Data data=new Data();

        while(true){
            Scanner in=new Scanner(System.in);
            String[] message = in.nextLine().split(":");
            String cmd = message[0];
            if(cmd.equals("msg")){
                data.cmd = CommandTypes.msg;
            } else if(cmd.equals("list")){
                data.cmd = CommandTypes.list_clients;
            } else if(cmd.equals("ip")){
                data.cmd = CommandTypes.get_ip;
            }
            if(message.length>2){
                data.receiver = message[1];
                data.content = message[2];
            }
            Q.add(data);
        }
    }
    
}
