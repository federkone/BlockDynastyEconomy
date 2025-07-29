package BlockDynasty.Economy.domain.entities.account;

public class Player {
    private String uuid;
    private String nickname;

    public Player(String uuid, String nickname) {
        this.uuid = uuid;
        this.nickname = nickname;
    }
    public Player(Player player) {
        this.uuid = player.uuid;
        this.nickname = player.nickname;
    }
    public String getUuid() {
        return uuid;
    }
    public String getNickname() {
        return nickname;
    }
    public void setUuid(String uuid) {
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
