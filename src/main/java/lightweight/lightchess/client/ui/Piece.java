package lightweight.lightchess.client.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Piece extends ImageView {
    private double mouseX;
    private double mouseY;

    public Piece(Image image) {
        super(image);

        setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
            System.out.println("clicked");
        });

        setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mouseX;
            double deltaY = event.getSceneY() - mouseY;

            relocate(getLayoutX() + deltaX, getLayoutY() + deltaY);

            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });
    }
}
