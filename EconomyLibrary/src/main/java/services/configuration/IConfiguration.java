package services.configuration;

public interface IConfiguration {

    boolean getBoolean(String path);
    String getString(String path);
    int getInt(String path);
    double getDouble(String path);
}
