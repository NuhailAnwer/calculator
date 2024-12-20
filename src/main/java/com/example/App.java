package com.example;

import java.util.HashMap;
import java.util.Stack;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

   // private static final Node AlphaButton = null;
   History history = new History();
    
    private Label inputLabel = new Label();
    private Label outputLabel = new Label();
    private Stack<String> inputStack = new Stack<>();
    private Stack<String> historyStack = new Stack<>();
    private boolean isShiftActive = false; // Tracks if the Shift button is active
    private boolean isAlphaActive = false;
    // HashMap structure to map functions and their inverses depending on Shift state
    private HashMap<Boolean, HashMap<String, String>> functionMap = new HashMap<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Scientific Calculator");

        // Initialize the function map
        initializeFunctionMap();

        // Input and Output Areas
        inputLabel.setStyle("-fx-font-size: 16px; -fx-border-color: black; -fx-padding: 10;");
        inputLabel.setMinHeight(50);
        outputLabel.setStyle("-fx-font-size: 16px; -fx-border-color: black; -fx-padding: 10;");
        outputLabel.setMinHeight(50);

        VBox display = new VBox(10, inputLabel, outputLabel);
        display.setPadding(new Insets(10));
        display.setStyle("-fx-border-color: black; -fx-border-width: 2px;");

        // Shift and Alpha Buttons
        HBox shiftAlphaBar = new HBox(10);
        shiftAlphaBar.setAlignment(Pos.CENTER);
        shiftAlphaBar.setPadding(new Insets(10));
        Button shiftButton = new Button("Shift");
        shiftButton.setPrefSize(100, 40);
        shiftButton.setStyle("-fx-background-color: lightgrey; -fx-text-fill: red; -fx-font-size: 14px; -fx-font-weight: bold;");
        shiftButton.setOnAction(e -> {
            isShiftActive = !isShiftActive; // Toggle Shift mode
            isAlphaActive = false; // Disable Alpha mode when Shift is active
        });
        Button alphaButton = new Button("Alpha");
        alphaButton.setPrefSize(100, 40);
        alphaButton.setStyle("-fx-font-size: 14px; -fx-text-fill: pink; -fx-font-weight: bold;");
        alphaButton.setOnAction(e -> {
            isAlphaActive = !isAlphaActive; // Toggle Alpha mode
            isShiftActive = false; // Disable Shift mode when Alpha is active
        });

// Adds the buttons to the HBox
shiftAlphaBar.getChildren().addAll(alphaButton, shiftButton);
       
