package com.es2.decorator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CommonWordsValidator extends Decorator {
    public CommonWordsValidator(AuthInterface auth) {
        super(auth);
    }

    @Override
    public void auth(String username, String password) throws AuthException, IOException {
        boolean isCommonWord = false;

        try {
            String result = getHTTPRequest(password);
            isCommonWord = isLikelyDictionaryWord(result);
        } catch (IOException e) {
            isCommonWord = false;
        }

        if (isCommonWord) {
            throw new AuthException();
        }

        super.auth(username, password);
    }

    public String getHTTPRequest(String word) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL("https://owlbot.info/api/v2/dictionary/" + word + "?format=json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)"
        );
        conn.setRequestMethod("GET");

        int statusCode = conn.getResponseCode();
        InputStream responseStream = statusCode >= 200 && statusCode < 300
            ? conn.getInputStream()
            : conn.getErrorStream();

        if (responseStream == null) {
            return "";
        }

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(responseStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        }

        return result.toString();
    }

    private boolean isLikelyDictionaryWord(String responseJson) {
        String normalized = responseJson == null ? "" : responseJson.toLowerCase();
        if (normalized.isBlank()) {
            return false;
        }

        return !normalized.contains("no definition")
            && !normalized.contains("not found")
            && !normalized.contains("error");
    }
}
