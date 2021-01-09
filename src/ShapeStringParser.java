import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class ShapeStringParser {

    public static Shape getShape(String[] tokens, int w, int h) {
        switch (tokens[0]) {
            case "line":
                Line line = new Line(
                        translateX(tokens[1], w),
                        translateY(tokens[2], h),
                        translateX(tokens[3], w),
                        translateY(tokens[4], h)
                );
                line.setStrokeWidth(2.5);
                line.setStroke(Color.BLACK);
                line.setFill(null);
            case "polyline":
                Polyline polyline = new Polyline(
                );
                for (int i = 1; i < tokens.length; i += 2) {
                    polyline.getPoints().add(translateX(tokens[i], w));
                    polyline.getPoints().add(translateY(tokens[i+1], h));
                }
                polyline.setStrokeWidth(2.5);
                polyline.setStroke(Color.BLACK);
                polyline.setFill(null);
                return polyline;
            case "arc":
                Arc arc = new Arc(
                        translateX(tokens[1], w),
                        translateY(tokens[2], h),
                        translateX(tokens[3], w),
                        translateY(tokens[4], h),
                        Double.parseDouble(tokens[5]),
                        90
                );
                arc.setStrokeWidth(2.5);
                arc.setStroke(Color.BLACK);
                arc.setFill(null);
            case "ellipse":
                Ellipse ellipse = new Ellipse(
                        translateX(tokens[1], w),
                        translateY(tokens[2], h),
                        translateX(tokens[3], w),
                        translateY(tokens[4], h)
                );
                ellipse.setStrokeWidth(2.5);
                ellipse.setStroke(Color.BLACK);
                ellipse.setFill(null);
            default:
                return new Shape() {};
        }
    }

    private static double translateX(String x, int width) {
        return Double.parseDouble(x) + width/2;
    }

    private static double translateY(String y, int height) {
        return height/2 - Double.parseDouble(y);
    }
}
