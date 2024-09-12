package fr.maxlego08.zvoteparty.exceptions;

public class ButtonCreateItemStackNullPointerException extends Exception {

    private static final long serialVersionUID = 1L;

    public ButtonCreateItemStackNullPointerException() {
        super(); // Calls the default constructor of Exception
    }

    public ButtonCreateItemStackNullPointerException(String message) {
        super(message); // Passes the message to the Exception constructor
    }

    public ButtonCreateItemStackNullPointerException(Throwable cause) {
        super(cause); // Passes the cause to the Exception constructor
    }

    public ButtonCreateItemStackNullPointerException(String message, Throwable cause) {
        super(message, cause); // Passes both message and cause to the Exception constructor
    }

    public ButtonCreateItemStackNullPointerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace); // Passes all parameters to the Exception constructor
    }
}
