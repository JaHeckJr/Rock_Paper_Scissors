# Rock-Paper-Scissors Game with Database and GUI

## Overview

This is a JavaFX-based Rock-Paper-Scissors game that includes a graphical user interface (GUI) and a database to store game statistics, such as the number of wins, losses, and ties.

## Features

- **User-friendly GUI**: The game has a simple and interactive interface built with JavaFX.
- **Game Mechanics**: Players can choose Rock, Paper, or Scissors, and the computer randomly selects a move.
- **Database Integration**: Game results are stored in an SQLite database.
- **Score Tracking**: The game keeps track of wins, losses, and ties across sessions.
- **Reset Feature**: Players can reset their scores at any time.

## Technologies Used

- **Java** (for core logic and game implementation)
- **JavaFX** (for graphical user interface design)
- **SQLite** (for storing game statistics)

## How to Run

1. **Ensure Java and JavaFX are installed** on your system.
2. **Download or clone the repository** containing the source code.
3. **Compile and run the program** using the following command:
   ```sh
   javac org/example/testjava/HelloApplication.java
   java org.example.testjava.HelloApplication
   ```
4. **Play the game** by selecting Rock, Paper, or Scissors from the GUI.
5. **View game statistics**, which are automatically stored in the database.

## Database Structure

The SQLite database (`RockPaperScissors.db`) contains a table named `results` with the following structure:

```sql
CREATE TABLE IF NOT EXISTS results (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    wins INTEGER DEFAULT 0,
    losses INTEGER DEFAULT 0,
    ties INTEGER DEFAULT 0
);
```

## Resetting Scores

To reset your scores, simply click the **Reset Scores** button in the game. This will update the database to set all values to zero.

## Contact

For any questions or collaboration opportunities, feel free to reach out:

- **Email:** [joshua.heck@langston.edu](mailto\:joshua.heck@langston.edu)

This README provides detailed information about the Rock-Paper-Scissors game, showcasing JavaFX development and database integration.
