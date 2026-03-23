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
### Flow of interaction diagrams
#### Create Profile
```mermaid
flowchart
  subgraph **Create profile**
    create[[create profile]]
    home[[Home display]]
    create-result{Valid username and password crated?}
    create ==inputs: username, password==> create-result
    create-result -. incorrect username or password .-> create
    create-result -. successfully Made profile.-> home
  end
```
#### Logging in 
```mermaid
flowchart
  subgraph **Logging in**
    login[[Login]]  
    end
```
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
    +clear() void
  }
  note for Activity "Preconditions:
    * addExerciseLog: log != null
    * getTotalDistance: since != null
    Postconditions:
    * addExerciseLog: logs contains log
    * removeExerciseLog: log removed if found
    Class Invariants:
    * logs != null"

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
    +getTimestamp() LocalDateTime
    +getDistance() double
    +resetIdCounter() void
  }
  note for ExerciseLog "Preconditions (constructor):
    * name != null && !empty
    * exercise != null
    * route != null && !empty
    Postconditions (constructor):
    * id assigned, id > 0
    * points contains route elements
    * timestamp set to now
    Class Invariants:
    * name != null && !empty
    * points != null && !empty
    * exercise != null
    * id > 0"

  class Exercise {
    -String name
    -Unit unit
    +getName() String
    +getUnit() Unit
  }
  note for Exercise "Preconditions (constructor):
    * name != null && !empty
    * unit != null
    Class Invariants:
    * name != null && !empty
    * unit != null"

  class Grid {
    -int width
    -int height
    -List~ObstaclePlacement~ obstacles
    +getWidth() int
    +getHeight() int
    +isInBounds(Point p) boolean
    +isValid(Point p) boolean
    +addObstacle(ObstaclePlacement o) void
    +removeObstacle(int id) boolean
    +getObstacles() List~ObstaclePlacement~
    +getObstacleAt(Point p) ObstaclePlacement
  }
  note for Grid "Preconditions (constructor):
    * width > 0 && height > 0
    Preconditions (methods):
    * isInBounds: p != null
    * isValid: p != null
    * addObstacle: o != null, location in bounds
    Postconditions:
    * addObstacle: obstacles contains o
    Class Invariants:
    * width > 0
    * height > 0
    * obstacles != null"

  class Point {
    -int x
    -int y
    +getX() int
    +getY() int
    +equals(Object obj) boolean
    +hashCode() int
  }
  note for Point "Preconditions (constructor):
    * x >= 0 && y >= 0
    Class Invariants:
    * x >= 0
    * y >= 0"

  class ObstaclePlacement {
    -int id
    -Point location
    -Obstacle type
    +getId() int
    +getLocation() Point
    +getType() Obstacle
    +occupies(Point p) boolean
    +resetIdCounter() void
  }
  note for ObstaclePlacement "Preconditions (constructor):
    * location != null
    * type != null
    Preconditions (methods):
    * occupies: p != null
    Postconditions (constructor):
    * id assigned, id > 0
    Class Invariants:
    * location != null
    * type != null
    * id > 0"

  class Obstacle {
    <<enumeration>>
    TREE
    BUILDING
    ROCK
    WATER
  }

  class Unit {
    <<enumeration>>
    KILOMETERS
    MILES
    METERS
    STEPS
  }

  Activity --* ExerciseLog
  ExerciseLog --* Exercise
  ExerciseLog --* Point
  Grid --* ObstaclePlacement
  ObstaclePlacement --* Obstacle
  Exercise --* Unit
  ObstaclePlacement --* Point
```
