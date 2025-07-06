package useCaseTest.transaction.MoksStubs;

import me.BlockDynasty.Economy.BlockDynastyEconomy;
import me.BlockDynasty.Economy.config.logging.AbstractLogger;

import java.io.File;

public class LoggerTest extends AbstractLogger {

    public LoggerTest() {
        super();
    }

    @Override
    public void save() {

    }

    @Override
    public File getLatest() {
        return null;
    }

    @Override
    public File getFolder() {
        return null;
    }

    @Override
    public void zipAndReplace() {

    }

    @Override
    public void log(String message) {

    }

    @Override
    public void warn(String message) {

    }

    @Override
    public void error(String message, Exception ex) {

    }

    @Override
    public String getDateAndTime() {
        return null;
    }
}
