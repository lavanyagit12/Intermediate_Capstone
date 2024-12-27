package Utilities;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Properties;

public class ConfigReader {

    private static final String CONFIG_FILE = "src/test/resources/config.properties";

    // Load properties while preserving their order
    
    public static LinkedHashMap<String, String> loadProperties() {
        LinkedHashMap<String, String> propertiesMap = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                    propertiesMap.put(line, null); 
                } else {
                    String[] parts = line.split("=", 2);
                    propertiesMap.put(parts[0].trim(), parts.length > 1 ? parts[1].trim() : "");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file.", e);
        }
        return propertiesMap;
    }

    // Update properties while preserving the order and updating only stock values of the provided symbol
    public static void updateStockValues(String stockName, String currentValue, String highValue, String lowValue) {
        LinkedHashMap<String, String> propertiesMap = loadProperties();

        // Update stock values for the given stock name
        propertiesMap.put(stockName + "_currentValue", currentValue);
        propertiesMap.put(stockName + "_highValue", highValue);
        propertiesMap.put(stockName + "_lowValue", lowValue);

        // Write the updated map back to the config file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
               for (String key : propertiesMap.keySet()) {
                if (propertiesMap.get(key) == null) {
                    writer.write(key + "\n"); 
                } else {
                    writer.write(key + "=" + propertiesMap.get(key) + "\n"); 
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to update configuration file.", e);
        }
    }

    // Retrieve property values (unchanged functionality)
    public static String getProperty(String key) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file.", e);
        }
        return properties.getProperty(key);
    }
}
