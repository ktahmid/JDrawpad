import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUtil {

    private String getSvgCode() {
        return "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xml:space=\"preserve\">\n" +
                "</svg>\n";
    }

    public static void saveToFile(File outFile, Pane canvas) {
        try (PrintWriter fileWriter = new PrintWriter(outFile)) {
            String fileContent = produceShapeString(canvas);
            fileWriter.print(fileContent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String produceShapeString(Pane canvas) {
        return "";
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
        return "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xml:space=\"preserve\">\n" +
                "</svg>\n";
    }
}
