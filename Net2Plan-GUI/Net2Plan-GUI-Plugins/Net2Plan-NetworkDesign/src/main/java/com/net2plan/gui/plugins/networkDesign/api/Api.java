package com.net2plan.gui.plugins.networkDesign.api;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openstack4j.model.telemetry.Meter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Api {

    protected HttpURLConnection connection = null;

    public Api (){

    }

    protected Object Get(String URL,String token){



        try {
            //Create connection
            URL url = new URL(URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Auth-Token", token);
            connection.setRequestProperty("Content-type", "application/json");

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;

            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            return response;

        } catch (Exception e) {

            e.printStackTrace();

        }
        return null;
    }

    protected Object Post(String URL,String token,JSONObject data){

        try {
            //Create connection
            URL url = new URL(URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("X-Auth-Token", token);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(data.toString().getBytes("UTF-8"));
            os.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;

            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            return response;

        } catch (Exception e) {

            e.printStackTrace();

        }
        return null;
    }

    protected Object Put(){

        return null;
    }

}
