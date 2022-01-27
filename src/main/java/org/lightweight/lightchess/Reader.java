/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lightweight.lightchess;


public class Reader implements Runnable{
    public NetworkConnection netConnection;
    String msg="";
    public static boolean check = false;
    public Reader(NetworkConnection nc){
        netConnection=nc;
    }
    public void setMessage(String msg){
        this.msg=msg;
    }


    @Override
    public void run() {
        while(true){
            String msg;
            Object obj=netConnection.read();
            msg = (String) obj;
            //Data dataObj=(Data)obj;
            setMessage(msg);
            System.out.println("Received : "+msg);
            check = false;
        }
    }

}
