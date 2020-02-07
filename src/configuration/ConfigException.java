package configuration;

public class ConfigException extends RuntimeException {

    /**
     * Create an exception based on an issue in our code.
     */
    public ConfigException (String message, Object ... values) {
        super(String.format(message, values));
    }

    /**
     * Create an exception based on a caught exception with a different message.
     */
    public ConfigException (Throwable cause, String message, Object ... values) {
        super(String.format(message, values), cause);
    }

    /**
     * Create an exception based on a caught exception, with no additional message.
     */
    public ConfigException (Throwable exception) {
        super(exception);
    }


}
