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
    private static final Color HINT_COLOR = Color.BLUE;
    private static final double HINT_THICKNESS = 1.5;

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
            hintLine.setStroke(HINT_COLOR); hintLine.setStrokeWidth(HINT_THICKNESS);
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
            // If left-click...
            if (click.getButton() == MouseButton.PRIMARY) {
                if (hintPolyline == null) {  // If its the first click for this polyline, ...
                    // ...begin drawing the hint polyline
                    hintPolyline = new Polyline(click.getX(), click.getY());
                    hintPolyline.setStroke(HINT_COLOR); hintPolyline.setStrokeWidth(HINT_THICKNESS);
                    hintCanvas.getChildren().add(hintPolyline);
                } else {
                    // ...otherwise, add one more point to the existing one
                    hintPolyline.getPoints().addAll(click.getX(), click.getY());
                }
            }
            // If right-click, its the last point of the polyline
            else if (click.getButton() == MouseButton.SECONDARY) {
                // Draw the Polyline
                hintPolyline.getPoints().addAll(click.getX(), click.getY());
                d.drawPolyline(canvas, hintPolyline);

                // Reset hintPolyline
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
            double x = drag.getX(), y = drag.getY();
            DoubleProperty cursorX = new SimpleDoubleProperty(drag.getX());
            DoubleProperty cursorY = new SimpleDoubleProperty(drag.getY());
            DoubleProperty radiusX = new SimpleDoubleProperty(Math.abs(x-prevPoint.getX()));
            DoubleProperty radiusY = new SimpleDoubleProperty(Math.abs(y-prevPoint.getY()));
            DoubleProperty startAngle = new SimpleDoubleProperty(
                    (y >= prevPoint.getY())
                            ? ((x >= prevPoint.getX()) ? 180 : 270)
                            : ((x >= prevPoint.getX()) ?  90 :   0)
            );

            // Draw the hint arc
            hintCanvas.getChildren().remove(hintArc);
            hintArc = new Arc(cursorX.get(), prevPoint.getY(), radiusX.get(), radiusY.get(), startAngle.get(), 90);
            hintArc.setStroke(HINT_COLOR); hintArc.setStrokeWidth(HINT_THICKNESS); hintArc.setFill(null);
            hintCanvas.getChildren().add(hintArc);

            // Draw the 2nd hint point
            hintCanvas.getChildren().remove(hintDot2);
            hintDot2 = new DrawingHelper.Dot(x, y, HINT_COLOR);
            hintCanvas.getChildren().add(hintDot2);

            // Binding them to the cursor
            cursorX.addListener(ov -> { hintArc.centerXProperty().bind(cursorX); hintDot2.centerXProperty().bind(cursorX); });
            cursorY.addListener(ov -> hintDot2.centerYProperty().bind(cursorY));
            radiusX.addListener(ov -> hintArc.radiusXProperty().bind(radiusX));
            radiusY.addListener(ov -> hintArc.radiusYProperty().bind(radiusY));
            startAngle.addListener(ov -> hintArc.startAngleProperty().bind(startAngle));
        });
        canvas.setOnMouseReleased(release -> {
            // Clear the hintCanvas
            hintCanvas.getChildren().clear();

            // Draw the arc
            double x = release.getX(), y = release.getY();
            double radiusX = Math.abs(x - prevPoint.getX());
            double radiusY = Math.abs(y - prevPoint.getY());
            double startAngle =
                    (y >= prevPoint.getY())
                            ? ((x >= prevPoint.getX()) ? 180 : 270)
                            : ((x >= prevPoint.getX()) ?  90 :   0);
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
            DoubleProperty cursorX = new SimpleDoubleProperty(drag.getX());
            DoubleProperty cursorY = new SimpleDoubleProperty(drag.getY());
            DoubleProperty radiusX = new SimpleDoubleProperty(Math.abs(drag.getX()-prevPoint.getX()));
            DoubleProperty radiusY = new SimpleDoubleProperty(Math.abs(drag.getY()-prevPoint.getY()));

            // Draw the hint ellipse
            hintCanvas.getChildren().remove(hintEllipse);
            hintEllipse = new Ellipse(prevPoint.getX(), prevPoint.getY(), radiusX.get(), radiusY.get());
            hintEllipse.setStroke(HINT_COLOR); hintEllipse.setStrokeWidth(HINT_THICKNESS); hintEllipse.setFill(null);
            hintCanvas.getChildren().add(hintEllipse);

            // Bind its radius to the cursor
            radiusX.addListener(ov -> hintEllipse.radiusXProperty().bind(radiusX));
            radiusY.addListener(ov -> hintEllipse.radiusYProperty().bind(radiusY));
        });
        canvas.setOnMouseReleased(release -> {
            // Clear the hintCanvas
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
