package BlockDynasty.Economy.domain.services.courier;

public interface Courier {
     void sendUpdateMessage(String type, String name);
     void sendUpdateMessage(String type, String data,String target);
}
