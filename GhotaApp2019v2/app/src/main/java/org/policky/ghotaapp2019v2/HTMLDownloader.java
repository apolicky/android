package org.policky.ghotaapp2019v2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class HTMLDownloader {

    /**
     * Returns source code of page 'page'
     * @param page web page to be downloaded
     * @return returns the source of page @page
     * @throws IOException throws IOException when inputstream from 'page' can not be opened.
     */
    public static String getPage(String page) throws IOException {
        String result = null;

        URL url_ = new URL(page);
        try (BufferedReader BR = new BufferedReader(new InputStreamReader(url_.openStream()))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = BR.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            result = sb.toString();
        }
        return result;
    }

}
