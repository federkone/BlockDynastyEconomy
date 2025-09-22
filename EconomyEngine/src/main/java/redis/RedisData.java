package redis;

import files.Configuration;

public class RedisData {
    private String host= "localhost";
    private int port= 6379;
    private String password= "";
    private String username= "";

    public RedisData(Configuration configuration){
        this.host = configuration.getString("redis.host");
        this.port = configuration.getInt("redis.port");
        this.password = configuration.getString("redis.password");
        this.username = configuration.getString("redis.username");
    }
    public String getChannelName() {
        return "proxy:blockdynastyeconomy";
    }
    public String getHost() {
        return host;
    }
    public int getPort() {
        return port;

    }
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
}
