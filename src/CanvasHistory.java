import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class CanvasHistory {
    private ArrayList<Pane> paneSnapshots;
    private Pane canvasHolder;
    private int currentIndex;

    CanvasHistory(Pane canvasInitialSnapshot, Pane canvasHolder) {
        Pane canvasClone = new Pane();
        canvasClone.setPrefHeight(Main.canvas.getPrefHeight());
        canvasClone.setPrefWidth(Main.canvas.getPrefWidth());
        canvasClone.getChildren().addAll(Main.canvas.getChildren());

        this.canvasHolder = canvasHolder;
        paneSnapshots = new ArrayList<Pane>();
        paneSnapshots.add(canvasClone);
        currentIndex = 0;
    }

    public Pane getCurrent() {
        return paneSnapshots.get(currentIndex);
    }

    public void update(Pane canvas) {
//        canvasHolder.getChildren().remove(getCurrent());
        Pane canvasClone = new Pane();
        canvasClone.setStyle("-fx-background-color: #FFFFFF;");
        canvasClone.setPrefHeight(canvas.getPrefHeight());
        canvasClone.setPrefWidth(canvas.getPrefWidth());
        canvasClone.getChildren().addAll(canvas.getChildren());

        Main.canvasBackground.getChildren().remove(Main.canvas);
        currentIndex++;
        Main.canvas = canvasClone;
        paneSnapshots.add(currentIndex, canvasClone);
        Main.canvasBackground.getChildren().add(Main.canvas);

//        currentIndex++;
//        canvasHolder.getChildren().add(getCurrent());
        System.out.println("Total: "+paneSnapshots.size()+", Current: "+(currentIndex+1)); // diagnostics
    }

    public void undo() {
        System.out.print("in History.  "); // diagnostics
        if (!canUndo()) {
            System.out.println("can't undo"); // diagnostics
        } else {
            System.out.print("total: "+paneSnapshots.size()+", previously: " + currentIndex); // diagnostics
            Main.canvasBackground.getChildren().remove(getCurrent());
            currentIndex--;
            Main.canvasBackground.getChildren().add(getCurrent());
            System.out.println(", current: " + currentIndex + ". undid successfully."); // diagnostics
        }
    }

    public void redo() {
        System.out.print("in History: ");
        if (!canRedo()) {
            System.out.println("can't redo");
        } else {
            System.out.print("total: "+paneSnapshots.size()+", previously: " + currentIndex); // diagnostics
            Main.canvasBackground.getChildren().remove(getCurrent());
            currentIndex++;
            Main.canvasBackground.getChildren().add(getCurrent());
            System.out.println(", current: " + currentIndex + ". redid successfully."); // diagnostics
        }
    }

    public boolean canUndo() {
        return (currentIndex > 0);
    }

    public boolean canRedo() {
        return (currentIndex < paneSnapshots.size() - 1);
    }
}
