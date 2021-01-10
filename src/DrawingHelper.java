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

    public static class Arrow implements EventHandler<MouseEvent> {
        private boolean linestate = false;
        private Line main_line, arr_1, arr_2;
        private Pane pane;
        private double x1, y1, x2, y2;
        double in_arrangle = Math.toRadians(35);// more degree more angles towards the main line
        double arrlen = 10;

        public Arrow(Pane pane) {
            this.pane = pane;
        }

        @Override
        public void handle(MouseEvent event) {
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {// starts when mouse clicks on something
                if (!linestate) {
                    this.x1 = event.getX(); // initially they are all at the same point
                    this.x2 = event.getX();
                    this.y1 = event.getY();
                    this.y2 = event.getY();
                    main_line = new Line(x1, y1, x2, y2);
                    arr_1 = new Line(x2, y2, x2, y2);
                    arr_2 = new Line(x2, y2, x2, y2);
                    pane.getChildren().add(main_line);
                    pane.getChildren().add(arr_1);
                    pane.getChildren().add(arr_2);
                    main_line.setStroke(Color.BLUE);
                    main_line.setStrokeWidth(1);
                    linestate = true; // line is alive. time to calculate arrow heads
                } else {
                    main_line.setStroke(Color.BLACK);
                    main_line.setStrokeWidth(2.5);
                    main_line = null; // no line if not clicked
                    linestate = false;
                }
            } else {
                if (main_line != null) { // if line drawn
                    x2 = event.getX(); // get the end points
                    y2 = event.getY();
                    main_line.setEndX(x2);
                    main_line.setEndY(y2);
                    double dx = x2 - x1;
                    double dy = y2 - y1;
                    double slope = Math.atan2(dy, dx);// find the slope
                    double x, y;
                    double f_arrangle = slope + in_arrangle;
                    x = x2 - arrlen * Math.cos(f_arrangle);
                    y = y2 - arrlen * Math.sin(f_arrangle);
                    arr_1.setStartX(x2);
                    arr_1.setStartY(y2);
                    arr_1.setEndX(x);
                    arr_1.setEndY(y);
                    double a2_f_arrangle = slope - in_arrangle;// 180 conversion for the other arrow head
                    x = x2 - arrlen * Math.cos(a2_f_arrangle);
                    y = y2 - arrlen * Math.sin(a2_f_arrangle);
                    arr_2.setStartX(x2);
                    arr_2.setStartY(y2);
                    arr_2.setEndX(x);
                    arr_2.setEndY(y);
                    // bug found
                }
            }
        }
    }

        public void drawLine(Pane canvas, double x1,double y1, double x2,double y2) {
        Line line = new Line(x1,y1, x2,y2);
        line.setStrokeWidth(STROKE_WIDTH);
        canvas.getChildren().add(line);
    }

    public void drawPolyline(Pane canvas, Polyline polyline) {
        Polyline newPolyline = polyline;
        newPolyline.setStroke(Color.BLACK); newPolyline.setStrokeWidth(STROKE_WIDTH);
        canvas.getChildren().add(newPolyline);
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
