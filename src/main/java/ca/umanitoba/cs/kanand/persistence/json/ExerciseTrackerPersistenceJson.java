package ca.umanitoba.cs.kanand.persistence.json;

import ca.umanitoba.cs.kanand.model.*;
import ca.umanitoba.cs.kanand.persistence.ExerciseTrackerPersistence;
import com.google.common.base.Preconditions;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * JSON-P (javax.json / GlassFish) implementation of persistence for the Exercise Tracker.
 *
 * Persists users, their activities, exercise logs, and following relationships
 * to a JSON file. Following relationships are stored as username references
 * to avoid circular serialization.
 *
 * Class Invariant:
 *   - filePath != null
 */
public class ExerciseTrackerPersistenceJson implements ExerciseTrackerPersistence {
    private final Path filePath;

    /**
     * Creates a new JSON persistence instance that reads/writes to the given file path.
     *
     * Precondition:
     *   - filePath != null
     *
     * Postcondition:
     *   - This persistence instance is ready to save/load data
     *
     * @param filePath the path to the JSON file for persistence
     */
    public ExerciseTrackerPersistenceJson(Path filePath) {
        Preconditions.checkNotNull(filePath, "Precondition failed: filePath cannot be null");
        this.filePath = filePath;
    }

    /**
     * Saves all user data to a JSON file.
     *
     * Precondition:
     *   - users != null
     *
     * Postcondition:
     *   - All users with their activities, exercise logs, and following
     *     relationships are written to the JSON file
     *   - Following relationships are stored as username strings
     *
     * @param users the list of users to persist
     */
    @Override
    public void saveUsers(List<User> users) {
        Preconditions.checkNotNull(users, "Precondition failed: users cannot be null");

        JsonArrayBuilder usersArrayBuilder = Json.createArrayBuilder();

        for (User user : users) {
            usersArrayBuilder.add(userToJson(user));
        }

        JsonObject root = Json.createObjectBuilder()
                .add("users", usersArrayBuilder)
                .build();

        Map<String, Object> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(config);

        try (OutputStream os = Files.newOutputStream(filePath);
             JsonWriter writer = writerFactory.createWriter(os)) {
            writer.writeObject(root);
        } catch (IOException e) {
            System.out.println("ERROR: Could not save data: " + e.getMessage());
        }
    }

