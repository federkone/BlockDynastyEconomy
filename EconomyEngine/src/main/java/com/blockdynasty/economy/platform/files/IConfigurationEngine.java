package com.blockdynasty.economy.platform.files;

import lib.abstractions.IConfigurationGUI;
import services.configuration.IConfiguration;

import java.util.Map;

public interface IConfigurationEngine extends IConfiguration, IConfigurationGUI {
    String getDatabasePath();
    String getLogsPath();
    void updateConfig(Map<Object, Object> newConfig);
}
