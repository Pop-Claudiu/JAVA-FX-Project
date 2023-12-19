package Domain;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    private String repository;
    private String runConfig;

    public String getRunConfig() {
        return runConfig;
    }

    private String car;
    private String inchiriere;

    public String getRepository() {
        return repository;
    }

    public String getCar() {
        return car;
    }

    public String getInchiriere() {
        return inchiriere;
    }

    static Settings settings;
    private Settings(String fileName) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(fileName));
        repository = properties.getProperty("Repository");
        car = properties.getProperty("Car");
        inchiriere = properties.getProperty("Inchiriere");
        runConfig = properties.getProperty("runConfiguration");
    }

    public static synchronized Settings getInstance() throws IOException {
        if(settings == null) {
            settings = new Settings("src/main/java/Domain/settings.properties");
        }
        return settings;
    }
}
