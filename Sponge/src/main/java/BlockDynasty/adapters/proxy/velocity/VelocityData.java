package BlockDynasty.adapters.proxy.velocity;

import proxy.ProxyData;

import java.io.DataOutputStream;
import java.io.IOException;

public class VelocityData implements ProxyData {
    @Override
    public String getChannelName() {
        return "velocity:economy";
    }

    @Override
    public void setAdditionalData(DataOutputStream out) throws IOException {
        //empty
    }
}
