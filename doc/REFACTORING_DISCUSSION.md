### Refactoring Discussion

Today, we removed all use of JavaFX Color on the back-end so that a hexadecimal string is used
instead. We made changes to the visualization to account for this as well. We started removing all
references to the 2D array structure of the grid (currently still in progress). We created a separate
configuration package separate from the simulation package. We also began discussing how we would refactor the 
configuration so that the simulation is constructed there and all error-handling involving invalid
XML input would be done there.

The static analysis tool pointed out a few issues that we are addressing (in order of priority) :

* Making instance variables private in PredatorPreyCell and writing appropriate getters/setters
* Reducing complexity of the longest method (determineNextState in PredatorPreyCell)
* Removing some of the deeply nested if/else statements in SegregationCell
* Removing use of "magic numbers" in various places