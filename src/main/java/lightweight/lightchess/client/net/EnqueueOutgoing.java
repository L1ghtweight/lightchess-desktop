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
            } else if(cmd.equals("getlist")){
                client.fetchUsersList();
            } else if(cmd.equals("list")){
                client.fetchUsersList();
                while(!client.isUsersListFetched){}

            }
            else if(cmd.equals("ip")){
                data.cmd = CommandTypes.get_ip;
            } else if(cmd.equals("play")){
                data.cmd = CommandTypes.request_to_play;
                data.receiver = message[1];
            } else if(cmd.equals("accept")){
                data.cmd = CommandTypes.playrequest_accepted;
                data.receiver = client.opponentUsername;
                client.startMatch(client.opponentUsername);
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
            else if(cmd.equals("register")){
                data.cmd = CommandTypes.register_for_tournament;
            } else  if(cmd.equals("ready")){
                data.cmd = CommandTypes.ready_to_play;
            }

            else if(cmd.equals("getscore")){
                client.fetchScoreBoard();
                continue;
            }
            else if(cmd.equals("printscore")){
                client.printScoreBoard();
                continue;
            }
            else if(cmd.equals("finish")){
                data.cmd = CommandTypes.tournament_match_end;
                data.content = message[1];
            }

            else if(cmd.equals("userinfo")){
                data.cmd = CommandTypes.get_user_info;
                data.content = message[1];
            }
            else if(cmd.equals("getinfo")){
                client.fetchUpdatedUserInfo();
            }
            else if(cmd.equals("prntinfo")){
                client.printUserInfo(true);
                continue;
            }
            else if(cmd.equals("get_op_inf")){
                client.fetchUserInfo(client.opponentUsername);
                continue;
            }

            else if(cmd.equals("print_op_inf")){
                client.printUserInfo(false);
                continue;
            }

            else if(cmd.equals("check_ul")){
                {
                System.out.println("User List Fetched --> " + client.isUsersListFetched);
            }


            }
            Q.add(data);
        }
    }
    
}
