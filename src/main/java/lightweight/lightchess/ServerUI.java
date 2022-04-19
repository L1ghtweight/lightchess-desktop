package lightweight.lightchess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lightweight.lightchess.Main;
import lightweight.lightchess.client.net.ClientNet;
import lightweight.lightchess.client.ui.Dashboard;
import lightweight.lightchess.client.ui.HostTournament;
import lightweight.lightchess.server.net.ServerNet;

public class ServerUI extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    public ClientNet clientNet;
    public ServerNet serverNet = new ServerNet();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HostTournament.fxml"));
        rootLayout = loader.load();
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        HostTournament controller = loader.getController();
        serverNet.startServer();

        primaryStage.show();
    }
}
