import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class UIHandler {

    private Color HINT_COLOR = Color.GRAY;

    public void highlightGridPoints(Pane canvasBackground, Pane hintCanvas) {
        canvasBackground.setOnMouseMoved(e -> {
            System.out.println(e.getSceneY()+", "+e.getSceneY()); // for diagnotics
//            if (e.getSceneX()%Main.GRID_X_GAP==0 && e.getSceneY()%Main.GRID_Y_GAP==0) {
                (new DrawingHelper()).drawDot(hintCanvas,e.getSceneX(),e.getSceneY(),HINT_COLOR);
//            }
        });
    }
}
