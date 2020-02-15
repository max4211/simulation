# Simulation Design Final
### Max Smith, James Rumsey, Braeden Ward

## Team Roles and Responsibilities

 * Max Smith - Visualization and Configuration

 * James Rumsey - Simulation and Configuration

 * Braeden Ward - Simulation and Configuration


## Design goals
Our major design goals were to make the project flexible to additions with regards to new simulations as well as new features to 
the GUI. The simulation class holds concrete implementations of an abstract cell object. These concrete implementations 
which define the abstract methods associated with each cell define how these cells interact (mainly how neighbor states affect
cell updates) and are the core functionality behind new simulation types.

Additionally, the GUI leverages the JavaFX BorderPane funcitonality to allow for easy implementation of new features (e.g. 
the chart was trivial to add to the top portion of the layout pane).

#### What Features are Easy to Add
As mentioned above, it is trivial to add new simulation types (strictly cells) as well as new features to the GUI layout.
Addtionally, it is easy to add new level generators (through an impelemntation of the grid generator abstract class). Additionally,
the Visualization can support any type of Cell modification (through the getters and setters in each concrete cell class implementation).

## High-level Design
At a high level, we have a configuraiton that creates a simulation object and passes it to the visualizaiton.
Then, on each step the visualzition calls for a cell update (when applicable given toggle button settings) and updates
the simulation accordingly. The full visualization and controller functionality operates through the visualization class.

#### Core Classes
The core classes in the program are the reflection core namings in each packege (e.g. Visualizaiton, Configuraiton, and Simulation).
Each core class holds the majority of the relevent objects. As a design feature, we encapsulated fucntionality when possible.

## Assumptions that Affect the Design
One key assumption we made is that the grid can be represented as a list of lists. This assumption
does indeed hold for triangle and hexagonal shapes given careful algebraic manipulations (e.g. in 
a hexagon, skip the first cell every other row, and skip the last cell every other row). In hindsight,
our design could have been greatly improved by implementing the grid as a single class
with getters and setters that were configured based on the grid design. This would have hidden the specific
grid implementation and decreased dependendices in respective visualization and configuration classes on the 
grid representation as a 2D collection.

#### Features Affected by Assumptions

## New Features HowTo
Additional features should be added to the package they most pertain to, with additional modificaitons
to any dependencies on said package.

#### Easy to Add Features
One easy set of features to add are new simulation types and new neighborhood types. For new cell types, a new implementation
of the abstract cell class must be created which handles cell updates based on a given state and neighbor states. For new neighborhoods,
all that needs to be added is a single switch case condition in the configuration stage with appropriate neighbor pairs
for said neighborhood.

#### Other Features not yet Done
Some features not yet done are different types of simulation component shapes (e.g. hexagonal and triangular)
as well as customizable visualizations (e.g. background, grid ouutlines, etc.
. 

To incoroporate new types of simluations (shapes of objects), the grid should be encapsulated within a singular object
with appropriate getters and setters to eliminate this dependency within the visualziation and simulation classes. Additionally,
the visualizaiton regions should take on the desired shape (e.g. triangle or hexagon). Regarding visualization customization,
there would need to be an added layer to containerize the visualization, as well as a refactoring to contain a visualization 
constructor. This design, along with a splash screen, would allow a singular visualizaiton with one simulation to be created
with all necessary objects, with a single set of controls per simulation. This comes with the
assumption that each simulation would require a single set of controls, which we believe is a reasonable assumption.
