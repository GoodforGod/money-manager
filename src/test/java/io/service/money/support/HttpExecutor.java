package io.service.money.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 15.11.2018
 */
public class HttpExecutor {

    private String performRequest(String urlAsString, String method) throws IOException {
        final URL url = new URL(urlAsString);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod(method);
        int status = connection.getResponseCode();
        final String data = readData(connection);
        connection.disconnect();
        return data;
    }

    public String get(final String urlAsString) throws IOException {
        return performRequest(urlAsString, "GET");
    }

    public String put(final String urlAsString) throws IOException {
        return performRequest(urlAsString, "PUT");
    }

    private String readData(final HttpURLConnection connection) throws IOException {
        final StringBuilder content = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                content.append(inputLine);

            in.close();
        }

        return content.toString();
    }
}
