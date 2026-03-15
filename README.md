---
title: Exercise Tracker
author: Karan Anand (<anandk@myumanitoba.ca>)
date: January 30th 2026
---

# REPL

### Building and Running the REPL

The project has been built and tested to be run in IntelliJ. Open the project
there, open the "ca.umanitoba.cs.kanand" folder, then the "Main.java" file.
Finally, click "Run" in the top menu bar!

### Additional Commands

I've added the following commands to support the Exercise Tracker application:

* `ADD MAP` - Initialize the map with a specified width and height!
* `ADD OBSTACLE` - Add a rectangular obstacle to the map!
* `ADD ACTIVITY` - Track a new activity with a route on the map!
* `SHOW MAP` - Display the map with all routes and obstacles!
* `SHOW OBSTACLES` - List all obstacles on the map!
* `SHOW ACTIVITIES` - List all tracked activities!
* `SHOW ACTIVITY` - Display map with a single activity's route!
* `REMOVE ACTIVITY` - Remove an activity by ID!
* `REMOVE OBSTACLE` - Remove an obstacle by ID!
* `REMOVE MAP` - Remove the entire map!

## Domain model

### Resources

* I learned about Java design patterns and domain modeling at
  <https://www.baeldung.com/>.
* I referenced constraint checking practices from Guava library documentation.

### Changes

* I realized when implementing my flow of interaction that
  `Exercise` really should include a `Unit` to measure the exercise properly.
* I needed to add `ObstaclePlacement` to represent obstacles on the grid!
* I created an `ExerciseLog` to track individual exercises with their routes on a grid.
* I implemented a `Grid` class to validate exercise routes and manage obstacles.

### Diagram

Here is the updated diagram for my domain model. This design allows tracking exercises on a grid with obstacles, supporting flexible exercise types measured in different units.

```mermaid
classDiagram
  class Activity {
    -List~ExerciseLog~ logs
    +addExerciseLog(ExerciseLog log) void
    +removeExerciseLog(int id) boolean
    +getExerciseLog(int id) ExerciseLog
    +getAllLogs() List~ExerciseLog~
    +getTotalDistance(LocalDateTime since) double
    +getLifetimeDistance() double
  }
  note for Activity "Manages all exercise activities
    Invariants:
    • logs != null"

  class ExerciseLog {
    -int id
    -String name
    -List~Point~ points
    -Exercise exercise
    -LocalDateTime timestamp
    -double distance
    +getId() int
    +getName() String
    +getPoints() List~Point~
    +getExercise() Exercise
  }
  note for ExerciseLog "Single activity entry with route
    Invariants:
    • name != null && !empty
    • points != null && !empty
    • exercise != null
    • id > 0"

  class Exercise {
    -String name
    -Unit unit
    +getName() String
    +getUnit() Unit
  }
  note for Exercise "Exercise type with measurement unit
    Invariants:
    • name != null && !empty
    • unit != null"

  class Grid {
    -int width
    -int height
    -List~ObstaclePlacement~ obstacles
    +isInBounds(Point p) boolean
    +isValid(Point p) boolean
    +addObstacle(ObstaclePlacement o) void
    +removeObstacle(int id) void
    +getObstacles() List~ObstaclePlacement~
  }
  note for Grid "Map grid with obstacle management
    Invariants:
    • width > 0
    • height > 0
    • obstacles != null"

  class Point {
    -int x
    -int y
    +getX() int
    +getY() int
  }
  note for Point "Grid coordinate
    Invariants:
    • x >= 0
    • y >= 0"

  class ObstaclePlacement {
    -int id
    -Point location
    -Obstacle type
    +getId() int
    +getLocation() Point
    +getType() Obstacle
  }
  note for ObstaclePlacement "Obstacle at specific location
    Invariants:
    • location != null
    • type != null
    • id > 0"

  class Obstacle {
    <<enumeration>>
    TREE
    BUILDING
    ROCK
    WATER
  }
  note for Obstacle "Obstacle type constants"

  class Unit {
    <<enumeration>>
    KILOMETERS
    MILES
    METERS
    STEPS
  }
  note for Unit "Distance measurement units"

  Activity --> "*" ExerciseLog : contains
  ExerciseLog --> "1" Exercise : has
  ExerciseLog --> "*" Point : route
  Grid --> "*" ObstaclePlacement : has
  ObstaclePlacement --> "1" Obstacle : type
  ObstaclePlacement --> "1" Point : location
  Exercise --> "1" Unit : unit
```
