package useCaseTest.transaction.MoksStubs;

import me.BlockDynasty.Integrations.bungee.UpdateForwarder;
import org.bukkit.entity.Player;

public class UpdateForwarderTest extends UpdateForwarder {

    public UpdateForwarderTest() {
        super();
    }

    @Override
    public void onPluginMessageReceived(String channel, Player notInUse, byte[] message) {

    }

    @Override
    public void sendUpdateMessage(String type, String name) {

    }
}
