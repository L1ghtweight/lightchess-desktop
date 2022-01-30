/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightweight.lightchess.net;

import java.io.Serializable;

public class Data implements Serializable,Cloneable{
    
    public String message;
    public String cmd;
    public String receiver,sender;

    public Data(){
        message = cmd = receiver = "";
    }

    public Data(String rec,String cm,String msg){
        receiver = rec;
        cmd = cm;
        message = msg;
    }

    public Data(String sen,String rec,String cm,String msg){
        receiver = rec;
        sender = sen;
        cmd = cm;
        message = msg;
    }

    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }  
}
  