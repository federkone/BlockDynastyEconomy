package BlockDynasty.adapters;

import BlockDynasty.SpongePlugin;
import Main.IConfiguration;
import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;

import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigurationAdapter implements IConfiguration {


    @Override
    public String getHost() {
        return "localhost";
    }

    @Override
    public int getPort() {
        return 3306;
    }

    @Override
    public String getDatabase() {
        return "minecraft";
    }

    @Override
    public String getUsername() {
        return "root";
    }

    @Override
    public String getPassword() {
        return "password";
    }

    @Override
    public String getDbFilePath() {
        return SpongePlugin.databasePath;
    }

    @Override
    public boolean enableServerConsole() {
        return true;
    }

    @Override
    public String getType() {
        return "h2";
    }
}
