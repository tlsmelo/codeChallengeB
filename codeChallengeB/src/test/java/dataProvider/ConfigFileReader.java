package dataProvider;

import com.google.common.base.Strings;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigFileReader {

    private final Properties configProperties;
    private static ConfigFileReader instance;
    private String environment;

    public static ConfigFileReader getInstance() {
        return instance == null ? instance = new ConfigFileReader() : instance;
    }

    private ConfigFileReader() {
        environment = System.getProperty("ENVIRONMENT");
        if(Strings.isNullOrEmpty(environment)){
            environment = System.getenv("ENVIRONMENT");
        }
        environment = Strings.isNullOrEmpty(environment) ? "local" : environment.toLowerCase();

        String PROPERTY_FILE_PATH = "configs" + File.separator + environment + "Configuration.properties";
        try {
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(PROPERTY_FILE_PATH))) {
                configProperties = new Properties();
                configProperties.load(reader);
            }
        } catch (IOException e) {
            throw new RuntimeException(environment + "Configuration.properties not found at " + PROPERTY_FILE_PATH);
        }
    }

    private String readProperty(String property) {
        String propertyValue = configProperties.getProperty(property);
        if (Strings.isNullOrEmpty(propertyValue)) {
            throw new RuntimeException(property + " not specified in the " + environment + "Configuration.properties file.");
        } else {
            return propertyValue;
        }
    }
    public static String getProperty(String property) {
        return getInstance().readProperty(property);
    }

    public String getTestAPIUrl() {
        return readProperty("testApiUrl");
    }

    public String getTestAPIUser() {
        return readProperty("testUser");
    }

    public String getTestAPIPassword() {
        return readProperty("testPwd");
    }

    public String getSupportFilesDirectory() {
        return readProperty("supportFilesDirectory");
    }

}