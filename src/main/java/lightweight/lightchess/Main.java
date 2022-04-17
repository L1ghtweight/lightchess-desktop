package lightweight.lightchess;

import com.github.bhlangonijr.chesslib.Board;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lightweight.lightchess.client.net.ClientNet;
import lightweight.lightchess.client.ui.ChessBoard;
import lightweight.lightchess.client.ui.Dashboard;
import lightweight.lightchess.client.ui.Login;
import lightweight.lightchess.client.ui.SignUp;
import lightweight.lightchess.logic.Logic;

import java.io.IOException;
import java.util.List;

public class Main extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    ClientNet clientNet;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.primaryStage.setResizable(false);

        Board gameBoard = new Board();
        Logic logic = new Logic();

        //Debug stuff

        final Parameters params = getParameters();
        final List<String> args = params.getRaw();

        ChessBoard chessBoard = new ChessBoard(500, Color.web("#f0d9b5"), Color.web("#b58863"), gameBoard, logic);
        clientNet = new ClientNet(chessBoard);
        clientNet.main = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                clientNet.start(args);
            }
        }).start();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(loader.load(), 500, 800);

        Login controller = loader.getController();
        controller.setMain(this);
        controller.setClientNet(clientNet);

        this.primaryStage.centerOnScreen();
        this.primaryStage.setTitle("LightChess");
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
    }

    public void showSignUp() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
        rootLayout = loader.load();
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        SignUp controller = loader.getController();
        controller.setMain(this);
        controller.setClientNet(clientNet);
    }

    public void showLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        rootLayout = loader.load();
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        Login controller = loader.getController();
        controller.setMain(this);
        controller.setClientNet(clientNet);
    }

    public void showDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
        rootLayout = loader.load();
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        Dashboard controller = loader.getController();
        controller.setMain(this);
        controller.setClientNet(clientNet);
        primaryStage.centerOnScreen();
    }

    public void showChessBoard() throws IOException {
        Scene scene = new Scene(clientNet.chessBoard, 500, 500);
        primaryStage.setScene(scene);
    }

    public void loginResponse(boolean ok) throws IOException {
        if(ok)
            showChessBoard();
    }

    public void signUpResponse(boolean ok) throws IOException {
        System.out.println("Response " + ok);
        if(ok)
            showLogin();
    }

}
