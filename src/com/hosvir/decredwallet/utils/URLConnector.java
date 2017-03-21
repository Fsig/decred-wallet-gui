package com.hosvir.decredwallet.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

/**
 * @author fsig
 * @version 1.00
 * @since 20/03/17
 */
public class URLConnector {
    private static String html = "";
    private static String data = "";
    private static String line;

    private static URL url;
    private static URLConnection conn;
    private static BufferedReader rd;
    private static OutputStreamWriter wr;

    public static String getPage(String urlString, String[] fields, String[] values){
        try{
            url = new URL(urlString);
            conn = url.openConnection();

            /**
             * Set request properties to emulate a browser.
             */
            conn.addRequestProperty("Accept","text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            conn.addRequestProperty("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7");
            conn.addRequestProperty("Accept-Encoding", "gzip,deflate");
            conn.addRequestProperty("Accept-Language", "en-gb,en;q=0.5");
            conn.addRequestProperty("Connection", "keep-alive");
            conn.addRequestProperty("Host", urlString);
            conn.addRequestProperty("Keep-Alive", "300");
            conn.addRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/A.B (KHTML, like Gecko) Chrome/X.Y.Z.W Safari/A.B.");

            /**
             * Post values, if needed.
             */
            if(fields != null && values != null){
                for(int i = 0; i < fields.length; i++){
                    if(i > 0)
                        data += "&";

                    System.out.println(values[i]);
                    data += URLEncoder.encode(fields[i], "UTF-8") + "=" + URLEncoder.encode(values[i], "UTF-8");
                }

                conn.setDoOutput(true);
                wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
                wr.close();
            }

            /**
             * Accept GZip if applicable
             */
            if(conn.getContentEncoding() != null && conn.getContentEncoding().equals("gzip"))
                rd = new BufferedReader(new InputStreamReader(new GZIPInputStream(conn.getInputStream())));
            else
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));


            /**
             * Read in the results.
             */
            while ((line = rd.readLine()) != null) {
                html += line + "\n";
            }

            rd.close();
            url = null;
            rd = null;
            conn = null;
        }catch(Exception e){e.printStackTrace();}

        return html;
    }
}
