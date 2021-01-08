import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;

public class ShapeSvgGenerator {

    public String convert(Line line) {
        return "<path d=\"" +
                " M" + line.getStartX() + "," + line.getStartY() +
                " L" + line.getEndX() + "," + line.getEndY() +
                "\" stroke-width=\"" + line.getStrokeWidth() + "px\"" +
                " stroke=\"black\" fill=\"none\" />\n";
    }

    public String convert(Arc arc) {
        double mY = arc.getCenterY();
        double aX = arc.getCenterX();
        double aY = mY + (arc.getStartAngle() <= 90 ? -arc.getRadiusY() : arc.getRadiusY());
        double mX = aX + (arc.getStartAngle()==90 || arc.getStartAngle()==180 ? -arc.getRadiusX() : arc.getRadiusX());
        return "<path d=\"" +
                " M" + mX + "," + mY +
                " A" + arc.getRadiusX() + "," + arc.getRadiusY() +
                " 0 0 1 " + aX + "," + aY +
                "\" stroke-width=\"" + arc.getStrokeWidth() + "px\"" +
                " stroke=\"black\" fill=\"none\" />\n";
    }

    public String convert(Ellipse ellipse) {
        double cX = ellipse.getCenterX(), cY = ellipse.getCenterY();
        double rX = ellipse.getRadiusX(), rY = ellipse.getRadiusY();
        return "<ellipse" +
                " cx=\""+cX+"\" " + "cy=\""+cY+"\"" +
                " rx=\""+rX+"\" " + "ry=\""+rY+"\"" +
                " stroke-width=\"" + ellipse.getStrokeWidth() + "px\"" +
                " stroke=\"black\" fill=\"none\" />\n";
    }

}
