package io.github.ctimet.lieinbedapp0.properties;

import io.github.ctimet.lieinbedapp0.gui.Draw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AppProperties {
    private static final Logger logger = LoggerFactory.getLogger(AppProperties.class);
    private static final Properties setting = new Properties();
    private static UsersProperties usersProperties;
    private static AppProperties appProperties;

    public AppProperties() {
        try {
            logger.info("Loading lieinbed/setting.properties");
            setting.load(new FileInputStream("lieinbed/setting.properties"));
        } catch (IOException e) {
            logger.info("The code threw an exception when loading properties");
            Draw.drawErr(e);
        }
    }

    public String getProperty(String key) {
        return setting.getProperty(key);
    }

    public void setProperty(String key, String value) {
        setting.setProperty(key, value);
    }

    public void save() {
        try {
            setting.store(new FileOutputStream("lieinbed/setting.properties"), "");
        } catch (IOException e) {
            logger.error("The code threw an exception when storing lieinbed/setting.properties");
            Draw.drawErr(e);
        }
    }

    public static void checkProperties() {
        File folder = new File("./lieinbed");
        logger.info("Creating folder ./lieinbed {}", folder.mkdir());

        File settingProperties = new File("lieinbed/setting.properties");
        File userProperties = new File("lieinbed/users.properties");
        try {
            logger.info("Creating lieinbed/setting.properties {}", settingProperties.createNewFile());
            logger.info("Creating lieinbed/users.properties {}", userProperties.createNewFile());
        } catch (Exception e) {
            logger.error("The code threw an exception when creating properties");
            Draw.drawErr(e);
        }

        appProperties = new AppProperties();
        usersProperties = new UsersProperties();
    }

    public static AppProperties getAppProperties() {
        return appProperties;
    }

    public static UsersProperties getUsersProperties() {
        return usersProperties;
    }
}
