---
title: Exercise Tracker
author: Karan Anand (<anandk@myumanitoba.ca>)
date: January 30th 2026
---

## Domain model
### Diagram

Here is the diagram for my Exercise tracker app this has the minimal requirements needed I also made it so you could give the exercise a name and the unit to measure it so we can choose a exercise we would like to track and not have it tied down to one exercise

```mermaid
classDiagram

class Activity{
  List~ExerciseLog~ logs
  addExerciseLog(ExerciseLog log) void
}

note for Activity "
Invariant Properties:
  *logs != null

PreConditions:
  *log != null

PostConditions:
  *logs contains log
"

class Exercise{
  String name
  Unit unit
}

note for Exercise "
Invariant Properties:
  *name != null
  *name.length > 0
  *unit != null
"


class ExerciseLog{
  List~Point~ points
  Grid grid
  Exercise exercise
  addPoint(Point point) void
}

note for ExerciseLog "
Invariant Properties:
  *points != null
  *grid != null
  *exercise != null

PreConditions:
  *point != null
  *grid.isValid(point) == true

PostConditions:
  *points contains point
"

class Grid{
  int width
  int height
  List~ObstaclePlacement~ obstacles
  isValid(Point point) boolean
}

note for Grid "
Invariant Properties:
  *width > 0
  *height > 0
  *obstacles != null

PreConditions:
  *point != null

PostConditions:
  *isValid(point) returns true iff:
  *0 <= point.x < width
  *0 <= point.y < height
  *no obstacle exists at point
"

class Point{
  int x
  int y
}

note for Point "
Invariant Properties:
  *x >= 0
  *y >= 0
"

class ObstaclePlacement{
  Point location
  Obstacle type
}

note for ObstaclePlacement "
Invariant Properties:
  *location != null
  *type != null
"

class Obstacle{
  <<enumeration>>
  TREE
  BUILDING
}

Activity --* ExerciseLog
ExerciseLog --o Exercise
ExerciseLog --* Grid
Grid --* Point
Grid --* ObstaclePlacement

```

## REPL Commands

| Command | Description |
|---------|-------------|
| `HELP` | Lists all available commands and input formats |
| `ADD MAP` | Initialize the map with width and height |
| `ADD OBSTACLE` | Add a rectangular obstacle to the map |
| `ADD ACTIVITY` | Track a new activity with a route |
| `SHOW MAP` | Display the map with all routes and obstacles |
| `SHOW OBSTACLES` | List all obstacles on the map |
| `SHOW ACTIVITIES` | List all tracked activities |
| `SHOW ACTIVITY` | Display map with a single activity's route |
| `REMOVE ACTIVITY` | Remove an activity by ID |
| `REMOVE OBSTACLE` | Remove an obstacle by ID |
| `REMOVE MAP` | Remove the entire map and all data |
| `QUIT` / `EXIT` | Exit the application |

## Input Formats

- **Coordinates**: Space-separated integers (e.g., `3 5`)
- **Activities**: Identified by numeric ID
- **Obstacles**: Identified by numeric ID
- **Obstacle types**: `TREE`, `BUILDING`, `ROCK`, `WATER`
- **Units**: `KILOMETERS`, `MILES`, `METERS`, `STEPS`

## Map Symbols

| Symbol | Meaning |
|--------|---------|
| `.` | Empty location |
| `>` | Activity route point |
| `*` | Obstacle |
