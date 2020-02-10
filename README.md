simulation
====

This project implements a cellular automata simulator.

Names: Max Smith (ms724), James Rumsey (jpr31), Braeden Ward (bmw54)

### Timeline

Start Date: Sunday, January 26th

Finish Date: Sunday, February 9th

Hours Spent: 72 hours (~12 hours per person per week over 2 weeks)

### Primary Roles

*NOTE - Overall programmatic design decisions were made collectively. Individual roles had their associated components 
and design decisions were made on a local level (e.g. structure of JavaFX objects in GUI).*

James Rumsey


Max Smith
- User interface design and development
- Controller design and development

Braeden Ward
- Simulation design and development 
 

### Resources Used

- CS308 course readings and lab/lecture activities
- JavaFX Documentation and Duvall recommended tutorial
- StackOverflow help docuemnts


### Running the Program

Main class: ```Visualization.java```

Data files needed: All properties files are package local in the corresponding ```resources``` folder. Any Simulation 
file from the ```data``` folder can be used. When starting up the program, select any valid XML file to start a Simulation.

Features implemented:

- Simulation
    - Allow different arrangements of neighbors (hexagonal)
    - Allow different kind of grid eges (finite and toroidal)
- Configuration
    - Error checking
        - Incorrect file/simulation or non existent simulation deprecate load, prompted for new file
        - Default cell values to 0.0 (existent state in all Simulations) if incorrect value given
    - Simulation configurations
        - Simulation generator class that creates files based on pre set parameters
    - Simulation styling
        - Grid border are a preset boolean value in ```Visualization.SimPane.java```
    - Save current state of simulation (to XML file titled by time stamp)
- Visualization
    - Implemented a graph displaying populations of all the cell types over time (steps) in the simulation
    - Dynamic user interaction with Simulation
        - User can click on any rectangle in the grid, putting a selection (toggle) box on top in place of the chart, 
        where the user can then select and confirm an update


### Notes/Assumptions

Assumptions or Simplifications:

- All cell states can be mapped to a double value
- User screen resolution will be greater than 700x550 pixels

Interesting data files: 

- ```gameOfLife46``` is a massive grid (100x100)

Known Bugs:

- Once in cell editing mode, multiple cells can be selected to change but only the last selected cell is updated

Extra credit:


### Impressions

Great exercise for learning inheritance heirarchies and delegating assignments across a team. This was all of our first
 experiences working on a large code base from scratch, with a GUI adhering to the MVC model. We believe that our extensive 
 design phase at the start of the project paid dividends long term, making our ```Basic``` implementation much more streamlined, 
 and really helping us out on the backend in the ```Complete``` implementation.