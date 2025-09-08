package main.java.com.wis;

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class WeatherScanner {

    private static String locationDefault;
    private static String location;
    private static String city;
    private static String state;
    private static String country;
    private static String apiKey;
    private static Map<String, String> apiResponse = new HashMap<>();

    WeatherScanner() {
        apiKey = "Undefined";
        locationDefault = "Undefined";
    }

    WeatherScanner(String key, String loc) {
        apiKey = key;
        locationDefault = location;
    }


    public String[] callAPI(String location, String key) {
        this.decodeLocation(location);
        

    }


    public void decodeLocation(String location) {
        Scanner scnr = new Scanner(location);
        city = scnr.next();
        state = scnr.next();
        country = scnr.next();
        scnr.close();
    }


    public static String[] getInfo() {
        String[] info = new String[2];
        info[0] = getAPIKey();
        info[1] = getDefaultLocation();
        return info;
    }


    private static String getAPIKey() {
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


    private static String getDefaultLocation() {
        Path filepath = Paths.get("src/main/java/resources/location-default.txt");
        String fileContent = "";

        try {
            fileContent = Files.readString(filepath);
            Scanner scnr_in = new Scanner(fileContent);
            city = scnr_in.next();
            state = scnr_in.next();
            country = scnr_in.next();
            System.out.printf("Local Location Set As: \nCity: %s\nState/Province: %s\nCountry: %s\n",
                 city, state, country);
            scnr_in.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        return fileContent;

    }


    public String getLocation() {
        return location;
    }


    private static void setAPIKey(String key) {
        Path filepath = Paths.get("src/main/java/resources/api-key.txt");

        try {
            Files.write(filepath, key.getBytes());
            System.out.println("Writing: " + key + "\nTo: " + filepath);
        } catch (IOException e) {
            System.err.println("Error writing API Key to file: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("API Key Set!");

    }


    private static void setDefaultLocation(String city, String state, String country) {
        Path filepath = Paths.get("src/main/java/resources/location-default.txt");
        location = filepath.toString();

        try {
            Files.write(filepath, location.getBytes());
            System.out.println("Writing: " + location.toString() + "\nTo: " + filepath);
            locationDefault = location;
        } catch (IOException e) {
            System.err.println("Error Writing Location to File: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Default Location Set!");

    }


    private void setLocation(String city, String state, String country) {
        location = String.format("%s %s %s", city, state, country);
    }


    public static void tui() {
        Scanner scnr = new Scanner(System.in);
        String answer;
        char ch = ' ';
        WeatherScanner weatherScanner = new WeatherScanner(getInfo()[0], getInfo()[1]);
        /*TODO: Have user use saved key/location
         or input api-key/ location and save to: 
         ../resources/api-key.txt
         ../resources/location-default.txt
        */
        while (ch != 'Q') {

            System.out.println("----------  Weather Scanner System  ----------");
            System.out.println("A Tool That Allows You To Check Local/Distant Weather Data!");
            System.out.println("ENTER 'q' TO QUIT!\n");
            getInfo();
            
            if (WeatherScanner.apiKey == null || WeatherScanner.apiKey == "Undefined") {
                System.out.println("Please Enter an API Key From openweathermap.org:");
                answer = scnr.next();
                if (answer.length() == 1) {
                    ch = answer.toUpperCase().charAt(0);
                }
                setAPIKey(answer);
            }

            if (WeatherScanner.locationDefault == null || WeatherScanner.locationDefault == "Undefined") {
                System.out.println("Please Enter A Default Location:");
                System.out.print("City: ");
                city = scnr.nextLine();
                if (city.length() == 1) {
                    ch = city.toUpperCase().charAt(0);
                }
                System.out.print("State/Province: ");
                state = scnr.nextLine();
                System.out.print("Country: ");
                country = scnr.nextLine();
                setDefaultLocation(city, state, country);
            }

            apiKey = getAPIKey();
            locationDefault = getDefaultLocation();

            switch (ch) {
                case 'l':
                    getInfo();
                    break;
            
                default:
                    break;

            }

        }

        scnr.close();

    }


    public static void main(String[] args) {

        tui();

    }


}
