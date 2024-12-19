package com.example;

// import net.objecthunter.exp4j.Expression;
// import net.objecthunter.exp4j.ExpressionBuilder;

// public class Integral {
// // using trapozoidal rule to integrate function 

//     private final int interval = 10;
//     private int lower_limit;
//     private int upper_limit;
//     double[] value_of_x = new double[interval + 1];
//     double[] value_of_y = new double[interval + 1];

//     public String CalculateIntegral(String expression, double upper_limit, double lower_limit) {
//         double interval_lenght = (upper_limit - lower_limit) / interval;
//         double sum = lower_limit;
//         value_of_x[0] = sum;

//         for (int i = 1; i <= interval; i++) {
//             sum += interval_lenght;
//             value_of_x[i] = sum;
//         }

//         Expression expr = new ExpressionBuilder(expression).variable("x").build();
//         for (int i = 0; i <= interval; i++) {
//             value_of_y[i] = expr.setVariable("x", value_of_x[i]).evaluate();
//         }

//         // Correcting the formula
//         double integral = value_of_y[0] + value_of_y[interval];
//         for (int i = 1; i <= interval - 1; i++) {
//             integral += 2 * value_of_y[i];
//         }

//         double result = (interval_lenght / 2) * integral;
//         return String.valueOf(result);

//     }

// }
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

public class Integral  {
    private double upper_limit;
    private double lower_limit;
    private final double interval = 10;
    double[] value_of_x = new double[(int) (interval+1)];
    double[] value_of_y = new double[(int) (interval+1)];

    public double CalculateIntegral(String expression, double upper_limit, double lower_limit) {
        // Validate input
        if (expression == null) {
            throw new IllegalArgumentException("Expression cannot be null.");
        }
        if (upper_limit <= lower_limit) {
            throw new IllegalArgumentException("Upper limit must be greater than lower limit.");
        }

        double interval_length = (upper_limit - lower_limit) /  interval;
        if (interval_length == 0) {
            throw new ArithmeticException("Interval length cannot be zero.");
        }

        double sum = lower_limit;
        value_of_x[0] = sum;
        for (int i = 1; i <= interval; i++) {
            sum += interval_length;
            value_of_x[i] = sum;
        }

        Expression expr;
        try {
            expr = new ExpressionBuilder(expression).variable("x").build();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid expression: " + expression, e);
        }

        for (int i = 0; i <= interval; i++) {
            try {
                value_of_y[i] = expr.setVariable("x", value_of_x[i]).evaluate();
            } catch (Exception e) {
                throw new RuntimeException("Error evaluating expression at x = " + value_of_x[i], e);
            }
        }

        // Apply trapezoidal rule
        double integral = value_of_y[0] + value_of_y[(int) interval];
        for (int i = 1; i <= interval - 1; i++) {
            integral += 2 * value_of_y[i];
        }

        double result = (interval_length / 2) * integral;
        return result;
    }
}
