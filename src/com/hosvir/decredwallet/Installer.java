package com.hosvir.decredwallet;

import com.hosvir.decredwallet.utils.URLConnector;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author captain-redbeard
 * @version 1.00
 * @since 2/04/17
 */
public class Installer {
    private final String URL_TO_CHECK = "https://github.com/decred/decred-release/releases";
    private final String RELEASE_TAG = "<h1 class=\"release-title\">(.*?)<a href=\"(.*?)\">(.*?)</a>(.*?)</h1>";
    private final String VERSION_TAG = "<a href=\"/decred/decred-release/releases/download/(.*?)\" rel=\"nofollow\">(.*?)" +
            "<strong>dcrinstall-(.*?)-(.*?)-(.*?)</strong>(.*?)</a>";
    private String page = "";
    private Pattern pattern;
    private Matcher matcher;


    public static void main(String[] args) {
        new Installer();
    }

    public Installer() {
        System.out.println("Checking github for latest version.");

        String latestVersion = getLatestVersion();
        if (latestVersion != null) {
            System.out.println(" - version: " + latestVersion);
            String downloadLink = getDownloadLink();

            if (downloadLink != null) {
                System.out.println(" - download: " + downloadLink);

                //Download installer
                //downloadFile(downloadLink, "/tmp/dcrinstaller");

                //Linux, update permissions
                //LocalCommand localCommand = new LocalCommand();
                //localCommand.execute("chmod +x /tmp/dcrinstaller");

                //Run installer
                //LocalCommand localCommand = new LocalCommand();
                //localCommand.execute("/usr/bin/gnome-terminal --title='deCRED Installer' -e /tmp/dcrinstaller");
            }
        } else {
            System.out.println(" - failed to check latest version.");
        }
    }

    public String getLatestVersion() {
        page = URLConnector.getPage(URL_TO_CHECK);
        page = page.replaceAll("\n", "");

        pattern = Pattern.compile(RELEASE_TAG);
        matcher = pattern.matcher(page + "/latest");

        if (matcher.find()) {
            return matcher.group(3);
        } else {
            return null;
        }
    }

    public String getDownloadLink() {
        pattern = Pattern.compile(VERSION_TAG);
        matcher = pattern.matcher(page);

        while (matcher.find()) {
            if (matcher.group(3).toLowerCase().equals(Constants.getOSName().toLowerCase())) {
                if (matcher.group(4).toLowerCase().equals(Constants.getOSArch().toLowerCase())) {
                    return URL_TO_CHECK + "/download/" + matcher.group(1);
                }
            }
        }

        return null;
    }

    public boolean downloadFile(String url, String file) {
        try {
            URLConnection connection = new URL(url).openConnection();
            FileOutputStream output = new FileOutputStream(file);
            InputStream input = connection.getInputStream();

            byte[] temp = new byte[1024];
            int read;
            int lengthOfFile = connection.getContentLength();
            long total = 0;

            while ((read = input.read(temp)) != -1) {
                total += read;
                output.write(temp, 0, read);
                //Update progress bar?
                //total * 100 / lengthOfFile
            }

            output.flush();
            output.close();
            input.close();
            return true;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
