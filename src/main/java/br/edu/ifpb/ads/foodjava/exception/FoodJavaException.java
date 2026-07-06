package br.edu.ifpb.ads.foodjava.exception;

public abstract class FoodJavaException extends Exception {
    protected FoodJavaException(String message) {
        super(message);
    }
}
