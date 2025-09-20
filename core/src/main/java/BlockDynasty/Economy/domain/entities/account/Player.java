package BlockDynasty.Economy.domain.entities.account;

import java.util.UUID;

public class Player {
    private UUID uuid;
    private String nickname;

    public Player(UUID uuid, String nickname) {
        this.uuid = uuid;
        this.nickname = nickname;
    }
    public Player(Player player) {
        this.uuid = player.uuid;
        this.nickname = player.nickname;
    }
    public UUID getUuid() {
        return uuid;
    }
    public String getNickname() {
        return nickname;
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "Player{" +
                "uuid='" + uuid + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
