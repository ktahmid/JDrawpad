import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class History {
    private ArrayList<Pane> paneSnapshots;
    private int currentIndex;

    History(Pane snapshot) {
        paneSnapshots = new ArrayList<Pane>();
        paneSnapshots.add(snapshot);
        currentIndex = 0;
    }

    public Pane current() {
        return paneSnapshots.get(currentIndex);
    }

    public void update(Pane snapshot) {
        paneSnapshots.add(snapshot);
        currentIndex++;
        System.out.println("UPDATED"); // diagnostics
    }

    public void undo() {
        currentIndex--;
        System.out.println("Undo CALLED"); // diagnostics
    }

    public void redo() {
        currentIndex++;
    }

    public boolean canUndo() {
        return (currentIndex > 0);
    }

    public boolean canRedo() {
        return (currentIndex < paneSnapshots.size()-1);
    }
}
