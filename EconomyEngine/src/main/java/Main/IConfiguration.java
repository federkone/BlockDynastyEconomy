package Main;

public interface IConfiguration {

    String getHost();
    int getPort();
    String getDatabase();
    String getUsername();
    String getPassword();
    String getDbFilePath();
    boolean enableServerConsole();
    String getType();

}
