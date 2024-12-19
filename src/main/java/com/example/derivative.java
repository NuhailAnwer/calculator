package com.example;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

// public class derivative {

//     private final double interval = 0.0001;
//         double[] value_of_x = new double[2];
//     double[] value_of_y = new double[2];

//     public double CalculateDerivative(String expression,double value){
        
//        value_of_x[0]=value-interval;
//        System.out.println(value_of_x[0]);
//        value_of_x[1]=value+interval;
//        System.out.println(value_of_x[1]);
//         Expression expr;
//         try {
//             expr = new ExpressionBuilder(expression).variable("x").build();
//         } catch (Exception e) {
//             throw new IllegalArgumentException("Invalid expression: " + expression, e);
//         }

//         for (int i = 0; i < 2; i++) {
//             try {
//                 value_of_y[i] = expr.setVariable("x", value_of_x[i]).evaluate();
//             } catch (Exception e) {
//                 throw new RuntimeException("Error evaluating expression at x = " + value_of_x[i], e);
//             }
//         }
//         System.out.println(value_of_y[0]);
        
//         System.out.println(value_of_y[1]);
//         // forumla 
//         double deriva = (value_of_y[1]-value_of_y[0])/(2*interval);        
//         return deriva;
//     }
    
    
//}


public class derivative  {
    private final double interval = 0.0001; // Small interval for derivative approximation
    private final double[] value_of_x = new double[2];
    private final double[] value_of_y = new double[2];
    
    public double calculateDerivative(String expression, double value) {
        if (expression == null || expression.isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty.");
        }

        // Calculate x values for numerical approximation
        value_of_x[0] = value - interval;
        value_of_x[1] = value + interval;

        Expression expr;
        try {
            expr = new ExpressionBuilder(expression).variable("x").build();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid expression format: " + expression, e);
        }

        // Evaluate the expression for the calculated x values
        for (int i = 0; i < 2; i++) {
            try {
                value_of_y[i] = expr.setVariable("x", value_of_x[i]).evaluate();
            } catch (Exception e) {
                throw new RuntimeException("Error evaluating expression at x = " + value_of_x[i], e);
            }
        }

        if (!isFinite(value_of_y[0]) || !isFinite(value_of_y[1])) {
            throw new ArithmeticException("Expression evaluation resulted in non-finite values.");
        }

        // Numerical differentiation formula
        double derivative = (value_of_y[1] - value_of_y[0]) / (2 * interval);

        return derivative;
    }

    private boolean isFinite(double value) {
        return !Double.isNaN(value) && !Double.isInfinite(value);
    }

}


    

