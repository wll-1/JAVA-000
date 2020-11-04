package org.example.gateway;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class hellpClient {

    public static void main(String[] args) {
        try {
            URL url = new URL("http://127.0.0.1:8082");
            InputStream in = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String body = null;
            while ((body = reader.readLine()) != null)
                System.out.println(body);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
