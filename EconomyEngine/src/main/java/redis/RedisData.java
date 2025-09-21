package redis;

import Main.IConfiguration;

public class RedisData {
    private String host= "localhost";
    private int port= 6379;
    private String password= "";
    private String username= "";

    public RedisData(IConfiguration configuration){
        this.host = configuration.getConfig().getString("redis.host");
        this.port = configuration.getConfig().getInt("redis.port");
        this.password = configuration.getConfig().getString("redis.password");
        this.username = configuration.getConfig().getString("redis.username");
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
