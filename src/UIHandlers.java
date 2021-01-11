import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class UIHandlers {
    Pane canvas;
    Pane canvasBackground;
    double xGap;
    double yGap;

    public UIHandlers(Pane canvas, Pane canvasBackground, double xGap, double yGap) {
        this.xGap = xGap;
        this.yGap = yGap;
        this.canvas = canvas;
        this.canvasBackground = canvasBackground;
    }

    public void showCoordsInStatusBar(Label xLabel, Label yLabel, Pane canvasBackground) {
        canvasBackground.setOnMouseEntered(new StatusBarHandler(xLabel, yLabel, canvasBackground));
        canvasBackground.setOnMouseMoved(new StatusBarHandler(xLabel, yLabel, canvasBackground));
        canvasBackground.setOnMouseDragged(new StatusBarHandler(xLabel, yLabel, canvasBackground));
        canvasBackground.setOnMouseExited(e -> {
            xLabel.textProperty().bind(new SimpleStringProperty("--"));
            yLabel.textProperty().bind(new SimpleStringProperty("--"));
        });
    }

    private class StatusBarHandler implements EventHandler<MouseEvent> {
        private Pane gridContainer;
        Label xLabel, yLabel;

        public StatusBarHandler(Label xLabel, Label yLabel, Pane gridContainer) {
            this.gridContainer = gridContainer;
            this.xLabel = xLabel;
            this.yLabel = yLabel;
        }

        @Override
        public void handle(MouseEvent mouseEvent) {
            StringProperty x = new SimpleStringProperty(
                    String.format("%.2f", mouseEvent.getX()- gridContainer.getWidth()/2)
            );
            StringProperty y = new SimpleStringProperty(
                    String.format("%.2f", gridContainer.getHeight()/2-mouseEvent.getY())
            );
            xLabel.textProperty().bind(x);
            yLabel.textProperty().bind(y);
        }
    }
}
