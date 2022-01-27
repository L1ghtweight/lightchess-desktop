/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lightweight.lightchess;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;


public class ClientMain {
    public static void main(String[] args) throws IOException {

        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("www.google.com", 80));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client Started--- ");
        System.out.println(socket.getLocalAddress().getHostAddress());
        NetworkConnection nc=new NetworkConnection(socket.getLocalAddress().getHostAddress(),12345);

        System.out.println("Enter your username");
        Scanner in=new Scanner(System.in);
        String username=in.next();
        nc.write(username);

        LinkedBlockingQueue<Data> Q = new LinkedBlockingQueue<>();

        Thread readerThread=new Thread(new Reader(nc));
        Thread writerThread=new Thread(new Writer(nc,Q));
        Thread enquerThread=new Thread(new Enqueue(nc,Q));

        readerThread.start();
        writerThread.start();
        enquerThread.start();

        try{
            readerThread.join();
        }
        catch(Exception e){
            System.out.println("Thread exited");
        }
    }
}