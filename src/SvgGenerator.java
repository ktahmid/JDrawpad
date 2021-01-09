import javafx.scene.shape.*;

public class SvgGenerator {

    public static String convert(Shape shape) {
        if (shape instanceof Line)
            return convert((Line) shape);
        else if (shape instanceof Polyline)
            return convert((Polyline) shape);
        else if (shape instanceof Arc)
            return convert((Arc) shape);
        else if (shape instanceof Ellipse)
            return convert((Ellipse) shape);
        else
            return "\n";
    }

    public static String convert(Line line) {
        return "<path d=\"" +
                " M " + line.getStartX() + "," + line.getStartY() +
                " L " + line.getEndX() + "," + line.getEndY() +
                "\" stroke-width=\"" + line.getStrokeWidth() + "px\"" +
                " stroke=\"black\" fill=\"none\" />\n";
    }

    public static String convert(Polyline polyline) {
        String path = "<path d=\" M " + polyline.getPoints().get(0) + "," + polyline.getPoints().get(1);
        for (int i = 2; i < polyline.getPoints().size(); i += 2) {
            path += " L " + polyline.getPoints().get(i);
            path += "," + polyline.getPoints().get(i + 1);
        }
        path += "\" stroke-width=\"" + polyline.getStrokeWidth() + "px\"" +
                " stroke=\"black\" fill=\"none\" />\n";
        return path;
    }

    public static String convert(Arc arc) {
        double mY = arc.getCenterY();
        double aX = arc.getCenterX();
        double aY = mY + (arc.getStartAngle() <= 90 ? -arc.getRadiusY() : arc.getRadiusY());
        double mX = aX + (arc.getStartAngle()==90 || arc.getStartAngle()==180 ? -arc.getRadiusX() : arc.getRadiusX());
        return "<path d=\"" +
                " M " + mX + "," + mY +
                " A " + arc.getRadiusX() + "," + arc.getRadiusY() +
                " 0 0 1 " + aX + "," + aY +
                "\" stroke-width=\"" + arc.getStrokeWidth() + "px\"" +
                " stroke=\"black\" fill=\"none\" />\n";
    }

    public static String convert(Ellipse ellipse) {
        double cX = ellipse.getCenterX(), cY = ellipse.getCenterY();
        double rX = ellipse.getRadiusX(), rY = ellipse.getRadiusY();
        return "<ellipse" +
                " cx=\""+cX+"\" " + "cy=\""+cY+"\"" +
                " rx=\""+rX+"\" " + "ry=\""+rY+"\"" +
                " stroke-width=\"" + ellipse.getStrokeWidth() + "px\"" +
                " stroke=\"black\" fill=\"none\" />\n";
    }
}
