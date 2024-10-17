package utils;

import dataProvider.ConfigFileReader;

import java.io.File;

public class FileUtils extends org.apache.commons.io.FileUtils {

    public static String normalizeFilePath(String filePath) { return filePath.replace("\\", File.separator); }
    public static String makeSupportFilePath(String fileName) {
        String rawPath = System.getProperty("user.dir") + ConfigFileReader.getInstance().getSupportFilesDirectory() + fileName;
        return normalizeFilePath(rawPath);
    }
}
