package com.kotor4j.configs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitry
 */
public class Configuration {

    private File propertiesFile;
    private Properties properties = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class.getName());

    public Configuration() {
        File configFile = new File("./configuration.properties");
        if (!configFile.exists()) {
            File configFile2 = new File("../configuration.properties");
            if (!configFile2.exists()) {
                throw new IllegalStateException("Cannot find configuration file at [" + configFile.getAbsolutePath() + "] or at [" + configFile2.getAbsolutePath() + "]");
            }

            configFile = configFile2;
        }

        load(configFile);
    }

    public Configuration(File configurationFile) {
        load(configurationFile);
    }

    private void load(File configurationFile) {
        propertiesFile = configurationFile;
        if (!configurationFile.exists()) {
            throw new IllegalArgumentException("Cannot find configuration file [" + configurationFile.getAbsolutePath() + "]");
        }
        try {
            try (InputStreamReader streamReader = new InputStreamReader(new FileInputStream(configurationFile), StandardCharsets.UTF_8)) {
                properties.load(streamReader);
            }
        } catch (Exception ex) {
            logger.error("Error while loading configuration file [" + configurationFile.getAbsolutePath() + "]", ex);
            throw new RuntimeException(ex);
        }
    }

    public boolean hasProperty(String name) {
        return properties.containsKey(name);
    }

    private void checkAndThrowIfProperty(String propertyName) {
        if (!properties.containsKey(propertyName)) {
            throw new IllegalArgumentException("Configuration file [" + propertiesFile.getAbsolutePath() + "] does not have property [" + propertyName + "]");
        }
    }

    public boolean getBoolean(String name) {
        checkAndThrowIfProperty(name);
        return Boolean.parseBoolean(properties.getProperty(name));
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        if (!properties.containsKey(name)) {
            return defaultValue;
        }

        return Boolean.parseBoolean(properties.getProperty(name));
    }

    public String getString(String name) {
        checkAndThrowIfProperty(name);
        return properties.getProperty(name);
    }

    public String getString(String name, String defaultValue) {
        if (!properties.containsKey(name)) {
            return defaultValue;
        }

        return properties.getProperty(name);
    }

    public int getInt(String name) {
        checkAndThrowIfProperty(name);
        return Integer.parseInt(properties.getProperty(name));
    }

    public int getInteger(String name, int defaultValue) {
        if (!properties.containsKey(name)) {
            return defaultValue;
        }

        return Integer.parseInt(properties.getProperty(name));
    }

    public float getFloat(String name) {
        checkAndThrowIfProperty(name);
        return Float.parseFloat(properties.getProperty(name));
    }

    public float getFloat(String name, float defaultValue) {
        if (!properties.containsKey(name)) {
            return defaultValue;
        }

        return Float.parseFloat(properties.getProperty(name));
    }

    public File getFolder(String keyName) {
        String folder = getString(keyName);
        if (folder.startsWith("./") || folder.startsWith(".\\")) {
            folder = folder.substring("./".length());
            folder = ensureSlashAtEnd(getString(keyName)) + folder;
        }
        File f = new File(folder);
        if (!f.exists()) {
            throw new IllegalArgumentException("Folder [" + f.getAbsolutePath() + "] specified in config file under the property [" + keyName + "] is not exists");
        }
        if (!f.isDirectory()) {
            throw new IllegalArgumentException("Folder [" + f.getAbsolutePath() + "] specified in config file under the property [" + keyName + "] is not a folder, but file");
        }

        return f;
    }

    public File getFile(String keyName) {
        String file = getString(keyName);
        if (file.startsWith("./") || file.startsWith(".\\")) {
            file = file.substring("./".length());
            file = ensureSlashAtEnd(getString("game.folder")) + file;
        }
        File f = new File(file);
        if (!f.exists()) {
            throw new IllegalArgumentException("File [" + f.getAbsolutePath() + "] specified in config file under the property [" + keyName + "] is not exists");
        }
        return f;
    }

    private String ensureSlashAtEnd(String path) {
        if (path.contains("\\")) {
            path = path.replace('\\', '/');
        }
        if (!path.endsWith("/")) {
            return path.concat("/");
        }
        return path;
    }

    public void loadSwKotorIni(File iniFile) {
        try {
            if (!iniFile.exists()) {
                throw new IllegalArgumentException("Cannot find ini file [" + iniFile.getAbsolutePath() + "]");
            }

            Ini ini;
            try (InputStream is = wrapAndEscapeConfigFile(iniFile)) {
                ini = new Ini(is);
            }
            for (Section section : ini.values()) {
                String sectionName = "swkotor." + section.getName().replace(" ", "_").toLowerCase();
                for (Entry<String, String> pair : section.entrySet()) {
                    String keyName = sectionName + "." + pair.getKey().replace(" ", "_").toLowerCase();
                    properties.put(keyName, pair.getValue());
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error while parsing configuration ini file [" + iniFile.getAbsolutePath() + "]");
        }
    }

    /*
    For some reson library that do ini parsing, just eating slash symbol(something like this .\Saves will be like this: .Saves)<br>
    that is why we convert the file to escape slashes before
     */
    private InputStream wrapAndEscapeConfigFile(File file) throws IOException {
        List<String> strings = FileUtils.readLines(file, StandardCharsets.UTF_8);
        for (int i = 0; i < strings.size(); i++) {
            strings.set(i, strings.get(i).replace("\\", "\\\\"));
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream((int) (file.length() + 50));
        PrintStream printStream = new PrintStream(outputStream, true, StandardCharsets.UTF_8.name());
        for (String line : strings) {
            printStream.println(line);
        }

        printStream.flush();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
