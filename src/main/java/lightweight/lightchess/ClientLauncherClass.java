package lightweight.lightchess;


import javafx.application.Application;

public class ClientLauncherClass {
    public static void main(String[] args) {
        Main m = new Main();
        Application.launch(m.getClass(), args);
    }
}
