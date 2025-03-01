package net.ozaii.slimeOSCentry.managers.web;

import net.ozaii.slimeOSCentry.SlimeOSCentry;
import net.ozaii.slimeOSCentry.utils.JsonUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class VersionControlManager {

    private static final String VERSION_URL = "https://ozaiidev.com.tr/lisances/notamc/license.json";
    private static final Logger logger = SlimeOSCentry.getInstance().getLogger();

    /**
     * Fetches the version from the remote JSON file.
     *
     * @return the version string if found, or null if there is an issue with the request or JSON format
     */
    public static String getVersionFromRemote() {
        try {
            // Establish a connection to the provided URL
            HttpURLConnection connection = (HttpURLConnection) new URL(VERSION_URL).openConnection();
            connection.setRequestMethod("GET"); // Set the request method to GET

            // Check for a successful response (status code 200)
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                logger.warning("Error: Unable to connect to the server. HTTP code: " + connection.getResponseCode());
                return null;
            }

            // Read the response from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the response to extract the version
            JSONObject jsonResponse = JsonUtils.stringToJsonObject(response.toString());
            String version = jsonResponse.optString("version", null); // Extract the "version" field

            if (version == null) {
                logger.warning("Error: Version not found in the response.");
                return null;
            }

            logger.info("Fetched version: " + version); // Log the fetched version
            return version; // Return the version

        } catch (Exception e) {
            logger.warning("Error fetching the version: ");
            return null;
        }
    }

    /**
     * Checks if the fetched version matches the provided version.
     * Logs a warning if there is a version mismatch.
     *
     * @param v the version to compare with
     */
    public static void checkVersion(String v) {
        String version = getVersionFromRemote();
        if (version != null) {
            if (version.equals(v)) {
                logger.info("Plugin is up to date. Current version: " + v);
            } else {
                logger.warning("A new version of the plugin is available.");
                logger.warning("Please contact ozaii1337 for the latest version.");
            }
        }
    }
}
