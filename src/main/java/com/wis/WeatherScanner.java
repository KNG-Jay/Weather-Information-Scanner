package main.java.com.wis;

import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WeatherScanner {


    public String[] getInfo() {
        String[] info = new String[2];
        info[0] = this.getAPIKey();
        info[1] = this.getLocation();
        return info;
    }


    private String getAPIKey() {
        Path filepath = Paths.get("src/main/java/resources/api-key.txt");
        String fileContent = "";

        try {
            fileContent = Files.readString(filepath);
            System.out.println("API Key Saved As: \n" + fileContent);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        return fileContent;

    }


    private String getLocation() {
        Path filepath = Paths.get("src/main/java/resources/location-default.txt");
        String fileContent = "";
        String city;
        String state;
        String country;

        try {
            fileContent = Files.readString(filepath);
            Scanner scnr_in = new Scanner(fileContent);
            city = scnr_in.next();
            state = scnr_in.next();
            country = scnr_in.next();
            System.out.printf("Location Set As: \nCity: %s\nState/Province: %s\nCountry: %s\n", city, state, country);
            scnr_in.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        return fileContent;

    }


    private void setAPIKey(String key) {
        Path filepath = Paths.get("src/main/java/resources/api-key.txt");

        try {
            Files.write(filepath, key.getBytes());
            System.out.println("Writing: " + key + "\nTo: " + filepath);
        } catch (IOException e) {
            System.err.println("Error writing API Key to file: " + e.getMessage());
            e.printStackTrace();
        }

    }


    private void setLocation(String city, String state, String country) {
        Path filepath = Paths.get("src/main/java/resources/location-default.txt");
        String location = filepath.toString();

        try {
            Files.write(filepath, location.getBytes());
            System.out.println("Writing: " + location.toString() + "\nTo: " + filepath);
        } catch (IOException e) {
            System.err.println("Error Writing Location to File: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        WeatherScanner weatherScanner = new WeatherScanner();
        weatherScanner.getInfo();
        /*TODO: Have user use saved key
         or input api key and save to: 
         ../resources/api-key.txt
        */

    }

}
