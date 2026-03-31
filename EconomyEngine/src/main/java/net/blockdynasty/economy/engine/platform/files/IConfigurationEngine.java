package net.blockdynasty.economy.engine.platform.files;

import net.blockdynasty.economy.gui.abstractions.IConfigurationGUI;
import net.blockdynasty.economy.libs.services.configuration.IConfiguration;

import java.util.Map;

public interface IConfigurationEngine extends IConfiguration, IConfigurationGUI {
    String getDatabasePath();
    String getLogsPath();
    void updateConfig(Map<Object, Object> newConfig);
}
