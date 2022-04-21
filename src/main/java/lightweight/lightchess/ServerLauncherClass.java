package lightweight.lightchess;


import javafx.application.Application;

public class ServerLauncherClass {
    public static void main(String[] args) {
        ServerUI m = new ServerUI();
        Application.launch(m.getClass(), args);
    }
}
