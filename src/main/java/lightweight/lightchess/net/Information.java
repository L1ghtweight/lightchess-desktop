package lightweight.lightchess.net;

public class Information {
    public String username,id;
    public int score = 0;
    public NetworkConnection netConnection;
    public Information(String userId, NetworkConnection nConnection){
        id=userId;
        netConnection=nConnection;
    }

}
