package simulation;

public class State {
    private double myDouble;
    private String myString;
    private String myColor;

    public State(double state, String string, String color) {
        myDouble = state; myString = string; myColor = color;
    }

    public State(String string, String color) {
        myString = string; myColor = color;
    }

    public String toString() {return this.myString;}
    public double getState() {return this.myDouble;}
    public String getString() {return this.myString;}
    public String getColor() {return this.myColor;}

}
