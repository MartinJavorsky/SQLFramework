package sk.javorsky.SQLFramework.dbaccess;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    public static final String DB_URL = "db.url";
    public static final String DB_DRIVER = "db.driver";
    public static final String DB_USER = "db.user";
    public static final String DB_PSW = "db.psw";

    public String getProperty(String property,String filename) throws Exception {
        //String filename = "sorm.properties";

        Properties prop = new Properties();
        InputStream input = null;
        //input = getClass().getClassLoader().getResourceAsStream(filename);
        input = new FileInputStream(filename);
        if (input == null) {
            throw new Exception("Could not find properties file");
        }

        prop.load(input);

        String propertyValue = prop.getProperty(property);
        if (propertyValue != null && !propertyValue.isEmpty()) {
            return propertyValue;
        } else {
            throw new Exception("property '"+property+"' not specified in properties file");
        }
    }
}
