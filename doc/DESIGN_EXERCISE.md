# Design Exericse
## Lab Thursday January 23 2020

#### Simulation Design Questions

- How does a Cell know about its neighbors? How can it update itself without effecting its neighbors update?
Passed in information from individual cell getters from the Main Simulation class. It can update itself by having an
update method that is only dependent on neighbor information, and not on the neighbor objects themselves.

- What relationship exists between a Cell and a simulation's rules?
The cell has an update method that is determined by the simulation's rules.

- What is the grid? Does it have any behaviors? Who needs to know about it?


- What information about a simulation needs to be the configuration file?

- How is the graphical view of the simulation updated after all the cells have been updated?

#### Component Design
- Configuration is responsible for creating the cell society, and declares rules for update.
    - Rules for update is gonna be tricky.
- Simulation is responsible looping through each cell and updating it based on its neighbors and rules.
- Visualization has no editing power on the simulation grid, but is aware of the current state of cells.

#### General Notes
- Cell class should be abstract, in the sense that we can create different types of cells with different states.
- The basic cell functionality is constructor (creation), update (state/data), get (state/data).
- Cell could also have an ability to send a "lightweight" version to the visualization component to ensure that
the visualization class cannot update cells.
