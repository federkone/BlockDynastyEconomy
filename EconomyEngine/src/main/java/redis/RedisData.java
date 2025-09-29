package redis;

import files.Configuration;

import java.util.UUID;

public class RedisData {
    private String host= "localhost";
    private int port= 6379;
    private String password= "";
    private String username= "";
    private static final String INSTANCE_ID = UUID.randomUUID().toString();

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
    public String getInstanceID() {
        return INSTANCE_ID;
    }
}
