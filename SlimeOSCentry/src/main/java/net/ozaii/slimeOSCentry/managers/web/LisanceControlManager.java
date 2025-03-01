package net.ozaii.slimeOSCentry.managers.web;

import net.ozaii.slimeOSCentry.SlimeOSCentry;
import net.ozaii.slimeOSCentry.utils.JsonUtils;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.logging.Logger;

public class LisanceControlManager {

    private static final String VERSION_URL = "https://ozaiidev.com.tr/lisances/notamc/license.json";
    private static final Logger logger = SlimeOSCentry.getInstance().getLogger();

    /**
     * Fetches the version from the remote JSON file and checks the license status.
     *
     * @return true if the license is valid and the version matches, false otherwise
     */
    public static boolean validateLicense() {
        try {
            // Get the local machine's IP address
            String localIp = getLocalIpAddress();
            if (localIp == null) {
                logger.warning("Error: Could not retrieve the local machine's IP address.");
                return false;
            }

            // Fetch version and license data from the remote server
            String jsonResponse = fetchJsonResponseFromUrl(VERSION_URL);
            if (jsonResponse == null) {
                return false;
            }

            // Parse the JSON response
            JSONObject jsonObject = JsonUtils.stringToJsonObject(jsonResponse);

            // Extract the IP and license status from the JSON
            String serverIp = jsonObject.optString("ip", null);
            String licenseStatus = jsonObject.optString("licenseStatus", null);

            if (serverIp == null || licenseStatus == null) {
                logger.warning("Error: Missing required fields in the response (ip or licenseStatus).");
                return false;
            }

            // Check if the local IP matches the server's IP
            if (!localIp.equals(serverIp)) {
                logger.warning("Error: Local IP does not match the server IP. Disabling the plugin.");
                disablePlugin();
                return false;
            }

            // If the IPs match, check the license status
            if ("ACTIVE".equalsIgnoreCase(licenseStatus)) {
                logger.info("License is ACTIVE. Continuing with the plugin.");
                return true;  // License is valid, continue with the plugin logic
            } else {
                logger.warning("Error: License is not ACTIVE. Disabling the plugin.");
                disablePlugin();
                return false;
            }

        } catch (Exception e) {
            logger.warning("Error validating license: ");
            return false;
        }
    }

    /**
     * Fetches the local machine's IP address.
     *
     * @return the local IP address, or null if it cannot be determined
     */
    private static String getLocalIpAddress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (Exception e) {
            logger.warning("Error getting local IP address: ");
            return null;
        }
    }

    /**
     * Makes an HTTP GET request to fetch the JSON response from the provided URL.
     *
     * @param urlString the URL to fetch the JSON from
     * @return the raw JSON response as a string, or null if the request fails
     */
    private static String fetchJsonResponseFromUrl(String urlString) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                logger.warning("Error: Unable to connect to the server. HTTP code: " + connection.getResponseCode());
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();  // Return the raw JSON response

        } catch (Exception e) {
            logger.warning("Error fetching JSON from URL: ");
            return null;
        }
    }

    /**
     * Disables the plugin if the license is invalid or the IP doesn't match.
     */
    private static void disablePlugin() {
        SlimeOSCentry plugin = SlimeOSCentry.getInstance();
        logger.info("Disabling plugin...");


        try {
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            logger.info("Plugin has been disabled successfully.");
        } catch (Exception e) {
            logger.warning("Error disabling plugin: ");
        }
    }

    public static void checkLisance() {
        boolean isLicenseValid = validateLicense();
        if (isLicenseValid) {
            logger.info("Plugin is running with valid license.");
        } else {
            logger.info("Plugin was disabled due to invalid license or IP mismatch.");
        }
    }
}
