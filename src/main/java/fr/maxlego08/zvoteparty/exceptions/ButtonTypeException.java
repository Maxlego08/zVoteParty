package fr.maxlego08.zvoteparty.exceptions;

/**
 * Custom exception for button type errors.
 * 
 * @author Maxlego08
 */
public class ButtonTypeException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public ButtonTypeException() {
        super(); // Calls the default constructor of Exception
    }

    /**
     * Constructor with a message.
     * 
     * @param message The detail message for the exception.
     */
    public ButtonTypeException(String message) {
        super(message); // Passes the message to the Exception constructor
    }

    /**
     * Constructor with a cause.
     * 
     * @param cause The cause of the exception.
     */
    public ButtonTypeException(Throwable cause) {
        super(cause); // Passes the cause to the Exception constructor
    }

    /**
     * Constructor with a message and a cause.
     * 
     * @param message The detail message for the exception.
     * @param cause The cause of the exception.
     */
    public ButtonTypeException(String message, Throwable cause) {
        super(message, cause); // Passes both message and cause to the Exception constructor
    }

    /**
     * Constructor with a message, a cause, suppression, and writable stack trace.
     * 
     * @param message The detail message for the exception.
     * @param cause The cause of the exception.
     * @param enableSuppression Whether or not suppression is enabled.
     * @param writableStackTrace Whether or not the stack trace is writable.
     */
    public ButtonTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace); // Passes all parameters to the Exception constructor
    }
}
