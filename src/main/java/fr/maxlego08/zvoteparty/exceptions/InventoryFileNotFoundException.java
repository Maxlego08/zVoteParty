package fr.maxlego08.zvoteparty.exceptions;

/**
 * Exception thrown when an inventory file is not found.
 */
public class InventoryFileNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public InventoryFileNotFoundException() {
        super(); // Calls the default constructor of Exception
    }

    /**
     * Constructor with a message.
     * 
     * @param message The detail message for the exception.
     */
    public InventoryFileNotFoundException(String message) {
        super(message); // Passes the message to the Exception constructor
    }

    /**
     * Constructor with a cause.
     * 
     * @param cause The cause of the exception.
     */
    public InventoryFileNotFoundException(Throwable cause) {
        super(cause); // Passes the cause to the Exception constructor
    }

    /**
     * Constructor with a message and a cause.
     * 
     * @param message The detail message for the exception.
     * @param cause The cause of the exception.
     */
    public InventoryFileNotFoundException(String message, Throwable cause) {
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
    public InventoryFileNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace); // Passes all parameters to the Exception constructor
    }
}
