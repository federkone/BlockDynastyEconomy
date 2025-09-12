package BlockDynasty.adapters.logs;
import  BlockDynasty.Economy.domain.services.log.Log;
import BlockDynasty.utils.Console;

public class AbstractLog implements Log {

    @Override
    public void log(String message) {
        Console.log(message);
    }

    @Override
    public void save() {

    }
}
