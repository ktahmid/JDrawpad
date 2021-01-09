import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class DrawingHandler {

    private Point2D prevPoint;
    private DrawingHelper.Dot hintDot1;
    private DrawingHelper.Dot hintDot2;
    private Line hintLine;
    private Ellipse hintEllipse;
    private Arc hintArc;
    private Polyline hintPolyline;
    private static final Color HINT_COLOR = Color.LIGHTBLUE;
    private static final double HINT_STROKE_WIDTH = 1.5;

    private DrawingHelper d = new DrawingHelper();


    public void handleLineDrawing(Pane canvas, Pane hintCanvas) {
        canvas.setOnMousePressed(press -> {
            double x = press.getX(), y = press.getY();
            prevPoint = new Point2D(x, y);
            hintDot1 = new DrawingHelper.Dot(x, y, HINT_COLOR);
            hintCanvas.getChildren().add(hintDot1);
        });
        canvas.setOnMouseDragged(drag -> {
            DoubleProperty cursorX = new SimpleDoubleProperty(drag.getX());
            DoubleProperty cursorY = new SimpleDoubleProperty(drag.getY());

            // Draw the hint line
            hintCanvas.getChildren().remove(hintLine);
            hintLine = new Line(prevPoint.getX(), prevPoint.getY(), cursorX.get(), cursorY.get());
            hintLine.setStroke(HINT_COLOR);
            hintCanvas.getChildren().add(hintLine);

            // Draw the 2nd hint point
            hintCanvas.getChildren().remove(hintDot2);
            hintDot2 = new DrawingHelper.Dot(cursorX.get(), cursorY.get(), HINT_COLOR);
            hintCanvas.getChildren().add(hintDot2);

            // Bind them to the cursor's coordinates
            cursorX.addListener(ov -> { hintLine.endXProperty().bind(cursorX); hintDot2.centerXProperty().bind(cursorX); });
            cursorY.addListener(ov -> { hintLine.endYProperty().bind(cursorY); hintDot2.centerYProperty().bind(cursorY); });
        });
        canvas.setOnMouseReleased(release -> {
            // Clear the hintCanvas
            hintCanvas.getChildren().clear();

            // Draw the line
            d.drawLine(canvas,
                    prevPoint.getX(), prevPoint.getY(), release.getX(), release.getY()
            );
            prevPoint = null;
        });
        canvas.setOnMouseClicked(e->{});
    }

    public void handlePolylineDrawing(Pane canvas, Pane hintCanvas) {
        canvas.setOnMouseClicked(click -> {
            if (click.getButton() == MouseButton.PRIMARY) {
                if (hintPolyline == null) {
                    hintPolyline = new Polyline(click.getX(), click.getY());
                    hintCanvas.getChildren().add(hintPolyline);
                } else {
                    hintPolyline.getPoints().addAll(click.getX(), click.getY());
                }
            } else if (click.getButton() == MouseButton.SECONDARY) {
                // Draw the Polyline
                hintPolyline.getPoints().addAll(click.getX(), click.getY());
                d.drawPolyline(canvas, hintPolyline);

                // Reset the hintPolyline
                hintPolyline = null;

                // Clear the hintCanvas
                hintCanvas.getChildren().removeAll();
                hintCanvas.getChildren().clear();
            }
        });
        canvas.setOnMousePressed(e->{});
        canvas.setOnMouseDragged(e->{});
        canvas.setOnMouseReleased(e->{});
    }

    public void handleArcDrawing(Pane canvas, Pane hintCanvas) {
        canvas.setOnMousePressed(click -> {
            double x = click.getX(), y = click.getY();
            prevPoint = new Point2D(x, y);
            hintDot1 = new DrawingHelper.Dot(x, y, HINT_COLOR);
            hintCanvas.getChildren().add(hintDot1);
        });
        canvas.setOnMouseDragged(drag -> {
            // Draw the hint arc
            double x = drag.getX(), y = drag.getY();
            hintCanvas.getChildren().remove(hintArc);
            DoubleProperty centerX = new SimpleDoubleProperty(drag.getX());
            DoubleProperty radiusX = new SimpleDoubleProperty(Math.abs(x-prevPoint.getX()));
            DoubleProperty radiusY = new SimpleDoubleProperty(Math.abs(prevPoint.getY()-y));
            DoubleProperty startAngle = new SimpleDoubleProperty(
                    (y >= prevPoint.getY())
                            ? ((x >= prevPoint.getX()) ? 180 : 270)
                            : ((x >= prevPoint.getX()) ?  90 :   0)
            );
            hintArc = new Arc(centerX.get(), prevPoint.getY(), radiusX.get(), radiusY.get(), startAngle.get(), 90);
            centerX.addListener(ov -> hintArc.centerXProperty().bind(centerX));
            radiusX.addListener(ov -> hintArc.radiusXProperty().bind(radiusX));
            radiusY.addListener(ov -> hintArc.radiusYProperty().bind(radiusY));
            startAngle.addListener(ov -> hintArc.startAngleProperty().bind(startAngle));

            hintArc.setStroke(HINT_COLOR);
            hintArc.setFill(null);
            hintCanvas.getChildren().add(hintArc);
        });
        canvas.setOnMouseReleased(release -> {
            // Clear the hintCanvas
            hintCanvas.getChildren().removeAll();
            hintCanvas.getChildren().clear();

            // Draw the arc
            double x = release.getX(), y = release.getY();
            double radiusX = Math.abs(x - prevPoint.getX());
            double radiusY = Math.abs(y - prevPoint.getY());
            double startAngle =
                    (y >= prevPoint.getY())
                            ? ((x >= prevPoint.getX()) ? 180 : 270)
                            : ((x >= prevPoint.getX()) ?  90 :   0);

            hintDot1 = new DrawingHelper.Dot(x, y, HINT_COLOR);
            hintCanvas.getChildren().add(hintDot1);
            d.drawArc(canvas, x, prevPoint.getY(), radiusX, radiusY, startAngle);
            prevPoint = null;
        });
        canvas.setOnMouseClicked(e->{});
    }

    public void handleEllipseDrawing(Pane canvas, Pane hintCanvas) {
        canvas.setOnMousePressed(click -> {
            double x = click.getX(), y = click.getY();
            prevPoint = new Point2D(x, y);
            hintDot1 = new DrawingHelper.Dot(x, y, HINT_COLOR);
            hintCanvas.getChildren().add(hintDot1);
        });
        canvas.setOnMouseDragged(drag -> {
            // Draw the hint ellipse
            DoubleProperty radiusX = new SimpleDoubleProperty(Math.abs(drag.getX()-prevPoint.getX()));
            DoubleProperty radiusY = new SimpleDoubleProperty(Math.abs(drag.getY()-prevPoint.getY()));

            hintCanvas.getChildren().removeAll();
            hintEllipse = new Ellipse(prevPoint.getX(), prevPoint.getY(), radiusX.get(), radiusY.get());
            radiusX.addListener(ov -> hintEllipse.radiusXProperty().bind(radiusX));
            radiusY.addListener(ov -> hintEllipse.radiusYProperty().bind(radiusY));

            hintEllipse.setStroke(HINT_COLOR);
            hintEllipse.setFill(null);
            hintCanvas.getChildren().add(hintEllipse);
        });
        canvas.setOnMouseReleased(release -> {
            // Clear the hintCanvas
            hintCanvas.getChildren().removeAll();
            hintCanvas.getChildren().clear();

            // Draw the ellipse
            double radiusX = Math.abs(release.getX()-prevPoint.getX());
            double radiusY = Math.abs(release.getY()-prevPoint.getY());
            d.drawEllipse(canvas, prevPoint.getX(),prevPoint.getY(), radiusX,radiusY);
            prevPoint = null;
        });
        canvas.setOnMouseClicked(e->{});
    }
}
