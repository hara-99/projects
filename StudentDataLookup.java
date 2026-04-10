package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StudentDataLookup extends Application {
    // Use same absolute CSV path as saving app
    private static final String CSV_FILE = System.getProperty("user.home") + System.getProperty("file.separator") + "students_data.csv";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Pocket Money Manager - Lookup");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label studentIdLabel = new Label("Enter Student ID:");
        TextField studentIdField = new TextField();
        Button lookupButton = new Button("Lookup");

        grid.add(studentIdLabel, 0, 0);
        grid.add(studentIdField, 1, 0);
        grid.add(lookupButton, 1, 1);

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(350);
        resultArea.setWrapText(true);
        grid.add(resultArea, 0, 2, 2, 1);

        lookupButton.setOnAction(e -> {
            String studentId = studentIdField.getText().trim();
            if (studentId.isEmpty()) {
                showAlert("Validation Error", "Please enter a student ID.");
                return;
            }
            try {
                String result = findStudentRecord(studentId);
                if (result == null) {
                    showAlert("Not Found", "No record found for Student ID: " + studentId);
                    resultArea.clear();
                } else {
                    resultArea.setText(result);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to read the CSV file.");
            }
        });

        Scene scene = new Scene(grid, 600, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String findStudentRecord(String studentId) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String header = br.readLine();  // read header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(studentId)) {
                    return formatRecord(header, parts);
                }
            }
        }
        
        
        
        
        
        
        return null;
    }

    private String formatRecord(String headerLine, String[] dataParts) {
        String[] headers = headerLine.split(",");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < headers.length && i < dataParts.length; i++) {
            sb.append(headers[i]).append(": ").append(dataParts[i]).append("\n");
        }
        return sb.toString();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
