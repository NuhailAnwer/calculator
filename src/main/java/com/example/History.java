package com.example;

public class History {

    public int front = 0;
    public int rear = 0;
    private final int min = 0;
    private final int max = 5;
    double[] history = new double[5];
    private int currentViewIndex = -1; 

    public void addHistory(double value) {
        history[rear] = value;
        rear = (rear + 1) % max; 
        if (rear == front) { 
            front = (front + 1) % max;
        }
        currentViewIndex = rear; 
    }

   
    public String viewHistory() {
        if (front == rear && currentViewIndex == -1) { 
                        return null;
        }

        if (currentViewIndex == -1) {
            currentViewIndex = (rear - 1 + max) % max; 
        } else {
            currentViewIndex = (currentViewIndex - 1 + max) % max; 
                }

        if (currentViewIndex == front - 1 || 
            (front == 0 && currentViewIndex == max - 1)) {
            currentViewIndex = -1; 
            return null;
        }

        return String.valueOf(history[currentViewIndex]);
    }
}
   