package simulation;

/**
 * Represents a State of a cell, including its double mapping, String description, and color
 * as a hexadecimal String
 */
public class State {
    private double myDouble;
    private String myString;
    private String myColor;

    /**
     * Constructs a State object
     * @param state double mapping of state
     * @param string String description of state
     * @param color hexadecimal string representing color
     */
    public State(double state, String string, String color) {
        myDouble = state; myString = string; myColor = color;
    }

    /**
     * Constructs a state if no double mapping is present
     * @param string String description of state
     * @param color hexadecimal string representing color
     */
    public State(String string, String color) {
        myString = string; myColor = color;
    }

    /**
     * Accessors for instance variables above
     */
    public String toString() {return this.myString;}
    public double getState() {return this.myDouble;}
    public String getString() {return this.myString;}
    public String getColor() {return this.myColor;}

}
