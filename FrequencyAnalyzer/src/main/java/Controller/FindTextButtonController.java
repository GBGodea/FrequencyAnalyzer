package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class FindTextButtonController {
    @FXML
    Button button;
    @FXML
    TextArea writeText;
    @FXML
    TextArea foundedText;
    String text;

    @FXML
    BarChart<String, Number> barChart;
    @FXML
    CategoryAxis category;

    @FXML
    public void findText() {
        Stage stage = new Stage();
        FileChooser chooser = new FileChooser();

        File selectedFile = chooser.showOpenDialog(stage);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(selectedFile), StandardCharsets.UTF_8));
            int c;
            text = new String();
            while ((c = reader.read()) != -1) {
                char ch = (char) c;
                text += ch;
            }

            foundedText.setText(text);
        } catch (Exception ignored) {

        }
    }

    @FXML
    public void find() throws IOException {
        int i = 0;
        int length = foundedText.getLength();
        HashMap<Character, Integer> amountOfSymbols = new HashMap<Character, Integer>();
        while (length > i) {
            char ch = foundedText.getText().toLowerCase().charAt(i);
            if (ch != ' ' && ch != '\n') {
                amountOfSymbols.merge(ch, 1, Integer::sum);
            }
            i++;
        }


        XYChart.Series<String, Number> xy = new XYChart.Series<>();
        StringBuilder sb = new StringBuilder();
        for (Character c : amountOfSymbols.keySet()) {
            float chanceOfOccurrence = amountOfSymbols.get(c).floatValue() / length * 100;
            xy.getData().add(new XYChart.Data<String, Number>(c.toString(), chanceOfOccurrence));
            sb.append("Character \"").append(c).append("\" ").append("has a chance of occurrence: ").append(chanceOfOccurrence).append("%").append("\n");
        }

        writeText.setText(sb.toString());


        showInfoForm().getData().add(xy);
        barChart.lookupAll(".default-color0.chart-bar").forEach(e -> e.setStyle("-fx-bar-fill: " + toHexString()));
    }

    private String toHexString() {
        return String.format("#%02x%02x%02x", (byte) (Math.random() * 256), (byte) (Math.random() * 256 + 150), (byte) (Math.random() * 256 + 150));
    }

    private BarChart<String, Number> showInfoForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/BarChart.fxml"));
        loader.setController(this);

        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();

        category.setAnimated(false);
        barChart.setLegendVisible(false);

        stage.setScene(scene);
        stage.show();
        return barChart;
    }

}
