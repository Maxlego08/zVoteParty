package fr.maxlego08.zvoteparty.exceptions;

/**
 * Exception thrown when there is an issue creating an item.
 */
public class ItemCreateException extends Error {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public ItemCreateException() {
        super(); // Calls the default constructor of Error
    }

    /**
     * Constructor with a message.
     * 
     * @param message The detail message for the exception.
     */
    public ItemCreateException(String message) {
        super(message); // Passes the message to Error constructor
    }

    /**
     * Constructor with a cause.
     * 
     * @param cause The cause of the exception.
     */
    public ItemCreateException(Throwable cause) {
        super(cause); // Passes the cause to Error constructor
    }

    /**
     * Constructor with a message and a cause.
     * 
     * @param message The detail message for the exception.
     * @param cause The cause of the exception.
     */
    public ItemCreateException(String message, Throwable cause) {
        super(message, cause); // Passes both message and cause to Error constructor
    }

    /**
     * Constructor with a message, cause, suppression flag, and writable stack trace flag.
     * 
     * @param message The detail message for the exception.
     * @param cause The cause of the exception.
     * @param enableSuppression Whether or not suppression is enabled.
     * @param writableStackTrace Whether or not the stack trace is writable.
     */
    public ItemCreateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace); // Passes all parameters to Error constructor
    }
}
