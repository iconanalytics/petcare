package com.iconanalytics.petcare;



import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Utility class for using the Google Book Search API to download book
 * information.
 */
public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();


    private static final String BASE_URL =
            "https://iconanalytics.com/api/petcare.php"; //use a domain on ezoic will not work. iconanalytics.com is not on ezoic, so it works. beware



    // Parameter for the search string.
    private static final String QUERY_PARAM = "cmd";
    // Parameter that limits search results.
    private static final String MAX_RESULTS = "maxResults";
    // Parameter to filter by print type.
    private static final String PRINT_TYPE = "printType";


    /**
     * Static method to make the actual query to the Books API.
     *
     * @param queryString the query string.
     * @return the JSON response string from the query.
     */

    static String query(String... serverArgs) {

        // Set up variables for the try block that need to be closed in the
        // finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String outputString = "-99";



        try {

            Uri builtURI = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter("cmd", serverArgs[0])
                    .appendQueryParameter("agent", serverArgs[1])
                    .appendQueryParameter("email", serverArgs[2])
                    .appendQueryParameter("password", serverArgs[3])
                    .build();

            // Convert the URI to a URL,
            URL requestURL = new URL(builtURI.toString());

            // Open the network connection.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();




            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Create a buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Use a StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                // Add the current line to the string.
                builder.append(line);

                // Since this is JSON, adding a newline isn't necessary (it won't
                // affect parsing) but it does make debugging a *lot* easier
                // if you print out the completed buffer for debugging.
                //builder.append("\n");
            }

            if (builder.length() == 0) {
                // Stream was empty.  Exit without parsing.
                return null;
            }

            outputString = builder.toString();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the connection and the buffered reader.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Write the final JSON response to the log
        //Log.d(LOG_TAG, "testLog");

        return outputString;
    }
}
