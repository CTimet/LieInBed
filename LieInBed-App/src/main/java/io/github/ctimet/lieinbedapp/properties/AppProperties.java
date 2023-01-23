package io.github.ctimet.lieinbedapp.properties;

import io.github.ctimet.lieinbedapp.gui.Draw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

public class AppProperties {
    private static final Logger logger = LoggerFactory.getLogger(AppProperties.class);
    private static final File file = new File("lieinbed/app.properties");
    private static final Properties defaultValue = new Properties() {{
            put("connect.timeout", "5000");
            put("connect.scanner.encoding", "UTF-8");
            put("connect.printstream.autoflush", "true");
            put("connect.printstream.encoding", "UTF-8");
    }};
    private static final HashMap<String, ValueChecker<String>> propertiesChecker = new HashMap<>();
    private static final Properties pps = new Properties();
    public static void init() {
        File folder = new File("./lieinbed");
        try {
            logger.info("Creating folder ./lieinbed...{}", folder.mkdir());
            logger.info("Creating lieinbed/app.properties...{}", file.createNewFile());

            pps.load(new FileInputStream(file));
            clickProperty();
        } catch (IOException e) {
            Draw.drawErrThenClose(e);
        }
    }

    private static void clickProperty() {
        logger.info("Check APP Properties value...");
        PropertiesUtil.check(pps, propertiesChecker, defaultValue);
    }

    public static void reload() {
        pps.clear();
        init();
    }

    public static void setProperty(String key, String value) {
        pps.setProperty(key, value);
    }

    public static String getProperty(String key) {
        return pps.getProperty(key);
    }

    public static void removeProperty(String key) {
        pps.remove(key);
    }

    public static void addPropertiesCheck(String value, ValueChecker<String> checker) {
        propertiesChecker.put(value, checker);
    }

    public static void store() {
        try {
            pps.store(new FileWriter(file), "Latest changed at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
