import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class InteractivityHandler {
    private static final double STROKE_WIDTH = 2.5;
    private static final double DOT_RADIUS = 2.5;
    private static final Color HINT_COLOR = Color.LIGHTBLUE;
    private Point2D prevPoint;
    private Point2D hintDot;
    private Line hintLine;
    private Ellipse hintEllipse;
    private Arc hintArc;
    private Polyline hintPolyline;

    public void handleLineDrawing(Pane canvas, Pane hintCanvas) {
        canvas.setOnMousePressed(press -> {
            double x = press.getX(), y = press.getY();
            prevPoint = new Point2D(x, y);
            drawPointAt(canvas, x, y, HINT_COLOR);
        });
        canvas.setOnMouseDragged(drag -> {
            // Draw the hint line
            double x = drag.getX(), y = drag.getY();
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
            double x = release.getX(), y = release.getY();
            Line line = new Line(prevPoint.getX(), prevPoint.getY(), x, y);
            line.setStrokeWidth(STROKE_WIDTH);
            prevPoint = null;
            hintCanvas.getChildren().remove(hintLine);
            canvas.getChildren().add(line);
            System.out.println(line.toString());
            drawPointAt(canvas, x, y, HINT_COLOR);
        });
    }

    public void handleEllipseDrawing(Pane canvas, Pane hintCanvas) {
        canvas.setOnMousePressed(click -> {
            double x = click.getX(), y = click.getY();
            prevPoint = new Point2D(x, y);
            drawPointAt(canvas, x, y, HINT_COLOR);
        });
        canvas.setOnMouseDragged(drag -> {
            // Draw the hint circle
            double x = drag.getX(), y = drag.getY();
            hintCanvas.getChildren().remove(hintEllipse);
            DoubleProperty radiusX = new SimpleDoubleProperty(Math.abs(x-prevPoint.getX()));
            DoubleProperty radiusY = new SimpleDoubleProperty(Math.abs(y-prevPoint.getY()));
            hintEllipse = new Ellipse(prevPoint.getX(), prevPoint.getY(), radiusX.get(), radiusY.get());
            radiusX.addListener(ov -> hintEllipse.radiusXProperty().bind(radiusX));
            radiusY.addListener(ov -> hintEllipse.radiusYProperty().bind(radiusY));
            hintEllipse.setStroke(HINT_COLOR);
            hintEllipse.setFill(null);
            hintCanvas.getChildren().add(hintEllipse);
        });
        canvas.setOnMouseReleased(release -> {
            // Draw the circle
            double x = release.getX(), y = release.getY();
            double radiusX = Math.abs(x-prevPoint.getX());
            double radiusY = Math.abs(y-prevPoint.getY());
            Ellipse circle = new Ellipse(prevPoint.getX(),prevPoint.getY(),radiusX,radiusY);
            circle.setStroke(Color.BLACK); circle.setStrokeWidth(STROKE_WIDTH); circle.setFill(null);
            hintCanvas.getChildren().remove(hintEllipse);
            hintCanvas.getChildren().removeAll();
            canvas.getChildren().add(circle);
            System.out.println(circle.toString());
            prevPoint = null;
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
                canvas.getChildren().add(hintPolyline);
                hintPolyline = null;
                System.out.println("Right-click");
            }
        });
    }

    public void handleArcDrawing(Pane canvas, Pane hintCanvas) {
        canvas.setOnMousePressed(click -> {
            double x = click.getX(), y = click.getY();
            prevPoint = new Point2D(x, y);
            drawPointAt(canvas, x, y, HINT_COLOR);
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
            Arc arc = new Arc(x, prevPoint.getY(), radiusX, radiusY, startAngle, 90);
            arc.setStroke(Color.BLACK); arc.setStrokeWidth(STROKE_WIDTH); arc.setFill(null);
            hintCanvas.getChildren().remove(hintArc);
            canvas.getChildren().add(arc);
            System.out.println(arc.toString());
            drawPointAt(canvas, x, y, HINT_COLOR);
            prevPoint = null;
        });
    }

    public void handleSemicircleDrawing(Pane canvas, Pane hintCanvas) {
        canvas.setOnMousePressed(click -> {
            double x = click.getX(), y = click.getY();
            prevPoint = new Point2D(x, y);
            drawPointAt(canvas, x, y, HINT_COLOR);
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
            hintArc = new Arc(centerX.get(), prevPoint.getY(), radiusX.get(), radiusY.get(), startAngle.get(), 180);
            centerX.addListener(ov -> hintArc.centerXProperty().bind(centerX));
            radiusX.addListener(ov -> hintArc.radiusXProperty().bind(radiusX));
            radiusY.addListener(ov -> hintArc.radiusYProperty().bind(radiusY));
            startAngle.addListener(ov -> hintArc.startAngleProperty().bind(startAngle));
            hintArc.setStroke(HINT_COLOR);
            hintArc.setFill(null);
            hintCanvas.getChildren().add(hintArc);
            System.out.println(hintArc.toString());
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
            Arc arc = new Arc(x, prevPoint.getY(), radiusX, radiusY, startAngle, 180);
            arc.setStroke(Color.BLACK); arc.setStrokeWidth(STROKE_WIDTH); arc.setFill(null);
            hintCanvas.getChildren().remove(hintArc);
            canvas.getChildren().add(arc);
            System.out.println(arc.toString());
            prevPoint = null;
        });
    }

    public void highlightGridPoints(Pane canvasBackground, Pane hintCanvas) {
        canvasBackground.setOnMouseMoved(e -> {
//            System.out.println(e.getSceneY()+", "+e.getSceneY());
//            if (e.getSceneX()%Main.GRID_X_GAP==0 && e.getSceneY()%Main.GRID_Y_GAP==0) {
//                drawPointAt(hintCanvas,e.getSceneX(),e.getSceneY(),HINT_COLOR);
//            }
        });
    }

    public void drawPointAt(Pane pane, double x, double y, Color color) {
        pane.getChildren().add(new Circle(x, y, InteractivityHandler.DOT_RADIUS, color));
    }
}
