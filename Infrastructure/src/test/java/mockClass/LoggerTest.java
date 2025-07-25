package mockClass;

import BlockDynasty.Economy.domain.services.log.Log;

public class LoggerTest implements Log {

    @Override
    public void save() {
    }

    @Override
    public void log(String message) {
        System.out.println(message);
    }
}
