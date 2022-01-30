/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweight.lightchess.client.net;


import lightweight.lightchess.net.Data;
import lightweight.lightchess.net.NetworkConnection;

import java.util.concurrent.LinkedBlockingQueue;

public class EnqueueIncoming implements Runnable{
    public NetworkConnection netConnection;
    String msg="";
    public static boolean check = false;
    public LinkedBlockingQueue<Data> Q;

    public EnqueueIncoming(NetworkConnection nc, LinkedBlockingQueue<Data> QIn){
        netConnection=nc;
        Q = QIn;
    }


    @Override
    public void run() {
        while(true){
            String msg;
            Data obj=(Data) netConnection.read();
            Q.add(obj);
        }
    }

}
