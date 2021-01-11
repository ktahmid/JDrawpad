import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class DrawingHelper {
    private static final double STROKE_WIDTH = 2.5;
    private static final double DOT_RADIUS = 2.5;


    public static class Dot extends Circle {
        public double x, y;
        Dot(double x, double y, Color col) { super(x, y, DOT_RADIUS, col); setStroke(null);}
    }

    public void drawLine(Pane canvas, double x1,double y1, double x2,double y2) {
        Line line = new Line(x1,y1, x2,y2);
        line.setStrokeWidth(STROKE_WIDTH);
        canvas.getChildren().addAll(line);
    }

    public void drawPolyline(Pane canvas, Polyline polyline) {
        polyline.setStroke(Color.BLACK); polyline.setStrokeWidth(STROKE_WIDTH);
        canvas.getChildren().add(polyline);
    }

    public void drawArc(Pane canvas, double centerX,double centerY, double radiusX,double radiusY, double startAngle) {
        Arc arc = new Arc(centerX, centerY, radiusX, radiusY, startAngle, 90);
        arc.setStroke(Color.BLACK); arc.setStrokeWidth(STROKE_WIDTH); arc.setFill(null);
        canvas.getChildren().add(arc);
    }

    public void drawEllipse(Pane canvas, double centerX, double centerY, double radiusX, double radiusY) {
        Ellipse ellipse = new Ellipse(centerX,centerY, radiusX,radiusY);
        ellipse.setStroke(Color.BLACK); ellipse.setStrokeWidth(STROKE_WIDTH); ellipse.setFill(null);
        canvas.getChildren().add(ellipse);
    }

    public void drawDot(Pane pane, double x, double y, Color color) {
        pane.getChildren().add(new Circle(x, y, DOT_RADIUS, color));
    }
}
