package com.example;

public class History {
    public int front = 0;
    public int rear = 0;
    private final int min = 0;
    private final int max = 4;
    double[] history = new double[5];

    // history is working on the principle of circular queue
    //where it have a fix size and will rewrite the answers 
    //all the maintainence of this array is through 2 pointers that are rear and front 
    public void addHistory(double value) {
        if (rear == 0 && front == 0) {
            history[rear] = value;
            rear++;
        } else {
            history[rear] = value;
            rear = (rear + 1) % max;  // Wrap around if it reaches the end of the array
            if (rear == front) {  // Queue is full
                front = (front + 1) % max;  // Wrap around front as well
            }
        }
    }
    
    public String viewHistory() {
        if (rear == 0 && front == 0) {
            return null;
        }
       if(front==max){
        String result = String.valueOf(history[front]);
        front = min;
        return result;
       }
       String result = String.valueOf(history[front]);
       front ++;
       return result;
    }
}