// Shift and Alpha Buttons

        // Buttons and Labels
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(15);
        buttonGrid.setVgap(15);
        buttonGrid.setPadding(new Insets(15));

        String[] buttons = {
            "7", "8", "9", "/", "sin", "cos", "tan",
            "4", "5", "6", "*", "C", "(", ")",
            "1", "2", "3", "-", "DEL", "d/dx", "∫",
            "00", "0", ".", "+", "=", "log", "^", "HIS"
        };
        Label xalphabetLabel = new Label("x");
        Label yalphabetLabel = new Label("y");
        Label zalphabetLabel = new Label("z");

        // Add inverse trigonometric labels above buttons
        Label sinInverseLabel = new Label("sin⁻¹");
        Label cosInverseLabel = new Label("cos⁻¹");
        Label tanInverseLabel = new Label("tan⁻¹");

        sinInverseLabel.setStyle("fx-background-color: red; -fx-padding:-0.1; -fx-font-size: 14px;");
        cosInverseLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px; -fx-font-weight: bold;");
        tanInverseLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px; -fx-font-weight: bold;");

        xalphabetLabel.setStyle("fx-background-color: red; -fx-padding:-0.1; -fx-font-size: 14px");
        yalphabetLabel.setStyle("fx-background-color: red; -fx-padding:-0.1; -fx-font-size: 14px");
        zalphabetLabel.setStyle("fx-background-color: red; -fx-padding:-0.1; -fx-font-size: 14px");
        // Position the inverse function labels above their respective buttons (sin, cos, tan)
        buttonGrid.add(sinInverseLabel, 4, 0); // Position above "sin" button
        buttonGrid.add(cosInverseLabel, 5, 0); // Position above "cos" button
        buttonGrid.add(tanInverseLabel, 6, 0); // Position above "tan" button

        buttonGrid.add(xalphabetLabel, 0, 0); // Position above "7" button
        buttonGrid.add(yalphabetLabel, 1, 0); // Position above "8" button
        buttonGrid.add(zalphabetLabel, 2, 0);//Position above "9" button

        int row = 0, col = 0;
        for (String text : buttons) {
            Button button = new Button(text);
            button.setPrefWidth(120);
            button.setPrefHeight(80);
            button.setStyle("-fx-background-color: lightgray; -fx-text-fill: black;");
            button.setOnAction(e -> handleButtonPress(button.getText()));

            buttonGrid.add(button, col, row);
            col++;

            if (col == 7) {
                col = 0;
                row++;
            }
        }

        // History Button
        Button historyButton = new Button("History");
        historyButton.setPrefWidth(140);
        historyButton.setOnAction(e -> showHistoryScreen());

        VBox mainLayout = new VBox(10, shiftAlphaBar, display, buttonGrid, historyButton);
        mainLayout.setPadding(new Insets(10));

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showHistoryScreen() {
        Stage historyStage = new Stage();
        historyStage.setTitle("Calculation History");

        VBox historyLayout = new VBox(10);
        historyLayout.setPadding(new Insets(10));

        StringBuilder historyText = new StringBuilder();
        for (String historyEntry : historyStack) {
            historyText.append(historyEntry).append("\n");
        }

        Label historyLabel = new Label(historyText.toString());
        historyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

        historyLayout.getChildren().add(historyLabel);

        Scene historyScene = new Scene(historyLayout, 300, 400);
        historyStage.setScene(historyScene);
        historyStage.show();
    }

    private void initializeFunctionMap() {
        HashMap<String, String> standardFunctions = new HashMap<>();
        standardFunctions.put("sin", "sin");
        standardFunctions.put("cos", "cos");
        standardFunctions.put("tan", "tan");

        HashMap<String, String> shiftedFunctions = new HashMap<>();
        shiftedFunctions.put("sin", "asin");
        shiftedFunctions.put("cos", "acos");
        shiftedFunctions.put("tan", "atan");

        HashMap<String, String> alphaFunctions = new HashMap<>();
        alphaFunctions.put("sin", "x");
        alphaFunctions.put("cos", "y");
        alphaFunctions.put("tan", "z");

        functionMap.put(false, standardFunctions);
        functionMap.put(true, shiftedFunctions);
        functionMap.put(null, alphaFunctions);
    }


    private void handleButtonPress(String text) {

        Expressions expression = new Expressions();
       
        Integral integral = new Integral();
        derivative deri = new derivative();
        switch (text) {
            case "C":
                inputLabel.setText("");
                outputLabel.setText("");
                inputStack.clear();
                break;
            case "=":
                try {
                    String input = inputLabel.getText();
                    // Check if parentheses are missing for log and functions like sin, cos, etc.
                    if (input.contains("log") && !input.contains(")")) {
                        input = input + ")";
                    }
                    //double result = evaluateExpression(input);
                    // outputLabel.setText(String.valueOf(result));
                    String result = String.valueOf(expression.Evaluate(input));
                    outputLabel.setText(result);
                    history.addHistory(expression.Evaluate(input));
                    historyStack.push(input + " = " + result);
                    inputLabel.setText("");
                } catch (Exception e) {
                    outputLabel.setText("Error");
                }
                break;
            case "sin":
            case "cos":
            case "tan":
                String function;
                if (isAlphaActive) {
                    function = functionMap.get(null).get(text);
                } else if (isShiftActive) {
                    function = functionMap.get(true).get(text);
                } else {
                    function = functionMap.get(false).get(text);
                }
                inputLabel.setText(inputLabel.getText() + function + "(");
                inputStack.push(function + "(");
                break;
            case ")":
                inputLabel.setText(inputLabel.getText() + text);
                inputStack.push(")");
                break;
            case "DEL":
                if (!inputStack.isEmpty()) {
                    inputStack.pop();
                    StringBuilder newInput = new StringBuilder();
                    for (String item : inputStack) {
                        newInput.append(item);
                    }
                    inputLabel.setText(newInput.toString());
                }
                break;
                case "HIS":
                String historyEntry = history.viewHistory();
                if (historyEntry == null) {
                    outputLabel.setText("History: Empty");
                } else {
                    outputLabel.setText("History: " + historyEntry);
                }
                break;
            case "∫":
                try {
                    // Prompt for upper limit
                    TextInputDialog upperDialog = new TextInputDialog();
                    upperDialog.setTitle("Upper Limit");
                    upperDialog.setHeaderText("Enter the upper limit:");
                    upperDialog.setContentText("Upper limit:");
                    double upperLimit = Double.parseDouble(upperDialog.showAndWait().orElseThrow(() -> new IllegalArgumentException("No input provided")));

                    // Prompt for lower limit
                    TextInputDialog lowerDialog = new TextInputDialog();
                    lowerDialog.setTitle("Lower Limit");
                    lowerDialog.setHeaderText("Enter the lower limit:");
                    lowerDialog.setContentText("Lower limit:");
                    double lowerLimit = Double.parseDouble(lowerDialog.showAndWait().orElseThrow(() -> new IllegalArgumentException("No input provided")));
                    // Prompt for the mathematical expression
                    TextInputDialog expressionDialog = new TextInputDialog();
                    expressionDialog.setTitle("Function Expression");
                    expressionDialog.setHeaderText("Enter the function in terms of 'x' (e.g., x^2):");
                    expressionDialog.setContentText("Expression:");
                    String functionExpression = expressionDialog.showAndWait().orElseThrow(() -> new IllegalArgumentException("No input provided"));

                    double result = integral.CalculateIntegral(functionExpression, upperLimit, lowerLimit); // Using 1000 intervals
                    outputLabel.setText(String.valueOf(result));
                    historyStack.push("∫(" + lowerLimit + "," + upperLimit + ")" + functionExpression + " = " + result);
                    history.addHistory(result);
                } catch (Exception e) {
                    outputLabel.setText("Error: " + e.getMessage());
                }
                break;
            case "d/dx":
                try {
                    // Prompt for the point of evaluation
                    TextInputDialog pointDialog = new TextInputDialog();
                    pointDialog.setTitle("Point of Derivative");
                    pointDialog.setHeaderText("Enter the point where derivative is evaluated:");
                    pointDialog.setContentText("Point:");
                    double point = Double.parseDouble(pointDialog.showAndWait().orElseThrow(() -> new IllegalArgumentException("No input provided")));

                    // Prompt for the mathematical expression
                    TextInputDialog expressionDialog = new TextInputDialog();
                    expressionDialog.setTitle("Function Expression");
                    expressionDialog.setHeaderText("Enter the function in terms of 'x' (e.g., x^2):");
                    expressionDialog.setContentText("Expression:");
                    String functionExpression = expressionDialog.showAndWait().orElseThrow(() -> new IllegalArgumentException("No input provided"));

                    double delta = 1e-6;
                    double result = deri.calculateDerivative(functionExpression, point);
                    outputLabel.setText(String.valueOf(result));
                    historyStack.push("d/dx[" + functionExpression + "] at x=" + point + " = " + result);
                    history.addHistory(result);
                } catch (Exception e) {
                    outputLabel.setText("Error: " + e.getMessage());
                }
                break;

            default:
                inputLabel.setText(inputLabel.getText() + text);
                inputStack.push(text);
        }
    }

}
