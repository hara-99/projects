
package application;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class StudentPocketMoneyManagerSave extends Application {
    // Use absolute path here to avoid location confusion - update path as needed
    private static final String CSV_FILE = System.getProperty("user.home") + File.separator + "students_data.csv";

    private Label fileLinkLabel = new Label();
    private Label savingBehaviorLabel = new Label();
    private Label financialStressLabel = new Label();
    private Label spendingTypeLabel = new Label();
    private Label futureLabel = new Label();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Pocket Money Manager");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(8);
        grid.setHgap(10);

        TextField studentIdField = new TextField();
        TextField pocketMoneyField = new TextField();
        TextField expenseField = new TextField();
        TextField savingsField = new TextField();
        TextField educationMoneyField = new TextField();
        TextField transportCostField = new TextField();
        TextField borrowMoneyAmountField = new TextField();

        grid.add(new Label("Student ID:"), 0, 0);
        grid.add(studentIdField, 1, 0);
        grid.add(new Label("Pocket Money Given:"), 0, 1);
        grid.add(pocketMoneyField, 1, 1);
        grid.add(new Label("Expense:"), 0, 2);
        grid.add(expenseField, 1, 2);
        grid.add(new Label("Savings:"), 0, 3);
        grid.add(savingsField, 1, 3);
        grid.add(new Label("Education Money:"), 0, 4);
        grid.add(educationMoneyField, 1, 4);
        grid.add(new Label("Transport Cost:"), 0, 5);
        grid.add(transportCostField, 1, 5);
        grid.add(new Label("Borrow Money Amount:"), 0, 6);
        grid.add(borrowMoneyAmountField, 1, 6);

        Button submitButton = new Button("Submit & Predict");
        grid.add(submitButton, 1, 7);

        Button downloadButton = new Button("Open CSV File");
        grid.add(downloadButton, 1, 8);

        grid.add(new Label("Saving Behavior:"), 0, 9);
        grid.add(savingBehaviorLabel, 1, 9);
        grid.add(new Label("Financial Stress:"), 0, 10);
        grid.add(financialStressLabel, 1, 10);
        grid.add(new Label("Spending Type:"), 0, 11);
        grid.add(spendingTypeLabel, 1, 11);
        grid.add(new Label("Future:"), 0, 12);
        grid.add(futureLabel, 1, 12);

        grid.add(new Label("CSV File Path:"), 0, 13);
        grid.add(fileLinkLabel, 1, 13);
        fileLinkLabel.setWrapText(true);

        submitButton.setOnAction(e -> {
            try {
                handleSubmission(studentIdField,
                        pocketMoneyField,
                        expenseField,
                        savingsField,
                        educationMoneyField,
                        transportCostField,
                        borrowMoneyAmountField);
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to save data to CSV: " + ex.getMessage());
            }
        });

        downloadButton.setOnAction(e -> {
            File csvFile = new File(CSV_FILE);
            if (!csvFile.exists()) {
                showAlert("File Not Found", "CSV file does not exist. Please add data first.");
                return;
            }
            try {
                getHostServices().showDocument(csvFile.toURI().toString());
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to open CSV file.");
            }
        });

        Scene scene = new Scene(grid, 600, 650);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Show CSV file path on UI at launch
        fileLinkLabel.setText(new File(CSV_FILE).getAbsolutePath());
    }

    private void handleSubmission(TextField studentIdField,
                                  TextField pocketMoneyField,
                                  TextField expenseField,
                                  TextField savingsField,
                                  TextField educationMoneyField,
                                  TextField transportCostField,
                                  TextField borrowMoneyAmountField) throws IOException {

        String studentId = studentIdField.getText();
        String pocketMoneyStr = pocketMoneyField.getText();
        String expenseStr = expenseField.getText();
        String savingsStr = savingsField.getText();
        String educationMoneyStr = educationMoneyField.getText();
        String transportCostStr = transportCostField.getText();
        String borrowMoneyAmountStr = borrowMoneyAmountField.getText();

        if (studentId.isEmpty() || pocketMoneyStr.isEmpty() || expenseStr.isEmpty()
                || savingsStr.isEmpty() || educationMoneyStr.isEmpty() || transportCostStr.isEmpty()
                || borrowMoneyAmountStr.isEmpty()) {
            showAlert("Validation Error", "Please fill all fields.");
            return;
        }

        double pocketMoney, expense, savings, educationMoney, transportCost, borrowMoneyAmount;
        try {
            pocketMoney = Double.parseDouble(pocketMoneyStr);
            expense = Double.parseDouble(expenseStr);
            savings = Double.parseDouble(savingsStr);
            educationMoney = Double.parseDouble(educationMoneyStr);
            transportCost = Double.parseDouble(transportCostStr);
            borrowMoneyAmount = Double.parseDouble(borrowMoneyAmountStr);
        } catch (NumberFormatException nfe) {
            showAlert("Validation Error", "Please enter valid numeric values.");
            return;
        }

        double spendingPercent = (expense / pocketMoney) * 100;
        double savingPercent = (savings / pocketMoney) * 100;
        double discretionaryPercent = ((expense - (educationMoney + transportCost)) / pocketMoney) * 100;
        double nonDiscretionaryPercent = ((educationMoney + transportCost) / pocketMoney) * 100;

        String savingBehavior;
        String future;
        if (spendingPercent > 80) {
            savingBehavior = "High consumption & low savings";
            future = "Future Consumer Mindset";
        } else if (savingPercent > 40) {
            savingBehavior = "Balanced consumption & savings";
            future = "Future Planner";
        } else {
            savingBehavior = "Moderate";
            future = "Uncertain Future";
        }

        String financialStress;
        if (borrowMoneyAmount > 0 || expense > pocketMoney * 1.10) {
            financialStress = "Future Financial Stress";
        } else {
            financialStress = "Financially Secure Future";
        }

        String spendingType;
        if (discretionaryPercent > 50) {
            spendingType = "Lifestyle-Oriented Student";
        } else if (nonDiscretionaryPercent > 10) {
            spendingType = "Goal-Oriented Student";
        } else {
            spendingType = "Mixed Spending";
        }

        File csvFile = new File(CSV_FILE);
        boolean fileExists = csvFile.exists();

        // Debug prints
        System.out.println("Saving data to CSV file at: " + csvFile.getAbsolutePath());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))) {
            if (!fileExists) {
                writer.write("studentId,pocketMoneyGiven,expense,savings,educationMoney,transportCost,SavingBehavior,borrowMoneyAmount,FinancialStress,SpendingType,Future");
                writer.newLine();
            }
            DecimalFormat df = new DecimalFormat("#.##");
            String line = String.join(",",
                    studentId,
                    df.format(pocketMoney),
                    df.format(expense),
                    df.format(savings),
                    df.format(educationMoney),
                    df.format(transportCost),
                    savingBehavior,
                    df.format(borrowMoneyAmount),
                    financialStress,
                    spendingType,
                    future);
            writer.write(line);
            writer.newLine();
            writer.flush();
            System.out.println("Data successfully written to CSV.");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            showAlert("Error", "Failed to save to CSV: " + ioe.getMessage());
            return;
        }

        // Confirm file existence and size
        if (csvFile.exists()) {
            System.out.println("CSV File Size: " + csvFile.length() + " bytes.");
        }

        savingBehaviorLabel.setText(savingBehavior);
        financialStressLabel.setText(financialStress);
        spendingTypeLabel.setText(spendingType);
        futureLabel.setText(future);
        fileLinkLabel.setText(csvFile.getAbsolutePath());

        showAlert("Success", "Data saved and predictions done! CSV saved at:\n" + csvFile.getAbsolutePath());

        studentIdField.clear();
        pocketMoneyField.clear();
        expenseField.clear();
        savingsField.clear();
        educationMoneyField.clear();
        transportCostField.clear();
        borrowMoneyAmountField.clear();
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
