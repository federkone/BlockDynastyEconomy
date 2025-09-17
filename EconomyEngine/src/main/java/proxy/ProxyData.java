package proxy;

import java.io.DataOutputStream;
import java.io.IOException;

public interface ProxyData {
    String getChannelName();
    void setAdditionalData(DataOutputStream out) throws IOException;
}
