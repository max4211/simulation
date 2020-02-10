package configuration;

public class InvalidSimulationException extends Throwable {

    private static final String INVALID_MESSAGE = "Invalid Simulation";

    public InvalidSimulationException() {
        super(INVALID_MESSAGE);
    }
}
