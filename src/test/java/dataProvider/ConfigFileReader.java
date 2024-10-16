/*
 * Copyright (C) 2018-2020 TAG QA TEAM
 * This file is part of  TAG-automation
 * Created at 7/2/18 2:32 PM by yzheng
 */

package dataProvider;

import com.beust.jcommander.internal.Maps;
import com.google.common.base.Strings;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

public class ConfigFileReader {

    private final Properties configProperties;
    private static ConfigFileReader instance;
    private String environment;
    private final static Map<String, Map<String,String>> users = Maps.newHashMap();

    static {
        users.put("superUser",Maps.newHashMap("automationTAGSUPER","automationTAGSUPER_password"));
        users.put("FNBO super",Maps.newHashMap("automationFNBOSUPER","automationFNBOSUPER_password"));
        users.put("User1",Maps.newHashMap("automationUser1","automationUser1_password"));
        users.put("User2",Maps.newHashMap("automationUser2","automationUser2_password"));
        users.put("User3",Maps.newHashMap("automationUser3","automationUser3_password"));
        users.put("FISUPER",Maps.newHashMap("automationFISUPER","automationFISUPER_password"));
        users.put("TestPatFISUPER",Maps.newHashMap("automationTestPatFISUPER","automationTestPatFISUPER_password"));
        users.put("PartnerSuper",Maps.newHashMap("automationPartnerSuper","automationPartnerSuper_password"));
        users.put("TAGCRM",Maps.newHashMap("automationTAGCRM","automationTAGCRM_password"));
        users.put("TAGQA",Maps.newHashMap("automationTAGQA","automationTAGQA_password"));
        users.put("WEX Bank User1",Maps.newHashMap("automationWEXBankUser1","automationWEXBankUser1_password"));
        users.put("PAT Bank User1",Maps.newHashMap("automationPATBankUser1","automationPATBankUser1_password"));
        users.put("Only View User",Maps.newHashMap("automationOnlyViewUser","automationOnlyViewUser_password"));
    }

    public static ConfigFileReader getInstance() {
        return instance == null ? instance = new ConfigFileReader() : instance;
    }

    private ConfigFileReader() {
        //for backward compatibility I decided to keep the old getenv logic along with the new getProperty logic with getProperty taking precedence.
        //to use the getProperty("ENVIRONMENT") you can use -DENVIRONMENT=local in VM Options
        environment = System.getProperty("ENVIRONMENT");
        if(Strings.isNullOrEmpty(environment)){
            //to use the getenv("ENVIRONMENT") you can use ENVIRONMENT=local in Environment Variables
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