import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Main extends Application {

    private static MenuBar menuBar = new MenuBar();     // the menu bar
    private static GridPane toolbar = new GridPane();    // the toolbar
    private static ToggleGroup toolbarGroup = new ToggleGroup(); // toggle group for the tools, so that at most one tool can be selected at a time
    private static VBox toolbarHolder = new VBox(5);
    private static AnchorPane toolbarSection = new AnchorPane(toolbarHolder);  // left pane of the app
    private static Pane canvas;
    private static Pane hintCanvas = new Pane();
    private static Pane canvasBackground = new Pane();
    private static ScrollPane canvasSection = new ScrollPane(canvasBackground);  // middle pane of the app; scrollable
    private static ScrollPane optionsSection = new ScrollPane();  // right pane of the app; scrollable
    private static BorderPane appBody = new BorderPane(canvasSection, null, optionsSection, null, toolbarSection);  // container for everything except the menu bar
    private static VBox root = new VBox(menuBar, appBody);  // root node; container for everything

    // Menus
    private static Menu menuFile = new Menu("File");
    private static Menu menuEdit = new Menu("Edit");
    private static Menu menuHelp = new Menu("Help");

    // File menu items
    private static MenuItem mitemNew = new MenuItem("New…");
    private static MenuItem mitemOpen = new MenuItem("Open…");
    private static MenuItem mitemSave = new MenuItem("Save");
    private static MenuItem mitemSaveAs = new MenuItem("Save As…");
    private static MenuItem mitemExport = new MenuItem("Export…");
    private static MenuItem mitemExit = new MenuItem("Exit");

    // Edit menu items
    // TODO

    // Help menu items
    // TODO

    // Drawing tools
    private static ToolButton toolbtnLine = new ToolButton("Line");
//    private static ToolButton toolbtnArrow = new ToolButton("Arrow");
    private static ToolButton toolbtnPolyline = new ToolButton("Polyline");
    private static ToolButton toolbtnArc = new ToolButton("Arc");
//    private static ToolButton toolbtnSemicircle = new ToolButton("Semi-circle");
    private static ToolButton toolbtnEllipse = new ToolButton("Ellipse");

    public static final int GRID_X_GAP = 10;
    public static final int GRID_Y_GAP = 10;

    private Point2D prevPoint;
    private Line hintLine;
    private Circle hintCircle;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("NamelessDrawingApp");

        appBody.prefHeightProperty().bind(
                root.heightProperty().subtract(menuBar.heightProperty())
        );

        setupMenus();
        setupToolbar();
        setupOptionsArea();

        createCanvas(600,400);

        DrawingHandler dh = new DrawingHandler();

        toolbtnLine.setOnAction(click -> dh.handleLineDrawing(canvas, hintCanvas));
        toolbtnPolyline.setOnAction(click -> dh.handlePolylineDrawing(canvas, hintCanvas));
        toolbtnArc.setOnAction(click -> dh.handleArcDrawing(canvas,hintCanvas));
        toolbtnEllipse.setOnAction(click -> dh.handleEllipseDrawing(canvas,hintCanvas));

        mitemExport.setOnAction(e -> {
            FileChooser svgFileChooser = new FileChooser();
            File svgFile = svgFileChooser.showSaveDialog(primaryStage);
            if (svgFile != null) {
                FileUtil.exportToSvgFile(svgFile, canvas);
            }
        });

        mitemSaveAs.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File outFile = fileChooser.showSaveDialog(primaryStage);
            if (outFile != null) {
                FileUtil.saveToFile(outFile, canvas);
            }
        });
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();
    }

    private void setupOptionsArea() {
        optionsSection.setTooltip(new Tooltip("Options"));
        optionsSection.prefHeightProperty().bind(appBody.heightProperty());
    }

    private void setupToolbar() {
        toolbar.getColumnConstraints().add(new ColumnConstraints(50)); // for 1st column in the toolbar
        toolbar.getColumnConstraints().add(new ColumnConstraints(50)); // for 2nd column
        toolbar.addColumn(0, toolbtnLine, toolbtnPolyline);
        toolbar.addColumn(1, toolbtnArc, toolbtnEllipse);
        Label toolsLabel = new Label("Tools");
        toolsLabel.setFont(new Font(18));
        toolbarHolder.getChildren().addAll(toolsLabel, toolbar);
        AnchorPane.setLeftAnchor(toolbarHolder, 20.0);
        AnchorPane.setTopAnchor(toolbarHolder, 20.0);
        AnchorPane.setRightAnchor(toolbarHolder, 20.0);
    }

    private void setupMenus() {
        menuFile.getItems().addAll(
                mitemNew,
                mitemOpen,
                mitemSave,
                mitemSaveAs,
                mitemExport,
                mitemExit
        );
        mitemNew.setOnAction(e -> showNewFileDialog());
        mitemExit.setOnAction(e -> Platform.exit());

        menuBar.getMenus().addAll(menuFile, menuEdit, menuHelp);
    }

    private void showNewFileDialog() {
        Stage newFileDialog = new Stage();
        newFileDialog.setTitle("Create a new file");
        newFileDialog.setResizable(false);

        Label instruction = new Label("Enter the dimensions of the canvas in centimeters.");
        HBox topPane = new HBox(instruction);

        Label widthLabel = new Label("Width: ");
        Label multiplicationSign = new Label(" × ");
        Label heightLabel = new Label("Height: ");
        TextField widthField = new TextField();
        widthField.setPrefColumnCount(5);
        TextField heightField = new TextField();
        heightField.setPrefColumnCount(5);
        HBox middlePane = new HBox(widthLabel, widthField, multiplicationSign, heightLabel, heightField);
        middlePane.setAlignment(Pos.CENTER);

        Button btnCancel = new Button("Cancel");
        btnCancel.setCancelButton(true);
        btnCancel.setOnAction(e -> newFileDialog.close());

        Button btnCreate = new Button("Create");
        btnCreate.setDefaultButton(true);
        btnCreate.setOnAction(e -> {
            String width = widthField.getText(), height = heightField.getText();
            if (width.matches("^[1-9]\\d*") && height.matches("^[1-9]\\d*")) {  // check valid input using regex
                createCanvas(Integer.parseInt(width), Integer.parseInt(height));
                newFileDialog.close();
            }
        });
        HBox bottomPane = new HBox(10, btnCancel, btnCreate);
        bottomPane.setAlignment(Pos.CENTER_RIGHT);

        VBox dialogHolder = new VBox(20, topPane, middlePane, bottomPane);
        dialogHolder.setPadding(new Insets(10, 10, 10, 10));
        newFileDialog.setScene(new Scene(dialogHolder));
        newFileDialog.initModality(Modality.APPLICATION_MODAL);
        newFileDialog.show();
    }

    public void createCanvas(int width, int height) {
        hintCanvas.setPrefSize(width, height);
        hintCanvas.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        canvasBackground.setPrefSize(width, height);
        canvasBackground.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        canvas = new Pane();
        canvas.setPrefSize(width, height);

        // Make the canvas background white
        canvasBackground.setStyle("-fx-background-color: #FFFFFF;");
        drawGridMarks(width, height);

        canvasBackground.getChildren().add(hintCanvas);
        hintCanvas.getChildren().add(canvas);

        // The canvasBackground has to be manually centered within the scrollable
        // canvasSection, else the canvasBackground (and hence the canvas itself)
        // appears at the top-left corner of the scrollable canvasSection.
        canvasBackground.translateXProperty().bind(
                canvasSection.widthProperty()
                        .subtract(canvasBackground.widthProperty())
                        .divide(2)
        );
        canvasBackground.translateYProperty().bind(
                canvasSection.heightProperty()
                        .subtract(canvasBackground.heightProperty())
                        .divide(2)
        );

        UIHandler uih = new UIHandler();
        uih.highlightGridPoints(canvasBackground,hintCanvas);
    }

    private void drawGridMarks(int width, int height) {
        addGridLine(0,height/2, width,height/2, 1, Color.GRAY); // x-axis
        addGridLine(width/2,0, width/2,height, 1, Color.GRAY); // y-axis
        for (int y = 0; y < height; y += GRID_X_GAP)
            addGridLine(0, y, width, y, 0.5, Color.LIGHTGRAY);  // horizontal marks
        for (int x = 0; x < width; x += GRID_Y_GAP)
            addGridLine(x, 0, x, height, 0.5, Color.LIGHTGRAY); // vertical marks
    }

    private void addGridLine(double x1, double y1, double x2, double y2, double thickness, Color color) {
        Line gridLine = new Line(x1, y1, x2, y2);
        gridLine.setStrokeWidth(thickness);
        gridLine.setStroke(color);
        canvasBackground.getChildren().add(gridLine);
    }

    private static class ToolButton extends ToggleButton {
        ToolButton(String name) {
            super(name);
            this.setPrefWidth(50);
            this.setTooltip(new Tooltip(name));  // add a mouse-over tooltip that says the tool's name
            setToggleGroup(toolbarGroup);  // add it to the toolbar's toggle group
            this.setUserData(name);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
