# SeaBattle

It is a Sea Battle game for Windows. It lets you play with computer (AI) or with a friend by establishing a connection between two games on two different PCs.

## Features

* has a graphical user interface
* enables single and multi-player gameplay
* keeps a record of best scores (single player)
* enables saving and loading gameplay (single player)
* enables sending and receiving messages (multi player)

## Source code

The main package *game* includes *Game* (starter class), *GameExecutor* (executes runs and file & network i/o operations) and *GameState* (keeps data about track of current game).
It also includes file & network i/o operations classes.

A game is represented by a run (e.g. *SinglePlayerGame*). A run has phases (e.g. *Gameplay*) that are divided into elements (e.g. *PlayerMove*).
They are kept in separate packages.

The *gui* package consits of classes of graphical user interface objects.

## Usage

The runnable file is placed in jar folder. You may need to update Java on your PC for it to play.

## License

The game is free to download and use in unmodified state.