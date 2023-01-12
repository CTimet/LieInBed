package io.github.ctimet.lieinbedapp.properties;

import io.github.ctimet.lieinbedapp.gui.Draw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class UsersProperties {
    private static final Logger logger = LoggerFactory.getLogger(UsersProperties.class);
    private static final Properties pps = new Properties();
    public UsersProperties() {
        File userProperties = new File("lieinbed/users.properties");
        try {
            logger.info("Loading lieinbed/users.properties");
            pps.load(new FileInputStream(userProperties));
        } catch (Exception e) {
            logger.error("The code threw an exception when loading properties");
            Draw.drawErr(e);
        }
    }

    public boolean contains(String k, String v) {
        return pps.getProperty(k).equals(v);
    }
}
