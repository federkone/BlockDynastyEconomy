package mockClass;

import BlockDynasty.Economy.domain.services.courier.Courier;

public class CourierTest implements Courier {

    public void sendUpdateMessage(String type, String name) {
        System.out.println("[BUNGEE CHANNEL SEND] " + type + " " + name);
    }


}
