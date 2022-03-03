package lightweight.lightchess.net;

public class Information {
    public String username,id;
    public NetworkConnection netConnection;
    public Information(String userId, NetworkConnection nConnection){
        id=userId;
        netConnection=nConnection;
    }

}
