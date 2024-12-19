package com.example;

import java.util.Map;

import net.objecthunter.exp4j.ExpressionBuilder;

public class Expressions {

    // expression class that will evaluate any type  of expression and also diffreciation and Integration In Sha Allah 
    //

    public double  Evaluate(String expression) {
        double result = new ExpressionBuilder(expression).build().evaluate();
        return result;
    }

    public double  Evaluate(String expression, Map<String, Double> vars) {

        if (vars == null) {
            return (Evaluate(expression));
        }
        double result = (new ExpressionBuilder(expression)).variables(vars.keySet()).build().setVariables(vars).evaluate();
        return result;

    }

}
