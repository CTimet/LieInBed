package io.github.ctimet.remoteserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class AppProperties {
    private static final Properties pps = new Properties();
    public static void checkProperties() {
        File folder = new File("LieInBed-RemoteServer");
        File p = new File("LieInBed-RemoteServer/app.properties");
        folder.mkdir();
        try {
            if (p.exists()) {
                pps.load(new FileInputStream(p));
            } else {
                p.createNewFile();
                pps.load(new FileInputStream(p));
                pps.setProperty("isFirstStart", "true");
                pps.setProperty("serverVersion", "null");
                pps.setProperty("serverCore", "null");
                pps.setProperty("serverPath", "null");
                pps.setProperty("pluginPath", "null");
                pps.setProperty("modPath", "null");
                pps.setProperty("debug", "false");
                pps.store(new FileWriter(p), "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Properties getPps() {
        return pps;
    }
}
