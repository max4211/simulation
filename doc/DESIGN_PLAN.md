# Simulation Design Plan
### Team 25
### James Rumsey, Max Smith, Braeden Ward

## Introduction
Our team is developing a Cellular Automata simulation that can support a variety of simulations containing various Cell types and rules. The extension of the CA to new simulation types is very open, and is purely dependent on the extension of the Cell abstract class, implementation of a new ```concreteCell()``` constructor and ```determineUpdate()``` methods, as well as an additional configuration option. The simulation is also open to the consideration of new neighbors, provided a new mapping from a neighbor description (e.g. Von Neumann or Moore) to cell row and column deltas. The CA is closed to the consideration of updating a Cell dependant on neighbor *positional weights* and states. The CA is also closed to states that cannot be mapped to a double value, and cell grids that cannot be represented as a 2D Array.

## Overview
We plan on implementing a package for the major, separable components of the project (e.g. Simulation, and Visualiztion).

The Visualization class is the main driver of the program, and supports user input to load file (from ```resources``` folder), pause, play, step, and set animation rate for the Simulation. It is an extension of the JavaFX Application interface, and holds the stage that displays the GUI organized in Layout Panes. When the user presses a button in the display to load a file and selects it, a new Simulation constructor is called on that file. The Visualization is also responsible for dictating when to update the Simulation (by calling ```Simulation.updateGrid()```). The rate of update is determined from user input via button selection (for ```Pause```, ```Step```, ```Play```) or the ```animationRate``` slider. The ```updateGrid``` method returns a double of ```javafx.Color``` values that are then directly populated in the Visualization grid.

The Simulation needs to know the Cell Objects in the grid and which neighbors to consider for updates. The initial simulation state is given to the Simulation class by a configuration method. Cell updates are performed with respect to their state, and the state of their relevant neighbors. The Simulation class maintains the grid of Cell Objects, dictates when to perform updates, and communicates Cell states to the Visualization component. The Simulation parameters are determined in the configuration stage from the XMLFile and are stored in individual Cell Objects. The Simulation constructor takes in a String `configFile` that is the path to a correctly formatted `.xml` file. The constructor (with helper methods) reads the grid size, simulation type, and initial states for all cells and constructs `myGrid` accordingly. Based on the simulation type, `ROW_DELTA` and `COL_DELTA` are also assigned.

The Simulation class is responsible for implementation of simulations of a variety of types, maintain memory of cells in a grid, and update cells based on their relevant neighbors. To determine relevant neighbors, there is mapping from an XML inputted string to row and column deltas (see *Example Simulation Code* below). We decided to represent the Cell grid as a 2D array of Cell objects. The Cell Array has benefits of ease of lookup and neighbor reference. Furthermore, to handle the possibility of edge Cells and corner Cells in the grid with *incomplete neighborhoods*, we decided to pass the ```neighborStates``` as a Collection of States (doubles). 

Each Cell subclass is an extension of the abstract Cell class. All Cells need to be able to represent their current state, communicate their current state, and update their current state. We chose to implement this by having two instance variables representing the current state (```myState```) of the Cell and the next State (```nextState```), along with three public methods to ```updateState()```, ```determineUpdate()```, and ```getState()```. The two state change methods (```determineUpdate()``` and ```updateState()```) have been separated to account for the entire simulation updating with respect to current neighbor states. This allows us to iterate over the grid twice per step, once to determine updates (stored in ```nextState``` instance variable), and then again to update states without already determined updates affecting other cells in the same step. Another option considered to ensure that Cell updates are performed at the *same time* in Simulation space was to copy the grid over, then perform updates on the new grid dependend on the old grid states. However, we determined that this is an unnecessary use of space and duplication of Objects, when all we need to know are their current states.

The abstraction of the Cell class with polymorphic calls to ```updateState()```, ```determineUpdate()```, and ```getState()``` allow us to implement a variety of Cell types with varying rules on update by simply extending the abstract Cell class. The alternative implementation would be to have multiple Cell types implemented through the same concrete class, and having a large if-else tree when determining updates along with various types of instance variables to account for different types of Cells. This is a clear violation of the Open-Close principle.

### CRC Cards

