package cz.apolicky.summercampapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * A class providing access to html of web pages
 */
public class HTMLDownloader {

    /**
     * Returns source code of page 'page'
     * @param page web page to be downloaded
     * @return returns the source of page @page
     * @throws IOException throws IOException when inputstream from 'page' can not be opened.
     */
    public static String getPage(String page) throws IOException {
        String result = null;

        if(!page.startsWith("http://") && !page.startsWith("https://")){
            page = "http://" + page;
        }

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

    /**
     * Checks if connection to 'page' can be established.
     * @param page the web page to be downloaded
     * @return true if connection can be established
     */
    public static boolean tryPage(String page){
        if(!page.startsWith("http://") && !page.startsWith("https://")){
            page = "http://" + page;
        }
        try{
            URL url_ = new URL(page);
            url_.openStream();
            return true;
        }
        catch (IOException e){
            return false;
        }
    }

}
