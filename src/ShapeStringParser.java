import javafx.scene.shape.*;

public class ShapeStringParser {

    public static Shape getShape(String[] tokens, int w, int h) {
        switch (tokens[0]) {
            case "line":
                return new Line(
                        convert(tokens[1], w),
                        convert(tokens[2], h),
                        convert(tokens[3], w),
                        convert(tokens[4], h)
                );
            case "polyline":
                Polyline polyline = new Polyline();
                for (int i = 1; i < tokens.length; i += 2) {
                    polyline.getPoints().add(convert(tokens[i], w));
                    polyline.getPoints().add(convert(tokens[i+1], h));
                }
                return polyline;
            case "arc":
                return new Arc(
                        convert(tokens[1], w),
                        convert(tokens[2], h),
                        convert(tokens[3], w),
                        convert(tokens[4], h),
                        Double.parseDouble(tokens[5]),
                        90
                );
            case "ellipse":
                return new Ellipse(
                        convert(tokens[1], w),
                        convert(tokens[2], h),
                        convert(tokens[3], w),
                        convert(tokens[4], h)
                );
            default:
                return new Shape() {};
        }
    }

    private static double convert(String xOrY, int widthORHeight) {
        return Double.parseDouble(xOrY) + widthORHeight / 2;
    }
}
