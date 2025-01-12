# Angry Birds Game

## Overview
Welcome to the Angry Birds Game project! This is a simplified recreation of the iconic Angry Birds game, developed using the **libGDX framework** in Java. The project showcases core game development concepts and implements various **object-oriented programming (OOP)** principles such as inheritance, encapsulation, and polymorphism for structured and modular code. It leverages libGDX features, including built-in support for graphics, audio, input handling, and physics, to create an engaging and immersive gameplay experience.

## Game Features
- **User-Friendly Controls**:
  - Drag-and-release mechanics for launching birds.
  - Intuitive key-based controls for pausing, restarting, and saving the game.

- **Three Levels**:
  - Levels: Easy, Medium, and Hard.
  - Each level has unique designs, obstacles, and targets, offering increasing difficulty.
  
- **Diverse Game Elements**:
  - **Birds**: Three unique types of birds with distinct properties.
  - **Pigs**: Three types of pigs with varying durability.
  - **Materials**: Three types of blocks (wood, stone, glass) used to build obstacles.

- **Progression and Scoring**:
  - Players must eliminate all pigs to proceed to the next level.
  - The score increases as pigs are eliminated.
  - Players have three chances to complete each level.

- **Game Pause and Restart**:
  - Press `P` to pause the game.
  - Use the pause menu to resume, restart, or return to the main menu.
  - Restart directly by pressing the `Space` bar.

## Serialization
- Save your progress:
  - Press `S` to save the game state (score and remaining chances).
  - The game state is saved as a `.ser` file (e.g., `game_state_<levelname>.ser`) in the assets folder.
- Load a saved game:
  - Press `L` to load the last saved state.

## How to Run the Game
To run the game, use the following command in your terminal:
```
./gradlew lwjgl3:run --stacktrace
```

## JUnit Testing
The game includes **JUnit tests** to ensure functionality and accuracy. Key features tested include:
- Trajectory calculations for launching birds.
- Verifying the X and Y coordinates of sprites during gameplay.

## Sources
- Images and textures have been sourced from the Angry Birds movie and various free texture repositories.

## Built With
- **Java**: The primary programming language for development.
- **libGDX**: A cross-platform game development framework.
- **Maven** and **Gradle**: For dependency management and build automation.



