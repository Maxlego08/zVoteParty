package fr.maxlego08.zvoteparty.exceptions;

/**
 * Exception thrown when there is an issue opening an inventory.
 */
public class InventoryOpenException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public InventoryOpenException() {
        super(); // Calls the default constructor of Exception
    }

    /**
     * Constructor with a message and a cause.
     * 
     * @param message The detail message for the exception.
     * @param cause The cause of the exception.
     * @param enableSuppression Whether or not suppression is enabled.
     * @param writableStackTrace Whether or not the stack trace is writable.
     */
    public InventoryOpenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace); // Passes all parameters to the Exception constructor
    }

    /**
     * Constructor with a message and a cause.
     * 
     * @param message The detail message for the exception.
     * @param cause The cause of the exception.
     */
    public InventoryOpenException(String message, Throwable cause) {
        super(message, cause); // Passes both message and cause to the Exception constructor
    }

    /**
     * Constructor with a message.
     * 
     * @param message The detail message for the exception.
     */
    public InventoryOpenException(String message) {
        super(message); // Passes the message to the Exception constructor
    }

    /**
     * Constructor with a cause.
     * 
     * @param cause The cause of the exception.
     */
    public InventoryOpenException(Throwable cause) {
        super(cause); // Passes the cause to the Exception constructor
    }
}
