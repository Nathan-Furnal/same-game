# Introduction

Repository for the "Same Game" project. It's composed of a model and two views as well as two controllers to manage the
game. Written in Java and JavaFX for the UI.

# Demo

Here is a demo of the game in action:

https://user-images.githubusercontent.com/45597572/151636316-c2673a17-2472-46d1-bb05-c5c2c33577bb.mp4 

# Structure

Here under is the abridged structure of the project.

```bash
src/
├── main
│   ├── java
│   │   ├── g55803
│   │   │   └── samegame
│   │   │       ├── fx
│   │   │       │   ├── fxcontroller
│   │   │       │   │   └── Controller.java
│   │   │       │   ├── fxview
│   │   │       │   │   ├── ButtonBox.java
│   │   │       │   │   ├── FXView.java
│   │   │       │   │   ├── GameLayer.java
│   │   │       │   │   ├── ScoreBox.java
│   │   │       │   │   └── StartLayer.java
│   │   │       │   └── Main.java
│   │   │       ├── model
│   │   │       │   ├── Color.java
│   │   │       │   ├── commands
│   │   │       │   │   ├── Command.java
│   │   │       │   │   └── PlayCommand.java
│   │   │       │   ├── Direction.java
│   │   │       │   ├── Facade.java
│   │   │       │   ├── Field.java
│   │   │       │   ├── Position.java
│   │   │       │   ├── State.java
│   │   │       │   └── Tile.java
│   │   │       ├── terminal
│   │   │       │   ├── controller
│   │   │       │   │   └── TermController.java
│   │   │       │   ├── Main.java
│   │   │       │   └── termview
│   │   │       │       └── TerminalView.java
│   │   │       └── utils
│   │   │           ├── Observable.java
│   │   │           ├── Observer.java
│   │   │           └── ParsingUtils.java
│   │   └── module-info.java
│   └── resources
│       ├── css
│       └── fonts
└── test
    └── java
        └── g55803
            └── samegame
                └── model
                    ├── FieldTest.java
                    ├── PositionTest.java
                    └── TileTest.java
```

The model holds the logic of the game : the rules, the algorithms to play the game and the values accepted during a
game.

There are two views and controllers under `terminal` and `fx` respectively as well as a `utils` folder with various
useful classes and a `test` folder to test the game logic and the error/exception handling.

## Design patterns

This section details the design patterns and their implementation in the project.

### Command Pattern

This pattern is implemented in the model. It implements an interface that has only two methods : `execute` and `cancel`.
The only class implementing this command is the `PlayCommand`. When move is played by the user, the state of the model
is saved in the command. An action and its state are added to the Facade when a move is played in order to have a
history of the moves played.

In short,

- A move is saved any time it is played.
- There are two stacks (undo and redo) which save an action.
- Playing a move adds to the undo-stack and clears the redo-stack.
- The user can go back in the undo-stack, which adds to the redo-stack.

### Facade Pattern

The facade is implemented in the model, it provides an easy-to-use middle layer to interact with the views and the
controllers. It also protects against changes in state since only allowed members are interacted with through the
facade.

### Observer Pattern

The observer and observable interfaces are found in the `utils` folder. The facade is considered to be the observable,
the JavaFX view is the observer. This pattern was not used in the terminal view. Here, the observable updates the
observer any time it is needed, which triggers a reaction from the observer. Here I chose to have only one observer
which delegates the relevant actions to its components necessary.

### MVC

This section addresses how to code was broken down to implement the Model-View-Controller design pattern.

#### Model

The model is a field (a 2d array) of tiles. Tiles are defined by their color, most of the interactions between tiles
only consider position or color. The model provides colors in the form of strings. Views are encouraged to map those
strings to a more convenient representations. Such as emojis or JavaFX paints.

#### Terminal View

To be used in the terminal, with or without emojis. Most of the focus of the view is the interaction with the controller
as well as the arguments parsing.

#### JavaFX View

A GUI for the game. Most of the focus is with state management and listening to changes of state or user actions.

#### Controllers

Both controllers are meant to orchestrate the view(s) with the model while providing a measure of safety by checking
methods arguments, states and catching exceptions when possible. 


# Credits

Project devised and created during my studies at the École Supérieure d'Informatique (ESI), Brussels.
