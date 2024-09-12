package fr.maxlego08.zvoteparty.exceptions;

/**
 * Exception thrown for SQL runtime errors.
 */
public class RuntimeSqlException extends RuntimeException {

    private static final long serialVersionUID = 5224696788505678598L;

    /**
     * Default constructor.
     */
    public RuntimeSqlException() {
        super(); // Calls the default constructor of RuntimeException
    }

    /**
     * Constructor with a message.
     * 
     * @param message The detail message for the exception.
     */
    public RuntimeSqlException(String message) {
        super(message); // Passes the message to RuntimeException constructor
    }

    /**
     * Constructor with a message and cause.
     * 
     * @param message The detail message for the exception.
     * @param cause The cause of the exception.
     */
    public RuntimeSqlException(String message, Throwable cause) {
        super(message, cause); // Passes both message and cause to RuntimeException constructor
    }

    /**
     * Constructor with a cause.
     * 
     * @param cause The cause of the exception.
     */
    public RuntimeSqlException(Throwable cause) {
        super(cause); // Passes the cause to RuntimeException constructor
    }
}
