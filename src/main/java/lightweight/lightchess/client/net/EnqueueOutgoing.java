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
    ClientNet client;

    public EnqueueOutgoing(NetworkConnection nc, LinkedBlockingQueue<Data> Q, ClientNet c){
        netConnection=nc;
        this.Q = Q;
        client = c;
    }

    @Override
    public void run() {
        
        Data data=new Data();

        while(true){
            Scanner in=new Scanner(System.in);
            String[] message = in.nextLine().split(":");
            String cmd = message[0];

            if(message.length>2){
                data.receiver = message[1];
                data.content = message[2];
            }

            if(cmd.equals("msg")){
                data.cmd = CommandTypes.msg;
            } else if(cmd.equals("logout")){
                data.cmd = CommandTypes.logout;
            }
            else if(cmd.equals("login")){
                if(message.length<3){
                    System.out.println("Provide username and passowrd");
                    continue;
                }
                data.cmd = CommandTypes.login;
                data.content = message[1];
                data.content2 = message[2];
            }
            else if(cmd.equals("list_connected")){
                data.cmd = CommandTypes.list_clients;
            } else if(cmd.equals("list")){
                data.cmd = CommandTypes.list_loggedInClients;
            }
            else if(cmd.equals("ip")){
                data.cmd = CommandTypes.get_ip;
            } else if(cmd.equals("play")){
                data.cmd = CommandTypes.requestToPlay;
                data.receiver = message[1];
            } else if(cmd.equals("accept")){
                data.cmd = CommandTypes.playRequestAccecpted;
                data.receiver = client.opponentUsername;
                client.startMatch(client.opponentUsername);
            } else if(cmd.equals("move")){
                if(!client.isMyTurn){
                    System.out.println("Not your turn, is your head okay?");
                    continue;
                }
                data.cmd = CommandTypes.move;
                data.receiver = client.opponentUsername;
                data.content = message[1];
                String move = message[1];
                if(!client.logic.makeMove(client.board,move)){
                        System.out.println("Invalid move");
                        continue;
                }
                System.out.println("Sending move to "+ data.receiver);
                System.out.println(client.board.toString());
                if(client.hasUI){
                    client.updateBoard();
                }
                client.isMyTurn = false;
            } else if(cmd.equals("getboard")){
                System.out.println(client.board.toString());
                continue;
            } else if(cmd.equals("signup")){
                data.cmd = CommandTypes.signup;
                if(message.length<3){
                    System.out.println("Provide username:password");
                    continue;
                }
                data.content = message[1];
                data.content2 = message[2];
            } else if(cmd.equals("get_tournament")){
                data.cmd = CommandTypes.get_tournament_details;
            }


            Q.add(data);
        }
    }
    
}
