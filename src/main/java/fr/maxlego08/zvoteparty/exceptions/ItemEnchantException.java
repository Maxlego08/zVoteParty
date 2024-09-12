package fr.maxlego08.zvoteparty.exceptions;

/**
 * Exception thrown when there is an issue with item enchanting.
 */
public class ItemEnchantException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public ItemEnchantException() {
        super(); // Calls the default constructor of Exception
    }

    /**
     * Constructor with a message.
     * 
     * @param message The detail message for the exception.
     */
    public ItemEnchantException(String message) {
        super(message); // Passes the message to Exception constructor
    }

    /**
     * Constructor with a cause.
     * 
     * @param cause The cause of the exception.
     */
    public ItemEnchantException(Throwable cause) {
        super(cause); // Passes the cause to Exception constructor
    }

    /**
     * Constructor with a message and a cause.
     * 
     * @param message The detail message for the exception.
     * @param cause The cause of the exception.
     */
    public ItemEnchantException(String message, Throwable cause) {
        super(message, cause); // Passes both message and cause to Exception constructor
    }

    /**
     * Constructor with a message, cause, suppression flag, and writable stack trace flag.
     * 
     * @param message The detail message for the exception.
     * @param cause The cause of the exception.
     * @param enableSuppression Whether or not suppression is enabled.
     * @param writableStackTrace Whether or not the stack trace is writable.
     */
    public ItemEnchantException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace); // Passes all parameters to Exception constructor
    }
}
