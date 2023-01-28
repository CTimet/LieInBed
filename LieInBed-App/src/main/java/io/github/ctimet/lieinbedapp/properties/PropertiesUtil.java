package io.github.ctimet.lieinbedapp.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;

public class PropertiesUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    public static void check(Properties pps, HashMap<String, ValueChecker<String>> checker, Properties defaultValue) {
        LinkedList<String> waitChange = new LinkedList<>();
        pps.forEach((k, v) -> {
            if (!checker.get((String) k).check((String) v)) {
                waitChange.add((String) k);
            }
        });
        waitChange.forEach(s -> {
            logger.warn("键 {} 对应的值 {} 无法通过 ValueChecker 检查，已恢复成默认值 {}",
                            s, pps.getProperty(s),            defaultValue.getProperty(s));
            pps.setProperty(s, defaultValue.getProperty(s));
        });
    }
}