    /**
     * Loads all user data from the JSON file.
     *
     * Postcondition:
     *   - Returns a list of users with their activities, exercise logs,
     *     and following relationships restored
     *   - If the file doesn't exist or is corrupted, returns an empty list
     *
     * @return the list of users loaded from storage
     */
    @Override
    public List<User> loadUsers() {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try (InputStream is = Files.newInputStream(filePath);
             JsonReader reader = Json.createReader(is)) {

            JsonObject root = reader.readObject();
            JsonArray usersArray = root.getJsonArray("users");

            List<User> users = new ArrayList<>();
            Map<String, List<String>> followingMap = new HashMap<>();

            for (int i = 0; i < usersArray.size(); i++) {
                JsonObject userObj = usersArray.getJsonObject(i);
                User user = jsonToUser(userObj);
                users.add(user);

                List<String> followingNames = new ArrayList<>();
                JsonArray followingArray = userObj.getJsonArray("following");
                for (int j = 0; j < followingArray.size(); j++) {
                    followingNames.add(followingArray.getString(j));
                }
                followingMap.put(user.getUsername(), followingNames);
            }

            restoreFollowingRelationships(users, followingMap);

            return users;
        } catch (IOException | JsonException e) {
            System.out.println("ERROR: Could not load data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Converts a User object to a JsonObject.
     *
     * Precondition:
     *   - user != null
     *
     * @param user the user to convert
     * @return the JsonObject representing the user
     */
    private JsonObject userToJson(User user) {
        Preconditions.checkNotNull(user, "Precondition failed: user cannot be null");

        JsonArrayBuilder logsBuilder = Json.createArrayBuilder();
        for (ExerciseLog log : user.getActivity().getAllLogs()) {
            logsBuilder.add(exerciseLogToJson(log));
        }

        JsonArrayBuilder followingBuilder = Json.createArrayBuilder();
        for (User followed : user.getFollowing()) {
            followingBuilder.add(followed.getUsername());
        }

        return Json.createObjectBuilder()
                .add("username", user.getUsername())
                .add("password", user.getPasswordForPersistence())
                .add("exerciseLogs", logsBuilder)
                .add("following", followingBuilder)
                .build();
    }

    /**
     * Converts an ExerciseLog to a JsonObject.
     *
     * Precondition:
     *   - log != null
     *
     * @param log the exercise log to convert
     * @return the JsonObject representing the exercise log
     */
    private JsonObject exerciseLogToJson(ExerciseLog log) {
        Preconditions.checkNotNull(log, "Precondition failed: log cannot be null");

        JsonArrayBuilder pointsBuilder = Json.createArrayBuilder();
        for (Point point : log.getPoints()) {
            pointsBuilder.add(Json.createObjectBuilder()
                    .add("x", point.getX())
                    .add("y", point.getY()));
        }

        return Json.createObjectBuilder()
                .add("name", log.getName())
                .add("exerciseName", log.getExercise().getName())
                .add("exerciseUnit", log.getExercise().getUnit().name())
                .add("distance", log.getDistance())
                .add("timestamp", log.getTimestamp().toString())
                .add("points", pointsBuilder)
                .build();
    }

    /**
     * Reconstructs a User from a JsonObject.
     * Does not restore following relationships (done separately after all users are loaded).
     *
     * Precondition:
     *   - userObj != null
     *   - userObj contains valid "username" and "password" fields
     *
     * @param userObj the JSON object to read from
     * @return the reconstructed User
     */
    private User jsonToUser(JsonObject userObj) {
        Preconditions.checkNotNull(userObj, "Precondition failed: userObj cannot be null");

        String username = userObj.getString("username");
        String password = userObj.getString("password");

        Preconditions.checkState(username != null && !username.isEmpty(),
                "Postcondition failed: loaded username must be non-empty");
        Preconditions.checkState(password != null && !password.isEmpty(),
                "Postcondition failed: loaded password must be non-empty");

        User user = new User(username, password);

        JsonArray logsArray = userObj.getJsonArray("exerciseLogs");
        for (int i = 0; i < logsArray.size(); i++) {
            JsonObject logObj = logsArray.getJsonObject(i);
            ExerciseLog log = jsonToExerciseLog(logObj);
            user.getActivity().addExerciseLog(log);
        }

        return user;
    }

    /**
     * Reconstructs an ExerciseLog from a JsonObject.
     *
     * Precondition:
     *   - logObj != null
     *   - logObj contains valid fields for name, exerciseName, exerciseUnit, distance, points
     *
     * @param logObj the JSON object to read from
     * @return the reconstructed ExerciseLog
     */
    private ExerciseLog jsonToExerciseLog(JsonObject logObj) {
        Preconditions.checkNotNull(logObj, "Precondition failed: logObj cannot be null");

        String name = logObj.getString("name");
        String exerciseName = logObj.getString("exerciseName");
        String exerciseUnit = logObj.getString("exerciseUnit");
        double distance = logObj.getJsonNumber("distance").doubleValue();

        Preconditions.checkState(name != null && !name.isEmpty(),
                "Postcondition failed: loaded log name must be non-empty");
        Preconditions.checkState(exerciseName != null && !exerciseName.isEmpty(),
                "Postcondition failed: loaded exercise name must be non-empty");

        Unit unit = Unit.valueOf(exerciseUnit);
        Exercise exercise = new Exercise(exerciseName, unit);

        JsonArray pointsArray = logObj.getJsonArray("points");
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < pointsArray.size(); i++) {
            JsonObject pointObj = pointsArray.getJsonObject(i);
            points.add(new Point(pointObj.getInt("x"), pointObj.getInt("y")));
        }

        Preconditions.checkState(!points.isEmpty(),
                "Postcondition failed: loaded route must have at least one point");

        return new ExerciseLog(name, exercise, points, distance);
    }

    /**
     * Restores following relationships between users after all users have been loaded.
     * Uses username matching to reconnect User object references.
     *
     * Precondition:
     *   - users != null
     *   - followingMap != null
     *
     * Postcondition:
     *   - Each user's following list is populated with the correct User references
     *
     * @param users the list of all loaded users
     * @param followingMap a map from username to list of followed usernames
     */
    private void restoreFollowingRelationships(List<User> users, Map<String, List<String>> followingMap) {
        Preconditions.checkNotNull(users, "Precondition failed: users cannot be null");
        Preconditions.checkNotNull(followingMap, "Precondition failed: followingMap cannot be null");

        Map<String, User> usersByName = new HashMap<>();
        for (User user : users) {
            usersByName.put(user.getUsername(), user);
        }

        for (User user : users) {
            List<String> followedNames = followingMap.get(user.getUsername());
            if (followedNames != null) {
                for (String followedName : followedNames) {
                    User followed = usersByName.get(followedName);
                    if (followed != null && followed != user) {
                        user.addFollowing(followed);
                    }
                }
            }
        }
    }
}
