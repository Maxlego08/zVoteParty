package fr.maxlego08.zvoteparty.exceptions;

/**
 * Exception thrown when an inventory already exists.
 */
public class InventoryAlreadyExistException extends Error {

    private static final long serialVersionUID = -5611455794293458580L;

    /**
     * Default constructor.
     */
    public InventoryAlreadyExistException() {
        super(); // Calls the default constructor of Error
    }

    /**
     * Constructor with a message and a cause.
     * 
     * @param message The detail message for the exception.
     * @param cause The cause of the exception.
     */
    public InventoryAlreadyExistException(String message, Throwable cause) {
        super(message, cause); // Passes both message and cause to the Error constructor
    }

    /**
     * Constructor with a message.
     * 
     * @param message The detail message for the exception.
     */
    public InventoryAlreadyExistException(String message) {
        super(message); // Passes the message to the Error constructor
    }

    /**
     * Constructor with a cause.
     * 
     * @param cause The cause of the exception.
     */
    public InventoryAlreadyExistException(Throwable cause) {
        super(cause); // Passes the cause to the Error constructor
    }

    /**
     * Constructor with a message, a cause, suppression, and writable stack trace.
     * 
     * @param message The detail message for the exception.
     * @param cause The cause of the exception.
     * @param enableSuppression Whether or not suppression is enabled.
     * @param writableStackTrace Whether or not the stack trace is writable.
     */
    public InventoryAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace); // Passes all parameters to the Error constructor
    }
}
