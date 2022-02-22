/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweight.lightchess.net;

import java.io.Serializable;

public class Data implements Serializable,Cloneable{
    
    public String content;
    public CommandTypes cmd;
    public String receiver,sender;

    public Data(){
        content = receiver = ""; cmd = CommandTypes.msg;
    }

    public Data(String rec, CommandTypes cm, String msg){
        receiver = rec;
        cmd = cm;
        content = msg;
    }

    public Data(String sen, String rec, CommandTypes cm, String msg){
        receiver = rec;
        sender = sen;
        cmd = cm;
        content = msg;
    }

    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }  
}
  