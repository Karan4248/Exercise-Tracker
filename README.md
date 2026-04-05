---
title: Exercise Tracker
author: Karan Anand (<anandk@myumanitoba.ca>)
date: March 24th 2026
---
## Resources

*mermaid flow chart syntax: <https://mermaid.ai/open-source/syntax/flowchart.html>

# Flows of Interaction

## Diagrams

### Overall Flow

```mermaid
flowchart
  subgraph Overall
    direction TB
    createProfile[[Create/Select Profile]]
    viewFeed["View Activity Feed"]
    addActivity["Add Activity"]
    findRoute["Find Route"]
    logout[[Logout]]
    
    createProfile -- "Profile created/Selected" --> viewFeed
    viewFeed -- "Add activity" --> addActivity
    addActivity -- "Return to feed" --> viewFeed
    viewFeed -- "Find route" --> findRoute
    findRoute -- "Return to feed" --> viewFeed
    viewFeed -- "Logout" --> logout
  end
```

#### Create or Select User Profile

```mermaid
flowchart
  subgraph **CREATE OR SELECT PROFILE**
    direction TB
    start[[Start]]
    login["Login or create profile"]
    success[[Logged in]]
    valid{validate credentials}
    start -- User enters info --> login
    login -- input: users info -->valid
    valid -- Credentials valid, logged in --> success
    valid -. Invalid credentials .-> login
  end
```

#### Edit User Profile

```mermaid
flowchart
  subgraph **EDIT PROFILE**
    direction TB
    start[[Home]]
    edit["Edit username or password"]
    success[[Changes saved]]
    valid{validate changes}
    start -- Select edit --> edit
    edit -- input:users new info --> valid
    valid -- New value valid, changes saved --> success
    valid -. Invalid input, try again .-> edit
  end
```

#### Logout

```mermaid
flowchart
  subgraph **LOGOUT**
    direction TB
    start[[Home]]
    confirm["Confirm<br/>logout"]
    success[[Logged out]]
    
    start -- Select logout --> confirm
    confirm -- Yes, logout confirmed --> success
    confirm -. Cancel, return to home .-> start
  end
```

#### Add Activity

```mermaid
flowchart
  subgraph **ADD ACTIVITY**
    direction TB
    start[[Home]]
    info["Enter activity name and type"]
    route["Copy existing<br/>route or new route"]
    input["Route points"]
    obs["Add obstacles"]
    addobs["Enter obstacles"]
    validate{Validate route}
    success[[Activity saved]]
    start -- Select add activity --> info
    info -- Enter details --> route
    route -- Copy from previous --> input
    route -- Create new --> input
    input -- input:route points or selected routes -->validate
    validate -. invalid route .-> input
    validate -- Route valid --> obs
    
    obs -- Yes, add obstacles --> addobs
    obs -- No obstacles --> success
    addobs -- Obstacles entered --> success
  end
```

#### View Feed & Follow Users

```mermaid
flowchart
  subgraph **VIEW FEED & FOLLOW**
    direction TB
    start[[Home]]
    browse["Browse and follow users"]
    success[[Feed updated]]
    
    start -- Select view feed --> browse
    browse -- Continue browsing, find more users --> browse
    browse -- Done browsing, users followed --> success
  end
```

#### Find New Route

```mermaid
flowchart
  subgraph **FIND NEW ROUTE**
    direction TB
    start[[Home]]
    find["Select route source and enter points"]
    route{"Validate and find route"}
    result["Save route"]
    success[[Route saved]]
    start -- Select find route --> find
    find -- Route source and points selected --> route
    route -- Route valid, Route algorithm executed, path found --> result
    route -. No path between points, try different points .-> find
    result -- Yes, save as activity --> success
    result -. No, discard route .-> start
  end
```

# REPL

## Building and Running the REPL

The project has been built and tested to be run in IntelliJ. Open the project
there, open the "ca.umanitoba.cs.kanand" folder, then the "Main.java" file.
Finally, click "Run" in the top menu bar!

## Additional Commands

I've added the following commands to support the Exercise Tracker application:

* `CREATE PROFILE` - Create a new user profile with username and password
* `LOGIN` - Select and login to an existing user profile
* `LOGOUT` - Log out of current profile
* `EDIT PROFILE` - Modify username or password
* `FOLLOW USER` - Follow another user to see their activities in feed
* `SHOW FEED` - Display activity feed from followed users
* `ADD MAP` - Initialize the world map with specified width and height
* `ADD ACTIVITY` - Track a new activity with a route on the map
* `SHOW ACTIVITY` - Display map with a single activity's route
* `ADD OBSTACLE` - Add an obstacle to the map
* `FIND ROUTE` - Use path-finding to discover new routes on the map
* `SHOW OBSTACLES` - List all obstacles on the map
* `REMOVE ACTIVITY` - Remove an activity by ID
* `REMOVE OBSTACLE` - Remove an obstacle by ID
* `REMOVE MAP` - Remove the entire map
* `EXIT` - Quit the application

## Domain Model

### Resources

* I learned about Java design patterns and domain modeling at
  <https://www.baeldung.com/>.
* I learned about stack data structures and linked list implementations from
  <https://www.geeksforgeeks.org/stack-data-structure-introduction-and-program/>.
* I referenced constraint checking practices from Guava library documentation
  <https://github.com/google/guava>.
* I studied graph traversal and path-finding algorithms at
  <https://www.baeldung.com/java-graphs#dfs-stack>.

### Changes

**For Phase 3, I made the following significant changes to support multi-user functionality, activity feeds, and path-finding:**

