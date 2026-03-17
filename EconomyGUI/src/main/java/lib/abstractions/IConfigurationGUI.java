package lib.abstractions;

import java.util.Map;

public interface IConfigurationGUI {
    void saveButtonConfig(int slot, boolean value);
    Map<Integer, Boolean> getButtonsConfig();
    boolean getBoolean(String path);
}
