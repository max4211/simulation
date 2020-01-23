# Design Exericse
## Lab Thursday January 23 2020

#### RPS Abstract Design
For our RPS game, our discussion lead to the following prospective implementation.

Three object types - Game, Player, and Weapon - that interact to operate the rules of the game.

The Weapon class is constructed with a name, collection of weapon names it defeats, and a collection of weapons
that it is defeated by. The Weapon class has a single public function that inputs another weapon string (identifier) 
and returns a boolean value determining whether or not the weapon won the contest. The Weapon class interacts with
Players (who select weapons), and the Game class to process weapon selection and competitions.

The Player class will be an abstract class, with extensions that allocate a specific Collection of allowed weapons to 
select, as well as a strategy for how to select weapons and their initial score. It will have a single global function to select weapons 
that returns a String corresponding to the Player's currently selected weapon. It interacts with the game to return its
selections as well as the weapon class to select weapons. We decided to make the player class abstract to account for 
variations in player strategy, as well as various allowances for weapon selection. Although this information may seem 
redundant, it is very possible that a player is generated with restrictions on weapons they can select, so it makes sense
for each Player object to hold all possible selections.

The Game class is constructed with a collection of players (with associated strategies and weapon choices), a Collection
of Weapons (potentially in Map form) that declares the rules of the game. It also contains methods to compare Player 
Weapon selection and determine who wins, as well as a game loop to iterate through, fetch Player weapons, and have 
them compete against each other until some termination condition (e.g. points scored, rounds played).