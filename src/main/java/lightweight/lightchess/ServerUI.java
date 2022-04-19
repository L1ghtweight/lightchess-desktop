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
import lightweight.lightchess.client.ui.HostedTournamentInfo;
import lightweight.lightchess.server.net.ServerNet;

import java.io.IOException;

public class ServerUI extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    public ClientNet clientNet;
    public ServerNet serverNet;

    @Override
    public void start(Stage primaryStage) throws Exception {
        serverNet = new ServerNet();

        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HostTournament.fxml"));
        rootLayout = loader.load();
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        HostTournament controller = loader.getController();
        controller.serverNet = serverNet;
        serverNet.startServer();
        System.out.println("Server started");
        controller.serverUI = this;
        primaryStage.show();
    }

    public void showHostedTournamentInfo() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HostedTournamentInfo.fxml"));
        rootLayout = loader.load();
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        HostedTournamentInfo controller = loader.getController();
        controller.serverNet = serverNet;
        controller.serverUI = this;
        primaryStage.show();
    }
}