![](https://i.imgur.com/IMGbn5r.png)
![](https://i.imgur.com/uvLSolf.png)

### Functionality Overview
![](https://i.imgur.com/QDJlpGl.png)

## User Interface
All of the different games would have the same user interface. Below the visualization of the grid of cells we will have a slider to control the simulation speed. Below that we will have several buttons that load, pause, resume, and step through the simulation. The UI will look similar to the drawing below (which takes inspiration from the predator-prey simulation):

![](https://i.imgur.com/bkv8uvP.jpg)

When an ```.xml``` file is uploaded that is incorrectly formatted or an invalid file type is uploaded, an error pop-up would display. If there is a mismatch between the size (height and width) provided by the ```.xml``` file and the configuration of initial states provided, then an error message will also display.

## Design Details

### Simulation
Due to the variance in CA types, we made the design decision to have an inheritance hierarchy of `Cells` stemming from a single abstract `Cell` class. This allows us to uphold the Open-Closed principle in the development of new simulations. Additional requirements coming in the form of new Simulations can be easily incorporated via the extension of the Cell class.

When prompted by the Visualization, the Simulation iterates through the cell grid, determining updates to make by calling ```Cell.determineUpdate()```, and then performs those updates by calling the polymorphic ```Cell.updateState()``` method. A ```Cell```'s ```myColor``` is also updated, since the Visualization requires a ```Color``` mapping for each state to display. An instance of the abstract class, and all extensions need to know the current state (```myState```) as well as their next state (```nextState```). Furthermore, because Cell updates are only dependent on the state of their neighbors, the full Cell object is never passed around to preserve object integrity. Instead, its functionality (update/get State and get Color) is fully encapsulated within each Cell class and Cell states are passed in a Collection to determine the next State. Additionally, the Visualization only needs to respond to Color input, which is why each cell has a series of color mapping methods to determine how they are displayed. Each Cell has a ```colorMap``` data type so for each simulation, colors can be customized. 

Another major design decision for the Simulation was how to represent the grid of Cells. We selected a 2D Array for its ease of access with respect to both individual components and neighbors. Also, we envision simulations where the *neighborhood* comes in varying forms (as configured in the XML file). The result of this configuration is the declaration of ```int[] ROW_DELTA``` and ```int[] COL_DELTA``` instance variables which determine the relevant neighbors for updates. To work around the variation in Cells without full neighborhoods, before appending the Collection of ```neighborStates``` we verify that the cell is ```inBounds```. This generalization and implementation of the ```neighborStates``` as a Collection ensure that the process is the same for all Cells, regardless of their position or number of neighbors.

### Example Simulation Code
```java
public class Simulation {

    private Cell[][] myGrid;
    private int[] ROW_DELTA;
    private int[] COL_DELTA;
    
    public Simulation(String configFile){
        // Construct myGrid here depending on simulation type
        // Assign neighborhoods based on neighborhood attributes
    }

    public Color[][] getColorGrid() {
        updateGrid();
        return new Color[0][0];
    }
    
    private void updateGrid() {
        // Determine Cell updates
        // Implement cell updates
    }

    private Collection<Double> getNeighborStates(int row, int col) {
        return new ArrayList<Integer>();
    }

    private boolean inBounds(int row, int col) {
        return (row <= myGrid.length) && (col <= myGrid[0].length);
    }

}
```

### Example Cell Abstract Code and Concrete Extension
```java
public abstract class Cell {
    private double myState;
    private double nextState;
    private Map<Double, Color> myColorMap;

    public Cell(double initialState) {
        this.myState = initialState;
        createColorMap();
    }
    
    public abstract void createColorMap();

    public abstract void determineState(Collection<Double> neighborStates);
    
    public abstract double mapKey(double myState);

    public void updateState() { 
        this.myState = this.nextState;
    }

    public double getState() { return this.myState; }
    
    public Color getColor() { 
        double key = mapKey(this.myState);
        return this.myColorMap.get(key); 
    } 
}
```

```java
public class FireCell extends Cell {

    private double PROB_CATCH;

    public FireCell(double initialState, double probCatch) {
        super(initialState);
        this.PROB_CATCH = probCatch;
    }

    @Override
    public void determineState(Collection<Integer> neighborStates) {

    }
    
    @Override
    public void createColorMap() {

    }
    
    @Override
    public void mapKey(double myState) {

    } 
}
```

### Visualization
The Visualization will be the class that extends Application and directs updates from the Simulation. This class acts as the main JavaFX application that runs the program. Through the Load button, the user can tell the Visualization which `.XML` file to simulate. A method `loadFile()` returns a String with the `.xml` file name, which is passed into the constructor for Simulation.The Visualization checks to make sure that the file type is correct before constructing a new Simulation with the file name as the only argument. 

At the rate specified by the UI, the Visualization will prompt the Simulation to update the states of the `Cell`s and to return the updated colors of each `Cell`. The Visualization will use that to update the image on screen. The `step()` method is called whenever an update to the grid should be made and should call `getColorGrid` from Simulation. This method is either called when pressing the "Step" button in the window (when the game is paused), or continuously based on mySpeed.

### Use Cases

* Apply the rules to a middle cell: set the next state of a cell to dead by counting its number of neighbors using the Game of Life rules for a cell in the middle (i.e., with all its neighbors)

The Simulation class calls ```getNeighborStates(int row, int col)``` on the cell that we are on in the grid. It then checks all surrounding neighbors, verifying that they are ```inBounds()``` within the simulation before appending there ```getState()``` values to the neighborStates collection. The Simulation then passes the ```Collection<Double> neighborStates``` data structure into the middle cells ```determineState(Collection<Double> neighborStates)```) method. The Cell then uses its own built in rules (in this case, its number of alive/dead neighbors) to determine what its ```nextState``` instance variable should be dependent on its ```myState``` field and its ```neighborStates``` Collection. Once the ```nextState``` fields have been computed for all Cells in the grid, the Simulation then runs through the Cell grid again, calling the superclass ```updateState()``` method, which assigns the state accordingly. Another note is that all states have been mapped to a ```double``` value (e.g. Alive --> 1, Dead --> 0).

* Apply the rules to an edge cell: set the next state of a cell to live by counting its number of neighbors using the Game of Life rules for a cell on the edge (i.e., with some of its neighbors missing)

Our ```inBounds()``` check on all prospective neighbors before updating their values, along with holding ```neighborStates``` as a Collection of values uphold the same process integrity as above. The only change is that the Collection of ```neighborStates``` will be slightly smaller.

* Move to the next generation: update all cells in a simulation from their current state to their next state and display the result graphically

Moving to the next generation is prompted of the Simulation by the Visualization in the ```Simulation.getColorGrid()``` method call.  The method calls a private instance of ```updateGrid()``` which performs the two phase update on each Cell Object (```Cell.determineUpdate()``` --> ```Cell.updateState()```). Once the simulation has been updated according to its built in rules contained within each cell, the final step in the process is to call the ```getColor()``` method of each Cell, and returning all of these ```javaFX.Color``` data types in a grid which is then published graphically by the Visualization method.

* Set a simulation parameter: set the value of a global configuration parameter, probCatch, for a simulation, Fire, based on the value given in an XML fire

Given our design of an inheritance heirarchy with abstraction of Cell classes to create new Simulations, global configuration parameters will be stored in each Cell object and instantiated as the XML file is read in. This parameter will then be used in the computation for ```Cell.determineState()```.

* Switch simulations: load a new simulation from an XML file, stopping the current running simulation, Segregation, and starting the newly loaded simulation, Wator

When a simulation is changed (via file selection from the Visualization GUI), the Visualization instance variable ```mySimulation``` is updated to reflect this change. The file path is sent into the ```Simulation.Simulation(fileName)``` constructor, which parses the new XML file metadata and returns a new instance of said Simulation. The layers of abstraction and polymorphism regarding the Simulation still hold. The new Wator simulation will replace the old Segregation simulation, however all other method calls will remain the same. The Visualization component still requests a Color grid from the Wator simulation for each user configured step (via animation rate or pause/step functionality). 


## Design Considerations

### Creating Simulations of Different Types
To keep as much of our program constant across all different simulation types, we decided the only thing to change would be the type of cell that is used. Each cell knows its state and how that maps to its color as well as the rules for how to update its state based on its neighbors. The rest of the program can remain unchanged across all simulations, making it easier to implement new CAs.

We discussed whether to also have several different simulation classes that would store the rules for each type of simulation, but when we realized that the majority of the simulation's code is constant across all different simulation types, we decided it would be easier to store all type-specific code in classes that implement the `Cell` abstract class.

### Cell Design
We wanted to create a cell whose implementation is flexible enough to be applied to nearly any type of cell no matter how many states the cell could be in, but we also wanted to keep this implementation reletively simple. We chose to represent the state of each cell by a single double which would be mapped to a certain state within each extension of the cell according to that cell's coded rules.

We have talked about using ints instead of doubles. This would probably simplify things and would work for most cases, but we wanted to leave the option open to create cells for which the state can be a value on a continuous gradient.

### Grid Design
We decided thae the best way to store the grid would be as a 2D array. We chose this because it is easier to work with than other possible data structures and it makes it easy to a Cells and its neighbors.

### Cell Updates
If the Simulation updated the states of the cells as it scanned through them, it would call errors as following cells used the updated data instead of the previous data. To avoid this problem, we decided to split the updating into two steps. In one step, each cell checks what its next state will be and 


## Team Responsibilities

 * James - Back End
 * Braeden - Back End
 * Max - Front End


During the first sprint, James and Braeden will work on the first four steps under Simulation below together and divide the four remaining simulation types equally between us. Max will be working on the front-end visualization. When we are ready to integrate, the three of us will be working together.

### Basic Project Steps
#### Simulation (Back End)
1. Figure out how we're creating .XML files
2. Figure out how we're reading .XML files
3. Implement Cell abstract class and Game of Life Cell class
4. Implement Simulation class
5. Divide and implement other cell/simulation types

#### Visualization (Front End)
1. Research panes to determine concrete implementation
2. Create pane layout draft, approve with rest of team
3. Get user input to update Visualization instance variables and step rate
4. Populate graphical interface with 2D Color Array
5. Integrate with Simulation