* **Added `User` class** - Represents a user profile with username, password, and owned activities. Supports multiple people using the same application instance.

* **Added `Stack` interface and `LinkedStack` implementation** - Required for the stack-based path-finding algorithm. `Stack` defines push/pop operations; `LinkedStack` implements using a linked list structure. Invariants and contracts are fully specified through preconditions, postconditions, and state requirements in the Javadoc.

* **Added `StackNode` inner class** - Used by `LinkedStack` for linked list structure. Includes invariant that `data != null` and maintains proper node chaining for path-finding algorithm.

* **`Grid` class as hard-coded map** - The map is hard-coded and initialized when the software starts. It begins completely empty with no obstacles, but users can add obstacles as they encounter them during activities. Also maintains reference to which points have been covered by routes, enabling path-finding queries.

* **Added `RouteScope` enum** - Supports two path-finding modes: "MY_ROUTES_ONLY" (personal activities) or "ALL_ROUTES" (from feed with followed users).

### Diagram

Here is the domain model for the Exercise Tracker. This design supports multi-user profiles, activity tracking on a hard-coded grid map, path-finding with stack ADT, and activity feeds for following other users. **All invariants are documented in class notes** to ensure the model maintains valid states at all times.

```mermaid
classDiagram
    class User {
        -String username
        -String password
        -Activity activity
        -List~User~ following
        
        +getUsername() String
        +authenticate(String pwd) boolean
        +getActivity() Activity
        +addFollowing(User user) void
        +removeFollowing(User user) void
        +getFollowing() List~User~
    }
    
    note for User "Invariant properties:
        * username not null/empty
        * username unique
        * password not null/empty
        * activity != null
        * following != null
        * user can only access/modify own activities
        "

    class Activity {
        -List~ExerciseLog~ logs
        
        +addExerciseLog(ExerciseLog log) void
        +removeExerciseLog(int id) boolean
        +getExerciseLog(int id) ExerciseLog
        +getAllLogs() List~ExerciseLog~
    }
    
    note for Activity "Invariant properties:
        * logs != null
        * all ids > 0
        * all logs belong exclusively to owning user
        "

    class ExerciseLog {
        -int id
        -String name
        -List~Point~ points
        -Exercise exercise
        -LocalDateTime timestamp
        
        +getId() int
        +getName() String
        +getPoints() List~Point~
        +getExercise() Exercise
        +getTimestamp() LocalDateTime
    }
    
    note for ExerciseLog "Invariant properties:
        * id > 0
        * name not null/empty
        * points not null/empty
        * exercise != null
        * timestamp <= now
        "

    class Exercise {
        -String name
        -Unit unit
        
        +getName() String
        +getUnit() Unit
    }
    
    note for Exercise "Invariant properties:
        * name not null/empty
        * unit != null
        "

    class Grid {
        -int width
        -int height
        -List~ObstaclePlacement~ obstacles
        -Set~Point~ coveredPoints
        
        +getWidth() int
        +getHeight() int
        +isInBounds(Point p) boolean
        +addObstacle(ObstaclePlacement o) void
        +removeObstacle(int id) boolean
        +getObstacles() List~ObstaclePlacement~
        +addCoveredPoint(Point p) void
        +isCovered(Point p) boolean
    }

    class Point {
        -int x
        -int y
        
        +getX() int
        +getY() int
        +equals(Object) boolean
        +hashCode() int
    }
    
    note for Point "Invariant properties:
        * x >= 0
        * y >= 0
        "

    class ObstaclePlacement {
        -int id
        -Point location
        -Obstacle type
        
        +getId() int
        +getLocation() Point
        +getType() Obstacle
    }
    
    note for ObstaclePlacement "Invariant properties:
        * id > 0
        * location not null
        * type != null
        "

    class Stack {
        <<interface>>
        +push(Object item) void
        +pop() Object
        +peek() Object
        +isEmpty() boolean
        +size() int
    }
    
    note for Stack "Invariants:
        * pop requires !isEmpty()
        * peek requires !isEmpty()
        * push increases size by 1
        * pop decreases size by 1
        * size returns count of items
        "
    


    class LinkedStack {
        -StackNode head
        
        +push(Object item) void
        +pop() Object
        +peek() Object
        +isEmpty() boolean
        +size() int
    }
    
    note for LinkedStack "Invariants:
        * head == null iff stack is empty
        * push adds item to head
        * pop removes and returns head
        * all nodes reachable from head
        * size counts reachable nodes
        "

    class StackNode {
        -Object data
        -StackNode next
        
        +getData() Object
        +getNext() StackNode
        +setNext(StackNode node) void
    }
    
    note for StackNode "Inner class of LinkedStack
        Invariant properties:
        * data != null
        "

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

    class RouteScope {
        <<enumeration>>
        MY_ROUTES_ONLY
        ALL_ROUTES
    }

    %% Relationships
    User *-- Activity
    User o-- "*" User
    
    Activity *-- "*" ExerciseLog
    ExerciseLog *-- "*" Point
    ExerciseLog *-- Exercise
    Exercise *-- Unit
    
    Grid *-- "*" ObstaclePlacement
    Grid o-- "*" Point
    
    ObstaclePlacement *-- Point
    ObstaclePlacement *-- Obstacle
    
    LinkedStack *-- StackNode
    StackNode o-- StackNode
    Stack <|.. LinkedStack
    
    note for Grid "Invariant properties:
        * width > 0
        * height > 0
        * obstacles != null
        * coveredPoints != null
        * hard-coded and initialized at startup
        "
```
