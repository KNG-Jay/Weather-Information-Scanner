package com.wis;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;


public class WeatherScanner {

    private static String locationDefault;
    private static String location;
    private static String city;
    private static String state;
    private static String country;
    private static String apiKey;
    private static Map<String, Object> apiResponse = new HashMap<>();

    WeatherScanner() {
        apiKey = "Undefined";
        locationDefault = "Undefined";
    }

    WeatherScanner(String key, String dLocation) {
        apiKey = key;
        locationDefault = dLocation;
    }

    // TODO: Pull API Request Into apiResponse HashMap
    // TODO: Pull API Request By Zip Code
    public String callAPI(String location, String key) {
        String request;
        String inputLine;
        StringBuilder response = new StringBuilder();
        int responseCode;
        URI uri;
        URL url;
        HttpURLConnection con;

        try {

            this.decodeLocation(location);

            request = String.format("http://api.openweathermap.org" 
                + "/geo/1.0/direct?q=%s,%s,%s&limit=1&appid=%s",
                city, state, country, key);

            uri = new URI(request);
            url = uri.toURL();
            System.out.println("Calling API At: " + url.toString());

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("API Response:\n"
                    + response.toString());

            } else {
                System.err.println("API Call Failed!");
            }

            con.disconnect();

        } catch (MalformedURLException e) {
            System.err.println("Malformed URL Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An Unexpected Error Occurred: " + e.getMessage());
            e.printStackTrace();
        }

        return response.toString();

    }


    public Map<String, Object> jsonToMap(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            apiResponse = objectMapper.readValue(jsonResponse, apiResponse.getClass());

            System.out.println("Key : Value Pairs:" + apiResponse.size());

        } catch (Exception e) {
            System.err.println("Error Occurred: " + e.getMessage());
            e.printStackTrace();
        }

        return apiResponse;

    }
    

    // TODO: Display Information In A Pleasing Manner


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
            country = scnr_in.nextLine();
            System.out.printf("Local Location Set As:"
                + "\nCity: %s\nState/Province: %s\nCountry Code: %s\n",
                city, state, country);
            scnr_in.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        return fileContent;

    }


    public String getLocation() {
        System.out.println("Location Currently in Buffer:");

        if (location != null || location.isBlank()) {
            return null;
        } else {
            return location;
        }

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
        location = String.format("%s %s %s", city, state, country);

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
        Character ch = ' ';
        WeatherScanner weatherScanner = new WeatherScanner(getAPIKey(), getDefaultLocation());

        System.out.println("\n----------  Weather Scanner System  ----------");
        System.out.println("\nA Tool That Allows You To Check Local/Distant Weather Data!\n");
        System.out.println("ENTER 'q' TO QUIT!\n\n");

        while (ch != 'Q') {
            
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
                if (state.length() == 1) {
                    ch = state.toUpperCase().charAt(0);
                }
                System.out.print("Country Code: ");
                country = scnr.nextLine();
                if (country.length() == 1) {
                    ch = country.toUpperCase().charAt(0);
                }
                setDefaultLocation(city, state, country);
            }

            System.out.println("------------  MENU  ------------");
            System.out.println("OPTIONS:"
                + "\nl - List Instance Info"
                + "\ne - Edit Instance Info"
                + "\nx - Enter Query Menu "
                + "\nq - QUIT"
                + "\n"
                );
            ch = scnr.next().toUpperCase().charAt(0);

            switch (Character.toUpperCase(ch)) {

                case 'L':
                    System.out.printf("\n%s\n%s",getAPIKey(), getDefaultLocation());
                    break;

                case 'E':
                    System.out.println("\n<-EDITOR->\n");
                    System.out.println("Options:"
                        + "\nk - Edit API Key"
                        + "\nl - Edit Default Location"
                        + "\nq - QUIT\n"
                        );
                    ch = scnr.next().toUpperCase().charAt(0);

                    switch (ch) {

                        case 'K':
                            System.out.println("Enter Your Key From OpenWeather's API");
                            answer = scnr.next().trim();
                            if (answer.length() == 1 && answer.toUpperCase().charAt(0) == 'Q') {
                                break;
                            }
                            setAPIKey(answer);
                            break;

                        case 'L':
                            System.out.println("Enter The Location That Launches On Startup:");
                            System.out.print("City: ");
                            city = scnr.nextLine().trim();
                            if (city.length() == 1 && city.toUpperCase().charAt(0) == 'Q') {
                                break;
                            }
                            System.out.print("State: ");
                            state = scnr.nextLine().trim();
                            if (state.length() == 1 && state.toUpperCase().charAt(0) == 'Q') {
                                break;
                            }
                            System.out.print("Country Code: ");
                            country = scnr.nextLine().trim();
                            if (country.length() == 1 && country.toUpperCase().charAt(0) == 'Q') {
                                break;
                            }
                            setDefaultLocation(city, state, country);
                            break;

                        case 'Q':
                            System.out.println("Exiting Editor...");
                            ch = ' ';
                            break;

                        default:
                            System.err.println("***Failed To Find Command... Please Try Again.");
                            break;
                            
                    }
                    break;

                case 'X':
                    // TODO: Add Interactive Menu to Make Requests to OpenWeather
                    System.out.print("Enter A Location To Survey:\nCity: ");
                    city = scnr.next().trim();
                    System.out.print("\nState/Province: ");
                    state = scnr.next().trim();
                    System.out.print("\nCountry Code: ");
                    scnr.nextLine();
                    country = scnr.nextLine().toString();
                    
                    weatherScanner.setLocation(city, state, country);
                    String res = weatherScanner.callAPI(location, apiKey);
                    apiResponse = weatherScanner.jsonToMap(res);
                    
                    System.out.printf("\n\nWeather In %s:\n\n", city);
                    System.out.println(apiResponse);

                    break;
                
                case 'Q':
                    System.out.println("\nGoodbye! :)\n");
                    System.exit(1);
                    break;
                
                default:
                    System.err.println("***Failed To Find Command... Please Try Again.");
                    break;

            }

        }

        scnr.close();

    }


    public static void main(String[] args) {

        tui();

    }


}
