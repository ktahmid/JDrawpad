import javafx.scene.shape.*;

public class ShapeStringGenerator {

    public static String getStr(Shape shape, int w, int h) {
        if (shape instanceof Line)
            return getStr((Line) shape, w, h);
        else if (shape instanceof Polyline)
            return getStr((Polyline) shape, w, h);
        else if (shape instanceof Arc)
            return getStr((Arc) shape, w, h);
        else if (shape instanceof Ellipse)
            return getStr((Ellipse) shape, w, h);
        else
            return "\n";
    }

    public static String getStr(Line line, int w, int h) {
        return "line," +
                convert(line.getStartX(), w) + "," +
                convert(line.getStartY(), h) + "," +
                convert(line.getEndX(), w) + "," +
                convert(line.getEndY(), h);
    }

    public static String getStr(Polyline polyline, int w, int h) {
        StringBuilder str = new StringBuilder("polyline");
        for (int i = 0; i < polyline.getPoints().size(); i += 2) {
            str.append(",").append(convert(polyline.getPoints().get(i), w));
            str.append(",").append(convert(polyline.getPoints().get(i + 1), h));
        }
        return str.toString();
    }

    public static String getStr(Arc arc, int w, int h) {
        return "arc," +
                convert(arc.getCenterX(), w) + "," +
                convert(arc.getCenterY(), h) + "," +
                convert(arc.getRadiusX(), w) + "," +
                convert(arc.getRadiusY(), h) + "," +
                arc.getStartAngle();
    }

    public static String getStr(Ellipse ellipse, int w, int h) {
        return "ellipse," +
                convert(ellipse.getCenterX(), w) + "," +
                convert(ellipse.getCenterY(), h) + "," +
                convert(ellipse.getRadiusX(), w) + "," +
                convert(ellipse.getRadiusY(), h);
    }

    private static double convert(double xOrY, int widthORHeight) {
        return xOrY - widthORHeight / 2;
    }
}
