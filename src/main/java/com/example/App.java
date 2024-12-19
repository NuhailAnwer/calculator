package com.example;
import java.util.HashMap;
import java.util.Stack;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class App extends Application {
    private Label inputLabel = new Label();
    private Label outputLabel = new Label();
    private Stack<String> inputStack = new Stack<>();
    private Stack<String> historyStack = new Stack<>();
    private boolean isShiftActive = false; // Tracks if the Shift button is active
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
        shiftButton.setStyle("-fx-background-color: lightgrey; -fx-text-fill: yellow; -fx-font-size: 14px; -fx-font-weight: bold;");
        shiftButton.setOnAction(e -> isShiftActive = !isShiftActive); // Toggle Shift mode
        Button alphaButton = new Button("Alpha");
        alphaButton.setPrefSize(100, 40);
        alphaButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        shiftAlphaBar.getChildren().addAll(shiftButton, alphaButton);
        // Buttons and Labels
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(20); // Increased horizontal gap
        buttonGrid.setVgap(20); // Increased vertical gap
        buttonGrid.setPadding(new Insets(20)); // Add padding around the entire grid
        String[] buttons = {
            "7", "8", "9", "/", "sin", "cos", "tan",
            "4", "5", "6", "*", "log", "sqrt", "^",
            "1", "2", "3", "-", "d/dx", "∫", "C", ")",
            "0", ".", "=", "+", "DEL"
        };
        // Add inverse trigonometric labels above buttons
        Label sinInverseLabel = new Label("sin⁻¹");
        Label cosInverseLabel = new Label("cos⁻¹");
        Label tanInverseLabel = new Label("tan⁻¹");
        sinInverseLabel.setStyle("-fx-text-fill: yellow; -fx-font-size: 12px; -fx-font-weight: bold;");
        cosInverseLabel.setStyle("-fx-text-fill: yellow; -fx-font-size: 12px; -fx-font-weight: bold;");
        tanInverseLabel.setStyle("-fx-text-fill: yellow; -fx-font-size: 12px; -fx-font-weight: bold;");
        // Position the inverse function labels above their respective buttons (sin, cos, tan)
        buttonGrid.add(sinInverseLabel, 4, 0); // Position above "sin" button
        buttonGrid.add(cosInverseLabel, 5, 0); // Position above "cos" button
        buttonGrid.add(tanInverseLabel, 6, 0); // Position above "tan" button
        int row = 1, col = 0;
        for (String text : buttons) {
            Button button = new Button(text);
            button.setPrefWidth(60);
            button.setPrefHeight(40);
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
        Scene scene = new Scene(mainLayout, 600, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void showHistoryScreen() {
        Stage historyStage = new Stage();
        historyStage.setTitle("Calculation History");
        // Create a VBox layout to display the history
        VBox historyLayout = new VBox(10);
        historyLayout.setPadding(new Insets(10));
        // Create a list view or label to display the history items
        StringBuilder historyText = new StringBuilder();
        for (String historyEntry : historyStack) {
            historyText.append(historyEntry).append("\n");
        }
        // Label to display the history
        Label historyLabel = new Label(historyText.toString());
        historyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        // Add history label to the layout
        historyLayout.getChildren().add(historyLabel);
        // Set the scene for the history window
        Scene historyScene = new Scene(historyLayout, 300, 400);
        historyStage.setScene(historyScene);
        historyStage.show();
    }
    // Initialize the function map for Shift and inverse functions
    private void initializeFunctionMap() {
        HashMap<String, String> standardFunctions = new HashMap<>();
        standardFunctions.put("sin", "sin");
        standardFunctions.put("cos", "cos");
        standardFunctions.put("tan", "tan");
        standardFunctions.put("log", "log");
        standardFunctions.put("sqrt", "sqrt");
        HashMap<String, String> shiftedFunctions = new HashMap<>();
        shiftedFunctions.put("sin", "asin");
        shiftedFunctions.put("cos", "acos");
        shiftedFunctions.put("tan", "atan");
        shiftedFunctions.put("log", "log10"); // assuming log10 for simplicity
        shiftedFunctions.put("sqrt", "sqrt"); // no inverse for sqrt, kept as it is
        functionMap.put(false, standardFunctions); // Standard functions when Shift is not active
        functionMap.put(true, shiftedFunctions);   // Inverse functions when Shift is active
    }
    private void handleButtonPress(String text) {
Expressions expression = new Expressions();
History history = new History();
        switch (text) {
            case "C":
                inputLabel.setText("");
                outputLabel.setText("");
                inputStack.clear();  // Clear stack when "C" is pressed
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
                    String result=String.valueOf(expression.Evaluate(input));
                    outputLabel.setText(result);
                    history.addHistory(expression.Evaluate(input));  
                    //historyStack.push(input + " = " + result);
                } catch (Exception e) {
                    outputLabel.setText("Error");
                }
                break;
            case "sin": case "cos": case "tan": case "log": case "sqrt":
                String function = text;
                if (isShiftActive) {
                    // Use the inverse function if Shift is active
                    function = functionMap.get(true).get(function);
                } else {
                    // Use the standard function if Shift is not active
                    function = functionMap.get(false).get(function);
                }
                inputLabel.setText(inputLabel.getText() + function + "(");  // Add parentheses for functions
                inputStack.push(function + "(");  // Push function with opening parentheses onto stack
                break;
            case ")":
                inputLabel.setText(inputLabel.getText() + text);  // Add closing parenthesis
                inputStack.push(")");  // Push closing parenthesis onto stack
                break;
            case "DEL":
                if (!inputStack.isEmpty()) {
                    inputStack.pop();  // Remove the last element from the stack
                    StringBuilder newInput = new StringBuilder();
                    for (String item : inputStack) {
                        newInput.append(item);  // Rebuild the input from the stack
                    }
                    inputLabel.setText(newInput.toString());
                }
                break;
            default:
                inputLabel.setText(inputLabel.getText() + text);  // Add the button text to input
                inputStack.push(text);  // Push the entered text onto stack
        }
    }
 }
// import java.util.HashMap;
// import java.util.Stack;

// import javafx.application.Application;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.layout.GridPane;
// import javafx.scene.layout.HBox;
// import javafx.scene.layout.VBox;
// import javafx.stage.Stage;

// public class App extends Application {

//     private Label inputLabel = new Label();
//     private Label outputLabel = new Label();
//     private Stack<String> inputStack = new Stack<>();
//     private Stack<String> historyStack = new Stack<>();
//     private boolean isShiftActive = false; // Tracks if the Shift button is active
//     private boolean isAlphaActive = false;
//     // HashMap structure to map functions and their inverses depending on Shift state
//     private HashMap<Boolean, HashMap<String, String>> functionMap = new HashMap<>();

//     public static void main(String[] args) {
//         launch(args);
//     }

//     @Override
//     public void start(Stage primaryStage) {
//         primaryStage.setTitle("Scientific Calculator");

//         // Initialize the function map
//         initializeFunctionMap();

//         // Input and Output Areas
//         inputLabel.setStyle("-fx-font-size: 16px; -fx-border-color: black; -fx-padding: 10;");
//         inputLabel.setMinHeight(50);
//         outputLabel.setStyle("-fx-font-size: 16px; -fx-border-color: black; -fx-padding: 10;");
//         outputLabel.setMinHeight(50);

//         VBox display = new VBox(10, inputLabel, outputLabel);
//         display.setPadding(new Insets(10));
//         display.setStyle("-fx-border-color: black; -fx-border-width: 2px;");

//         // Shift and Alpha Buttons
//         HBox shiftAlphaBar = new HBox(10);
//         shiftAlphaBar.setAlignment(Pos.CENTER);
//         shiftAlphaBar.setPadding(new Insets(10));

//         Button shiftButton = new Button("Shift");
//         shiftButton.setPrefSize(100, 40);
//         shiftButton.setStyle("-fx-background-color: lightgrey; -fx-text-fill: red; -fx-font-size: 14px; -fx-font-weight: bold;");
//         shiftButton.setOnAction(e -> {
//             isShiftActive = !isShiftActive; // Toggle Shift mode
//             isAlphaActive = false; // Disable Alpha mode when Shift is active
//         });

//         Button alphaButton = new Button("Alpha");
//         alphaButton.setPrefSize(100, 40);
//         alphaButton.setStyle("-fx-font-size: 14px; -fx-text-fill: pink; -fx-font-weight: bold;");
//         alphaButton.setOnAction(e -> {
//             isAlphaActive = !isAlphaActive; // Toggle Alpha mode
//             isShiftActive = false; // Disable Shift mode when Alpha is active
//         });

//         shiftAlphaBar.getChildren().addAll(shiftButton, alphaButton);

//         // Buttons and Labels
//         GridPane buttonGrid = new GridPane();
//         buttonGrid.setHgap(15);
//         buttonGrid.setVgap(15);
//         buttonGrid.setPadding(new Insets(15));

//         String[] buttons = {
//             "7", "8", "9", "/", "sin", "cos", "tan",
//             "4", "5", "6", "*", "C", "(", ")",
//             "1", "2", "3", "-", "DEL", "d/dx", "∫",
//             "00", "0", ".", "+", "=", "log", "^",","
//             ,"HIS"

//         };
//         Label xalphabetLabel = new Label("x");
//         Label yalphabetLabel = new Label("y");
//         Label zalphabetLabel = new Label("z");

//         // Add inverse trigonometric labels above buttons
//         Label sinInverseLabel = new Label("sin⁻¹");
//         Label cosInverseLabel = new Label("cos⁻¹");
//         Label tanInverseLabel = new Label("tan⁻¹");

//         sinInverseLabel.setStyle("fx-background-color: red; -fx-padding:-0.1; -fx-font-size: 14px;");
//         cosInverseLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px; -fx-font-weight: bold;");
//         tanInverseLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px; -fx-font-weight: bold;");

//         xalphabetLabel.setStyle("fx-background-color: red; -fx-padding:-0.1; -fx-font-size: 14px");
//         yalphabetLabel.setStyle("fx-background-color: red; -fx-padding:-0.1; -fx-font-size: 14px");
//         zalphabetLabel.setStyle("fx-background-color: red; -fx-padding:-0.1; -fx-font-size: 14px");
//         // Position the inverse function labels above their respective buttons (sin, cos, tan)
//         buttonGrid.add(sinInverseLabel, 4, 0); // Position above "sin" button
//         buttonGrid.add(cosInverseLabel, 5, 0); // Position above "cos" button
//         buttonGrid.add(tanInverseLabel, 6, 0); // Position above "tan" button

//         buttonGrid.add(xalphabetLabel, 0, 0); // Position above "7" button
//         buttonGrid.add(yalphabetLabel, 1, 0); // Position above "8" button
//         buttonGrid.add(zalphabetLabel, 2, 0);//Position above "9" button

//         int row = 0, col = 0;
//         for (String text : buttons) {
//             Button button = new Button(text);
//             button.setPrefWidth(120);
//             button.setPrefHeight(80);
//             button.setStyle("-fx-background-color: lightgray; -fx-text-fill: black;");
//             button.setOnAction(e -> handleButtonPress(button.getText()));

//             buttonGrid.add(button, col, row);
//             col++;

//             if (col == 7) {
//                 col = 0;
//                 row++;
//             }
//         }

//         // // History Button
//         // Button historyButton = new Button("History");
//         // historyButton.setPrefWidth(140);
//         // historyButton.setOnAction(e -> showHistoryScreen());

//         // VBox mainLayout = new VBox(10, shiftAlphaBar, display, buttonGrid, historyButton);
//         // mainLayout.setPadding(new Insets(10));

//         // Scene scene = new Scene(mainLayout, 800, 600);
//         // primaryStage.setScene(scene);
//         // primaryStage.show();
//     }


//     private void initializeFunctionMap() {
//         HashMap<String, String> standardFunctions = new HashMap<>();
//         standardFunctions.put("sin", "sin");
//         standardFunctions.put("cos", "cos");
//         standardFunctions.put("tan", "tan");

//         HashMap<String, String> shiftedFunctions = new HashMap<>();
//         shiftedFunctions.put("sin", "sin⁻¹");
//         shiftedFunctions.put("cos", "cos⁻¹");
//         shiftedFunctions.put("tan", "tan⁻¹");

//         HashMap<String, String> alphaFunctions = new HashMap<>();
//         alphaFunctions.put("sin", "x");
//         alphaFunctions.put("cos", "y");
//         alphaFunctions.put("tan", "z");

//         functionMap.put(false, standardFunctions);
//         functionMap.put(true, shiftedFunctions);
//         functionMap.put(null, alphaFunctions);
//     }

//     private void handleButtonPress(String text) {

//         Expressions expression = new Expressions();
//         History history = new History();
//         switch (text) {
//             case "C":
//                 inputLabel.setText("");
//                 outputLabel.setText("");
//                 inputStack.clear();
//                 break;
//             case "=":
//                 try {
//                     String input = inputLabel.getText();
//                     // Check if parentheses are missing for log and functions like sin, cos, etc.
//                     if (input.contains("log") && !input.contains(")")) {
//                         input = input + ")";
//                     }
//                     //double result = evaluateExpression(input);
//                     // outputLabel.setText(String.valueOf(result));
//                     String result = String.valueOf(expression.Evaluate(input));
//                     outputLabel.setText(result);
//                     history.addHistory(expression.Evaluate(input));
//                     //historyStack.push(input + " = " + result);
//                 } catch (Exception e) {
//                     outputLabel.setText("Error");
//                 }
//                 break;
//             case "sin":
//             case "cos":
//             case "tan":
//                 String function;
//                 if (isAlphaActive) {
//                     function = functionMap.get(null).get(text);
//                 } else if (isShiftActive) {
//                     function = functionMap.get(true).get(text);
//                 } else {
//                     function = functionMap.get(false).get(text);
//                 }
//                 inputLabel.setText(inputLabel.getText() + function + "(");
//                 inputStack.push(function + "(");
//                 break;
//             case ")":
//                 inputLabel.setText(inputLabel.getText() + text);
//                 inputStack.push(")");
//                 break;
//             case "DEL":
//                 if (!inputStack.isEmpty()) {
//                     inputStack.pop();
//                     StringBuilder newInput = new StringBuilder();
//                     for (String item : inputStack) {
//                         newInput.append(item);
//                     }
//                     inputLabel.setText(newInput.toString());
//                 }
//                 break;
//             case "HIS":
//             outputLabel.setText(history.viewHistory());
//             break;
//             default:
//                 inputLabel.setText(inputLabel.getText() + text);
//                 inputStack.push(text);
//         }
//     }

// }





















/*
private double evaluateExpression(String input) {
    input = input.replaceAll("\\s+", ""); // Remove all whitespaces

    // Handle integrals and derivatives
    if (input.contains("∫") || input.contains("d/dx")) {
        if (input.contains("∫")) {
            // Extract function and limits for integration
            int startIndex = input.indexOf("(") + 1;
            int endIndex = input.lastIndexOf(")");
            String expression = input.substring(startIndex, endIndex);

            // Assume the format is: ∫(expression,a,b)
            String[] parts = expression.split(",");
            if (parts.length == 3) {
                String func = parts[0]; // Function to integrate
                double lowerLimit = Double.parseDouble(parts[1]); // Lower limit
                double upperLimit = Double.parseDouble(parts[2]); // Upper limit

                return integrate(func, lowerLimit, upperLimit);
            }
        } else if (input.contains("d/dx")) {
            // Extract function and point for differentiation
            int startIndex = input.indexOf("(") + 1;
            int endIndex = input.lastIndexOf(")");
            String expression = input.substring(startIndex, endIndex);

            // Assume the format is: d/dx(expression,x)
            String[] parts = expression.split(",");
            if (parts.length == 2) {
                String func = parts[0]; // Function to differentiate
                double point = Double.parseDouble(parts[1]); // Point at which to differentiate

                return differentiate(func, point);
            }
        }
    }

    // Existing functionality for basic operations and functions (sin, cos, etc.)
    return 0.0; // Placeholder for other cases
}

// Helper method for numeric differentiation
private double differentiate(String func, double x) {
    double h = 1e-5; // Small step for numerical differentiation
    return (evaluateFunction(func, x + h) - evaluateFunction(func, x - h)) / (2 * h);
}

// Helper method for numeric integration using the Trapezoidal Rule
private double integrate(String func, double a, double b) {
    int n = 1000; // Number of intervals
    double h = (b - a) / n; // Step size
    double integral = 0.0;

    for (int i = 0; i <= n; i++) {
        double x = a + i * h;
        double weight = (i == 0 || i == n) ? 0.5 : 1.0; // Trapezoidal weights
        integral += weight * evaluateFunction(func, x);
    }

    integral *= h;
    return integral;
}

// Helper method to evaluate a function at a given value of x
private double evaluateFunction(String func, double x) {
    // Replace 'x' in the function with the actual value
    String replacedFunc = func.replace("x", String.valueOf(x));
    // Parse and evaluate the function (extend this as needed for more complex functions)
    return evaluateBasicExpression(replacedFunc);
}

// Basic evaluation for arithmetic expressions
private double evaluateBasicExpression(String input) {
    // A simple implementation of arithmetic operations (+, -, *, /, ^)
    // Extend this if needed
    return 0.0; // Placeholder for actual implementation
}

*/
