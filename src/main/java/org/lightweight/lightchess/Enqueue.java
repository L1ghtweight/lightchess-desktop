/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lightweight.lightchess;

import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;


public class Enqueue implements Runnable{
    public NetworkConnection netConnection;
    public LinkedBlockingQueue<Data> Q;
    public Enqueue(NetworkConnection nc, LinkedBlockingQueue<Data> Q){
        netConnection=nc;
        this.Q = Q;
    }

    @Override
    public void run() {
        
        Data data=new Data();

        while(true){
            Scanner in=new Scanner(System.in);
            String[] message = in.nextLine().split(":");
            data.cmd = message[0];
            if(message.length>2){
                data.receiver = message[1];
                data.message = message[2];
            }
            Q.add(data);
        }
    }
    
}
