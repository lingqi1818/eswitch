package org.codeanywhere.easyRestful.base.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Properties prop;
    static {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("easyRestful.properties");
        prop = new Properties();
        try {
            prop.load(in);
        } catch (IOException e) {
            throw new RuntimeException("load easyRestful.properties from classpath error:", e);
        }
    }

    public static String getConfig(String key, String defaultValue) {
        return prop.getProperty(key, defaultValue);
    }

    public static void main(String args[]) {
        System.out.println(Config.getConfig("servlet.package", "com.test"));
    }

}
