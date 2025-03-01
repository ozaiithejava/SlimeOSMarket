package net.ozaii.slimeOSCentry.utils;


import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtils {

    /**
     * Converts a JSON string into a JSONObject.
     *
     * @param jsonString the string representation of a JSON object
     * @return the JSONObject corresponding to the given JSON string
     * @throws IllegalArgumentException if the input string is not valid JSON
     */
    public static JSONObject stringToJsonObject(String jsonString) {
        try {
            return new JSONObject(jsonString); // Parses the string into a JSONObject
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON string", e);
        }
    }

    /**
     * Converts a JSONObject into a string representation.
     *
     * @param jsonObject the JSONObject to be converted
     * @return the string representation of the JSONObject
     */
    public static String jsonObjectToString(JSONObject jsonObject) {
        return jsonObject.toString(); // Converts the JSONObject back to a string
    }

    /**
     * Extracts a value from a JSONObject using a specified key.
     *
     * @param jsonObject the JSONObject from which the value is to be extracted
     * @param key the key for which to retrieve the value
     * @return the value associated with the given key in the JSON object
     * @throws IllegalArgumentException if the key does not exist
     */
    public static Object getValueFromJson(JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            return jsonObject.get(key); // Returns the value for the specified key
        } else {
            throw new IllegalArgumentException("Key not found: " + key); // Handles missing key
        }
    }

    /**
     * Adds a key-value pair to a JSONObject.
     *
     * @param jsonObject the JSONObject to which the key-value pair is to be added
     * @param key the key of the new entry
     * @param value the value of the new entry
     * @return the updated JSONObject with the new entry
     */
    public static JSONObject addKeyValueToJson(JSONObject jsonObject, String key, Object value) {
        jsonObject.put(key, value); // Adds the key-value pair to the JSONObject
        return jsonObject; // Returns the updated JSONObject
    }

    /**
     * Converts a JSON string into a JSONArray.
     *
     * @param jsonString the string representation of a JSON array
     * @return the JSONArray corresponding to the given JSON string
     * @throws IllegalArgumentException if the input string is not a valid JSON array
     */
    public static JSONArray stringToJsonArray(String jsonString) {
        try {
            return new JSONArray(jsonString); // Parses the string into a JSONArray
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON array string", e);
        }
    }

    /**
     * Converts a JSONArray into a string representation.
     *
     * @param jsonArray the JSONArray to be converted
     * @return the string representation of the JSONArray
     */
    public static String jsonArrayToString(JSONArray jsonArray) {
        return jsonArray.toString(); // Converts the JSONArray back to a string
    }

    /**
     * Retrieves a value from a JSONArray at a given index.
     *
     * @param jsonArray the JSONArray from which the value is to be retrieved
     * @param index the index at which to retrieve the value
     * @return the value at the specified index in the JSONArray
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public static Object getValueFromJsonArray(JSONArray jsonArray, int index) {
        try {
            return jsonArray.get(index); // Retrieves the value at the given index
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }

    /**
     * Adds a value to a JSONArray.
     *
     * @param jsonArray the JSONArray to which the value is to be added
     * @param value the value to be added
     * @return the updated JSONArray with the new value
     */
    public static JSONArray addValueToJsonArray(JSONArray jsonArray, Object value) {
        jsonArray.put(value); // Adds the value to the JSONArray
        return jsonArray; // Returns the updated JSONArray
    }

}
