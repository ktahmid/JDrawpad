import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class DrawingHandler {
    private static DrawingHelper d = new DrawingHelper();

    private Point2D prevPoint;
    private Point2D hintDot;
    private Line hintLine;
    private Ellipse hintEllipse;
    private Arc hintArc;
    private Polyline hintPolyline;
    private static final Color HINT_COLOR = Color.LIGHTBLUE;

    public void handleLineDrawing(Pane canvas, Pane hintCanvas) {
        canvas.setOnMousePressed(press -> {
            double x = press.getX(), y = press.getY();
            prevPoint = new Point2D(x, y);
        });
        canvas.setOnMouseDragged(drag -> {
            // Draw the hint line
            hintCanvas.getChildren().remove(hintLine);
            DoubleProperty mouseX = new SimpleDoubleProperty(drag.getX());
            DoubleProperty mouseY = new SimpleDoubleProperty(drag.getY());
            hintLine = new Line(prevPoint.getX(), prevPoint.getY(), mouseX.get(), mouseY.get());
            mouseX.addListener(ov -> hintLine.endXProperty().bind(mouseX));
            mouseY.addListener(ov -> hintLine.endYProperty().bind(mouseY));
            hintLine.setStroke(HINT_COLOR);
            hintCanvas.getChildren().add(hintLine);
        });
        canvas.setOnMouseReleased(release -> {
            // Draw the line
            d.drawLine(canvas,
                    prevPoint.getX(), prevPoint.getY(), release.getX(), release.getY()
            );
            prevPoint = null;
            hintCanvas.getChildren().remove(hintLine);
        });
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
                hintPolyline.getPoints().addAll(click.getX(), click.getY());
                hintCanvas.getChildren().removeAll();
                d.drawPolyline(canvas, hintPolyline);
                hintPolyline = null;
            }
        });
    }

    public void handleArcDrawing(Pane canvas, Pane hintCanvas) {
        canvas.setOnMousePressed(click -> {
            double x = click.getX(), y = click.getY();
            prevPoint = new Point2D(x, y);
            d.drawPoint(hintCanvas, x, y, HINT_COLOR);
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
            // Draw the arc
            double x = release.getX(), y = release.getY();
            double radiusX = Math.abs(x - prevPoint.getX());
            double radiusY = Math.abs(y - prevPoint.getY());
            double startAngle =
                    (y >= prevPoint.getY())
                            ? ((x >= prevPoint.getX()) ? 180 : 270)
                            : ((x >= prevPoint.getX()) ?  90 :   0);

            d.drawPoint(hintCanvas, x, y, HINT_COLOR);
            hintCanvas.getChildren().removeAll();
            d.drawArc(canvas, x, prevPoint.getY(), radiusX, radiusY, startAngle);
            prevPoint = null;
        });
    }

    public void handleEllipseDrawing(Pane canvas, Pane hintCanvas) {
        canvas.setOnMousePressed(click -> {
            double x = click.getX(), y = click.getY();
            prevPoint = new Point2D(x, y);
            d.drawPoint(hintCanvas, x, y, HINT_COLOR);
        });
        canvas.setOnMouseDragged(drag -> {
            // Draw the hint ellipse
            DoubleProperty radiusX = new SimpleDoubleProperty(Math.abs(drag.getX()-prevPoint.getX()));
            DoubleProperty radiusY = new SimpleDoubleProperty(Math.abs(drag.getY()-prevPoint.getY()));

            hintCanvas.getChildren().remove(hintEllipse);
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

            // Draw the ellipse
            double radiusX = Math.abs(release.getX()-prevPoint.getX());
            double radiusY = Math.abs(release.getY()-prevPoint.getY());
            d.drawEllipse(canvas, prevPoint.getX(),prevPoint.getY(), radiusX,radiusY);
            prevPoint = null;
        });
    }
}
