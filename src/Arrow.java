import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
class Arrow implements EventHandler<MouseEvent> {
    private boolean linestate = false;
    private Line main_line, arr_1, arr_2;
    private Pane  pane;
    private double  x1, y1, x2, y2;
    double in_arrangle = Math.toRadians(35);// more degree more angles towards the main line
    double arrlen = 10;

    public Arrow( Pane pane ) {
        this.pane = pane;
    }
    @Override
    public void handle( MouseEvent event ) {
        if( event.getEventType() == MouseEvent.MOUSE_CLICKED ) {// starts when mouse clicks on something
            if( !linestate ) {
                this.x1=event.getX(); // initially they are all at the same point
                this.x2=event.getX();
                this.y1=event.getY();
                this.y2=event.getY();
                main_line= new Line(x1,y1,x2,y2 );
                arr_1= new Line(x2,y2,x2,y2 );
                arr_2= new Line(x2,y2,x2,y2 );
                pane.getChildren().add(main_line);
                pane.getChildren().add(arr_1);
                pane.getChildren().add( arr_2 );
                linestate = true; // line is alive. time to calculate arrow heads
            } else {
                main_line = null; // no line if not clicked
                linestate = false;
            }
        } else {
            if( main_line != null ) { // if line drawn
                x2 = event.getX(); // get the end points
                y2 = event.getY();
                main_line.setEndX( x2 );
                main_line.setEndY( y2 );
                double dx = x2 - x1;
                double dy = y2 - y1;
                double slope = Math.atan2( dy, dx );// find the slope
                double x, y;
                double f_arrangle = slope + in_arrangle;
                x= x2-arrlen*Math.cos(f_arrangle);
                y= y2-arrlen*Math.sin(f_arrangle);
                arr_1.setStartX(x2);
                arr_1.setStartY(y2);
                arr_1.setEndX(x);
                arr_1.setEndY(y);
                double a2_f_arrangle =slope-in_arrangle;// 180 conversion for the other arrow head
                x= x2-arrlen*Math.cos(a2_f_arrangle);
                y= y2-arrlen*Math.sin(a2_f_arrangle);
                arr_2.setStartX(x2);
                arr_2.setStartY(y2);
                arr_2.setEndX(x);
                arr_2.setEndY(y);
                // bug found
            }
        }
    }

}