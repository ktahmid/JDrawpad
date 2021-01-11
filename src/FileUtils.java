import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.io.*;
import java.util.ArrayList;

public class FileUtils {

    public static void saveToFile(File outFile, Pane canvas) {
        try (PrintWriter fileWriter = new PrintWriter(outFile)) {
            String fileContent = produceCsv(canvas);
            fileWriter.print(fileContent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String produceCsv(Pane canvas) {
        String csvContent = "";
        for (int i = 0; i < canvas.getChildren().size(); i++) {
            csvContent += CsvGenerator.getStr(
                    (Shape) canvas.getChildren().get(i),
                    (int) canvas.getPrefWidth(),
                    (int) canvas.getPrefHeight()
            ) + "\n";
        }
        return csvContent;
    }

    public static void exportToSvgFile(File svgFile, Pane canvas) {
        try (PrintWriter svgWriter = new PrintWriter(svgFile)) {
            String svgCode = produceSvg(canvas);
            svgWriter.print(svgCode);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String produceSvg(Pane canvas) {
        String svgCode = "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xml:space=\"preserve\">\n";
        for (Node node : canvas.getChildren()) {
            svgCode += "  " + SvgGenerator.convert((Shape) node);
        }
        svgCode += "</svg>\n";
        return svgCode;
    }

    public static void readFileToCanvas(File csvFile, Pane canvas) {
        for (String[] shapeTokens : readCsv(csvFile)) {
            canvas.getChildren().add(
                    CsvParser.getShape(
                            shapeTokens,
                            (int) canvas.getPrefWidth(),
                            (int) canvas.getPrefHeight())
            );
        }
    }

    private static ArrayList<String[]> readCsv(File csvFile) {
        ArrayList<String[]> splitLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                splitLines.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return splitLines;
    }
}
